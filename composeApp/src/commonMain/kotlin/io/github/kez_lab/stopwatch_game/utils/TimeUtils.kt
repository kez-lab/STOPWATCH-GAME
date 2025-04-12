package io.github.kez_lab.stopwatch_game.utils

import kotlin.math.abs
import kotlin.random.Random

/**
 * 시간 관련 유틸리티 함수들
 */
object TimeUtils {
    /**
     * 밀리초를 포맷팅된 시간 문자열로 변환
     * @param timeInMillis 변환할 밀리초
     * @param showMillis 밀리초 표시 여부
     * @return "mm:ss.SSS" 또는 "mm:ss" 형식의 문자열
     */
    fun formatTime(
        timeInMillis: Long,
        showMillis: Boolean = true
    ): String {
        val minutes = (timeInMillis / 60000)
        val seconds = (timeInMillis % 60000) / 1000
        val millis = (timeInMillis % 1000)

        return if (showMillis) {
            "${padZero(minutes)}:${padZero(seconds)}.${padMillis(millis)}"
        } else {
            "${padZero(minutes)}:${padZero(seconds)}"
        }
    }

    private fun padZero(value: Long): String = if (value < 10) "0$value" else "$value"

    private fun padMillis(value: Long): String =
        when {
            value < 10 -> "00$value"
            value < 100 -> "0$value"
            else -> "$value"
        }
    
    /**
     * 목표 시간과의 차이 계산
     * @param actualTime 실제 시간 (밀리초)
     * @param targetTime 목표 시간 (밀리초)
     * @return 차이 (밀리초)
     */
    fun calculateDifference(actualTime: Long, targetTime: Long): Long {
        return abs(actualTime - targetTime)
    }
    
    /**
     * 밀리초 끝자리 추출
     * @param timeInMillis 밀리초
     * @return 끝자리 숫자 (0-9)
     */
    fun getLastDigit(timeInMillis: Long): Int {
        return (timeInMillis % 10).toInt()
    }
    
    /**
     * 랜덤 타겟 시간 생성
     * @param minSeconds 최소 초
     * @param maxSeconds 최대 초
     * @return 랜덤 시간 (밀리초)
     */
    fun generateRandomTargetTime(minSeconds: Int = 3, maxSeconds: Int = 10): Long {
        val randomSeconds = Random.nextInt(minSeconds, maxSeconds + 1)
        val randomMillis = Random.nextInt(0, 1000)
        return (randomSeconds * 1000L) + randomMillis
    }
} 