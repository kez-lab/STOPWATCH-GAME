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
    
    // 특정 화면으로 이동하면서 백스택 정리
    fun navigateToWithPopUpTo(screen: Screen, popUpTo: Screen? = null, inclusive: Boolean = false) {
        if (currentScreen != screen) {
            // popUpTo가 지정된 경우 해당 화면까지 백스택 정리
            if (popUpTo != null) {
                val index = backStack.indexOfLast { 
                    (it::class == popUpTo::class) && 
                    (it is Screen.GamePlay && popUpTo is Screen.GamePlay) && 
                    (it.gameId == popUpTo.gameId) 
                }
                
                if (index != -1) {
                    if (inclusive) {
                        // 지정 화면 포함하여 제거
                        backStack.removeAll { backStack.indexOf(it) >= index }
                    } else {
                        // 지정 화면 이후 화면만 제거
                        backStack.removeAll { backStack.indexOf(it) > index }
                    }
                }
            }
            
            backStack.add(currentScreen)
            currentScreen = screen
        }
    }
    
    // 현재 화면을 교체 (백스택에 추가하지 않음)
    fun replaceCurrentScreen(screen: Screen) {
        currentScreen = screen
    }
    
    // 백스택 클리어 후 이동
    fun navigateWithClearBackStack(screen: Screen) {
        backStack.clear()
        currentScreen = screen
    }
    
    // 이전 화면으로 돌아가기
    fun goBack() {
        if (backStack.isNotEmpty()) {
            currentScreen = backStack.removeLast()
        }
    }
    
    // 백스택 비우기
    fun clearBackStack() {
        backStack.clear()
    }
    
    // 현재 백스택 크기 확인
    fun backStackSize(): Int = backStack.size
} 