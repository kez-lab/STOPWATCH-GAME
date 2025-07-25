package io.github.kez_lab.stopwatch_game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.kez_lab.stopwatch_game.theme.AppTheme
import io.github.kez_lab.stopwatch_game.ui.navigation.AppNavigation
import io.github.kez_lab.stopwatch_game.ui.viewmodel.LocalAppViewModel
import io.github.kez_lab.stopwatch_game.viewmodel.AppViewModel
import org.jetbrains.compose.resources.Font
import stopwatch_game.composeapp.generated.resources.NotoSans
import stopwatch_game.composeapp.generated.resources.Res

@Composable
internal fun App(fontFamily: FontFamily = FontFamily(Font(resource = Res.font.NotoSans))) =
    AppTheme(fontFamily = fontFamily) {
        val appViewModel: AppViewModel = viewModel {
            AppViewModel()
        }
        CompositionLocalProvider(LocalAppViewModel provides appViewModel) {
            AppNavigation()
        }
    }
