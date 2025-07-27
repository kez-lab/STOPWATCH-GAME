package io.github.kez_lab.stopwatch_game.ui.screens.game.play

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/**
 * 게임 종료 확인 다이얼로그 컴포넌트
 */
@Composable
internal fun GameExitConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("게임 종료") },
        text = { Text("게임을 종료하시겠습니까?\n현재 진행 중인 게임 결과는 저장되지 않습니다.") },
        confirmButton = {
            TextButton(onClick = onConfirm) { 
                Text("종료") 
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("계속 진행")
            }
        }
    )
}