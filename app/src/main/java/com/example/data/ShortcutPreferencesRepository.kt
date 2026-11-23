package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.shortcutDataStore: DataStore<Preferences> by preferencesDataStore(
  name = "pintasan_sistem_prefs"
)

data class ShortcutPreferencesState(
  val pinnedIds: Set<String> = SystemShortcutCatalog.defaultPinnedIds,
  val recentIds: List<String> = emptyList(),
  val tapCounts: Map<String, Int> = emptyMap(),
  val isGridView: Boolean = true,
)

class ShortcutPreferencesRepository(private val context: Context) {

  private object Keys {
    val PINNED_IDS = stringSetPreferencesKey("pinned_ids")
    val HAS_INITIALIZED_PINS = booleanPreferencesKey("has_initialized_pins")
    val RECENT_IDS = stringPreferencesKey("recent_ids_csv")
    val TAP_COUNTS = stringPreferencesKey("tap_counts_csv")
    val IS_GRID_VIEW = booleanPreferencesKey("is_grid_view")
  }

  val preferencesFlow: Flow<ShortcutPreferencesState> = context.shortcutDataStore.data.map { prefs ->
    val initialized = prefs[Keys.HAS_INITIALIZED_PINS] ?: false
    val pinned = if (initialized) {
      prefs[Keys.PINNED_IDS] ?: emptySet()
    } else {
      SystemShortcutCatalog.defaultPinnedIds
    }
    val recent = prefs[Keys.RECENT_IDS]
      ?.split(",")
      ?.filter { it.isNotBlank() }
      ?: emptyList()
    val counts = parseTapCounts(prefs[Keys.TAP_COUNTS])
    val isGrid = prefs[Keys.IS_GRID_VIEW] ?: true
    ShortcutPreferencesState(
      pinnedIds = pinned,
      recentIds = recent,
      tapCounts = counts,
      isGridView = isGrid,
    )
  }

  suspend fun togglePinned(shortcutId: String) {
    context.shortcutDataStore.edit { prefs ->
      val initialized = prefs[Keys.HAS_INITIALIZED_PINS] ?: false
      val current = if (initialized) {
        (prefs[Keys.PINNED_IDS] ?: emptySet()).toMutableSet()
      } else {
        SystemShortcutCatalog.defaultPinnedIds.toMutableSet()
      }
      if (!current.add(shortcutId)) {
        current.remove(shortcutId)
      }
      prefs[Keys.HAS_INITIALIZED_PINS] = true
      prefs[Keys.PINNED_IDS] = current
    }
  }

  suspend fun recordLaunch(shortcutId: String) {
    context.shortcutDataStore.edit { prefs ->
      val existingRecent = prefs[Keys.RECENT_IDS]
        ?.split(",")
        ?.filter { it.isNotBlank() }
        ?.toMutableList()
        ?: mutableListOf()
      existingRecent.remove(shortcutId)
      existingRecent.add(0, shortcutId)
      prefs[Keys.RECENT_IDS] = existingRecent.take(6).joinToString(",")

      val counts = parseTapCounts(prefs[Keys.TAP_COUNTS]).toMutableMap()
      counts[shortcutId] = (counts[shortcutId] ?: 0) + 1
      prefs[Keys.TAP_COUNTS] = counts.entries.joinToString(",") { "${it.key}:${it.value}" }
    }
  }

  suspend fun setGridView(isGrid: Boolean) {
    context.shortcutDataStore.edit { prefs ->
      prefs[Keys.IS_GRID_VIEW] = isGrid
    }
  }

  private fun parseTapCounts(raw: String?): Map<String, Int> {
    if (raw.isNullOrBlank()) return emptyMap()
    return buildMap {
      raw.split(",").forEach { entry ->
        val parts = entry.split(":")
        if (parts.size == 2) {
          val count = parts[1].toIntOrNull()
          if (parts[0].isNotBlank() && count != null) {
            put(parts[0], count)
          }
        }
      }
    }
  }
}
