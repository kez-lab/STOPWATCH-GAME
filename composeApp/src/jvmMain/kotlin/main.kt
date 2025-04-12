import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension
import io.github.kez_lab.stopwatch_game.App
import org.jetbrains.compose.reload.DevelopmentEntryPoint

fun main() = application {
    Window(
        title = "StopWatch Game",
        state = rememberWindowState(width = 800.dp, height = 600.dp),
        onCloseRequest = ::exitApplication,
    ) {
        window.minimumSize = Dimension(350, 600)
        DevelopmentEntryPoint {
            App()
        }
    }
}

@Preview
@Composable
fun AppPreview() { App() }
