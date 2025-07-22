package io.github.kez_lab.stopwatch_game.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.github.kez_lab.stopwatch_game.model.GameType
import io.github.kez_lab.stopwatch_game.ui.screens.game.GameSelectionScreen
import io.github.kez_lab.stopwatch_game.ui.screens.game.play.GamePlayScreen
import io.github.kez_lab.stopwatch_game.ui.screens.home.SplashScreen
import io.github.kez_lab.stopwatch_game.ui.screens.player.PlayerRegistrationScreen
import io.github.kez_lab.stopwatch_game.ui.screens.ranking.ResultScreen
import kotlinx.serialization.Serializable

// 앱 내 라우트 정의
@Serializable
sealed class Routes {
    @Serializable
    data object Splash : Routes()

    @Serializable
    data object PlayerRegistration : Routes()

    @Serializable
    data object GameSelection : Routes()

    @Serializable
    data class GamePlay(val gameType: GameType) : Routes()

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
        modifier = Modifier.safeDrawingPadding(),
        navController = navController,
        startDestination = Routes.Splash::class,
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis = 150))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = 150))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(durationMillis = 150))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(durationMillis = 150))
        }
    ) {
        composable<Routes.Splash> {
            SplashScreen(navController = navController)
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
            )
        }
        composable<Routes.Result> {
            ResultScreen(navController = navController)
        }
    }
}