package io.github.kez_lab.stopwatch_game.ui.screens.game.play

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.github.kez_lab.stopwatch_game.model.GameResult
import io.github.kez_lab.stopwatch_game.model.GameType
import io.github.kez_lab.stopwatch_game.ui.components.TimerButton
import io.github.kez_lab.stopwatch_game.ui.components.TimerDisplay
import io.github.kez_lab.stopwatch_game.ui.viewmodel.LocalAppViewModel
import io.github.kez_lab.stopwatch_game.viewmodel.AppViewModel
import io.github.kez_lab.stopwatch_game.viewmodel.GameTimerViewModel

@Composable
internal fun PlayingScreen(
    navController: NavController,
    game: GameType,
    timerViewModel: GameTimerViewModel,
) {
    val appViewModel: AppViewModel = LocalAppViewModel.current
    val timerUiState by timerViewModel.uiState.collectAsState()
    val hapticFeedback = LocalHapticFeedback.current

    var firstValue by remember { mutableStateOf<Int?>(null) }
    var isSecondAttempt by remember { mutableStateOf(false) }

    // 타이머 종료 감지
    LaunchedEffect(timerUiState.isFinished) {
        if (timerUiState.isFinished) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            val secondValue = timerViewModel.uiState.value.lastDigit

            val result = when (game) {
                GameType.CONG_PAT -> {
                    GameResult(
                        gameId = game.id,
                        timeTaken = timerUiState.elapsedTime,
                        targetTime = timerUiState.targetTime,
                        formattedTime = timerUiState.formattedTime,
                        specialValue = firstValue ?: -1,
                        specialValue2 = secondValue
                    )
                }

                GameType.RandomMS -> {
                    GameResult(
                        gameId = game.id,
                        timeTaken = timerUiState.elapsedTime,
                        targetTime = timerUiState.targetTime,
                        formattedTime = timerUiState.formattedTime,
                        specialValue = if (game == GameType.RandomMS) timerUiState.lastDigit else -1
                    )
                }
            }
            appViewModel.saveGameResult(result)
            navController.navigate(GamePlayRoutes.Result.route) {
                popUpTo(GamePlayRoutes.Playing.route) { inclusive = true }
            }
        }
    }

    val handleCongPatStop = {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        if (!isSecondAttempt) {
            // 첫 번째 시도: 타이머 정지 후 값 가져오기
            timerViewModel.stopTimer()
            firstValue = timerViewModel.uiState.value.lastDigit
            isSecondAttempt = true
            timerViewModel.resetTimer()
            timerViewModel.startTimer()
        } else {
            // 두 번째 시도: 타이머 정지 후 값 가져오기
            timerViewModel.stopTimer()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 게임 타입별 UI 분기
        if (game == GameType.CONG_PAT) {
            Text(
                text = if (!isSecondAttempt) "첫 번째 시도!" else "두 번째 시도!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            if (isSecondAttempt) {
                Text(
                    text = "첫 번째 소수점: $firstValue",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            TimerDisplay(
                displayTime = timerUiState.formattedTime,
                isRunning = timerUiState.isRunning,
                isFinished = timerUiState.isFinished
            )
        }

        Spacer(modifier = Modifier.height(60.dp))

        TimerButton(
            isRunning = timerUiState.isRunning,
            isFinished = timerUiState.isFinished,
            onClick = {
                if (timerUiState.isRunning) {
                    if (game == GameType.CONG_PAT) {
                        handleCongPatStop()
                    } else {
                        timerViewModel.stopTimer()
                    }
                }
            }
        )
    }
}

