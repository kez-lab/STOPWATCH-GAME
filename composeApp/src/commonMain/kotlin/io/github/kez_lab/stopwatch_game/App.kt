package io.github.kez_lab.stopwatch_game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.kez_lab.stopwatch_game.theme.AppTheme
import io.github.kez_lab.stopwatch_game.ui.navigation.AppNavigation
import io.github.kez_lab.stopwatch_game.viewmodel.AppViewModel

@Composable
internal fun App() = AppTheme {
    // 앱 전체에서 공유될 단일 AppViewModel 인스턴스 생성
    val appViewModel: AppViewModel = viewModel()
    
    // ViewModel을 AppNavigation에 전달
    AppNavigation(appViewModel = appViewModel)
}
