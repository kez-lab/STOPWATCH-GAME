package io.github.kez_lab.stopwatch_game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.kez_lab.stopwatch_game.theme.AppTheme
import io.github.kez_lab.stopwatch_game.ui.navigation.AppNavigation
import io.github.kez_lab.stopwatch_game.ui.viewmodel.LocalAppViewModel
import io.github.kez_lab.stopwatch_game.viewmodel.AppViewModel

@Composable
internal fun App() = AppTheme {
    val appViewModel: AppViewModel = viewModel()

    CompositionLocalProvider(LocalAppViewModel provides appViewModel) {
        AppNavigation()
    }
}
