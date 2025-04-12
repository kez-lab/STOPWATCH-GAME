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
    
    // 타이머 시작 시간
    private var startTimeMillis: Long = 0
    
    // UI 업데이트 간격 (16ms = 약 60FPS)
    private val UI_UPDATE_INTERVAL = 16L
    
    // 목표 시간 설정
    fun setTargetTime(targetTimeMillis: Long) {
        _uiState.update { currentState ->
            currentState.copy(
                targetTime = targetTimeMillis,
                formattedTargetTime = TimeUtils.formatTime(targetTimeMillis)
            )
        }
    }
    
    // 게임 타입 설정
    fun setGameType(gameType: GameType) {
        _uiState.update { currentState ->
            currentState.copy(
                gameType = gameType
            )
        }
        
        // 게임 타입에 따라 타겟 시간 자동 설정
        when (gameType) {
            GameType.EXACT_STOP -> setTargetTime(5000) // 5초
            GameType.SLOWEST_STOP -> setTargetTime(10000) // 10초 제한
            GameType.RANDOM_MATCH -> setTargetTime(TimeUtils.generateRandomTargetTime(3, 10))
            GameType.LAST_PERSON -> setTargetTime(15000) // 15초 제한
            GameType.MS_DIGIT -> { /* 타겟 시간 필요 없음 */ }
        }
    }
    
    // 타이머 시작
    fun startTimer() {
        if (timerJob != null) return
        
        startTimeMillis = System.currentTimeMillis()
        _uiState.update { currentState ->
            currentState.copy(
                isRunning = true,
                isFinished = false,
                elapsedTime = 0,
                formattedTime = "00:00.000"
            )
        }
        
        timerJob = viewModelScope.launch {
            while (isActive) {
                // 현재 시간에서 시작 시간을 빼서 정확한 경과 시간 계산
                val currentTime = System.currentTimeMillis()
                val elapsedTime = currentTime - startTimeMillis
                
                // UI 상태 업데이트
                _uiState.update { currentState ->
                    currentState.copy(
                        elapsedTime = elapsedTime,
                        formattedTime = TimeUtils.formatTime(elapsedTime)
                    )
                }
                
                // 게임 타입에 따른 자동 종료 로직
                val shouldAutoStop = when (_uiState.value.gameType) {
                    GameType.SLOWEST_STOP -> elapsedTime > _uiState.value.targetTime
                    GameType.LAST_PERSON -> elapsedTime > _uiState.value.targetTime
                    else -> false
                }
                
                if (shouldAutoStop) {
                    stopTimer(true)
                    break
                }
                
                // UI 업데이트 간격만큼 대기 (약 60FPS)
                delay(UI_UPDATE_INTERVAL)
            }
        }
    }
    
    // 타이머 정지
    fun stopTimer(isTimeout: Boolean = false) {
        timerJob?.cancel()
        timerJob = null
        
        // 정확한 최종 경과 시간 계산 
        val currentTime = System.currentTimeMillis()
        val finalElapsedTime = currentTime - startTimeMillis
        
        // ms의 신 게임에서 끝자리 숫자 계산
        val lastDigit = if (_uiState.value.gameType == GameType.MS_DIGIT) {
            TimeUtils.getLastDigit(finalElapsedTime)
        } else -1
        
        _uiState.update { currentState ->
            currentState.copy(
                isRunning = false,
                isFinished = true,
                elapsedTime = finalElapsedTime,
                formattedTime = TimeUtils.formatTime(finalElapsedTime),
                isTimeout = isTimeout,
                lastDigit = lastDigit
            )
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
    val gameType: GameType = GameType.EXACT_STOP,
    val isTimeout: Boolean = false,
    val lastDigit: Int = -1
) {
    // 목표 시간과의 차이 계산
    val timeDifference: Long
        get() = if (targetTime > 0) Math.abs(elapsedTime - targetTime) else 0
        
    // 포맷팅된 시간 차이
    val formattedDifference: String
        get() = TimeUtils.formatTime(timeDifference)
} 