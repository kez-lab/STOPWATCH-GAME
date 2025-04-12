package io.github.kez_lab.stopwatch_game.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Play
import compose.icons.feathericons.Square

/**
 * 개선된 타이머 제어 버튼 컴포넌트
 * @param isRunning 실행 중 여부
 * @param onClick 클릭 이벤트 핸들러
 * @param modifier 수정자
 * @param isEnabled 활성화 여부
 * @param isFinished 완료 여부
 */
@Composable
fun TimerButton(
    isRunning: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isFinished: Boolean = false
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        // 화면 크기에 따라 버튼 크기 조절
        val buttonSize = if (maxWidth < 400.dp) 110.dp else 130.dp
        val iconSize = if (maxWidth < 400.dp) 32.dp else 40.dp

        // 인터랙션 소스 (눌림 상태 감지)
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()

        // 햅틱 피드백
        val hapticFeedback = LocalHapticFeedback.current

        // 햅틱 피드백 효과 (사용자 상호작용 향상)
        LaunchedEffect(isPressed) {
            if (isPressed && isEnabled) {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        }

        // 색상 애니메이션 (부드러운 트랜지션)
        val buttonColor by animateColorAsState(
            targetValue = when {
                !isEnabled -> MaterialTheme.colorScheme.surfaceVariant
                isFinished -> MaterialTheme.colorScheme.tertiary
                isRunning -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.primary
            },
            animationSpec = tween(300),
            label = "Button color animation"
        )

        // 크기 애니메이션 (스프링 물리 기반으로 더 자연스럽게)
        val scale by animateFloatAsState(
            targetValue = when {
                isPressed -> 0.92f
                isRunning -> 1.08f
                isFinished -> 1.0f
                else -> 1.0f
            },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            label = "Button scale animation"
        )

        // 오프셋 애니메이션 (누를 때 약간 내려가는 효과)
        val yOffset by animateFloatAsState(
            targetValue = if (isPressed) 4f else 0f,
            animationSpec = tween(150),
            label = "Button y-offset animation"
        )

        // 회전 애니메이션
        val rotation by animateFloatAsState(
            targetValue = if (isRunning) 0f else -90f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            label = "Button rotation animation"
        )

        Button(
            onClick = {
                onClick()
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            },
            modifier = Modifier
                .size(buttonSize)
                .scale(scale)
                .offset(y = yOffset.dp)
                .graphicsLayer {
                    if (!isRunning) {
                        rotationZ = rotation
                    }
                },
            shape = CircleShape,
            enabled = isEnabled && !isFinished,
            interactionSource = interactionSource,
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                disabledContainerColor = if (isFinished) {
                    MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f)
                } else {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                }
            )
        ) {
            AnimatedContent(
                targetState = isRunning,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith
                            fadeOut(animationSpec = tween(200))
                },
                label = "Button content animation"
            ) { running ->
                if (running) {
                    // 정지 버튼
                    Icon(
                        imageVector = FeatherIcons.Square,
                        contentDescription = "정지",
                        modifier = Modifier.size(iconSize)
                    )
                } else {
                    // 시작 버튼
                    Icon(
                        imageVector = FeatherIcons.Play,
                        contentDescription = "시작",
                        modifier = Modifier.size(iconSize)
                    )
                }
            }
        }
    }
}
