package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.SystemShortcut

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShortcutGridCard(
  shortcut: SystemShortcut,
  isPinned: Boolean,
  tapCount: Int,
  onLaunch: () -> Unit,
  onTogglePin: () -> Unit,
  onOpenDetails: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val isDark = isSystemInDarkTheme()
  val accentColor = shortcut.category.accentColor(isDark)
  val haptics = LocalHapticFeedback.current
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.96f else 1f,
    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
    label = "grid_card_scale",
  )

  val containerColor by animateColorAsState(
    targetValue = if (isPinned) {
      MaterialTheme.colorScheme.surface
    } else {
      MaterialTheme.colorScheme.surface
    },
    label = "grid_card_bg",
  )

  val borderColor = if (isPinned) {
    accentColor.copy(alpha = 0.45f)
  } else {
    MaterialTheme.colorScheme.outlineVariant
  }

  Surface(
    modifier = modifier
      .graphicsLayer {
        scaleX = scale
        scaleY = scale
      }
      .fillMaxWidth()
      .height(164.dp)
      .clip(MaterialTheme.shapes.medium)
      .combinedClickable(
        interactionSource = interactionSource,
        indication = androidx.compose.material3.ripple(),
        onClick = {
          haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
          onLaunch()
        },
        onLongClick = {
          haptics.performHapticFeedback(HapticFeedbackType.LongPress)
          onOpenDetails()
        },
      )
      .testTag("shortcut_card_${shortcut.id}"),
    shape = MaterialTheme.shapes.medium,
    color = containerColor,
    tonalElevation = if (isPinned) 3.dp else 1.dp,
    border = BorderStroke(1.dp, borderColor),
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(start = 14.dp, end = 6.dp, top = 12.dp, bottom = 12.dp),
      verticalArrangement = Arrangement.SpaceBetween,
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
      ) {
        Box(
          modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(accentColor.copy(alpha = if (isDark) 0.20f else 0.12f)),
          contentAlignment = Alignment.Center,
        ) {
          Icon(
            imageVector = shortcut.icon,
            contentDescription = shortcut.title,
            tint = accentColor,
            modifier = Modifier.size(24.dp),
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = onOpenDetails,
            modifier = Modifier
              .minimumInteractiveComponentSize()
              .size(40.dp)
              .testTag("info_button_${shortcut.id}"),
          ) {
            Icon(
              imageVector = Icons.Rounded.MoreHoriz,
              contentDescription = stringResource(R.string.shortcut_details),
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(18.dp),
            )
          }

          IconButton(
            onClick = onTogglePin,
            modifier = Modifier
              .minimumInteractiveComponentSize()
              .size(40.dp)
              .testTag("pin_button_${shortcut.id}"),
          ) {
            Icon(
              imageVector = if (isPinned) Icons.Rounded.Star else Icons.Outlined.StarOutline,
              contentDescription = stringResource(
                if (isPinned) R.string.unpin_shortcut else R.string.pin_shortcut,
              ),
              tint = if (isPinned) {
                MaterialTheme.colorScheme.secondary
              } else {
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f)
              },
              modifier = Modifier.size(20.dp),
            )
          }
        }
      }

      Column(
        modifier = Modifier.padding(end = 8.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
      ) {
        Text(
          text = shortcut.title,
          style = MaterialTheme.typography.titleMedium,
          color = MaterialTheme.colorScheme.onSurface,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        Text(
          text = shortcut.subtitle,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
        )
      }

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(end = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Surface(
          shape = RoundedCornerShape(6.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f),
        ) {
          Text(
            text = shortcut.actionBadge,
            style = MaterialTheme.typography.labelSmall,
            color = accentColor,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
          )
        }

        if (tapCount > 0) {
          Text(
            text = "${tapCount}×",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShortcutListRow(
  shortcut: SystemShortcut,
  isPinned: Boolean,
  tapCount: Int,
  onLaunch: () -> Unit,
  onTogglePin: () -> Unit,
  onOpenDetails: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val isDark = isSystemInDarkTheme()
  val accentColor = shortcut.category.accentColor(isDark)
  val haptics = LocalHapticFeedback.current

  Surface(
    modifier = modifier
      .fillMaxWidth()
      .clip(MaterialTheme.shapes.medium)
      .combinedClickable(
        indication = androidx.compose.material3.ripple(),
        interactionSource = remember { MutableInteractionSource() },
        onClick = {
          haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
          onLaunch()
        },
        onLongClick = {
          haptics.performHapticFeedback(HapticFeedbackType.LongPress)
          onOpenDetails()
        },
      )
      .testTag("shortcut_card_${shortcut.id}"),
    shape = MaterialTheme.shapes.medium,
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = if (isPinned) 2.dp else 1.dp,
    border = BorderStroke(
      1.dp,
      if (isPinned) accentColor.copy(alpha = 0.42f) else MaterialTheme.colorScheme.outlineVariant,
    ),
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 12.dp, end = 6.dp, top = 10.dp, bottom = 10.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(accentColor.copy(alpha = if (isDark) 0.20f else 0.12f)),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = shortcut.icon,
          contentDescription = shortcut.title,
          tint = accentColor,
          modifier = Modifier.size(22.dp),
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(2.dp),
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
          Text(
            text = shortcut.title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
          Surface(
            shape = RoundedCornerShape(5.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
          ) {
            Text(
              text = shortcut.actionBadge,
              style = MaterialTheme.typography.labelSmall,
              color = accentColor,
              modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp),
            )
          }
          if (tapCount > 0) {
            Text(
              text = "${tapCount}×",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }
        Text(
          text = shortcut.subtitle,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }

      IconButton(
        onClick = onTogglePin,
        modifier = Modifier
          .minimumInteractiveComponentSize()
          .testTag("pin_button_${shortcut.id}"),
      ) {
        Icon(
          imageVector = if (isPinned) Icons.Rounded.Star else Icons.Outlined.StarOutline,
          contentDescription = stringResource(
            if (isPinned) R.string.unpin_shortcut else R.string.pin_shortcut,
          ),
          tint = if (isPinned) {
            MaterialTheme.colorScheme.secondary
          } else {
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f)
          },
          modifier = Modifier.size(20.dp),
        )
      }

      IconButton(
        onClick = onOpenDetails,
        modifier = Modifier
          .minimumInteractiveComponentSize()
          .testTag("info_button_${shortcut.id}"),
      ) {
        Icon(
          imageVector = Icons.Rounded.MoreHoriz,
          contentDescription = stringResource(R.string.shortcut_details),
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(20.dp),
        )
      }
    }
  }
}

@Composable
fun PinnedQuickChip(
  shortcut: SystemShortcut,
  onLaunch: () -> Unit,
  onLongPress: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val isDark = isSystemInDarkTheme()
  val accentColor = shortcut.category.accentColor(isDark)
  val haptics = LocalHapticFeedback.current

  @OptIn(ExperimentalFoundationApi::class)
  Surface(
    modifier = modifier
      .minimumInteractiveComponentSize()
      .clip(RoundedCornerShape(16.dp))
      .combinedClickable(
        indication = androidx.compose.material3.ripple(),
        interactionSource = remember { MutableInteractionSource() },
        onClick = {
          haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
          onLaunch()
        },
        onLongClick = {
          haptics.performHapticFeedback(HapticFeedbackType.LongPress)
          onLongPress()
        },
      )
      .testTag("pinned_quick_chip_${shortcut.id}"),
    shape = RoundedCornerShape(16.dp),
    color = accentColor.copy(alpha = if (isDark) 0.18f else 0.11f),
    border = BorderStroke(1.dp, accentColor.copy(alpha = 0.35f)),
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      Box(
        modifier = Modifier
          .size(30.dp)
          .clip(CircleShape)
          .background(accentColor),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = shortcut.icon,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.surface,
          modifier = Modifier.size(17.dp),
        )
      }
      Column {
        Text(
          text = shortcut.title,
          style = MaterialTheme.typography.labelLarge,
          color = MaterialTheme.colorScheme.onSurface,
          maxLines = 1,
        )
        Text(
          text = shortcut.category.shortLabel,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
        )
      }
      Icon(
        imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
        contentDescription = null,
        tint = accentColor,
        modifier = Modifier.size(15.dp),
      )
    }
  }
}
