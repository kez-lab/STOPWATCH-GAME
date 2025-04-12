import androidx.compose.ui.window.ComposeUIViewController
import io.github.kez_lab.stopwatch_game.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
