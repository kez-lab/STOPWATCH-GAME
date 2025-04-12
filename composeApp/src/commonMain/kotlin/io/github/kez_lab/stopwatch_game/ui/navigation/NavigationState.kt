package io.github.kez_lab.stopwatch_game.ui.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class NavigationState {
    private val backStack = mutableListOf<Screen>()

    var currentScreen by mutableStateOf<Screen>(Screen.Home)
        private set

    /**
     * 기본 화면 전환
     */
    fun navigateTo(screen: Screen) {
        if (screen != currentScreen) {
            backStack.add(currentScreen)
            currentScreen = screen
        }
    }

    /**
     * 특정 화면까지 백스택 제거 후 화면 전환
     * inclusive = true → popUpTo 대상도 제거
     */
    fun navigateToWithPopUpTo(
        screen: Screen,
        popUpTo: Screen? = null,
        inclusive: Boolean = false
    ) {
        // 원래의 백스택 복사
        val tempBackStack = backStack.toMutableList()
        
        // popUpTo 대상이 현재 화면인 경우 처리
        val currentIsPopUpTarget = popUpTo != null && currentScreen == popUpTo
        
        if (popUpTo != null) {
            val index = tempBackStack.indexOfLast { it == popUpTo }
            
            if (index != -1) {
                // 백스택에서 popUpTo 대상까지 제거
                val removeFrom = if (inclusive) index else index + 1
                if (removeFrom in 0..tempBackStack.lastIndex) {
                    tempBackStack.subList(removeFrom, tempBackStack.size).clear()
                }
            }
            
            // 현재 화면이 popUpTo 대상이고 inclusive=true인 경우,
            // 현재 화면도 제거 대상에 포함
            if (currentIsPopUpTarget && inclusive) {
                // 현재 화면은 백스택에 추가하지 않음
            } else if (screen != currentScreen) {
                // 그렇지 않으면 현재 화면을 백스택에 추가
                tempBackStack.add(currentScreen)
            }
        } else {
            // popUpTo가 null이면 현재 화면만 백스택에 추가
            if (screen != currentScreen) {
                tempBackStack.add(currentScreen)
            }
        }
        
        // 변경된 백스택 적용
        backStack.clear()
        backStack.addAll(tempBackStack)
        
        // 현재 화면 설정
        currentScreen = screen
    }

    /**
     * 현재 화면 교체 (백스택 추가 없이)
     */
    fun replaceCurrentScreen(screen: Screen) {
        currentScreen = screen
    }

    /**
     * 모든 백스택 제거 후 새 화면으로 이동
     */
    fun navigateWithClearBackStack(screen: Screen) {
        backStack.clear()
        currentScreen = screen
    }

    /**
     * 뒤로 가기
     */
    fun goBack() {
        if (backStack.isNotEmpty()) {
            currentScreen = backStack.removeLast()
        }
    }

    /**
     * 백스택 초기화 (화면 유지)
     */
    fun clearBackStack() {
        backStack.clear()
    }

    fun backStackSize(): Int = backStack.size
}
