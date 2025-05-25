//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Surface
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import io.github.kez_lab.stopwatch_game.model.Game
//import io.github.kez_lab.stopwatch_game.model.GameType
//import io.github.kez_lab.stopwatch_game.theme.AppTheme
//import io.github.kez_lab.stopwatch_game.ui.components.GameCard
//import io.github.kez_lab.stopwatch_game.ui.components.TimerButton
//import io.github.kez_lab.stopwatch_game.ui.components.TimerDisplay
//
//@Preview(name = "Timer Display Running")
//@Composable
//fun TimerDisplayRunningPreview() {
//    AppTheme {
//        Surface {
//            TimerDisplay(
//                displayTime = "00:05.432",
//                isRunning = true,
//                modifier = Modifier.padding(16.dp)
//            )
//        }
//    }
//}
//
//@Preview(name = "Timer Display Finished")
//@Composable
//fun TimerDisplayFinishedPreview() {
//    AppTheme {
//        Surface {
//            TimerDisplay(
//                displayTime = "00:03.250",
//                isFinished = true,
//                modifier = Modifier.padding(16.dp)
//            )
//        }
//    }
//}
//
//@Preview(name = "Timer Display Timeout")
//@Composable
//fun TimerDisplayTimeoutPreview() {
//    AppTheme {
//        Surface {
//            TimerDisplay(
//                displayTime = "TIME OUT",
//                isFinished = true,
//                modifier = Modifier.padding(16.dp)
//            )
//        }
//    }
//}
//
//@Preview(name = "Timer Buttons")
//@Composable
//fun TimerButtonsPreview() {
//    AppTheme {
//        Surface {
//            Column(modifier = Modifier.padding(16.dp)) {
//                TimerButton(
//                    isRunning = false,
//                    isFinished = false,
//                    onClick = { }
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                TimerButton(
//                    isRunning = true,
//                    isFinished = false,
//                    onClick = { }
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                TimerButton(
//                    isRunning = false,
//                    isFinished = true,
//                    onClick = { }
//                )
//            }
//        }
//    }
//}
//
//@Preview(name = "Game Card")
//@Composable
//fun GameCardPreview() {
//    AppTheme {
//        Surface {
//            GameCard(
//                game = Game(
//                    id = "ms_digit",
//                    name = "밀리초 끝자리 맞추기",
//                    description = "정확히 멈춰서 밀리초 끝자리가 가장 높은 사람이 승리!",
//                    gameType = GameType.MS_DIGIT,
//                    difficultyLevel = 1,
//                    iconName = "ic_ms_digit"
//                ),
//                onClick = { },
//            )
//        }
//    }
//}