package io.github.kez_lab.stopwatch_game.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.github.kez_lab.stopwatch_game.ui.screens.game.GameSelectionScreen
import io.github.kez_lab.stopwatch_game.ui.screens.game.play.GamePlayScreen
import io.github.kez_lab.stopwatch_game.ui.screens.home.HomeScreen
import io.github.kez_lab.stopwatch_game.ui.screens.player.PlayerRegistrationScreen
import io.github.kez_lab.stopwatch_game.ui.screens.result.ResultScreen

// 앱 내 라우트 정의
object Routes {
    const val HOME = "home"
    const val PLAYER_REGISTRATION = "player_registration"
    const val GAME_SELECTION = "game_selection"
    const val GAME_PLAY = "game_play/{gameId}"
    const val RESULT = "result"

    // 매개변수가 있는 경로를 위한 도우미 함수
    fun gamePlay(gameId: String) = "game_play/$gameId"
}

/**
 * 앱의 메인 네비게이션 구성요소
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(navController)
        }
        
        composable(Routes.PLAYER_REGISTRATION) {
            PlayerRegistrationScreen(navController)
        }
        
        composable(Routes.GAME_SELECTION) {
            GameSelectionScreen(navController)
        }
        
        composable(
            route = Routes.GAME_PLAY,
            arguments = listOf(navArgument("gameId") { type = NavType.StringType })
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId") ?: ""
            GamePlayScreen(navController, gameId)
        }
        
        composable(Routes.RESULT) {
            ResultScreen(navController)
        }
    }
}