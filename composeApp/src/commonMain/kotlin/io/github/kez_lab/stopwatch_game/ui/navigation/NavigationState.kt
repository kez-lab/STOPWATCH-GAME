package io.github.kez_lab.stopwatch_game.ui.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * 앱 내 네비게이션 상태를 관리하는 클래스
 */
class NavigationState {
    // 현재 화면 상태
    var currentScreen by mutableStateOf<Screen>(Screen.Home)
        private set
    
    // 이전 화면 히스토리
    private val backStack = mutableListOf<Screen>()
    
    // 새 화면으로 이동
    fun navigateTo(screen: Screen) {
        if (currentScreen != screen) {
            backStack.add(currentScreen)
            currentScreen = screen
        }
    }
    
    // 이전 화면으로 돌아가기
    fun goBack() {
        if (backStack.isNotEmpty()) {
            currentScreen = backStack.removeLast()
        }
    }
} 