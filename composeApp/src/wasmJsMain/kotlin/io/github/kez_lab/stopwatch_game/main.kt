package io.github.kez_lab.stopwatch_game

import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.preloadFont
import stopwatch_game.composeapp.generated.resources.NotoSans
import stopwatch_game.composeapp.generated.resources.Res

@OptIn(ExperimentalComposeUiApi::class, ExperimentalResourceApi::class)
fun main() {
    val body = document.body ?: return

    ComposeViewport(body) {
        val font by preloadFont(Res.font.NotoSans)
        font?.toFontFamily()?.let { App(fontFamily = it) }
    }
}
