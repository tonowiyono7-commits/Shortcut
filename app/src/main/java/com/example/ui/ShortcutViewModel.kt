package com.example.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.LaunchOutcome
import com.example.data.ShortcutPreferencesRepository
import com.example.data.ShortcutPreferencesState
import com.example.data.SystemShortcutCatalog
import com.example.data.SystemShortcutLauncher
import com.example.model.SystemCategory
import com.example.model.SystemShortcut
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface ShortcutUiEvent {
  data class LaunchFallbackNotice(val title: String) : ShortcutUiEvent
  data class LaunchUnavailableNotice(val title: String) : ShortcutUiEvent
  data class HomePinResult(val supported: Boolean) : ShortcutUiEvent
}

data class ShortcutUiState(
  val searchQuery: String = "",
  val selectedCategory: SystemCategory = SystemCategory.ALL,
  val isGridView: Boolean = true,
  val pinnedShortcuts: List<SystemShortcut> = emptyList(),
  val recentShortcuts: List<SystemShortcut> = emptyList(),
  val filteredShortcuts: List<SystemShortcut> = SystemShortcutCatalog.allShortcuts,
  val pinnedIds: Set<String> = SystemShortcutCatalog.defaultPinnedIds,
  val tapCounts: Map<String, Int> = emptyMap(),
  val selectedShortcutForSheet: SystemShortcut? = null,
  val totalCatalogCount: Int = SystemShortcutCatalog.allShortcuts.size,
)

class ShortcutViewModel(
  private val repository: ShortcutPreferencesRepository,
) : ViewModel() {

  private val searchQuery = MutableStateFlow("")
  private val selectedCategory = MutableStateFlow(SystemCategory.ALL)
  private val selectedShortcutForSheet = MutableStateFlow<SystemShortcut?>(null)

  private val _events = MutableSharedFlow<ShortcutUiEvent>()
  val events: SharedFlow<ShortcutUiEvent> = _events.asSharedFlow()

  val uiState: StateFlow<ShortcutUiState> = combine(
    repository.preferencesFlow,
    searchQuery,
    selectedCategory,
    selectedShortcutForSheet,
  ) { prefs: ShortcutPreferencesState, query: String, category: SystemCategory, sheetItem: SystemShortcut? ->
    val all = SystemShortcutCatalog.allShortcuts
    val trimmedQuery = query.trim().lowercase()

    val filtered = all.filter { shortcut ->
      val matchesCategory = category == SystemCategory.ALL || shortcut.category == category
      val matchesQuery = if (trimmedQuery.isEmpty()) {
        true
      } else {
        shortcut.title.lowercase().contains(trimmedQuery) ||
          shortcut.subtitle.lowercase().contains(trimmedQuery) ||
          shortcut.actionBadge.lowercase().contains(trimmedQuery) ||
          shortcut.primaryAction.lowercase().contains(trimmedQuery) ||
          shortcut.keywords.any { it.lowercase().contains(trimmedQuery) }
      }
      matchesCategory && matchesQuery
    }

    val pinned = all.filter { it.id in prefs.pinnedIds }
    val recent = prefs.recentIds.mapNotNull { SystemShortcutCatalog.findById(it) }

    ShortcutUiState(
      searchQuery = query,
      selectedCategory = category,
      isGridView = prefs.isGridView,
      pinnedShortcuts = pinned,
      recentShortcuts = recent,
      filteredShortcuts = filtered,
      pinnedIds = prefs.pinnedIds,
      tapCounts = prefs.tapCounts,
      selectedShortcutForSheet = sheetItem,
      totalCatalogCount = all.size,
    )
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5_000),
    initialValue = ShortcutUiState(),
  )

  fun onSearchQueryChange(query: String) {
    searchQuery.value = query
  }

  fun onCategorySelected(category: SystemCategory) {
    selectedCategory.value = category
  }

  fun resetFilters() {
    searchQuery.value = ""
    selectedCategory.value = SystemCategory.ALL
  }

  fun toggleViewMode() {
    val next = !uiState.value.isGridView
    viewModelScope.launch {
      repository.setGridView(next)
    }
  }

  fun togglePinned(shortcutId: String) {
    viewModelScope.launch {
      repository.togglePinned(shortcutId)
    }
  }

  fun openSheet(shortcut: SystemShortcut) {
    selectedShortcutForSheet.value = shortcut
  }

  fun dismissSheet() {
    selectedShortcutForSheet.value = null
  }

  fun launchShortcut(context: Context, shortcut: SystemShortcut) {
    val outcome = SystemShortcutLauncher.launch(context, shortcut)
    viewModelScope.launch {
      when (outcome) {
        is LaunchOutcome.Success -> {
          repository.recordLaunch(shortcut.id)
          if (outcome.usedFallback) {
            _events.emit(ShortcutUiEvent.LaunchFallbackNotice(shortcut.title))
          }
        }
        is LaunchOutcome.Unavailable -> {
          _events.emit(ShortcutUiEvent.LaunchUnavailableNotice(shortcut.title))
        }
      }
    }
  }

  fun pinToHomeScreen(context: Context, shortcut: SystemShortcut) {
    val supported = SystemShortcutLauncher.pinToHomeScreen(context, shortcut)
    viewModelScope.launch {
      _events.emit(ShortcutUiEvent.HomePinResult(supported))
    }
  }

  class Factory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      val repo = ShortcutPreferencesRepository(context.applicationContext)
      return ShortcutViewModel(repo) as T
    }
  }
}
