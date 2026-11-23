package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ViewList
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.model.SystemCategory
import com.example.ui.components.PinnedQuickChip
import com.example.ui.components.ShortcutDetailSheet
import com.example.ui.components.ShortcutGridCard
import com.example.ui.components.ShortcutListRow
import kotlinx.coroutines.launch

@Composable
fun ShortcutDashboardScreen(
  viewModel: ShortcutViewModel,
  modifier: Modifier = Modifier,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val context = LocalContext.current
  val focusManager = LocalFocusManager.current
  val snackbarHostState = remember { SnackbarHostState() }
  val coroutineScope = rememberCoroutineScope()
  val isDark = isSystemInDarkTheme()

  val fallbackMsgTemplate = stringResource(R.string.launch_fallback_notice)
  val unavailableMsgTemplate = stringResource(R.string.launch_unavailable_notice)
  val pinSupportedMsg = stringResource(R.string.pin_home_supported_sent)
  val pinUnsupportedMsg = stringResource(R.string.pin_home_unsupported)
  val copiedMsg = stringResource(R.string.intent_copied)

  LaunchedEffect(viewModel) {
    viewModel.events.collect { event ->
      when (event) {
        is ShortcutUiEvent.LaunchFallbackNotice -> {
          snackbarHostState.showSnackbar(String.format(fallbackMsgTemplate, event.title))
        }
        is ShortcutUiEvent.LaunchUnavailableNotice -> {
          snackbarHostState.showSnackbar(String.format(unavailableMsgTemplate, event.title))
        }
        is ShortcutUiEvent.HomePinResult -> {
          snackbarHostState.showSnackbar(
            if (event.supported) pinSupportedMsg else pinUnsupportedMsg,
          )
        }
      }
    }
  }

  val primaryGlow = MaterialTheme.colorScheme.primary.copy(alpha = if (isDark) 0.14f else 0.08f)
  val tertiaryGlow = MaterialTheme.colorScheme.tertiary.copy(alpha = if (isDark) 0.10f else 0.06f)

  Scaffold(
    modifier = modifier.fillMaxSize(),
    containerColor = MaterialTheme.colorScheme.background,
    contentWindowInsets = WindowInsets.safeDrawing,
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .drawBehind {
          drawCircle(
            brush = Brush.radialGradient(
              colors = listOf(primaryGlow, androidx.compose.ui.graphics.Color.Transparent),
              center = Offset(size.width * 0.15f, size.height * 0.05f),
              radius = size.width * 0.75f,
            ),
            radius = size.width * 0.75f,
            center = Offset(size.width * 0.15f, size.height * 0.05f),
          )
          drawCircle(
            brush = Brush.radialGradient(
              colors = listOf(tertiaryGlow, androidx.compose.ui.graphics.Color.Transparent),
              center = Offset(size.width * 0.92f, size.height * 0.18f),
              radius = size.width * 0.55f,
            ),
            radius = size.width * 0.55f,
            center = Offset(size.width * 0.92f, size.height * 0.18f),
          )
        }
        .padding(innerPadding),
      contentAlignment = Alignment.TopCenter,
    ) {
      BoxWithConstraints(
        modifier = Modifier
          .fillMaxSize()
          .widthIn(max = 980.dp),
      ) {
        val columnCount = when {
          !uiState.isGridView -> 1
          maxWidth >= 820.dp -> 4
          maxWidth >= 580.dp -> 3
          else -> 2
        }

        val showQuickDecks = uiState.searchQuery.isBlank() &&
          uiState.selectedCategory == SystemCategory.ALL

        LazyVerticalGrid(
          columns = GridCells.Fixed(columnCount),
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 12.dp,
            bottom = 28.dp,
          ),
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          // Top Header
          item(span = { GridItemSpan(columnCount) }) {
            DashboardHeader(
              totalCount = uiState.filteredShortcuts.size,
              isGridView = uiState.isGridView,
              onToggleViewMode = { viewModel.toggleViewMode() },
            )
          }

          // Search Bar
          item(span = { GridItemSpan(columnCount) }) {
            OutlinedTextField(
              value = uiState.searchQuery,
              onValueChange = viewModel::onSearchQueryChange,
              modifier = Modifier
                .fillMaxWidth()
                .testTag("search_input"),
              placeholder = {
                Text(
                  text = stringResource(R.string.search_placeholder),
                  style = MaterialTheme.typography.bodyLarge,
                )
              },
              leadingIcon = {
                Icon(
                  imageVector = Icons.Rounded.Search,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary,
                )
              },
              trailingIcon = {
                AnimatedVisibility(
                  visible = uiState.searchQuery.isNotEmpty(),
                  enter = fadeIn(),
                  exit = fadeOut(),
                ) {
                  IconButton(
                    onClick = { viewModel.onSearchQueryChange("") },
                    modifier = Modifier
                      .minimumInteractiveComponentSize()
                      .testTag("clear_search_button"),
                  ) {
                    Icon(
                      imageVector = Icons.Rounded.Close,
                      contentDescription = stringResource(R.string.clear_search),
                    )
                  }
                }
              },
              singleLine = true,
              shape = RoundedCornerShape(18.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
              ),
              keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
              keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
            )
          }

          // Category Filter Chips
          item(span = { GridItemSpan(columnCount) }) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
              horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
              SystemCategory.entries.forEach { category ->
                val isSelected = uiState.selectedCategory == category
                val accent = category.accentColor(isDark)
                FilterChip(
                  selected = isSelected,
                  onClick = { viewModel.onCategorySelected(category) },
                  label = {
                    Text(
                      text = category.label,
                      style = MaterialTheme.typography.labelLarge,
                    )
                  },
                  modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .testTag("category_chip_${category.name}"),
                  shape = RoundedCornerShape(14.dp),
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = accent.copy(alpha = if (isDark) 0.28f else 0.14f),
                    selectedLabelColor = if (isDark) accent else MaterialTheme.colorScheme.onSurface,
                  ),
                  border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = MaterialTheme.colorScheme.outlineVariant,
                    selectedBorderColor = accent,
                  ),
                )
              }
            }
          }

          // Pinned Favorites Deck
          if (showQuickDecks && uiState.pinnedShortcuts.isNotEmpty()) {
            item(span = { GridItemSpan(columnCount) }) {
              Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SectionTitleRow(
                  icon = Icons.Rounded.Star,
                  title = stringResource(R.string.section_pinned),
                  badge = "${uiState.pinnedShortcuts.size}",
                  iconTint = MaterialTheme.colorScheme.secondary,
                )

                LazyRow(
                  horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                  items(
                    items = uiState.pinnedShortcuts,
                    key = { "pinned_${it.id}" },
                  ) { shortcut ->
                    PinnedQuickChip(
                      shortcut = shortcut,
                      onLaunch = { viewModel.launchShortcut(context, shortcut) },
                      onLongPress = { viewModel.openSheet(shortcut) },
                    )
                  }
                }
              }
            }
          }

          // Recent Shortcuts Strip
          if (showQuickDecks && uiState.recentShortcuts.isNotEmpty()) {
            item(span = { GridItemSpan(columnCount) }) {
              Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SectionTitleRow(
                  icon = Icons.Rounded.History,
                  title = stringResource(R.string.section_recent),
                  badge = "${uiState.recentShortcuts.size}",
                  iconTint = MaterialTheme.colorScheme.tertiary,
                )

                LazyRow(
                  horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                  items(
                    items = uiState.recentShortcuts,
                    key = { "recent_${it.id}" },
                  ) { shortcut ->
                    AssistChip(
                      onClick = { viewModel.launchShortcut(context, shortcut) },
                      label = {
                        Text(
                          text = shortcut.title,
                          style = MaterialTheme.typography.labelMedium,
                        )
                      },
                      leadingIcon = {
                        Icon(
                          imageVector = shortcut.icon,
                          contentDescription = null,
                          tint = shortcut.category.accentColor(isDark),
                          modifier = Modifier.size(16.dp),
                        )
                      },
                      modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("recent_chip_${shortcut.id}"),
                      shape = RoundedCornerShape(12.dp),
                      colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                      ),
                    )
                  }
                }
              }
            }
          }

          // All / Filtered Catalog Header
          item(span = { GridItemSpan(columnCount) }) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
              SectionTitleRow(
                icon = Icons.Rounded.Bolt,
                title = if (uiState.selectedCategory == SystemCategory.ALL) {
                  stringResource(R.string.section_all_menus)
                } else {
                  uiState.selectedCategory.label
                },
                badge = stringResource(R.string.count_menus_format, uiState.filteredShortcuts.size),
                iconTint = MaterialTheme.colorScheme.primary,
              )
              Text(
                text = stringResource(R.string.long_press_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
          }

          // Empty State or Catalog Cards
          if (uiState.filteredShortcuts.isEmpty()) {
            item(span = { GridItemSpan(columnCount) }) {
              EmptySearchState(
                onResetFilters = { viewModel.resetFilters() },
              )
            }
          } else {
            items(
              items = uiState.filteredShortcuts,
              key = { it.id },
            ) { shortcut ->
              val isPinned = shortcut.id in uiState.pinnedIds
              val tapCount = uiState.tapCounts[shortcut.id] ?: 0
              if (uiState.isGridView) {
                ShortcutGridCard(
                  shortcut = shortcut,
                  isPinned = isPinned,
                  tapCount = tapCount,
                  onLaunch = { viewModel.launchShortcut(context, shortcut) },
                  onTogglePin = { viewModel.togglePinned(shortcut.id) },
                  onOpenDetails = { viewModel.openSheet(shortcut) },
                )
              } else {
                ShortcutListRow(
                  shortcut = shortcut,
                  isPinned = isPinned,
                  tapCount = tapCount,
                  onLaunch = { viewModel.launchShortcut(context, shortcut) },
                  onTogglePin = { viewModel.togglePinned(shortcut.id) },
                  onOpenDetails = { viewModel.openSheet(shortcut) },
                )
              }
            }
          }
        }
      }
    }
  }

  uiState.selectedShortcutForSheet?.let { sheetShortcut ->
    ShortcutDetailSheet(
      shortcut = sheetShortcut,
      isPinned = sheetShortcut.id in uiState.pinnedIds,
      onDismiss = { viewModel.dismissSheet() },
      onLaunchNow = { viewModel.launchShortcut(context, sheetShortcut) },
      onTogglePin = { viewModel.togglePinned(sheetShortcut.id) },
      onPinToHomeScreen = { viewModel.pinToHomeScreen(context, sheetShortcut) },
      onCopiedIntent = {
        coroutineScope.launch {
          snackbarHostState.showSnackbar(copiedMsg)
        }
      },
    )
  }
}

