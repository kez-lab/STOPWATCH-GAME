package io.github.kez_lab.stopwatch_game.ui.screens.game.play

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import compose.icons.FeatherIcons
import compose.icons.feathericons.SkipForward
import io.github.kez_lab.stopwatch_game.model.GameType
import io.github.kez_lab.stopwatch_game.ui.components.AppBar
import io.github.kez_lab.stopwatch_game.ui.components.AppBarActionItem
import io.github.kez_lab.stopwatch_game.ui.navigation.Routes
import io.github.kez_lab.stopwatch_game.ui.viewmodel.LocalAppViewModel
import io.github.kez_lab.stopwatch_game.viewmodel.GamePlayViewModel
import io.github.kez_lab.stopwatch_game.viewmodel.GameTimerViewModel

/**
 * 중첩 네비게이션 라우트
 */
internal sealed class GamePlayRoutes(val route: String) {
    data object Ready : GamePlayRoutes("ready")
    data object Countdown : GamePlayRoutes("countdown")
    data object Playing : GamePlayRoutes("playing")
    data object Result : GamePlayRoutes("result")
}


/**
 * 게임 플레이 화면 (중첩 네비게이션 호스트)
 */
@Composable
fun GamePlayScreen(
    navController: NavHostController,
    gameType: GameType,
) {
    val appViewModel = LocalAppViewModel.current
    val appUiState by appViewModel.uiState.collectAsState()

    val timerViewModel: GameTimerViewModel = viewModel { GameTimerViewModel(gameType) }
    val gamePlayViewModel: GamePlayViewModel = viewModel { GamePlayViewModel(appViewModel, gameType) }
    val uiState by timerViewModel.uiState.collectAsState()
    val game = uiState.game

    val nestedNavController = rememberNavController()
    var showBackConfirmation by rememberSaveable { mutableStateOf(false) }

    // 이벤트 처리
    LaunchedEffect(gamePlayViewModel) {
        gamePlayViewModel.events.collect { event ->
            when (event) {
                is GamePlayEvent.NavigateToResult -> {
                    appViewModel.calculateRanks()
                    navController.navigate(Routes.Result) {
                        popUpTo(Routes.GamePlay(gameType.id)) { inclusive = true }
                    }
                }
                is GamePlayEvent.NavigateToReady -> {
                    nestedNavController.navigate(GamePlayRoutes.Ready.route) {
                        popUpTo(nestedNavController.graph.startDestinationId) { inclusive = true }
                    }
                }
                is GamePlayEvent.PopBackStack -> {
                    navController.popBackStack()
                }
                is GamePlayEvent.ShowBackConfirmation -> {
                    showBackConfirmation = true
                }
                is GamePlayEvent.HideBackConfirmation -> {
                    showBackConfirmation = false
                }
            }
        }
    }

    // ViewModel에서 로직 처리
    val moveToNextPlayer = {
        gamePlayViewModel.moveToNextPlayer()
    }

    val handleBackPress = {
        val isAtRootLevel = nestedNavController.previousBackStackEntry == null
        val currentRoute = nestedNavController.currentBackStackEntry?.destination?.route
        
        if (isAtRootLevel) {
            gamePlayViewModel.handleBackPress(isAtRootLevel = true, currentRoute = null)
        } else {
            if (currentRoute == GamePlayRoutes.Playing.route || currentRoute == GamePlayRoutes.Countdown.route) {
                gamePlayViewModel.handleBackPress(isAtRootLevel = false, currentRoute = currentRoute)
            } else {
                nestedNavController.popBackStack()
            }
        }
    }

    // 게임 시작 시 타이머 설정 (한 번만 실행)
    LaunchedEffect(game) {
        timerViewModel.setGameType(game)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppBar(
                title = game.label,
                onBackClick = { handleBackPress() },
                currentPlayer = appUiState.currentPlayer,
                showPlayerInfo = true,
                actions = {
                    AppBarActionItem(
                        icon = FeatherIcons.SkipForward,
                        contentDescription = "패스",
                        onClick = { /* TODO: 패스 기능 구현 */ },
                        enabled = false
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 중첩 NavHost
            NavHost(
                navController = nestedNavController,
                startDestination = GamePlayRoutes.Ready.route,
                enterTransition = {
                    GamePlayAnimations.getEnterTransition(
                        initialState.destination.route,
                        targetState.destination.route
                    )
                },
                exitTransition = {
                    GamePlayAnimations.getExitTransition(
                        initialState.destination.route,
                        targetState.destination.route
                    )
                },
                popEnterTransition = {
                    GamePlayAnimations.getPopEnterTransition()
                },
                popExitTransition = {
                    GamePlayAnimations.getPopExitTransition()
                }
            ) {
                composable(GamePlayRoutes.Ready.route) {
                    ReadyScreen(
                        navController = nestedNavController,
                        game = game
                    )
                }
                composable(GamePlayRoutes.Countdown.route) {
                    CountdownScreen(
                        navController = nestedNavController,
                        timerViewModel = timerViewModel
                    )
                }
                composable(GamePlayRoutes.Playing.route) {
                    PlayingScreen(
                        navController = nestedNavController,
                        game = game,
                        timerViewModel = timerViewModel,
                    )
                }
                composable(GamePlayRoutes.Result.route) {
                    ResultScreen(
                        game = game,
                        onNextPlayer = { moveToNextPlayer() }
                    )
                }
            }
        }

        // 뒤로가기 확인 다이얼로그
        if (showBackConfirmation) {
            GameExitConfirmDialog(
                onConfirm = {
                    gamePlayViewModel.confirmGameExit()
                },
                onDismiss = {
                    gamePlayViewModel.cancelGameExit()
                }
            )
        }
    }
}