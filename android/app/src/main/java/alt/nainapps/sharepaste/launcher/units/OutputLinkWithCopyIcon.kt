package alt.nainapps.sharepaste.launcher.units

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun OutputLinkWithCopyIcon(link: String, label: String = "Link", singleLine: Boolean = false) {
    val context = LocalContext.current
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        TextField(
            value = link,
            onValueChange = {},
            label = { Text(label) },
            enabled = true,
            readOnly = true,
            singleLine = singleLine,
            modifier = Modifier
                .clickable(
                    onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link))) }
                ),
        )
        Row{
            IconButton(
                onClick = {
                    clipboardManager.setPrimaryClip(ClipData.newPlainText(label, link))
                    Toast.makeText(context, "Copied ${label.lowercase()}", Toast.LENGTH_LONG).show()
                }
            ) {
                Icon(imageVector = Icons.Default.Share, contentDescription = "Copy Link" )
            }
            IconButton(
                onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                }
            ) {
                Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Open Link" )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun OutputLinkWithCopyIconPreview() {
    OutputLinkWithCopyIcon("https://example.com/encrypted-paste")
}

@Preview(showBackground = true)
@Composable
fun InvalidLink() {
    OutputLinkWithCopyIcon("//example/encrypted-paste")
}


@Preview(showBackground = true)
@Composable
fun LongLink() {
    OutputLinkWithCopyIcon("https://example.com/encrypted-paste?jksdjvnksbvjkrbjkdrfbjkdrbk#jsdnkjvnerlknvgerlknblkernblker")
}
