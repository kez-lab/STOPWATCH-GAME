//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import io.github.kez_lab.stopwatch_game.model.Player
//import io.github.kez_lab.stopwatch_game.theme.AppTheme
//import io.github.kez_lab.stopwatch_game.ui.screens.player.components.PlayerInputSection
//import io.github.kez_lab.stopwatch_game.ui.screens.player.components.PlayerItem
//import io.github.kez_lab.stopwatch_game.ui.screens.player.components.PlayerListSection
//import io.github.kez_lab.stopwatch_game.ui.screens.player.components.ProceedButton
//
//@Preview(name = "Player Input Section")
//@Composable
//fun PlayerInputSectionPreview() {
//    AppTheme {
//        Surface {
//            PlayerInputSection(
//                name = "김철수",
//                onNameChange = { },
//                onAdd = { },
//                isError = false,
//                errorMessage = "",
//                modifier = Modifier.padding(16.dp)
//            )
//        }
//    }
//}
//
//@Preview(name = "Player Input Section Error")
//@Composable
//fun PlayerInputSectionErrorPreview() {
//    AppTheme {
//        Surface {
//            PlayerInputSection(
//                name = "",
//                onNameChange = { },
//                onAdd = { },
//                isError = true,
//                errorMessage = "이름을 입력해주세요",
//                modifier = Modifier.padding(16.dp)
//            )
//        }
//    }
//}
//
//@Preview(name = "Player Item")
//@Composable
//fun PlayerItemPreview() {
//    AppTheme {
//        Surface {
//            PlayerItem(
//                player = Player(id = "1", name = "김철수"),
//                onRemove = { },
//                modifier = Modifier.padding(16.dp)
//            )
//        }
//    }
//}
//
//@Preview(name = "Player List Section")
//@Composable
//fun PlayerListSectionPreview() {
//    AppTheme {
//        Surface {
//            Column {
//                PlayerListSection(
//                    players = listOf(
//                        Player(id = "1", name = "김철수"),
//                        Player(id = "2", name = "이영희"),
//                        Player(id = "3", name = "박민수")
//                    ),
//                    onRemove = { },
//                    modifier = Modifier.padding(16.dp)
//                )
//            }
//        }
//    }
//}
//
//@Preview(name = "Proceed Button")
//@Composable
//fun ProceedButtonPreview() {
//    AppTheme {
//        Surface {
//            ProceedButton(
//                onClick = { }
//            )
//        }
//    }
//}
//
//@Preview(name = "Simple Screen Layout")
//@Composable
//fun SimpleScreenLayoutPreview() {
//    AppTheme {
//        Surface {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp)
//            ) {
//                Text(
//                    text = "플레이어 등록",
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = MaterialTheme.colorScheme.primary
//                )
//
//                Text(
//                    text = "🎮 게임에 참여할 친구를 등록해 주세요!",
//                    fontSize = 16.sp,
//                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
//                    modifier = Modifier.padding(vertical = 16.dp)
//                )
//
//                PlayerInputSection(
//                    name = "새로운 플레이어",
//                    onNameChange = { },
//                    onAdd = { },
//                    isError = false,
//                    errorMessage = ""
//                )
//
//                PlayerListSection(
//                    players = listOf(
//                        Player(id = "1", name = "김철수"),
//                        Player(id = "2", name = "이영희")
//                    ),
//                    onRemove = { }
//                )
//            }
//        }
//    }
//}