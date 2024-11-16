package alt.nainapps.sharepaste.intents

import alt.nainapps.sharepaste.common.EncryptAndShareUI
import alt.nainapps.sharepaste.common.units.getAttachmentFromFileUri
import alt.nainapps.sharepaste.intents.ui.theme.SharePasteO2Theme
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.preference.PreferenceManager

class ShareAttachActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        assert(intent?.action == Intent.ACTION_SEND) {
            "This activity is expected to be intent filtered to Intent.ACTION_SEND"
        }
        @Suppress("ktlint:standard:property-naming")
        val customPrivatebinHost = PreferenceManager.getDefaultSharedPreferences(
            this
        ).getString("privatebin_host_url", null)

        // If there is some extra text, receive it.
        val text = intent.getStringExtra(Intent.EXTRA_TEXT).orEmpty()

        // If there's a file attached, we should read it and covert it's contents to dataUri.
        val contentUri: Uri? = intent.getParcelableExtra<Parcelable>(
            Intent.EXTRA_STREAM
        ) as? Uri
        val attachment = contentUri?.let { getAttachmentFromFileUri(contentResolver, it) }

        setContent {
            SharePasteO2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Note: TODO("We should maybe warn when too big file size")
                    EncryptAndShareUI(
                        text = text,
                        sharedAttachment = attachment,
                        customPrivatebinHost = customPrivatebinHost
                    )
                }
            }
        }
    }
}

@Composable
private fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun GreetingPreview() {
    SharePasteO2Theme {
        Greeting("Android")
    }
}
