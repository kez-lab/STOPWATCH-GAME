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
import io.github.kez_lab.stopwatch_game.viewmodel.AppViewModel
import kotlinx.serialization.Serializable

// 앱 내 라우트 정의
@Serializable
sealed class Routes(val route: String) {
    @Serializable
    data object Home : Routes("home")

    @Serializable
    data object PlayerRegistration : Routes("player_registration")

    @Serializable
    data object GameSelection : Routes("game_selection")

    @Serializable
    data class GamePlay(val gameId: String) : Routes("game_play")

    @Serializable
    data object Result : Routes("result")
}

/**
 * 앱의 메인 네비게이션 구성요소
 */
@Composable
fun AppNavigation(appViewModel: AppViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Home::class
    ) {
        composable<Routes.Home> {
            HomeScreen(navController = navController, appViewModel = appViewModel)
        }
        composable<Routes.PlayerRegistration> {
            PlayerRegistrationScreen(navController = navController, appViewModel = appViewModel)
        }
        composable<Routes.GameSelection> {
            GameSelectionScreen(navController = navController, appViewModel = appViewModel)
        }
        composable<Routes.GamePlay> { backStackEntry ->
            val args = backStackEntry.toRoute<Routes.GamePlay>()
            GamePlayScreen(
                navController = navController,
                gameId = args.gameId,
                appViewModel = appViewModel
            )
        }
        composable<Routes.Result> {
            ResultScreen(navController = navController, appViewModel = appViewModel)
        }
    }
}