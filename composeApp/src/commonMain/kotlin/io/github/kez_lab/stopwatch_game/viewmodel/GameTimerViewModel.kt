@file:OptIn(ExperimentalTime::class)

package io.github.kez_lab.stopwatch_game.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kez_lab.stopwatch_game.model.GameType
import io.github.kez_lab.stopwatch_game.utils.TimeUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * 게임 타이머 뷰모델
 */
class GameTimerViewModel : ViewModel() {
    // 내부 상태
    private val _uiState = MutableStateFlow(TimerUiState())

    // 외부에 노출되는 상태
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    // 타이머 작업
    private var timerJob: Job? = null

    // 타이머 시작 시간 (밀리초 단위로 저장)
    private var startTimeMillis: Long = 0

    // 높은 정밀도가 필요한 게임을 위한 UI 업데이트 간격 (8ms = 약 120FPS)
    private val UI_UPDATE_INTERVAL_PRECISE = 8L

    // 게임 타입 설정
    fun setGameType(gameType: GameType) {
        _uiState.update { currentState ->
            currentState.copy(
                gameType = gameType
            )
        }

        // MS_DIGIT 게임은 타겟 시간이 필요 없음
    }

    // 타이머 시작
    fun startTimer() {
        if (timerJob != null) return

        // 밀리초 단위로 정확히 시작 시간 저장
        startTimeMillis = Clock.System.now().toEpochMilliseconds()
        _uiState.update { currentState ->
            currentState.copy(
                isRunning = true,
                isFinished = false,
                elapsedTime = 0,
                formattedTime = "00:00.000"
            )
        }

        timerJob = viewModelScope.launch {
            try {
                // MS_DIGIT 게임을 위한 높은 정밀도의 업데이트 간격 사용
                val updateInterval = UI_UPDATE_INTERVAL_PRECISE

                while (isActive) {
                    // 현재 시간에서 시작 시간을 빼서 정확한 경과 시간 계산 (밀리초 단위)
                    val currentTimeMillis = Clock.System.now().toEpochMilliseconds()
                    val elapsedTimeMillis = currentTimeMillis - startTimeMillis

                    // UI 상태 업데이트
                    _uiState.update { currentState ->
                        currentState.copy(
                            elapsedTime = elapsedTimeMillis,
                            formattedTime = TimeUtils.formatTime(elapsedTimeMillis)
                        )
                    }

                    // UI 업데이트 간격만큼 대기
                    delay(updateInterval)
                }
            } catch (e: Exception) {
                // 예외 처리
                resetTimer()
            }
        }
    }

    // 타이머 정지
    fun stopTimer(isTimeout: Boolean = false) {
        timerJob?.cancel()
        timerJob = null

        try {
            // 정확한 최종 경과 시간 계산 (밀리초 단위)
            val currentTimeMillis = Clock.System.now().toEpochMilliseconds()
            val finalElapsedTimeMillis = currentTimeMillis - startTimeMillis

            // ms를 높여라 게임에서 끝자리 숫자 계산
            val lastDigit = TimeUtils.getLastDigit(finalElapsedTimeMillis)

            _uiState.update { currentState ->
                currentState.copy(
                    isRunning = false,
                    isFinished = true,
                    elapsedTime = finalElapsedTimeMillis,
                    formattedTime = TimeUtils.formatTime(finalElapsedTimeMillis),
                    isTimeout = isTimeout,
                    lastDigit = lastDigit
                )
            }
        } catch (e: Exception) {
            // 예외 발생 시 기본값으로 설정
            resetTimer()
        }
    }

    // 타이머 초기화
    fun resetTimer() {
        timerJob?.cancel()
        timerJob = null

        _uiState.update { currentState ->
            currentState.copy(
                isRunning = false,
                isFinished = false,
                elapsedTime = 0,
                formattedTime = "00:00.000",
                isTimeout = false,
                lastDigit = -1
            )
        }
    }

    // 뷰모델 정리
    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }
}

/**
 * 타이머 UI 상태 클래스
 */
data class TimerUiState(
    val isRunning: Boolean = false,
    val isFinished: Boolean = false,
    val elapsedTime: Long = 0,
    val targetTime: Long = 0,
    val formattedTime: String = "00:00.000",
    val formattedTargetTime: String = "00:00.000",
    val gameType: GameType = GameType.MS_DIGIT,
    val isTimeout: Boolean = false,
    val lastDigit: Int = -1
)