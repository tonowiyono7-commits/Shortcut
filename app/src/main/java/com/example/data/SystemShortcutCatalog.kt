package com.example.data

import android.content.Intent
import android.provider.Settings
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessibilityNew
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.AirplanemodeActive
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.BatteryChargingFull
import androidx.compose.material.icons.rounded.BatterySaver
import androidx.compose.material.icons.rounded.Bluetooth
import androidx.compose.material.icons.rounded.BrightnessMedium
import androidx.compose.material.icons.rounded.Cast
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.DataUsage
import androidx.compose.material.icons.rounded.DoNotDisturbOn
import androidx.compose.material.icons.rounded.Fingerprint
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Keyboard
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Nfc
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.NightlightRound
import androidx.compose.material.icons.rounded.Print
import androidx.compose.material.icons.rounded.QueryStats
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.SdStorage
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SettingsApplications
import androidx.compose.material.icons.rounded.SignalCellularAlt
import androidx.compose.material.icons.rounded.Storage
import androidx.compose.material.icons.rounded.SystemUpdate
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.rounded.VpnKey
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material.icons.rounded.WifiTethering
import com.example.model.SystemCategory
import com.example.model.SystemShortcut

object SystemShortcutCatalog {

  val defaultPinnedIds: Set<String> = setOf(
    "wifi",
    "bluetooth",
    "display",
    "battery",
    "apps",
    "developer_options",
  )

