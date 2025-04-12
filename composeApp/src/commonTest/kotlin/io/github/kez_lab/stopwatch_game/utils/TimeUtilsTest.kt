package io.github.kez_lab.stopwatch_game.utils

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TimeUtilsTest {

    @Test
    fun formatTime_withZero_returnsCorrectFormat() {
        val result = TimeUtils.formatTime(0)
        assertEquals("00:00.000", result)
    }
    
    @Test
    fun formatTime_withMilliseconds_returnsCorrectFormat() {
        val result = TimeUtils.formatTime(61234)
        assertEquals("01:01.234", result)
    }
    
    @Test
    fun formatTime_withoutMilliseconds_returnsCorrectFormat() {
        val result = TimeUtils.formatTime(61234, showMillis = false)
        assertEquals("01:01", result)
    }
    
    @Test
    fun calculateDifference_returnsAbsoluteDifference() {
        assertEquals(1000L, TimeUtils.calculateDifference(4000, 5000))
        assertEquals(1000L, TimeUtils.calculateDifference(5000, 4000))
    }
    
    @Test
    fun getLastDigit_returnsCorrectValue() {
        assertEquals(0, TimeUtils.getLastDigit(10))
        assertEquals(5, TimeUtils.getLastDigit(125))
        assertEquals(9, TimeUtils.getLastDigit(1239))
    }
    
    @Test
    fun generateRandomTargetTime_returnsValueInRange() {
        val minSeconds = 3
        val maxSeconds = 10
        val minMillis = minSeconds * 1000L
        val maxMillis = (maxSeconds + 1) * 1000L - 1 // 최대값 미만
        
        repeat(10) { // 랜덤 값이므로 여러 번 테스트
            val result = TimeUtils.generateRandomTargetTime(minSeconds, maxSeconds)
            assertTrue(result in minMillis..maxMillis)
        }
    }
} 