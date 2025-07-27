package io.github.kez_lab.stopwatch_game.ui.screens.game.play

/**
 * 게임 플레이 네비게이션 이벤트
 */
sealed class GamePlayEvent {
    object NavigateToResult : GamePlayEvent()
    object NavigateToReady : GamePlayEvent()
    object PopBackStack : GamePlayEvent()
    object ShowBackConfirmation : GamePlayEvent()
    object HideBackConfirmation : GamePlayEvent()
}