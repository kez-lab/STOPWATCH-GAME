package io.github.kez_lab.stopwatch_game.ui.screens.result

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import compose.icons.FeatherIcons
import compose.icons.feathericons.Award
import compose.icons.feathericons.Clock
import compose.icons.feathericons.Play
import io.github.kez_lab.stopwatch_game.model.GameType
import io.github.kez_lab.stopwatch_game.model.Player
import io.github.kez_lab.stopwatch_game.ui.components.AppBar
import io.github.kez_lab.stopwatch_game.ui.navigation.Routes
import io.github.kez_lab.stopwatch_game.ui.viewmodel.LocalAppViewModel
import kotlinx.coroutines.delay

/**
 * 결과 화면
 */
@Composable
fun ResultScreen(navController: NavHostController) {
    val appViewModel = LocalAppViewModel.current

    // 앱 상태
    val uiState by appViewModel.uiState.collectAsState()

    // 화면 상태
    var showPunishment by remember { mutableStateOf(false) }
    var showCongrats by remember { mutableStateOf(true) }

    // 게임 선택으로 돌아가기
    val navigateToGameSelection = {
        navController.navigate(Routes.GameSelection) {
            popUpTo(Routes.Result) {
                inclusive = true
            }
        }
    }

    // 벌칙 선택
    LaunchedEffect(Unit) {
        delay(3000) // 축하 화면 잠시 보여주기
        appViewModel.selectRandomPunishment()
        showCongrats = false
        delay(500)
        showPunishment = true
    }

    // 결과 정보
    val gameResults = uiState.rankedResults
    val selectedGame = uiState.selectedGame

    // 승자/패자
    val winner = gameResults.firstOrNull()?.first
    val loser = gameResults.lastOrNull()?.first

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 공통 AppBar 사용
            AppBar(
                title = "게임 결과",
                onBackClick = navigateToGameSelection
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 게임 이름
            selectedGame?.let { game ->
                Text(
                    text = game.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 축하 화면
            AnimatedVisibility(
                visible = showCongrats,
                enter = fadeIn(animationSpec = tween(500)),
                exit = fadeOut(animationSpec = tween(500))
            ) {
                winner?.let {
                    WinnerCelebration(player = it)
                }
            }

            // 벌칙 화면
            AnimatedVisibility(
                visible = showPunishment,
                enter = fadeIn(animationSpec = tween(500)),
                exit = fadeOut(animationSpec = tween(500))
            ) {
                if (loser != null && uiState.selectedPunishment != null) {
                    PunishmentCard(
                        playerName = loser.name,
                        punishmentName = uiState.selectedPunishment!!.name,
                        punishmentDescription = uiState.selectedPunishment!!.description
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 결과 목록
            Text(
                text = "순위표",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(gameResults) { (player, result) ->
                    ResultItem(
                        player = player,
                        time = result.formattedTime,
                        rank = result.rank,
                        isWinner = result.isWinner,
                        specialValue = result.specialValue,
                        gameType = selectedGame?.gameType ?: GameType.MS_DIGIT,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 하단 버튼
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 다시 하기 버튼
                Button(
                    onClick = {
                        uiState.selectedGame?.let { game ->
                            appViewModel.selectGame(game.id)
                            navController.navigate(Routes.GamePlay(game.id)) {
                                launchSingleTop = true
                                popUpTo(Routes.Result) {
                                    inclusive = true
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Icon(
                        imageVector = FeatherIcons.Play,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.size(8.dp))

                    Text(text = "다시 하기")
                }

                // 게임 선택으로 버튼
                Button(
                    onClick = navigateToGameSelection
                ) {
                    Text(text = "다른 게임 선택")
                }
            }
        }
    }
}

/**
 * 승자 축하 컴포넌트
 */
@Composable
private fun WinnerCelebration(player: Player) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 트로피 아이콘 (펄스 애니메이션)
        val infiniteTransition = rememberInfiniteTransition(label = "Trophy Animation")
        val scale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(800, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "Trophy Scale"
        )

        Icon(
            imageVector = FeatherIcons.Award,
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .scale(scale),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "축하합니다!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = player.name,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "승리했습니다!",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

/**
 * 벌칙 카드 컴포넌트
 */
@Composable
private fun PunishmentCard(
    playerName: String,
    punishmentName: String,
    punishmentDescription: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "꼴찌 벌칙",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = playerName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onErrorContainer
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = punishmentName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = punishmentDescription,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

/**
 * 결과 아이템 컴포넌트
 */
@Composable
private fun ResultItem(
    player: Player,
    time: String,
    rank: Int,
    isWinner: Boolean,
    specialValue: Int = -1,
    gameType: GameType,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isWinner) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 순위 원형 표시
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        when (rank) {
                            1 -> MaterialTheme.colorScheme.primary
                            2 -> MaterialTheme.colorScheme.secondary
                            3 -> MaterialTheme.colorScheme.tertiary
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rank.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (rank <= 3) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.size(16.dp))

            // 플레이어 이름
            Text(
                text = player.name,
                fontSize = 18.sp,
                fontWeight = if (isWinner) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = FeatherIcons.Clock,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.size(8.dp))

            // 시간 또는 특수값 표시
            if (gameType == GameType.MS_DIGIT && specialValue >= 0) {
                Text(
                    text = specialValue.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Text(
                    text = time,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
} 