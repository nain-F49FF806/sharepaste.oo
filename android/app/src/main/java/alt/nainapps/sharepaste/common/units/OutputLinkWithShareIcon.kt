package alt.nainapps.sharepaste.common.units

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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
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
import androidx.core.content.ContextCompat.startActivity

@Composable
fun OutputLinkWithShareIcon(link: String, label: String = "Link", singleLine: Boolean = false) {
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
                value = link,
                onValueChange = {},
                label = { Text(label) },
                enabled = true,
                readOnly = true,
                singleLine = singleLine,
                modifier = Modifier
                    .clickable(
                        onClick = {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                        }
                    )
            )
        }
        Row {
            IconButton(
                onClick = {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, link)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, "Share private paste link")
                    startActivity(context, shareIntent, null)
                }
            ) {
                Icon(imageVector = Icons.Default.Share, contentDescription = "Share Link")
            }
            IconButton(
                onClick = {
                    clipboardManager.setPrimaryClip(ClipData.newPlainText(label, link))
                    Toast.makeText(
                        context,
                        "Copied ${label.lowercase()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ) {
                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy Link")
            }
            IconButton(
                onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Open Link"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OutputLinkWithCopyIconPreview() {
    OutputLinkWithShareIcon("https://example.com/encrypted-paste")
}

@Preview(showBackground = true)
@Composable
fun InvalidLink() {
    OutputLinkWithShareIcon("//example/encrypted-paste")
}

@Preview(showBackground = true)
@Composable
fun LongLink() {
    OutputLinkWithShareIcon(
        "https://example.com/encrypted-paste?jksdjvnksbvjkrbjkdrfbjkdrbk#jsdnkjvnerlknvgerlknblkernblker"
    )
}
