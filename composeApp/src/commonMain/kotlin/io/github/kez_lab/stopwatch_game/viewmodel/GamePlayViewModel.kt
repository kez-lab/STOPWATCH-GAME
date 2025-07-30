package io.github.kez_lab.stopwatch_game.viewmodel

import androidx.lifecycle.ViewModel
import io.github.kez_lab.stopwatch_game.model.GameType
import io.github.kez_lab.stopwatch_game.ui.screens.game.play.GamePlayEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * 게임 플레이 화면의 비즈니스 로직을 담당하는 ViewModel
 */
class GamePlayViewModel(
    private val appViewModel: AppViewModel,
    private val gameType: GameType
) : ViewModel() {

    private val _events = Channel<GamePlayEvent>()
    val events = _events.receiveAsFlow()

    /**
     * 게임 전체 종료 처리
     */
    private fun finishGame() {
        appViewModel.calculateRanks()
        _events.trySend(GamePlayEvent.NavigateToResult)
    }

    /**
     * 다음 플레이어로 이동
     */
    fun moveToNextPlayer() {
        // 먼저 ReadyScreen으로 네비게이션 이벤트 전송
        _events.trySend(GamePlayEvent.NavigateToReady)
        
        // 플레이어 변경 후 게임 종료 여부 확인
        if (!appViewModel.moveToNextPlayer()) {
            finishGame()
        }
    }

    /**
     * 뒤로가기 처리 로직
     */
    fun handleBackPress(isAtRootLevel: Boolean, currentRoute: String?) {
        if (isAtRootLevel) {
            _events.trySend(GamePlayEvent.PopBackStack)
        } else {
            // 플레이 중(카운트다운 포함) 뒤로가기 시 확인 다이얼로그 표시
            if (currentRoute == "playing" || currentRoute == "countdown") {
                _events.trySend(GamePlayEvent.ShowBackConfirmation)
            } else {
                // 일반적인 뒤로가기는 UI에서 직접 처리
            }
        }
    }

    /**
     * 게임 종료 확인 다이얼로그에서 종료 선택 시
     */
    fun confirmGameExit() {
        _events.trySend(GamePlayEvent.HideBackConfirmation)
        _events.trySend(GamePlayEvent.PopBackStack)
    }

    /**
     * 게임 종료 확인 다이얼로그에서 계속 진행 선택 시
     */
    fun cancelGameExit() {
        _events.trySend(GamePlayEvent.HideBackConfirmation)
    }
}