package com.example.data

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import android.provider.Settings
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.example.MainActivity
import com.example.model.SystemShortcut

sealed interface LaunchOutcome {
  data class Success(val shortcut: SystemShortcut, val usedFallback: Boolean) : LaunchOutcome
  data class Unavailable(val shortcut: SystemShortcut) : LaunchOutcome
}

object SystemShortcutLauncher {

  const val EXTRA_SHORTCUT_ACTION = "com.example.extra.SHORTCUT_ACTION"
  const val EXTRA_SHORTCUT_FALLBACK = "com.example.extra.SHORTCUT_FALLBACK"
  const val EXTRA_SHORTCUT_ID = "com.example.extra.SHORTCUT_ID"

  fun launch(context: Context, shortcut: SystemShortcut): LaunchOutcome {
    val actionsToTry = listOf(shortcut.primaryAction) + shortcut.fallbackActions
    actionsToTry.forEachIndexed { index, action ->
      if (tryStartSettingsAction(context, action)) {
        return LaunchOutcome.Success(shortcut = shortcut, usedFallback = index > 0)
      }
    }
    return LaunchOutcome.Unavailable(shortcut)
  }

  fun launchRawAction(
    context: Context,
    primaryAction: String,
    fallbackAction: String? = null,
  ): Boolean {
    if (tryStartSettingsAction(context, primaryAction)) return true
    if (!fallbackAction.isNullOrBlank() && tryStartSettingsAction(context, fallbackAction)) return true
    return tryStartSettingsAction(context, Settings.ACTION_SETTINGS)
  }

  private fun tryStartSettingsAction(context: Context, action: String): Boolean {
    return try {
      val intent = Intent(action).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      }
      context.startActivity(intent)
      true
    } catch (_: ActivityNotFoundException) {
      false
    } catch (_: SecurityException) {
      false
    } catch (_: Exception) {
      false
    }
  }

  fun isPrimarySupported(context: Context, shortcut: SystemShortcut): Boolean {
    val pm = context.packageManager
    val intent = Intent(shortcut.primaryAction)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      pm.resolveActivity(
        intent,
        PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong()),
      ) != null
    } else {
      @Suppress("DEPRECATION")
      pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null
    }
  }

  fun pinToHomeScreen(context: Context, shortcut: SystemShortcut): Boolean {
    if (!ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
      return false
    }

    val launchIntent = Intent(context, MainActivity::class.java).apply {
      action = Intent.ACTION_VIEW
      putExtra(EXTRA_SHORTCUT_ID, shortcut.id)
      putExtra(EXTRA_SHORTCUT_ACTION, shortcut.primaryAction)
      putExtra(
        EXTRA_SHORTCUT_FALLBACK,
        shortcut.fallbackActions.firstOrNull() ?: Settings.ACTION_SETTINGS,
      )
      addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }

    val badgeBitmap = renderShortcutBadgeBitmap(shortcut)
    val info = ShortcutInfoCompat.Builder(context, "sys_shortcut_${shortcut.id}")
      .setShortLabel(shortcut.title)
      .setLongLabel("${shortcut.title} — ${shortcut.category.shortLabel}")
      .setIcon(IconCompat.createWithBitmap(badgeBitmap))
      .setIntent(launchIntent)
      .build()

    return ShortcutManagerCompat.requestPinShortcut(context, info, null)
  }

  private fun renderShortcutBadgeBitmap(shortcut: SystemShortcut): Bitmap {
    val size = 192
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
      color = shortcut.category.accentColor(isDark = false).toArgb()
    }
    val cardRect = RectF(12f, 12f, size - 12f, size - 12f)
    canvas.drawRoundRect(cardRect, 44f, 44f, bgPaint)

    val innerRingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
      color = 0x33FFFFFF
      style = Paint.Style.STROKE
      strokeWidth = 6f
    }
    canvas.drawRoundRect(RectF(24f, 24f, size - 24f, size - 24f), 34f, 34f, innerRingPaint)

    val boltPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
      color = 0xFFFFFFFF.toInt()
      style = Paint.Style.FILL
    }
    val boltPath = Path().apply {
      moveTo(size * 0.56f, size * 0.24f)
      lineTo(size * 0.34f, size * 0.54f)
      lineTo(size * 0.50f, size * 0.54f)
      lineTo(size * 0.44f, size * 0.76f)
      lineTo(size * 0.66f, size * 0.46f)
      lineTo(size * 0.50f, size * 0.46f)
      close()
    }
    canvas.drawPath(boltPath, boltPaint)

    return bitmap
  }
}
