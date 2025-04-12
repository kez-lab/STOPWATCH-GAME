package io.github.kez_lab.stopwatch_game.ui.screens.game.play

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import compose.icons.feathericons.ArrowRight
import compose.icons.feathericons.User
import io.github.kez_lab.stopwatch_game.model.GameRepository
import io.github.kez_lab.stopwatch_game.model.GameResult
import io.github.kez_lab.stopwatch_game.model.GameType
import io.github.kez_lab.stopwatch_game.ui.components.TimerButton
import io.github.kez_lab.stopwatch_game.ui.components.TimerDisplay
import io.github.kez_lab.stopwatch_game.ui.navigation.Routes
import io.github.kez_lab.stopwatch_game.ui.viewmodel.LocalAppViewModel
import io.github.kez_lab.stopwatch_game.viewmodel.GameTimerViewModel
import io.github.kez_lab.stopwatch_game.viewmodel.TimerUiState
import kotlinx.coroutines.delay

/**
 * 게임 화면 상태 열거형
 */
enum class GameScreenState {
    INFO,       // 게임 설명 화면
    COUNTDOWN,  // 카운트다운 화면
    PLAYING,    // 게임 플레이 화면
    RESULT      // 결과 화면
}

/**
 * 게임 플레이 화면
 */
@Composable
fun GamePlayScreen(
    navController: NavHostController,
    gameId: String
) {
    val appViewModel = LocalAppViewModel.current
    val timerViewModel: GameTimerViewModel = viewModel()
    val hapticFeedback = LocalHapticFeedback.current

    // 현재 게임
    val game = remember { GameRepository.getGameById(gameId) }

    // 앱 상태
    val appUiState by appViewModel.uiState.collectAsState()
    val timerUiState by timerViewModel.uiState.collectAsState()

    // 화면 상태 관리
    var screenState by remember { mutableStateOf(GameScreenState.INFO) }
    var countdown by remember { mutableStateOf(3) }

    // 뒤로가기 확인 다이얼로그 상태
    var showBackConfirmation by remember { mutableStateOf(false) }

    // 게임 종료 처리 함수
    val finishGame = {
        // 결과 계산 후 결과 화면으로 이동 (게임 플레이 화면까지 제거)
        appViewModel.calculateRanks()
        navController.navigate(Routes.Result) {
            popUpTo(Routes.GamePlay(gameId)) {
                inclusive = true
            }
        }
    }

    // 뒤로가기 처리 함수
    val handleBackPress = {
        when {
            // 실행 중이거나 카운트다운 중이면 확인 다이얼로그 표시
            screenState == GameScreenState.PLAYING || screenState == GameScreenState.COUNTDOWN -> {
                showBackConfirmation = true
            }
            // 게임 인포 화면일 때는 바로 이전 화면으로
            screenState == GameScreenState.INFO -> {
                navController.popBackStack()
            }
            // 결과 화면일 때는 다음 플레이어가 있으면 진행, 없으면 결과 화면으로
            screenState == GameScreenState.RESULT -> {
                val hasNextPlayer = appViewModel.moveToNextPlayer()
                if (hasNextPlayer) {
                    timerViewModel.resetTimer()
                    screenState = GameScreenState.INFO
                    countdown = 3
                } else {
                    finishGame()
                }
            }

            else -> Unit
        }
    }

    // 게임 시작 시 타이머 설정
    LaunchedEffect(game) {
        game?.let {
            timerViewModel.setGameType(it.gameType)
        }
    }

    // 게임 상태 변경 감지
    LaunchedEffect(screenState) {
        when (screenState) {
            GameScreenState.COUNTDOWN -> {
                // 카운트다운 시작
                countdown = 3
                delay(1000)
                countdown = 2
                delay(1000)
                countdown = 1
                delay(1000)

                // 타이머 시작
                timerViewModel.startTimer()
                screenState = GameScreenState.PLAYING

                // 햅틱 피드백
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            }

            else -> Unit
        }
    }

    // 타이머가 완료되었을 때 결과 저장
    LaunchedEffect(timerUiState.isFinished) {
        if (timerUiState.isFinished && screenState == GameScreenState.PLAYING) {
            // 햅틱 피드백
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

            val gameType = game?.gameType ?: GameType.EXACT_STOP
            val specialValue = if (gameType == GameType.MS_DIGIT) {
                timerUiState.lastDigit
            } else -1

            // 게임 결과 생성
            val result = GameResult(
                gameId = gameId,
                timeTaken = timerUiState.elapsedTime,
                targetTime = timerUiState.targetTime,
                formattedTime = timerUiState.formattedTime,
                specialValue = specialValue
            )

            // 결과 저장
            appViewModel.saveGameResult(result)

            // 약간의 지연 후 결과 화면으로 전환 (시각적 피드백을 위해)
            delay(300)
            screenState = GameScreenState.RESULT
        }
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
            // 헤더
            GameHeader(
                game = game,
                currentPlayer = appUiState.currentPlayer,
                onBackClick = { handleBackPress() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (screenState) {
                GameScreenState.INFO -> {
                    game?.let {
                        GameInfoCard(
                            game = it,
                            targetTime = timerUiState.formattedTargetTime,
                            onStartGame = { screenState = GameScreenState.COUNTDOWN }
                        )
                    }
                }

                GameScreenState.COUNTDOWN -> {
                    CountdownDisplay(countdown = countdown)
                }

                GameScreenState.PLAYING -> {
                    GamePlayContent(
                        timerUiState = timerUiState,
                        onStopClick = { timerViewModel.stopTimer() },
                        onStartClick = { timerViewModel.startTimer() },
                    )
                }

                GameScreenState.RESULT -> {
                    GameResultContent(
                        timerUiState = timerUiState,
                        gameType = game?.gameType ?: GameType.EXACT_STOP,
                        onNextButtonClicked = {
                            val hasNextPlayer = appViewModel.moveToNextPlayer()
                            if (hasNextPlayer) {
                                countdown = 3
                                timerViewModel.resetTimer()
                                screenState = GameScreenState.COUNTDOWN
                            } else {
                                finishGame()
                            }
                        }
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
                            navController.popBackStack()
                        }
                    ) {
                        Text("종료")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showBackConfirmation = false }
                    ) {
                        Text("계속 진행")
                    }
                }
            )
        }
    }
}

