package io.github.kez_lab.stopwatch_game.ui.screens.game.play

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import compose.icons.feathericons.ArrowRight
import compose.icons.feathericons.RefreshCw
import compose.icons.feathericons.User
import io.github.kez_lab.stopwatch_game.model.GameRepository
import io.github.kez_lab.stopwatch_game.model.GameResult
import io.github.kez_lab.stopwatch_game.model.GameType
import io.github.kez_lab.stopwatch_game.ui.components.TimerButton
import io.github.kez_lab.stopwatch_game.ui.components.TimerDisplay
import io.github.kez_lab.stopwatch_game.ui.navigation.LocalNavigationController
import io.github.kez_lab.stopwatch_game.ui.navigation.Screen
import io.github.kez_lab.stopwatch_game.viewmodel.AppViewModel
import io.github.kez_lab.stopwatch_game.viewmodel.GameTimerViewModel
import kotlinx.coroutines.delay

/**
 * 게임 플레이 화면
 */
@Composable
fun GamePlayScreen(gameId: String) {
    val navigationController = LocalNavigationController.current
    val appViewModel: AppViewModel = viewModel()
    val timerViewModel: GameTimerViewModel = viewModel()
    
    // 현재 게임
    val game = remember { GameRepository.getGameById(gameId) }
    
    // 앱 상태
    val appUiState by appViewModel.uiState.collectAsState()
    val timerUiState by timerViewModel.uiState.collectAsState()
    
    // 화면 상태
    var showGameInfo by remember { mutableStateOf(true) }
    var showCountdown by remember { mutableStateOf(false) }
    var showGameOver by remember { mutableStateOf(false) }
    var countdown by remember { mutableStateOf(3) }
    
    // 게임 시작 시 타이머 설정
    LaunchedEffect(game) {
        game?.let {
            timerViewModel.setGameType(it.gameType)
        }
    }
    
    // 게임 결과가 저장되었을 때 다음 화면으로 이동
    LaunchedEffect(showGameOver) {
        if (showGameOver) {
            delay(1500) // 결과 화면 잠시 보여주기
            
            // 다음 플레이어가 있으면 계속, 아니면 결과 화면으로
            val hasNextPlayer = appViewModel.moveToNextPlayer()
            if (hasNextPlayer) {
                // 타이머 초기화하고 같은 화면 유지
                timerViewModel.resetTimer()
                showGameInfo = true
                showGameOver = false
                countdown = 3
            } else {
                // 모든 플레이어가 완료했으면 결과 계산 후 결과 화면으로
                appViewModel.calculateRanks()
                navigationController.navigateTo(Screen.Result)
            }
        }
    }
    
    // 카운트다운 효과
    LaunchedEffect(showCountdown) {
        if (showCountdown) {
            countdown = 3
            delay(1000)
            countdown = 2
            delay(1000)
            countdown = 1
            delay(1000)
            
            // 타이머 시작
            timerViewModel.startTimer()
            showCountdown = false
        }
    }
    
    // 타이머가 완료되었을 때 결과 저장
    LaunchedEffect(timerUiState.isFinished) {
        if (timerUiState.isFinished && !showGameOver) {
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
            showGameOver = true
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 뒤로가기 버튼
                IconButton(onClick = { navigationController.goBack() }) {
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
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 현재 플레이어 표시
            appUiState.currentPlayer?.let { player ->
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
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 게임 정보 / 안내
            AnimatedVisibility(
                visible = showGameInfo,
                enter = fadeIn(animationSpec = tween(500)),
                exit = fadeOut(animationSpec = tween(500))
            ) {
                game?.let {
                    GameInfoCard(
                        game = it,
                        targetTime = timerUiState.formattedTargetTime,
                        onStartGame = { 
                            showGameInfo = false
                            showCountdown = true 
                        }
                    )
                }
            }
            
            // 카운트다운
            AnimatedVisibility(
                visible = showCountdown,
                enter = fadeIn(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300))
            ) {
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
            
            // 게임 실행 UI
            AnimatedVisibility(
                visible = !showGameInfo && !showCountdown && !showGameOver,
                enter = fadeIn(animationSpec = tween(500)),
                exit = fadeOut(animationSpec = tween(500))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // 목표 시간이 있는 게임은 표시
                    if (timerUiState.targetTime > 0 && timerUiState.gameType == GameType.EXACT_STOP) {
                        Text(
                            text = "목표 시간: ${timerUiState.formattedTargetTime}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                    
                    // 타이머 표시
                    TimerDisplay(
                        time = timerUiState.formattedTime,
                        isRunning = timerUiState.isRunning,
                        isTimeout = timerUiState.isTimeout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(60.dp))
                    
                    // 타이머 컨트롤 버튼
                    TimerButton(
                        isRunning = timerUiState.isRunning,
                        onClick = { 
                            if (timerUiState.isRunning) {
                                timerViewModel.stopTimer() 
                            } else {
                                timerViewModel.startTimer()
                            }
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // 타이머 리셋 버튼
                    if (!timerUiState.isRunning && !timerUiState.isFinished) {
                        Button(
                            onClick = { timerViewModel.resetTimer() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        ) {
                            Icon(
                                imageVector = FeatherIcons.RefreshCw,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            
                            Spacer(modifier = Modifier.size(8.dp))
                            
                            Text(text = "다시 시작하기")
                        }
                    }
                }
            }
            
            // 게임 결과 / 게임 오버
            AnimatedVisibility(
                visible = showGameOver,
                enter = fadeIn(animationSpec = tween(500)),
                exit = fadeOut(animationSpec = tween(500))
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
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = "기록: ${timerUiState.formattedTime}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    // ms의 신 게임이면 끝자리 표시
                    if (game?.gameType == GameType.MS_DIGIT && timerUiState.lastDigit >= 0) {
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "끝자리: ${timerUiState.lastDigit}",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                    
                    // 목표 시간과의 차이 표시 (정확히 멈춰! 게임)
                    if (game?.gameType == GameType.EXACT_STOP) {
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "차이: ${timerUiState.formattedDifference}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
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