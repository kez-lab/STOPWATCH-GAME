import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun SimpleButtonPreview() {
    MaterialTheme {
        Surface {
            Button(
                onClick = { },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("게임 시작하기")
            }
        }
    }
}

@Preview
@Composable
fun SimpleCardPreview() {
    MaterialTheme {
        Surface {
            Card(
                modifier = Modifier.padding(16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "TimeBattle",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "스톱워치 미니게임",
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun SimpleTimerDisplayPreview() {
    MaterialTheme {
        Surface {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(
                        Color.Gray.copy(alpha = 0.1f),
                        RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "00:05.432",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}