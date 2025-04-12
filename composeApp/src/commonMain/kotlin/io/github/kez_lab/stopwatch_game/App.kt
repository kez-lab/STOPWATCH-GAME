package io.github.kez_lab.stopwatch_game

import androidx.compose.runtime.Composable
import io.github.kez_lab.stopwatch_game.theme.AppTheme
import io.github.kez_lab.stopwatch_game.ui.navigation.AppNavigation

@Composable
internal fun App() = AppTheme {
    AppNavigation()
}
