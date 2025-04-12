package io.github.kez_lab.stopwatch_game.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.github.kez_lab.stopwatch_game.ui.screens.game.GameSelectionScreen
import io.github.kez_lab.stopwatch_game.ui.screens.game.play.GamePlayScreen
import io.github.kez_lab.stopwatch_game.ui.screens.home.HomeScreen
import io.github.kez_lab.stopwatch_game.ui.screens.player.PlayerRegistrationScreen
import io.github.kez_lab.stopwatch_game.ui.screens.result.ResultScreen
import kotlinx.serialization.Serializable

// 앱 내 라우트 정의
@Serializable
sealed class Routes {
    @Serializable
    data object Home : Routes()

    @Serializable
    data object PlayerRegistration : Routes()

    @Serializable
    data object GameSelection : Routes()

    @Serializable
    data class GamePlay(val gameId: String) : Routes()

    @Serializable
    data object Result : Routes()
}

/**
 * 앱의 메인 네비게이션 구성요소
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Home::class
    ) {
        composable<Routes.Home> {
            HomeScreen(navController = navController)
        }
        composable<Routes.PlayerRegistration> {
            PlayerRegistrationScreen(navController = navController)
        }
        composable<Routes.GameSelection> {
            GameSelectionScreen(navController = navController)
        }
        composable<Routes.GamePlay> { backStackEntry ->
            val args = backStackEntry.toRoute<Routes.GamePlay>()
            GamePlayScreen(
                navController = navController,
                gameId = args.gameId
            )
        }
        composable<Routes.Result> {
            ResultScreen(navController = navController)
        }
    }
}