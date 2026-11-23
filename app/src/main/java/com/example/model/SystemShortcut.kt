package com.example.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.AccentAppsDark
import com.example.ui.theme.AccentAppsLight
import com.example.ui.theme.AccentBatteryDark
import com.example.ui.theme.AccentBatteryLight
import com.example.ui.theme.AccentConnectivityDark
import com.example.ui.theme.AccentConnectivityLight
import com.example.ui.theme.AccentDisplayDark
import com.example.ui.theme.AccentDisplayLight
import com.example.ui.theme.AccentPrivacyDark
import com.example.ui.theme.AccentPrivacyLight
import com.example.ui.theme.AccentSystemDark
import com.example.ui.theme.AccentSystemLight

enum class SystemCategory(
  val label: String,
  val shortLabel: String,
) {
  ALL(label = "Semua", shortLabel = "Semua"),
  CONNECTIVITY(label = "Konektivitas", shortLabel = "Jaringan"),
  DISPLAY_SOUND(label = "Tampilan & Suara", shortLabel = "Layar & Suara"),
  BATTERY_STORAGE(label = "Baterai & Memori", shortLabel = "Daya & Memori"),
  APPS_NOTIFICATIONS(label = "Aplikasi & Notif", shortLabel = "Aplikasi"),
  PRIVACY_SECURITY(label = "Privasi & Keamanan", shortLabel = "Keamanan"),
  SYSTEM_DEV(label = "Sistem & Dev", shortLabel = "Sistem");

  fun accentColor(isDark: Boolean): Color = when (this) {
    ALL -> if (isDark) AccentSystemDark else AccentSystemLight
    CONNECTIVITY -> if (isDark) AccentConnectivityDark else AccentConnectivityLight
    DISPLAY_SOUND -> if (isDark) AccentDisplayDark else AccentDisplayLight
    BATTERY_STORAGE -> if (isDark) AccentBatteryDark else AccentBatteryLight
    APPS_NOTIFICATIONS -> if (isDark) AccentAppsDark else AccentAppsLight
    PRIVACY_SECURITY -> if (isDark) AccentPrivacyDark else AccentPrivacyLight
    SYSTEM_DEV -> if (isDark) AccentSystemDark else AccentSystemLight
  }
}

data class SystemShortcut(
  val id: String,
  val title: String,
  val subtitle: String,
  val primaryAction: String,
  val fallbackActions: List<String> = listOf("android.settings.SETTINGS"),
  val actionBadge: String,
  val category: SystemCategory,
  val icon: ImageVector,
  val keywords: List<String> = emptyList(),
)