@Composable
private fun DashboardHeader(
  totalCount: Int,
  isGridView: Boolean,
  onToggleViewMode: () -> Unit,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(top = 4.dp, bottom = 4.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      modifier = Modifier.weight(1f),
    ) {
      Box(
        modifier = Modifier
          .size(46.dp)
          .clip(RoundedCornerShape(15.dp))
          .background(
            Brush.linearGradient(
              colors = listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.tertiary,
              ),
            ),
          ),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = Icons.Rounded.Bolt,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onPrimary,
          modifier = Modifier.size(26.dp),
        )
      }

      Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
          text = stringResource(R.string.app_name),
          style = MaterialTheme.typography.headlineMedium,
          color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
          text = stringResource(R.string.app_tagline),
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }

    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
      Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
      ) {
        Text(
          text = "$totalCount",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onPrimaryContainer,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
        )
      }

      Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
      ) {
        IconButton(
          onClick = onToggleViewMode,
          modifier = Modifier
            .minimumInteractiveComponentSize()
            .testTag("view_mode_toggle"),
        ) {
          Icon(
            imageVector = if (isGridView) {
              Icons.AutoMirrored.Rounded.ViewList
            } else {
              Icons.Rounded.GridView
            },
            contentDescription = stringResource(
              if (isGridView) R.string.toggle_grid_view else R.string.toggle_list_view,
            ),
            tint = MaterialTheme.colorScheme.onSurface,
          )
        }
      }
    }
  }
}

@Composable
private fun SectionTitleRow(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  badge: String,
  iconTint: androidx.compose.ui.graphics.Color,
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = iconTint,
        modifier = Modifier.size(18.dp),
      )
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
      )
    }

    Surface(
      shape = RoundedCornerShape(8.dp),
      color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
    ) {
      Text(
        text = badge,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
      )
    }
  }
}

@Composable
private fun EmptySearchState(
  onResetFilters: () -> Unit,
) {
  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 24.dp),
    shape = MaterialTheme.shapes.large,
    color = MaterialTheme.colorScheme.surface,
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp, vertical = 32.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Box(
        modifier = Modifier
          .size(60.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = Icons.Rounded.SearchOff,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(30.dp),
        )
      }

      Text(
        text = stringResource(R.string.empty_search_title),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
      )
      Text(
        text = stringResource(R.string.empty_search_subtitle),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
      )

      Spacer(modifier = Modifier.height(4.dp))

      Button(
        onClick = onResetFilters,
        modifier = Modifier
          .minimumInteractiveComponentSize()
          .testTag("reset_filters_button"),
        shape = MaterialTheme.shapes.medium,
      ) {
        Text(text = stringResource(R.string.reset_filters))
      }
    }
  }
}
