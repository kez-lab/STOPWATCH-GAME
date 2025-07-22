package io.github.kez_lab.stopwatch_game.ui.screens.game.play

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import io.github.kez_lab.stopwatch_game.ui.components.AppBar
import io.github.kez_lab.stopwatch_game.ui.components.AppBarActionItem
import io.github.kez_lab.stopwatch_game.ui.navigation.Routes
import io.github.kez_lab.stopwatch_game.ui.viewmodel.LocalAppViewModel
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
) {
    val appViewModel = LocalAppViewModel.current
    val appUiState by appViewModel.uiState.collectAsState()

    val timerViewModel: GameTimerViewModel = viewModel { GameTimerViewModel() }
    val uiState by timerViewModel.uiState.collectAsState()
    val game = uiState.game

    val nestedNavController = rememberNavController()
    var showBackConfirmation by rememberSaveable { mutableStateOf(false) }

    // 게임 전체 종료 처리
    val finishGame = {
        appViewModel.calculateRanks()
        navController.navigate(Routes.Result) {
            popUpTo(Routes.GamePlay(game)) { inclusive = true }
        }
    }

    // 다음 플레이어로 이동
    val moveToNextPlayer = {
        if (appViewModel.moveToNextPlayer()) {
            timerViewModel.resetTimer()
            // ReadyScreen으로 돌아가기 (백스택 초기화)
            nestedNavController.navigate(GamePlayRoutes.Ready.route) {
                popUpTo(nestedNavController.graph.startDestinationId) { inclusive = true }
            }
        } else {
            finishGame()
        }
    }

    // 뒤로가기 처리
    val handleBackPress = {
        // 중첩 네비게이션의 현재 백스택에 화면이 1개만 남았는지 (즉, ReadyScreen인지) 확인
        if (nestedNavController.previousBackStackEntry == null) {
            navController.popBackStack()
        } else {
            // 플레이 중(카운트다운 포함) 뒤로가기 시 확인 다이얼로그 표시
            val currentRoute = nestedNavController.currentBackStackEntry?.destination?.route
            if (currentRoute == GamePlayRoutes.Playing.route || currentRoute == GamePlayRoutes.Countdown.route) {
                showBackConfirmation = true
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
            .padding(16.dp)
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
                startDestination = GamePlayRoutes.Ready.route
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
            AlertDialog(
                onDismissRequest = { showBackConfirmation = false },
                title = { Text("게임 종료") },
                text = { Text("게임을 종료하시겠습니까?\n현재 진행 중인 게임 결과는 저장되지 않습니다.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showBackConfirmation = false
                            navController.popBackStack() // 상위 네비게이션으로 돌아감
                        }
                    ) { Text("종료") }
                },
                dismissButton = {
                    TextButton(onClick = { showBackConfirmation = false }) {
                        Text("계속 진행")
                    }
                }
            )
        }
    }
}