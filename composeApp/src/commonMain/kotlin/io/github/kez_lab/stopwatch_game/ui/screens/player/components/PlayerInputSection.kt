package io.github.kez_lab.stopwatch_game.ui.screens.player.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import compose.icons.FeatherIcons
import compose.icons.feathericons.Plus
import compose.icons.feathericons.UserPlus
import io.github.kez_lab.stopwatch_game.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PlayerInputSection(
    name: String,
    onNameChange: (String) -> Unit,
    onAdd: () -> Unit,
    isError: Boolean,
    errorMessage: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text("플레이어 이름") },
        placeholder = { Text("이름을 입력하세요") },
        singleLine = true,
        isError = isError,
        supportingText = if (isError) {
            {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        } else {
            null
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { onAdd() }),
        leadingIcon = {
            Icon(
                imageVector = FeatherIcons.UserPlus,
                contentDescription = null
            )
        },
        trailingIcon = {
            IconButton(onClick = onAdd) {
                Icon(
                    imageVector = FeatherIcons.Plus,
                    contentDescription = "추가"
                )
            }
        }
    )
}

@Preview
@Composable
fun PlayerInputSectionPreview() {
    AppTheme {
        PlayerInputSection(
            name = "플레이어 이름",
            onNameChange = {},
            onAdd = {},
            isError = false,
            errorMessage = "에러 메시지"
        )
    }
}