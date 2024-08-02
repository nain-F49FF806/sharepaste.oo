package alt.nainapps.sharepaste.common.units

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun OutputTextWithCopyIcon(text: String, label: String = "Text", singleLine: Boolean = false) {
    val context = LocalContext.current
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        CompositionLocalProvider(
            // This disables keyboard functionality for this block
            LocalTextInputService provides null
        ) {
            TextField(
                value = text,
                onValueChange = {},
                label = { Text(label) },
                enabled = true,
                readOnly = true,
                singleLine = singleLine
            )
        }
        Row {
            IconButton(
                onClick = {
                    clipboardManager.setPrimaryClip(ClipData.newPlainText(label, text))
                    Toast.makeText(
                        context,
                        "Copied ${label.lowercase()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ) {
                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy Text")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OutputTextWithCopyIconPreview() {
    OutputTextWithCopyIcon("https://example.com/encrypted-paste")
}

@Preview(showBackground = true)
@Composable
fun LongText() {
    OutputTextWithCopyIcon(
        "Woo! We have a link https://example.com/encrypted-paste?jksdjvnksbvjkrbjkdrfbjkdrbk#jsdnkjvnerlknvgerlknblkernblker"
    )
}
