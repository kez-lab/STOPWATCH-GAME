package io.github.kez_lab.stopwatch_game.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import compose.icons.FeatherIcons
import compose.icons.feathericons.Clock
import compose.icons.feathericons.Info
import compose.icons.feathericons.PlayCircle
import compose.icons.feathericons.Users
import io.github.kez_lab.stopwatch_game.ui.navigation.Routes
import kotlinx.coroutines.delay

enum class HomeAnimationStage { NONE, TITLE, SUBTITLE, BUTTON }

@Composable
fun HomeScreen(navController: NavHostController) {
    var stage by remember { mutableStateOf(HomeAnimationStage.NONE) }

    // 배경 애니메이션 효과
    val density = LocalDensity.current
    val animatedRotation by animateFloatAsState(
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(60000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Background rotation"
    )

    val pulseAnimation by animateFloatAsState(
        targetValue = if (stage == HomeAnimationStage.BUTTON) 1.1f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Pulse animation"
    )

    LaunchedEffect(Unit) {
        stage = HomeAnimationStage.TITLE
        delay(600)
        stage = HomeAnimationStage.SUBTITLE
        delay(600)
        stage = HomeAnimationStage.BUTTON
    }
    val colorScheme = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                // 게임적인 그라데이션 배경
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            colorScheme.surface,
                            colorScheme.surface,
                            colorScheme.surfaceVariant
                        ),
                        center = Offset(size.width / 2, size.height / 2),
                        radius = size.width
                    )
                )

                // 배경 원형 장식 요소
                drawCircle(
                    color = colorScheme.primary.copy(alpha = 0.05f),
                    radius = size.width * 0.7f,
                    center = Offset(size.width / 2, size.height / 2),
                    style = Stroke(width = 50f)
                )

                // 배경 회전 원
                rotate(animatedRotation) {
                    drawCircle(
                        color = colorScheme.primary.copy(alpha = 0.03f),
                        radius = size.width * 0.4f,
                        center = Offset(size.width / 2, size.height / 2),
                        style = Stroke(width = 15f)
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        // 시계 아이콘 장식 (백그라운드)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = FeatherIcons.Clock,
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .alpha(0.05f)
                    .offset(y = (-20).dp)
                    .blur(radius = 3.dp)
                    .rotate(animatedRotation / 12),  // 느리게 회전
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Title(visible = stage >= HomeAnimationStage.TITLE)
            Spacer(modifier = Modifier.height(16.dp))
            Subtitle(visible = stage >= HomeAnimationStage.SUBTITLE)
            Spacer(modifier = Modifier.height(80.dp))

            // 게임 버튼 섹션
            AnimatedVisibility(
                visible = stage >= HomeAnimationStage.BUTTON,
                enter = fadeIn(animationSpec = tween(1000)) +
                        expandVertically(animationSpec = tween(800)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 메인 게임 시작 버튼
                    Button(
                        onClick = { navController.navigate(Routes.PlayerRegistration) },
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(0.8f)
                            .height(60.dp)
                            .scale(pulseAnimation)
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(30.dp)
                            ),
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        enabled = true
                    ) {
                        Icon(
                            imageVector = FeatherIcons.PlayCircle,
                            contentDescription = null,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "게임 시작하기",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 추가 버튼 섹션 - 멀티플레이어 강조
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // 플레이어 버튼
                        OutlinedButton(
                            onClick = { /* TODO */ },
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Icon(
                                imageVector = FeatherIcons.Users,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "멀티플레이어",
                                fontSize = 14.sp
                            )
                        }

                        // 정보 버튼
                        OutlinedButton(
                            onClick = { /* TODO */ },
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.tertiary
                            )
                        ) {
                            Icon(
                                imageVector = FeatherIcons.Info,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "게임 정보",
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // 하단 장식
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .alpha(if (stage >= HomeAnimationStage.BUTTON) 0.7f else 0f)
        ) {
            Text(
                text = "KEZ LAB",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun Title(visible: Boolean) {
    val transition = updateTransition(targetState = visible, label = "TitleTransition")
    val alpha by transition.animateFloat({ tween(600) }, label = "alpha") { if (it) 1f else 0f }
    val offsetY by transition.animateDp(
        { tween(700) },
        label = "offsetY"
    ) { if (it) 0.dp else 50.dp }

    // 타이틀 텍스트에 그래디언트 효과
    val titleBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary
        )
    )

    Box(
        modifier = Modifier
            .alpha(alpha)
            .offset(y = offsetY)
    ) {
        val colorScheme = MaterialTheme.colorScheme
        Text(
            text = "TimeBattle",
            fontSize = 60.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .graphicsLayer {
                    // 3D 효과
                    rotationX = 10f
                    shadowElevation = 15f
                }
                .drawBehind {
                    // 텍스트 뒤 그림자 효과
                    drawRoundRect(
                        color = colorScheme.primary.copy(alpha = 0.15f),
                        topLeft = Offset(10f, 10f),
                        size = size.copy(width = size.width - 20f, height = size.height - 20f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(20f, 20f)
                    )
                }
                .padding(16.dp),
            style = MaterialTheme.typography.headlineLarge.copy(
                brush = titleBrush,
                fontWeight = FontWeight.ExtraBold
            )
        )
    }
}

@Composable
private fun Subtitle(visible: Boolean) {
    val transition = updateTransition(targetState = visible, label = "SubtitleTransition")
    val alpha by transition.animateFloat({ tween(600) }, label = "alpha") { if (it) 1f else 0f }

    Box(
        modifier = Modifier
            .alpha(alpha)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(
            text = "친구들과 함께하는 스톱워치 미니게임",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}
