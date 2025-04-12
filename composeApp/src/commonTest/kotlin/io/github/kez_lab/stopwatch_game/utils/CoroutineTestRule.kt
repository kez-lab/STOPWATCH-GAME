package io.github.kez_lab.stopwatch_game.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

/**
 * 코루틴 테스트를 위한 규칙 클래스
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CoroutineTestRule {
    val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setupDispatcher() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
    }
} 