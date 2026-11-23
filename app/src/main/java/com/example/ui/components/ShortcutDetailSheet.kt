package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.rounded.AddToHomeScreen
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.SystemShortcutLauncher
import com.example.model.SystemShortcut

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShortcutDetailSheet(
  shortcut: SystemShortcut,
  isPinned: Boolean,
  onDismiss: () -> Unit,
  onLaunchNow: () -> Unit,
  onTogglePin: () -> Unit,
  onPinToHomeScreen: () -> Unit,
  onCopiedIntent: () -> Unit,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current
  val isDark = isSystemInDarkTheme()
  val accentColor = shortcut.category.accentColor(isDark)
  val isNativelySupported = remember(shortcut.id) {
    SystemShortcutLauncher.isPrimarySupported(context, shortcut)
  }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = MaterialTheme.colorScheme.surface,
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .navigationBarsPadding()
        .padding(horizontal = 24.dp)
        .padding(bottom = 24.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
      ) {
        Box(
          modifier = Modifier
            .size(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(accentColor.copy(alpha = if (isDark) 0.22f else 0.13f)),
          contentAlignment = Alignment.Center,
        ) {
          Icon(
            imageVector = shortcut.icon,
            contentDescription = shortcut.title,
            tint = accentColor,
            modifier = Modifier.size(30.dp),
          )
        }

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = shortcut.category.label,
            style = MaterialTheme.typography.labelMedium,
            color = accentColor,
          )
          Text(
            text = shortcut.title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
          )
          Text(
            text = shortcut.subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }

      // Compatibility & Intent Technical Box
      Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
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
                imageVector = if (isNativelySupported) {
                  Icons.Rounded.CheckCircle
                } else {
                  Icons.Rounded.SwapHoriz
                },
                contentDescription = null,
                tint = if (isNativelySupported) accentColor else MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(16.dp),
              )
              Text(
                text = stringResource(
                  if (isNativelySupported) R.string.status_available else R.string.status_fallback,
                ),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
              )
            }

            IconButton(
              onClick = {
                clipboardManager.setText(AnnotatedString(shortcut.primaryAction))
                onCopiedIntent()
              },
              modifier = Modifier
                .minimumInteractiveComponentSize()
                .testTag("copy_intent_button"),
            ) {
              Icon(
                imageVector = Icons.Rounded.ContentCopy,
                contentDescription = stringResource(R.string.copy_intent_action),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp),
              )
            }
          }

          Text(
            text = shortcut.primaryAction,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }

      Button(
        onClick = {
          onDismiss()
          onLaunchNow()
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("sheet_launch_button"),
        shape = MaterialTheme.shapes.medium,
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
          contentDescription = null,
          modifier = Modifier.size(18.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = stringResource(R.string.open_menu_now),
          style = MaterialTheme.typography.labelLarge,
        )
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        FilledTonalButton(
          onClick = onTogglePin,
          modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .testTag("sheet_pin_favorite_button"),
          shape = MaterialTheme.shapes.medium,
        ) {
          Icon(
            imageVector = if (isPinned) Icons.Rounded.Star else Icons.Outlined.StarOutline,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (isPinned) "Hapus Favorit" else "Favoritkan",
            style = MaterialTheme.typography.labelMedium,
          )
        }

        OutlinedButton(
          onClick = {
            onDismiss()
            onPinToHomeScreen()
          },
          modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .testTag("sheet_pin_homescreen_button"),
          shape = MaterialTheme.shapes.medium,
        ) {
          Icon(
            imageVector = Icons.Rounded.AddToHomeScreen,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Layar Utama",
            style = MaterialTheme.typography.labelMedium,
          )
        }
      }
    }
  }
}
