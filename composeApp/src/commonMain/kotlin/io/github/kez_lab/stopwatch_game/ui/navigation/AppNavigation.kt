package io.github.kez_lab.stopwatch_game.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import io.github.kez_lab.stopwatch_game.ui.screens.game.GameSelectionScreen
import io.github.kez_lab.stopwatch_game.ui.screens.game.play.GamePlayScreen
import io.github.kez_lab.stopwatch_game.ui.screens.home.HomeScreen
import io.github.kez_lab.stopwatch_game.ui.screens.player.PlayerRegistrationScreen
import io.github.kez_lab.stopwatch_game.ui.screens.result.ResultScreen

// 앱 내 화면 목록
sealed class Screen {
    data object Home : Screen()
    data object PlayerRegistration : Screen()
    data object GameSelection : Screen()
    data class GamePlay(val gameId: String) : Screen()
    data object Result : Screen()

    override fun equals(other: Any?): Boolean {
        return when {
            this is GamePlay && other is GamePlay -> this.gameId == other.gameId
            this::class == other?.let { it::class } -> true
            else -> false
        }
    }

    override fun hashCode(): Int {
        return when (this) {
            is GamePlay -> gameId.hashCode()
            else -> this::class.hashCode()
        }
    }
}

// 네비게이션 컨트롤러 인터페이스
interface NavigationController {
    fun navigateTo(screen: Screen)
    fun navigateToWithPopUpTo(
        screen: Screen, popUpTo: Screen? = null,
        inclusive: Boolean = false
    )

    fun navigateWithClearBackStack(screen: Screen)
    fun replaceCurrentScreen(screen: Screen)
    fun goBack()
    fun clearBackStack()
    fun backStackSize(): Int
}

// 네비게이션 컨트롤러 CompositionLocal
val LocalNavigationController = compositionLocalOf<NavigationController> {
    error("NavigationController not provided")
}

@Composable
fun AppNavigation() {
    val navigationState = remember { NavigationState() }

    // 안정적인 CompositionLocal 제공을 위해 navigationController는 navigationState에 의존
    val navigationController = remember(navigationState) {
        object : NavigationController {
            override fun navigateTo(screen: Screen) {
                navigationState.navigateTo(screen)
            }

            override fun navigateToWithPopUpTo(
                screen: Screen,
                popUpTo: Screen?,
                inclusive: Boolean
            ) {
                navigationState.navigateToWithPopUpTo(screen, popUpTo, inclusive)
            }

            override fun navigateWithClearBackStack(screen: Screen) {
                navigationState.navigateWithClearBackStack(screen)
            }

            override fun replaceCurrentScreen(screen: Screen) {
                navigationState.replaceCurrentScreen(screen)
            }

            override fun goBack() {
                navigationState.goBack()
            }

            override fun clearBackStack() {
                navigationState.clearBackStack()
            }

            override fun backStackSize(): Int = navigationState.backStackSize()
        }
    }

    // CompositionLocalProvider는 단 한 번 설정되고 내부 recomposition에 영향 없게 구성
    CompositionLocalProvider(LocalNavigationController provides navigationController) {
        when (val currentScreen = navigationState.currentScreen) {
            is Screen.Home -> HomeScreen()
            is Screen.PlayerRegistration -> PlayerRegistrationScreen()
            is Screen.GameSelection -> GameSelectionScreen()
            is Screen.GamePlay -> GamePlayScreen(currentScreen.gameId)
            is Screen.Result -> ResultScreen()
        }
    }
}