package io.github.kez_lab.stopwatch_game.ui.screens.game.play

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import compose.icons.FeatherIcons
import compose.icons.feathericons.AlertCircle
import compose.icons.feathericons.Clock
import compose.icons.feathericons.PlayCircle
import compose.icons.feathericons.SkipForward
import compose.icons.feathericons.Target
import compose.icons.feathericons.Users
import compose.icons.feathericons.ZapOff
import io.github.kez_lab.stopwatch_game.model.Game
import io.github.kez_lab.stopwatch_game.model.GameRepository
import io.github.kez_lab.stopwatch_game.model.GameResult
import io.github.kez_lab.stopwatch_game.model.GameType
import io.github.kez_lab.stopwatch_game.ui.components.AppBar
import io.github.kez_lab.stopwatch_game.ui.components.AppBarActionItem
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

    // 다음 플레이어로 이동하는 통합 함수
    val moveToNextPlayer = {
        val hasNextPlayer = appViewModel.moveToNextPlayer()
        if (hasNextPlayer) {
            timerViewModel.resetTimer()
            screenState = GameScreenState.INFO
            countdown = 3
            true
        } else {
            finishGame()
            false
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
                moveToNextPlayer()
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
            // 헤더 - 공통 AppBar 사용으로 변경
            AppBar(
                title = game?.name ?: "",
                onBackClick = { handleBackPress() },
                currentPlayer = appUiState.currentPlayer,
                showPlayerInfo = true,
                actions = {
                    // 패스 버튼
                    AppBarActionItem(
                        icon = FeatherIcons.SkipForward,
                        contentDescription = "패스",
                        onClick = { /* TODO: 패스 기능 구현 */ },
                        enabled = false // 현재 비활성화 상태
                    )
                }
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
                    CountdownDisplay(
                        countdown = countdown,
                        onCancel = {
                            // 카운트다운 취소하고 정보 화면으로 돌아가기
                            screenState = GameScreenState.INFO
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    )
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
                        onNextButtonClicked = { moveToNextPlayer() }
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
 * 게임 인포 카드 컴포넌트
 */
@Composable
private fun GameInfoCard(
    game: Game,
    targetTime: String,
    onStartGame: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 게임 타입 아이콘
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            )
                        )
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                val iconVector = when (game.gameType) {
                    GameType.EXACT_STOP -> FeatherIcons.Clock
                    GameType.SLOWEST_STOP -> FeatherIcons.ZapOff
                    GameType.RANDOM_MATCH -> FeatherIcons.AlertCircle
                    GameType.LAST_PERSON -> FeatherIcons.Users
                    GameType.MS_DIGIT -> FeatherIcons.Target
                }

                Icon(
                    imageVector = iconVector,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 게임 이름
            Text(
                text = game.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 게임 설명
            Text(
                text = game.description,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 목표 시간 표시 (있는 경우만)
            if (game.gameType == GameType.EXACT_STOP) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = FeatherIcons.Target,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "목표 시간: $targetTime",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // 시작 버튼
            Button(
                onClick = onStartGame,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(28.dp)
                    ),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = FeatherIcons.PlayCircle,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "시작하기",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * 게임 카운트다운 화면
 */
@Composable
private fun CountdownDisplay(
    countdown: Int,
    onCancel: () -> Unit
) {
    // 게임 느낌의 애니메이션 효과
    val animationProgress = remember { Animatable(0f) }
    val countdownColor = MaterialTheme.colorScheme.primary

    LaunchedEffect(countdown) {
        animationProgress.snapTo(0f)
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 900,
                easing = FastOutSlowInEasing
            )
        )
    }

    val scale = lerp(2f, 1f, animationProgress.value)
    val alpha = lerp(0f, 1f, animationProgress.value)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                // 게임스러운 배경 원
                drawCircle(
                    color = countdownColor.copy(alpha = 0.1f * animationProgress.value),
                    radius = size.minDimension * 0.6f * (1f + (1f - animationProgress.value))
                )

                // 동심원 애니메이션
                drawCircle(
                    color = countdownColor.copy(alpha = 0.05f),
                    radius = size.minDimension * 0.5f * (1f + (1f - animationProgress.value)),
                    style = Stroke(width = 8f)
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "준비...",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.alpha(alpha)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = countdown.toString(),
                fontSize = 160.sp,
                fontWeight = FontWeight.ExtraBold,
                color = countdownColor,
                modifier = Modifier
                    .scale(scale)
                    .alpha(alpha)
                    .drawBehind {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    countdownColor.copy(alpha = 0.3f),
                                    Color.Transparent
                                ),
                                radius = size.minDimension * 0.8f
                            ),
                            radius = size.minDimension * 0.5f
                        )
                    }
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 취소 버튼 추가
            Button(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                modifier = Modifier
                    .alpha(alpha)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "준비 취소",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
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
    // 결과 애니메이션
    val animationProgress = remember { Animatable(0f) }
    val colorScheme = MaterialTheme.colorScheme

    LaunchedEffect(Unit) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
    }

    // 햅틱 피드백 (게임 결과 발표시 진동 효과)
    val hapticFeedback = LocalHapticFeedback.current
    LaunchedEffect(Unit) {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        delay(300)
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 결과 상태 텍스트 (타임아웃 또는 완료)
        Box(
            modifier = Modifier
                .graphicsLayer {
                    alpha = animationProgress.value
                    scaleX = animationProgress.value
                    scaleY = animationProgress.value
                }
        ) {
            if (timerUiState.isTimeout) {
                Text(
                    text = "시간 초과!",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                )
            } else {
                Text(
                    text = "완료!",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // 결과 시간 표시
        Box(
            modifier = Modifier
                .graphicsLayer {
                    alpha = animationProgress.value
                }
        ) {
            // 고정된 결과 표시를 위해 isFinished 파라미터 추가
            val resultTime = remember { timerUiState.formattedTime }
            TimerDisplay(
                displayTime = resultTime,
                isFinished = true,
            )
        }

        // ms를 높여라 게임이면 끝자리 표시
        if (gameType == GameType.MS_DIGIT && timerUiState.lastDigit >= 0) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .graphicsLayer {
                        alpha = animationProgress.value
                        scaleX = animationProgress.value
                        scaleY = animationProgress.value
                    }
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(16.dp),
                            spotColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "끝자리",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )

                        Text(
                            text = "${timerUiState.lastDigit}",
                            fontSize = 80.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // 다음 버튼
        Button(
            onClick = onNextButtonClicked,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(56.dp)
                .graphicsLayer {
                    alpha = animationProgress.value
                },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                text = "다음 플레이어",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}