/**
 * 게임 헤더 컴포넌트
 */
@Composable
private fun GameHeader(
    game: io.github.kez_lab.stopwatch_game.model.Game?,
    currentPlayer: io.github.kez_lab.stopwatch_game.model.Player?,
    onBackClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // 헤더 상단 (게임 제목 및 네비게이션)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 뒤로가기 버튼
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = FeatherIcons.ArrowLeft,
                    contentDescription = "뒤로 가기"
                )
            }

            // 게임 제목
            game?.let {
                Text(
                    text = it.name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }

            // 패스 버튼 (나중에 구현)
            IconButton(
                onClick = { /* TODO */ },
                enabled = false
            ) {
                Icon(
                    imageVector = FeatherIcons.ArrowRight,
                    contentDescription = "패스",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 현재 플레이어 표시
        currentPlayer?.let { player ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = FeatherIcons.User,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.size(12.dp))

                    Text(
                        text = player.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

/**
 * 카운트다운 화면
 */
@Composable
private fun CountdownDisplay(countdown: Int) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = countdown.toString(),
            fontSize = 120.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * 게임 실행 화면
 */
@Composable
private fun GamePlayContent(
    timerUiState: TimerUiState,
    onStopClick: () -> Unit,
    onStartClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 목표 시간이 있는 게임은 표시
        if (timerUiState.targetTime > 0 && timerUiState.gameType == GameType.EXACT_STOP) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(
                    text = "목표 시간: ${timerUiState.formattedTargetTime}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // 타이머 표시
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

        // 타이머 컨트롤 버튼
        TimerButton(
            isRunning = timerUiState.isRunning,
            isFinished = timerUiState.isFinished,
            onClick = {
                if (timerUiState.isRunning) {
                    onStopClick()
                } else {
                    onStartClick()
                }
            }
        )
    }
}

/**
 * 게임 결과 화면
 */
@Composable
private fun GameResultContent(
    timerUiState: TimerUiState,
    gameType: GameType,
    onNextButtonClicked: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (timerUiState.isTimeout) {
            Text(
                text = "시간 초과!",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
        } else {
            Text(
                text = "완료!",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 고정된 결과 표시를 위해 isFinished 파라미터 추가
        val resultTime = remember { timerUiState.formattedTime }
        TimerDisplay(
            displayTime = resultTime,
            isFinished = true,
        )

        // ms의 신 게임이면 끝자리 표시
        if (gameType == GameType.MS_DIGIT && timerUiState.lastDigit >= 0) {
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(
                    text = "끝자리: ${timerUiState.lastDigit}",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                )
            }
        }

        // 목표 시간과의 차이 표시 (정확히 멈춰! 게임)
        if (gameType == GameType.EXACT_STOP) {
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (timerUiState.timeDifference < 100)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(
                    text = "차이: ${timerUiState.formattedDifference}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onNextButtonClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            enabled = true
        ) {
            Text(
                text = "다음",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(
                        vertical = 8.dp,
                        horizontal = 8.dp
                    )
            )
        }
    }
}

/**
 * 게임 정보 및 설명 카드
 */
@Composable
private fun GameInfoCard(
    game: io.github.kez_lab.stopwatch_game.model.Game,
    targetTime: String,
    onStartGame: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 게임 설명
            Text(
                text = game.description,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 목표 시간이 있는 게임은 표시
            if (game.gameType == GameType.EXACT_STOP || game.gameType == GameType.RANDOM_MATCH) {
                Text(
                    text = "목표 시간: $targetTime",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // 제한 시간이 있는 게임은 표시
            if (game.gameType == GameType.SLOWEST_STOP || game.gameType == GameType.LAST_PERSON) {
                val limitTime = if (game.gameType == GameType.SLOWEST_STOP) "10.00초" else "15.00초"
                Text(
                    text = "제한 시간: $limitTime",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 시작 버튼
            Button(
                onClick = onStartGame,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "시작하기",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}