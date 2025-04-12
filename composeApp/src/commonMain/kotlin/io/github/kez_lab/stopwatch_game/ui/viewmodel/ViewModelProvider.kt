package io.github.kez_lab.stopwatch_game.ui.viewmodel

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import io.github.kez_lab.stopwatch_game.viewmodel.AppViewModel

/**
 * AppViewModel을 위한 CompositionLocal
 * 앱 전체에서 단일 AppViewModel 인스턴스에 접근하기 위한 컨텍스트 제공
 */
val LocalAppViewModel: ProvidableCompositionLocal<AppViewModel> = compositionLocalOf {
    error("LocalAppViewModel이 제공되지 않았습니다. CompositionLocalProvider에서 AppViewModel을 설정해야 합니다.")
} 