package io.github.kez_lab.stopwatch_game.ui.screens.game.play

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically

/**
 * 게임 플레이 화면의 네비게이션 애니메이션 정의
 */
object GamePlayAnimations {
    
    /**
     * 게임 플로우에 맞는 진입 애니메이션
     */
    fun getEnterTransition(from: String?, to: String?) = when {
        // Ready → Countdown: 아래에서 위로 슬라이드 (준비 → 집중)
        from == GamePlayRoutes.Ready.route && to == GamePlayRoutes.Countdown.route ->
            slideInVertically(
                animationSpec = tween(durationMillis = 400),
                initialOffsetY = { it }
            ) + fadeIn(animationSpec = tween(durationMillis = 400))

        // Countdown → Playing: 스케일 + 페이드 (긴장감 증가)
        from == GamePlayRoutes.Countdown.route && to == GamePlayRoutes.Playing.route ->
            scaleIn(
                animationSpec = tween(durationMillis = 500),
                initialScale = 0.8f
            ) + fadeIn(animationSpec = tween(durationMillis = 500))

        // Playing → Result: 오른쪽에서 슬라이드 (결과 확인)
        from == GamePlayRoutes.Playing.route && to == GamePlayRoutes.Result.route ->
            slideInHorizontally(
                animationSpec = tween(durationMillis = 600),
                initialOffsetX = { it }
            ) + fadeIn(animationSpec = tween(durationMillis = 600))

        // Result → Ready: 왼쪽에서 빠른 슬라이드 (다음 플레이어)
        from == GamePlayRoutes.Result.route && to == GamePlayRoutes.Ready.route ->
            slideInHorizontally(
                animationSpec = tween(durationMillis = 100),
                initialOffsetX = { -it }
            ) + fadeIn(animationSpec = tween(durationMillis = 100))

        // 기본: 페이드인
        else -> fadeIn(animationSpec = tween(durationMillis = 300))
    }

    /**
     * 게임 플로우에 맞는 퇴장 애니메이션
     */
    fun getExitTransition(from: String?, to: String?) = when {
        // Ready → Countdown: 위로 슬라이드 아웃
        from == GamePlayRoutes.Ready.route && to == GamePlayRoutes.Countdown.route ->
            slideOutVertically(
                animationSpec = tween(durationMillis = 400),
                targetOffsetY = { -it }
            ) + fadeOut(animationSpec = tween(durationMillis = 400))

        // Countdown → Playing: 스케일 축소 + 페이드
        from == GamePlayRoutes.Countdown.route && to == GamePlayRoutes.Playing.route ->
            scaleOut(
                animationSpec = tween(durationMillis = 500),
                targetScale = 1.2f
            ) + fadeOut(animationSpec = tween(durationMillis = 500))

        // 기본: 페이드아웃
        else -> fadeOut(animationSpec = tween(durationMillis = 300))
    }

    /**
     * 뒤로가기 시 진입 애니메이션
     */
    fun getPopEnterTransition() =
        fadeIn(animationSpec = tween(durationMillis = 300))

    /**
     * 뒤로가기 시 퇴장 애니메이션
     */
    fun getPopExitTransition() =
        fadeOut(animationSpec = tween(durationMillis = 300))
}