  val allShortcuts: List<SystemShortcut> = listOf(
    // --- CONNECTIVITY ---
    SystemShortcut(
      id = "wifi",
      title = "Wi-Fi",
      subtitle = "Kelola jaringan nirkabel & IP",
      primaryAction = Settings.ACTION_WIFI_SETTINGS,
      fallbackActions = listOf(Settings.ACTION_WIRELESS_SETTINGS, Settings.ACTION_SETTINGS),
      actionBadge = "WIFI",
      category = SystemCategory.CONNECTIVITY,
      icon = Icons.Rounded.Wifi,
      keywords = listOf("wifi", "internet", "wlan", "jaringan", "wireless"),
    ),
    SystemShortcut(
      id = "bluetooth",
      title = "Bluetooth",
      subtitle = "Pasangkan audio & perangkat",
      primaryAction = Settings.ACTION_BLUETOOTH_SETTINGS,
      fallbackActions = listOf(Settings.ACTION_WIRELESS_SETTINGS, Settings.ACTION_SETTINGS),
      actionBadge = "BT",
      category = SystemCategory.CONNECTIVITY,
      icon = Icons.Rounded.Bluetooth,
      keywords = listOf("bluetooth", "headset", "audio", "tws", "pairing"),
    ),
    SystemShortcut(
      id = "mobile_network",
      title = "Jaringan Seluler",
      subtitle = "SIM, operator & roaming data",
      primaryAction = Settings.ACTION_NETWORK_OPERATOR_SETTINGS,
      fallbackActions = listOf(Settings.ACTION_DATA_ROAMING_SETTINGS, Settings.ACTION_WIRELESS_SETTINGS, Settings.ACTION_SETTINGS),
      actionBadge = "CELLULAR",
      category = SystemCategory.CONNECTIVITY,
      icon = Icons.Rounded.SignalCellularAlt,
      keywords = listOf("sim", "seluler", "4g", "5g", "operator", "apn", "roaming"),
    ),
    SystemShortcut(
      id = "data_usage",
      title = "Penggunaan Data",
      subtitle = "Pantau kuota seluler & Wi-Fi",
      primaryAction = "android.settings.DATA_USAGE_SETTINGS",
      fallbackActions = listOf(Settings.ACTION_WIRELESS_SETTINGS, Settings.ACTION_SETTINGS),
      actionBadge = "DATA",
      category = SystemCategory.CONNECTIVITY,
      icon = Icons.Rounded.DataUsage,
      keywords = listOf("kuota", "data", "usage", "hemat data", "internet"),
    ),
    SystemShortcut(
      id = "hotspot_wireless",
      title = "Jaringan & Hotspot",
      subtitle = "Tethering & pengaturan nirkabel",
      primaryAction = Settings.ACTION_WIRELESS_SETTINGS,
      fallbackActions = listOf(Settings.ACTION_WIFI_SETTINGS, Settings.ACTION_SETTINGS),
      actionBadge = "WIRELESS",
      category = SystemCategory.CONNECTIVITY,
      icon = Icons.Rounded.WifiTethering,
      keywords = listOf("hotspot", "tethering", "berbagi", "nirkabel", "wireless"),
    ),
    SystemShortcut(
      id = "airplane_mode",
      title = "Mode Pesawat",
      subtitle = "Matikan seluruh sinyal radio",
      primaryAction = Settings.ACTION_AIRPLANE_MODE_SETTINGS,
      fallbackActions = listOf(Settings.ACTION_WIRELESS_SETTINGS, Settings.ACTION_SETTINGS),
      actionBadge = "AIRPLANE",
      category = SystemCategory.CONNECTIVITY,
      icon = Icons.Rounded.AirplanemodeActive,
      keywords = listOf("pesawat", "airplane", "flight", "sinyal", "offline"),
    ),
    SystemShortcut(
      id = "nfc",
      title = "NFC & Pembayaran",
      subtitle = "Koneksi jarak dekat & tap bayar",
      primaryAction = Settings.ACTION_NFC_SETTINGS,
      fallbackActions = listOf(Settings.ACTION_WIRELESS_SETTINGS, Settings.ACTION_SETTINGS),
      actionBadge = "NFC",
      category = SystemCategory.CONNECTIVITY,
      icon = Icons.Rounded.Nfc,
      keywords = listOf("nfc", "emoney", "kartu", "tap", "pembayaran"),
    ),
    SystemShortcut(
      id = "vpn",
      title = "VPN",
      subtitle = "Profil jaringan pribadi virtual",
      primaryAction = Settings.ACTION_VPN_SETTINGS,
      fallbackActions = listOf(Settings.ACTION_WIRELESS_SETTINGS, Settings.ACTION_SETTINGS),
      actionBadge = "VPN",
      category = SystemCategory.CONNECTIVITY,
      icon = Icons.Rounded.VpnKey,
      keywords = listOf("vpn", "proxy", "tunnel", "privasi jaringan"),
    ),

    // --- DISPLAY & SOUND ---
    SystemShortcut(
      id = "display",
      title = "Layar & Kecerahan",
      subtitle = "Resolusi, tema gelap & waktu tunggu",
      primaryAction = Settings.ACTION_DISPLAY_SETTINGS,
      fallbackActions = listOf(Settings.ACTION_SETTINGS),
      actionBadge = "DISPLAY",
      category = SystemCategory.DISPLAY_SOUND,
      icon = Icons.Rounded.BrightnessMedium,
      keywords = listOf("layar", "kecerahan", "brightness", "dark mode", "tema", "refresh rate"),
    ),
    SystemShortcut(
      id = "night_light",
      title = "Cahaya Malam",
      subtitle = "Filter cahaya biru & kenyamanan mata",
      primaryAction = "android.settings.NIGHT_DISPLAY_SETTINGS",
      fallbackActions = listOf(Settings.ACTION_DISPLAY_SETTINGS, Settings.ACTION_SETTINGS),
      actionBadge = "NIGHT",
      category = SystemCategory.DISPLAY_SOUND,
      icon = Icons.Rounded.NightlightRound,
      keywords = listOf("malam", "night light", "eye comfort", "blue light", "mata"),
    ),
    SystemShortcut(
      id = "sound",
      title = "Suara & Getaran",
      subtitle = "Volume media, dering & haptik",
      primaryAction = Settings.ACTION_SOUND_SETTINGS,
      fallbackActions = listOf(Settings.ACTION_SETTINGS),
      actionBadge = "SOUND",
      category = SystemCategory.DISPLAY_SOUND,
      icon = Icons.Rounded.GraphicEq,
      keywords = listOf("suara", "volume", "getar", "dering", "audio", "ringtone"),
    ),
    SystemShortcut(
      id = "dnd",
      title = "Jangan Ganggu",
      subtitle = "Atur jadwal senyap & prioritas",
      primaryAction = "android.settings.ZEN_MODE_PRIORITY_SETTINGS",
      fallbackActions = listOf(Settings.ACTION_SOUND_SETTINGS, Settings.ACTION_SETTINGS),
      actionBadge = "DND",
      category = SystemCategory.DISPLAY_SOUND,
      icon = Icons.Rounded.DoNotDisturbOn,
      keywords = listOf("jangan ganggu", "dnd", "senyap", "fokus", "zen"),
    ),
    SystemShortcut(
      id = "cast",
      title = "Cast Layar",
      subtitle = "Proyeksikan layar ke TV/monitor",
      primaryAction = Settings.ACTION_CAST_SETTINGS,
      fallbackActions = listOf(Settings.ACTION_DISPLAY_SETTINGS, Settings.ACTION_SETTINGS),
      actionBadge = "CAST",
      category = SystemCategory.DISPLAY_SOUND,
      icon = Icons.Rounded.Cast,
      keywords = listOf("cast", "mirroring", "tv", "layar nirkabel", "chromecast"),
    ),

    // --- BATTERY & STORAGE ---
    SystemShortcut(
      id = "battery",
      title = "Penggunaan Baterai",
      subtitle = "Statistik daya & konsumsi aplikasi",
      primaryAction = Intent.ACTION_POWER_USAGE_SUMMARY,
      fallbackActions = listOf(Settings.ACTION_BATTERY_SAVER_SETTINGS, Settings.ACTION_SETTINGS),
      actionBadge = "BATTERY",
      category = SystemCategory.BATTERY_STORAGE,
      icon = Icons.Rounded.BatteryChargingFull,
      keywords = listOf("baterai", "daya", "charging", "battery", "konsumsi"),
    ),
    SystemShortcut(
      id = "battery_saver",
      title = "Penghemat Baterai",
      subtitle = "Mode hemat daya & jadwal otomatis",
      primaryAction = Settings.ACTION_BATTERY_SAVER_SETTINGS,
      fallbackActions = listOf(Intent.ACTION_POWER_USAGE_SUMMARY, Settings.ACTION_SETTINGS),
      actionBadge = "SAVER",
      category = SystemCategory.BATTERY_STORAGE,
      icon = Icons.Rounded.BatterySaver,
      keywords = listOf("hemat", "power saver", "baterai", "low power"),
    ),
    SystemShortcut(
      id = "storage",
      title = "Penyimpanan Internal",
      subtitle = "Ruang kosong, cache & berkas sistem",
      primaryAction = Settings.ACTION_INTERNAL_STORAGE_SETTINGS,
      fallbackActions = listOf(Settings.ACTION_MEMORY_CARD_SETTINGS, Settings.ACTION_SETTINGS),
      actionBadge = "STORAGE",
      category = SystemCategory.BATTERY_STORAGE,
      icon = Icons.Rounded.Storage,
      keywords = listOf("penyimpanan", "storage", "memori", "ruang", "cache"),
    ),
    SystemShortcut(
      id = "sd_card",
      title = "Kartu Memori",
      subtitle = "Kelola penyimpanan eksternal",
      primaryAction = Settings.ACTION_MEMORY_CARD_SETTINGS,
      fallbackActions = listOf(Settings.ACTION_INTERNAL_STORAGE_SETTINGS, Settings.ACTION_SETTINGS),
      actionBadge = "SDCARD",
      category = SystemCategory.BATTERY_STORAGE,
      icon = Icons.Rounded.SdStorage,
      keywords = listOf("sd card", "eksternal", "memori", "usb otg"),
    ),

    // --- APPS & NOTIFICATIONS ---
    SystemShortcut(
      id = "apps",
      title = "Semua Aplikasi",
      subtitle = "Daftar app terpasang, izin & cache",
      primaryAction = Settings.ACTION_APPLICATION_SETTINGS,
      fallbackActions = listOf(Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS, Settings.ACTION_SETTINGS),
      actionBadge = "APPS",
      category = SystemCategory.APPS_NOTIFICATIONS,
      icon = Icons.Rounded.Apps,
      keywords = listOf("aplikasi", "app", "uninstall", "hapus cache", "kelola"),
    ),
    SystemShortcut(
      id = "default_apps",
      title = "Aplikasi Default",
      subtitle = "Browser, peluncur & asisten utama",
      primaryAction = Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS,
      fallbackActions = listOf(Settings.ACTION_APPLICATION_SETTINGS, Settings.ACTION_SETTINGS),
      actionBadge = "DEFAULT",
      category = SystemCategory.APPS_NOTIFICATIONS,
      icon = Icons.Rounded.SettingsApplications,
      keywords = listOf("default", "launcher", "browser", "bawaan", "utama"),
    ),
    SystemShortcut(
      id = "notifications",
      title = "Notifikasi Sistem",
      subtitle = "Riwayat, gelembung & izin pemberitahuan",
      primaryAction = "android.settings.ALL_APPS_NOTIFICATION_SETTINGS",
      fallbackActions = listOf("android.settings.NOTIFICATION_SETTINGS", Settings.ACTION_APPLICATION_SETTINGS, Settings.ACTION_SETTINGS),
      actionBadge = "NOTIF",
      category = SystemCategory.APPS_NOTIFICATIONS,
      icon = Icons.Rounded.NotificationsActive,
      keywords = listOf("notifikasi", "pemberitahuan", "notif", "riwayat"),
    ),
    SystemShortcut(
      id = "overlay_permission",
      title = "Tampil di Atas App",
      subtitle = "Izin jendela mengambang / overlay",
      primaryAction = Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
      fallbackActions = listOf(Settings.ACTION_APPLICATION_SETTINGS, Settings.ACTION_SETTINGS),
      actionBadge = "OVERLAY",
      category = SystemCategory.APPS_NOTIFICATIONS,
      icon = Icons.Rounded.Layers,
      keywords = listOf("overlay", "mengambang", "floating", "izin khusus"),
    ),
    SystemShortcut(
      id = "usage_access",
      title = "Akses Penggunaan",
      subtitle = "Aplikasi pemantau durasi layar",
      primaryAction = Settings.ACTION_USAGE_ACCESS_SETTINGS,
      fallbackActions = listOf(Settings.ACTION_APPLICATION_SETTINGS, Settings.ACTION_SETTINGS),
      actionBadge = "USAGE",
      category = SystemCategory.APPS_NOTIFICATIONS,
      icon = Icons.Rounded.QueryStats,
      keywords = listOf("akses penggunaan", "usage access", "digital wellbeing", "statistik"),
    ),

    // --- PRIVACY & SECURITY ---
    SystemShortcut(
      id = "security",
      title = "Keamanan & Kunci",
      subtitle = "PIN, enkripsi & Find My Device",
      primaryAction = Settings.ACTION_SECURITY_SETTINGS,
      fallbackActions = listOf(Settings.ACTION_SETTINGS),
      actionBadge = "SECURITY",
      category = SystemCategory.PRIVACY_SECURITY,
      icon = Icons.Rounded.Lock,
      keywords = listOf("keamanan", "pin", "sandi", "kunci layar", "security"),
    ),
    SystemShortcut(
      id = "biometrics",
      title = "Sidik Jari & Wajah",
      subtitle = "Daftarkan biometrik perangkat",
      primaryAction = "android.settings.BIOMETRIC_ENROLL",
      fallbackActions = listOf(Settings.ACTION_SECURITY_SETTINGS, Settings.ACTION_SETTINGS),
      actionBadge = "BIOMETRIC",
      category = SystemCategory.PRIVACY_SECURITY,
      icon = Icons.Rounded.Fingerprint,
      keywords = listOf("sidik jari", "fingerprint", "face unlock", "biometrik"),
    ),
    SystemShortcut(
      id = "privacy",
      title = "Dasbor Privasi",
      subtitle = "Pengelola izin kamera, mic & lokasi",
      primaryAction = "android.settings.PRIVACY_SETTINGS",
      fallbackActions = listOf(Settings.ACTION_SECURITY_SETTINGS, Settings.ACTION_SETTINGS),
      actionBadge = "PRIVACY",
      category = SystemCategory.PRIVACY_SECURITY,
      icon = Icons.Rounded.Security,
      keywords = listOf("privasi", "izin", "kamera", "mikrofon", "privacy"),
    ),
    SystemShortcut(
      id = "location",
      title = "Lokasi & GPS",
      subtitle = "Layanan lokasi & akurasi Wi-Fi",
      primaryAction = Settings.ACTION_LOCATION_SOURCE_SETTINGS,
      fallbackActions = listOf(Settings.ACTION_SETTINGS),
      actionBadge = "GPS",
      category = SystemCategory.PRIVACY_SECURITY,
      icon = Icons.Rounded.LocationOn,
      keywords = listOf("lokasi", "gps", "maps", "posisi"),
    ),
    SystemShortcut(
      id = "accounts",
      title = "Akun & Sinkronisasi",
      subtitle = "Akun Google, cadangan & sinkron",
      primaryAction = Settings.ACTION_SYNC_SETTINGS,
      fallbackActions = listOf(Settings.ACTION_ADD_ACCOUNT, Settings.ACTION_SETTINGS),
      actionBadge = "ACCOUNTS",
      category = SystemCategory.PRIVACY_SECURITY,
      icon = Icons.Rounded.AccountCircle,
      keywords = listOf("akun", "google", "sinkronisasi", "sync", "email"),
    ),

    // --- SYSTEM & DEVELOPER ---
    SystemShortcut(
      id = "developer_options",
      title = "Opsi Developer",
      subtitle = "USB Debugging, skala animasi & log",
      primaryAction = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
      fallbackActions = listOf(Settings.ACTION_DEVICE_INFO_SETTINGS, Settings.ACTION_SETTINGS),
      actionBadge = "DEV_OPTS",
      category = SystemCategory.SYSTEM_DEV,
      icon = Icons.Rounded.Code,
      keywords = listOf("developer", "pengembang", "adb", "usb debugging", "animasi", "dpi"),
    ),
    SystemShortcut(
      id = "about_phone",
      title = "Tentang Ponsel",
      subtitle = "Versi Android, nomor build & IMEI",
      primaryAction = Settings.ACTION_DEVICE_INFO_SETTINGS,
      fallbackActions = listOf(Settings.ACTION_SETTINGS),
      actionBadge = "DEVICE",
      category = SystemCategory.SYSTEM_DEV,
      icon = Icons.Rounded.Info,
      keywords = listOf("tentang", "about", "versi android", "build number", "imei", "model"),
    ),
    SystemShortcut(
      id = "system_update",
      title = "Pembaruan Sistem",
      subtitle = "Cek update OS & patch keamanan",
      primaryAction = "android.settings.SYSTEM_UPDATE_SETTINGS",
      fallbackActions = listOf(Settings.ACTION_DEVICE_INFO_SETTINGS, Settings.ACTION_SETTINGS),
      actionBadge = "OTA",
      category = SystemCategory.SYSTEM_DEV,
      icon = Icons.Rounded.SystemUpdate,
      keywords = listOf("update", "pembaruan", "ota", "patch", "sistem"),
    ),
    SystemShortcut(
      id = "language_input",
      title = "Bahasa & Masukan",
      subtitle = "Preferensi bahasa sistem & wilayah",
      primaryAction = Settings.ACTION_LOCALE_SETTINGS,
      fallbackActions = listOf(Settings.ACTION_INPUT_METHOD_SETTINGS, Settings.ACTION_SETTINGS),
      actionBadge = "LOCALE",
      category = SystemCategory.SYSTEM_DEV,
      icon = Icons.Rounded.Language,
      keywords = listOf("bahasa", "language", "locale", "indonesia", "english"),
    ),
    SystemShortcut(
      id = "keyboard",
      title = "Keyboard Virtual",
      subtitle = "Kelola papan ketik & metode input",
      primaryAction = Settings.ACTION_INPUT_METHOD_SETTINGS,
      fallbackActions = listOf(Settings.ACTION_LOCALE_SETTINGS, Settings.ACTION_SETTINGS),
      actionBadge = "KEYBOARD",
      category = SystemCategory.SYSTEM_DEV,
      icon = Icons.Rounded.Keyboard,
      keywords = listOf("keyboard", "gboard", "ketik", "input"),
    ),
    SystemShortcut(
      id = "date_time",
      title = "Tanggal & Waktu",
      subtitle = "Zona waktu otomatis & format 24 jam",
      primaryAction = Settings.ACTION_DATE_SETTINGS,
      fallbackActions = listOf(Settings.ACTION_SETTINGS),
      actionBadge = "TIME",
      category = SystemCategory.SYSTEM_DEV,
      icon = Icons.Rounded.Schedule,
      keywords = listOf("jam", "waktu", "tanggal", "zona waktu", "ntp"),
    ),
    SystemShortcut(
      id = "accessibility",
      title = "Aksesibilitas",
      subtitle = "TalkBack, pembesaran & kontras",
      primaryAction = Settings.ACTION_ACCESSIBILITY_SETTINGS,
      fallbackActions = listOf(Settings.ACTION_SETTINGS),
      actionBadge = "A11Y",
      category = SystemCategory.SYSTEM_DEV,
      icon = Icons.Rounded.AccessibilityNew,
      keywords = listOf("aksesibilitas", "talkback", "ukuran font", "kontras"),
    ),
    SystemShortcut(
      id = "printing",
      title = "Layanan Cetak",
      subtitle = "Printer jaringan & spooler cetak",
      primaryAction = Settings.ACTION_PRINT_SETTINGS,
      fallbackActions = listOf(Settings.ACTION_SETTINGS),
      actionBadge = "PRINT",
      category = SystemCategory.SYSTEM_DEV,
      icon = Icons.Rounded.Print,
      keywords = listOf("print", "cetak", "printer", "pdf"),
    ),
    SystemShortcut(
      id = "main_settings",
      title = "Pengaturan Utama",
      subtitle = "Beranda lengkap Setelan Android",
      primaryAction = Settings.ACTION_SETTINGS,
      fallbackActions = emptyList(),
      actionBadge = "SETTINGS",
      category = SystemCategory.SYSTEM_DEV,
      icon = Icons.Rounded.Settings,
      keywords = listOf("setelan", "pengaturan", "settings", "utama"),
    ),
    SystemShortcut(
      id = "sound_tuner",
      title = "Caption & Subtitel",
      subtitle = "Teks otomatis & preferensi subtitel",
      primaryAction = Settings.ACTION_CAPTIONING_SETTINGS,
      fallbackActions = listOf(Settings.ACTION_ACCESSIBILITY_SETTINGS, Settings.ACTION_SETTINGS),
      actionBadge = "CAPTIONS",
      category = SystemCategory.SYSTEM_DEV,
      icon = Icons.Rounded.Tune,
      keywords = listOf("caption", "subtitel", "teks", "aksesibilitas"),
    ),
  )

  fun findById(id: String): SystemShortcut? = allShortcuts.firstOrNull { it.id == id }
}
