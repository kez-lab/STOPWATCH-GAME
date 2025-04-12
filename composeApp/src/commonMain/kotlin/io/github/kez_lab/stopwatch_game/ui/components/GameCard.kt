package io.github.kez_lab.stopwatch_game.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FeatherIcons
import compose.icons.feathericons.AlertCircle
import compose.icons.feathericons.Clock
import compose.icons.feathericons.Users
import compose.icons.feathericons.ZapOff
import io.github.kez_lab.stopwatch_game.model.Game
import io.github.kez_lab.stopwatch_game.model.GameType

/**
 * 게임 선택 카드 컴포넌트 - 게임적 요소 강화
 * @param game 게임 정보
 * @param onClick 클릭 이벤트 핸들러
 */
@Composable
fun GameCard(
    game: Game,
    onClick: () -> Unit
) {
    // 호버 상태 관리
    var isHovered by remember { mutableStateOf(false) }

    // 애니메이션 값
    val elevation by animateDpAsState(
        targetValue = if (isHovered) 8.dp else 4.dp,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "Card elevation"
    )

    val scale by animateFloatAsState(
        targetValue = if (isHovered) 1.03f else 1f,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "Card scale"
    )

    val cardColor by animateColorAsState(
        targetValue = if (isHovered)
            MaterialTheme.colorScheme.surfaceVariant
        else
            MaterialTheme.colorScheme.surface,
        animationSpec = tween(300),
        label = "Card color"
    )

    // 게임 타입에 따른 컬러
    val gameColor = getGameColor(game.gameType)

    Card(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .scale(scale)
            .shadow(elevation = elevation, shape = RoundedCornerShape(16.dp), clip = false)
            .graphicsLayer {
                this.rotationX = if (isHovered) 2f else 0f
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
    ) {
        // 마우스 이벤트 (호버)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            gameColor.copy(alpha = 0.1f),
                            Color.Transparent
                        )
                    )
                )
        ) {
            // 게임 타입 인디케이터
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(gameColor.copy(alpha = 0.2f))
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getGameIcon(game.gameType),
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = gameColor
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 게임 아이콘
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    gameColor.copy(alpha = 0.7f),
                                    gameColor.copy(alpha = 0.3f)
                                )
                            )
                        )
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getGameIcon(game.gameType),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // 게임 이름
                    Text(
                        text = game.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // 게임 설명
                    Text(
                        text = game.description,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 게임스러운 난이도 표시 - 프로그레스 바 스타일
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "난이도:",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(end = 8.dp)
                        )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(game.difficultyLevel / 5f)
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(gameColor)
                            )
                        }
                    }
                }

                // 마우스 움직일 때 호버 상태 변경
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                    .apply {
                        if (!isHovered) {
                            isHovered = true
                        } else {
                            isHovered = false
                        }
                    }
            }
        }
    }
}

/**
 * 게임 타입에 따른 아이콘 반환
 */
@Composable
private fun getGameIcon(gameType: GameType) = when (gameType) {
    GameType.EXACT_STOP -> FeatherIcons.Clock
    GameType.SLOWEST_STOP -> FeatherIcons.ZapOff
    GameType.RANDOM_MATCH -> FeatherIcons.AlertCircle
    GameType.LAST_PERSON -> FeatherIcons.Users
    GameType.MS_DIGIT -> FeatherIcons.Clock
}

/**
 * 게임 타입에 따른 색상 반환
 */
@Composable
private fun getGameColor(gameType: GameType): Color = when (gameType) {
    GameType.EXACT_STOP -> MaterialTheme.colorScheme.primary
    GameType.SLOWEST_STOP -> MaterialTheme.colorScheme.tertiary
    GameType.RANDOM_MATCH -> MaterialTheme.colorScheme.secondary
    GameType.LAST_PERSON -> MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
    GameType.MS_DIGIT -> MaterialTheme.colorScheme.tertiary
} 