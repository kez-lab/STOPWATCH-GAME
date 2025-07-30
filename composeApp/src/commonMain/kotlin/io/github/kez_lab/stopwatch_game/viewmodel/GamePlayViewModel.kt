package io.github.kez_lab.stopwatch_game.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kez_lab.stopwatch_game.ui.screens.game.play.GamePlayEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * 게임 플레이 화면의 비즈니스 로직을 담당하는 ViewModel
 */
class GamePlayViewModel : ViewModel() {

    private val _events = Channel<GamePlayEvent>()
    val events = _events.receiveAsFlow()

    /**
     * 게임 전체 종료 처리
     */
    private fun finishGame() {
        viewModelScope.launch {
            _events.send(GamePlayEvent.NavigateToResult)
        }
    }

    /**
     * 다음 플레이어로 이동
     */
    fun moveToNextPlayer(isExitNextPlayer: Boolean) {
        viewModelScope.launch {
            if (!isExitNextPlayer) {
                finishGame()
            } else {
                _events.send(GamePlayEvent.NavigateToReady)
            }
        }
    }

    /**
     * 뒤로가기 처리 로직
     */
    fun handleBackPress(isAtRootLevel: Boolean, currentRoute: String?) {
        viewModelScope.launch {
            if (isAtRootLevel) {
                _events.send(GamePlayEvent.PopBackStack)
            } else {
                // 플레이 중(카운트다운 포함) 뒤로가기 시 확인 다이얼로그 표시
                if (currentRoute == "playing" || currentRoute == "countdown") {
                    _events.send(GamePlayEvent.ShowBackConfirmation)
                } else {
                    // 일반적인 뒤로가기는 UI에서 직접 처리
                }
            }
        }
    }

    /**
     * 게임 종료 확인 다이얼로그에서 종료 선택 시
     */
    fun confirmGameExit() {
        viewModelScope.launch {
            _events.send(GamePlayEvent.HideBackConfirmation)
            _events.send(GamePlayEvent.PopBackStack)
        }
    }

    /**
     * 게임 종료 확인 다이얼로그에서 계속 진행 선택 시
     */
    fun cancelGameExit() {
        viewModelScope.launch {
            _events.send(GamePlayEvent.HideBackConfirmation)
        }
    }
}