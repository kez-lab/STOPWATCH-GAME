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
    // 앱 전체에서 공유될 단일 AppViewModel 인스턴스 생성
    val appViewModel: AppViewModel = viewModel()
    
    // CompositionLocalProvider를 통해 앱 전체에 AppViewModel 제공
    CompositionLocalProvider(LocalAppViewModel provides appViewModel) {
        // 명시적으로 ViewModel을 전달할 필요 없음
        AppNavigation()
    }
}
