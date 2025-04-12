package io.github.kez_lab.stopwatch_game.viewmodel

import io.github.kez_lab.stopwatch_game.model.GameType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GameTimerViewModelTest {
    
    private lateinit var viewModel: GameTimerViewModel
    private val testDispatcher = StandardTestDispatcher()
    
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = GameTimerViewModel()
    }
    
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun setTargetTime_updatesTargetTimeAndFormattedText() = runTest {
        // Given
        val targetTime = 5000L
        
        // When
        viewModel.setTargetTime(targetTime)
        
        // Then
        assertEquals(targetTime, viewModel.uiState.value.targetTime)
        assertEquals("00:05.000", viewModel.uiState.value.formattedTargetTime)
    }
    
    @Test
    fun setGameType_updatesGameTypeAndSetsAppropriateTargetTime() = runTest {
        // Given
        val gameType = GameType.EXACT_STOP
        
        // When
        viewModel.setGameType(gameType)
        
        // Then
        assertEquals(gameType, viewModel.uiState.value.gameType)
        assertEquals(5000L, viewModel.uiState.value.targetTime) // 기본 값 확인
    }
    
    @Test
    fun startTimer_updatesIsRunningState() = runTest {
        // When
        viewModel.startTimer()
        
        // Then
        assertTrue(viewModel.uiState.value.isRunning)
        assertFalse(viewModel.uiState.value.isFinished)
    }
    
    @Test
    fun stopTimer_updatesTimerStateCorrectly() = runTest {
        // Given
        viewModel.startTimer()
        testDispatcher.scheduler.advanceTimeBy(1000) // 1초 진행
        
        // When
        viewModel.stopTimer()
        
        // Then
        assertFalse(viewModel.uiState.value.isRunning)
        assertTrue(viewModel.uiState.value.isFinished)
    }
    
    @Test
    fun resetTimer_resetsAllTimerStates() = runTest {
        // Given
        viewModel.startTimer()
        testDispatcher.scheduler.advanceTimeBy(1000)
        viewModel.stopTimer()
        
        // When
        viewModel.resetTimer()
        
        // Then
        assertFalse(viewModel.uiState.value.isRunning)
        assertFalse(viewModel.uiState.value.isFinished)
        assertEquals(0L, viewModel.uiState.value.elapsedTime)
        assertEquals("00:00.000", viewModel.uiState.value.formattedTime)
    }
} 