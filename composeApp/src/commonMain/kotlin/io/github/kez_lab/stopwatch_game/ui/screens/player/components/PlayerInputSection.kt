package io.github.kez_lab.stopwatch_game.ui.screens.player.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Plus
import compose.icons.feathericons.UserPlus

@Composable
fun PlayerInputSection(
    name: String,
    onNameChange: (String) -> Unit,
    onAdd: () -> Unit,
    isError: Boolean,
    errorMessage: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            modifier = Modifier.weight(1f),
            label = { Text("플레이어 이름") },
            placeholder = { Text("이름을 입력하세요") },
            singleLine = true,
            isError = isError,
            supportingText = if (isError) {
                { Text("⚠ $errorMessage") }
            } else null,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onAdd() }),
            leadingIcon = {
                Icon(
                    imageVector = FeatherIcons.UserPlus,
                    contentDescription = null
                )
            }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(onClick = onAdd) {
            Icon(
                imageVector = FeatherIcons.Plus,
                contentDescription = "추가"
            )
        }
    }
}