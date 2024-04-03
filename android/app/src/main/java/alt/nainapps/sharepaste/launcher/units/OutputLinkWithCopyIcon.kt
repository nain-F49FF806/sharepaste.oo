package alt.nainapps.sharepaste.launcher.units

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun OutputLinkWithCopyIcon(link: String) {
    val context = LocalContext.current
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = link,
            modifier = Modifier.clickable {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                context.startActivity(browserIntent)
            }
        )

        IconButton(
            onClick = {
                clipboardManager.setPrimaryClip(ClipData.newPlainText("Encrypted Link", link))
                Toast.makeText(context, "Copied paste link", Toast.LENGTH_LONG).show()
            }
        ) {
            Icon(imageVector = Icons.Default.Share, contentDescription = "Copy Link" )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OutputLinkWithCopyIconPreview() {
    OutputLinkWithCopyIcon("https://example.com/encrypted-paste")
}