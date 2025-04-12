package io.github.kez_lab.stopwatch_game.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import io.github.kez_lab.stopwatch_game.ui.screens.home.HomeScreen
import io.github.kez_lab.stopwatch_game.ui.screens.player.PlayerRegistrationScreen
import io.github.kez_lab.stopwatch_game.ui.screens.game.GameSelectionScreen
import io.github.kez_lab.stopwatch_game.ui.screens.game.play.GamePlayScreen
import io.github.kez_lab.stopwatch_game.ui.screens.result.ResultScreen

// 앱 내 화면 목록
sealed class Screen {
    data object Home : Screen()
    data object PlayerRegistration : Screen()
    data object GameSelection : Screen()
    data class GamePlay(val gameId: String) : Screen()
    data object Result : Screen()
}

// 네비게이션 컨트롤러 인터페이스
interface NavigationController {
    fun navigateTo(screen: Screen)
    fun goBack()
}

// 네비게이션 컨트롤러 CompositionLocal
val LocalNavigationController = compositionLocalOf<NavigationController> { 
    error("NavigationController not provided") 
}

@Composable
fun AppNavigation() {
    val navigationState = remember { NavigationState() }
    
    // 네비게이션 컨트롤러 구현
    val navigationController = remember {
        object : NavigationController {
            override fun navigateTo(screen: Screen) {
                navigationState.navigateTo(screen)
            }
            
            override fun goBack() {
                navigationState.goBack()
            }
        }
    }
    
    // 네비게이션 컨트롤러를 하위 컴포넌트에 제공
    CompositionLocalProvider(LocalNavigationController provides navigationController) {
        // 현재 화면 렌더링
        when (val currentScreen = navigationState.currentScreen) {
            is Screen.Home -> HomeScreen()
            is Screen.PlayerRegistration -> PlayerRegistrationScreen()
            is Screen.GameSelection -> GameSelectionScreen()
            is Screen.GamePlay -> GamePlayScreen(gameId = currentScreen.gameId)
            is Screen.Result -> ResultScreen()
        }
    }
} 