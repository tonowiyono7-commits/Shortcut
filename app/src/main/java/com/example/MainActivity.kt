package com.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.data.SystemShortcutLauncher
import com.example.ui.ShortcutDashboardScreen
import com.example.ui.ShortcutViewModel
import com.example.ui.theme.PintasanSistemTheme

class MainActivity : ComponentActivity() {

  private val viewModel: ShortcutViewModel by viewModels {
    ShortcutViewModel.Factory(applicationContext)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (handlePinnedShortcutLaunch(intent)) {
      finish()
      return
    }
    enableEdgeToEdge()
    setContent {
      PintasanSistemTheme {
        ShortcutDashboardScreen(viewModel = viewModel)
      }
    }
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    if (handlePinnedShortcutLaunch(intent)) {
      finish()
    }
  }

  private fun handlePinnedShortcutLaunch(intent: Intent?): Boolean {
    val action = intent?.getStringExtra(SystemShortcutLauncher.EXTRA_SHORTCUT_ACTION)
      ?: return false
    val fallback = intent.getStringExtra(SystemShortcutLauncher.EXTRA_SHORTCUT_FALLBACK)
    SystemShortcutLauncher.launchRawAction(
      context = this,
      primaryAction = action,
      fallbackAction = fallback,
    )
    return true
  }
}
