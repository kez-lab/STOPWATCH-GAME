@file:OptIn(
    ExperimentalResourceApi::class,
    ExperimentalResourceApi::class,
    ExperimentalComposeUiApi::class
)

package io.github.kez_lab.stopwatch_game

import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.window.CanvasBasedWindow
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.preloadFont
import stopwatch_game.composeapp.generated.resources.NotoSans
import stopwatch_game.composeapp.generated.resources.Res

fun main() {
    CanvasBasedWindow("ComposeTarget") {
        val font by preloadFont(Res.font.NotoSans)
        font?.toFontFamily()?.let { App(fontFamily = it) }
    }
}
