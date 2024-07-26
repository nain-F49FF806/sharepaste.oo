package alt.nainapps.sharepaste.intents

import alt.nainapps.sharepaste.common.EncryptAndShareUI
import alt.nainapps.sharepaste.intents.ui.theme.SharePasteO2Theme
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
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
import java.io.FileNotFoundException
import java.io.IOException
import uniffi.pbcli.PasteFormat

class ShareTextActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        assert(intent?.action == Intent.ACTION_SEND) {
            "This activity is expected to be intent filtered to Intent.ACTION_SEND"
        }
        @Suppress("ktlint:standard:property-naming")
        val TAG = "ShareTextActivity"
        var text: String = ""
        var textFormat: PasteFormat? = null
        val customPrivatebinHost = PreferenceManager.getDefaultSharedPreferences(
            this
        ).getString("privatebin_host_url", null)

        if (intent.type?.startsWith("text/", ignoreCase = true) == true) {
            when (intent.type) {
                "text/plain" -> {
                    textFormat = PasteFormat.PLAINTEXT
                }
                "text/markdown" -> {
                    textFormat = PasteFormat.MARKDOWN
                }
                else -> {
                    textFormat = PasteFormat.SYNTAX
                }
            }
            text = intent.getStringExtra(Intent.EXTRA_TEXT).orEmpty()

            // If there's a text file attached, we should read in it's contents too.
            val contentUri: Uri? = intent.getParcelableExtra<Parcelable>(
                Intent.EXTRA_STREAM
            ) as? Uri

            contentUri?.let { contentUri ->
                try {
                    contentResolver.getType(contentUri)?.let { mimeType ->
                        if (mimeType.startsWith("text/") == false) {
                            Log.w(TAG, "Intent said text but file is: $mimeType")
                        }
                    }
                    contentResolver.openInputStream(contentUri)?.use { inputStream ->
                        // inputStream is guaranteed to be non-null here
                        // Process the input stream
                        Log.i(TAG, "Processing input stream...")
                        val inputAsString = inputStream.bufferedReader().use { it.readText() }
                        if (text.isEmpty()) {
                            text += (inputAsString)
                        } else {
                            text += ("\n$inputAsString")
                        }
                    }
                } catch (e: FileNotFoundException) {
                    // Handle the case where the file was not found
                    Log.e(TAG, "File not found: ${e.message}")
                } catch (e: IOException) {
                    // Handle general IO errors, including permission issues
                    Log.e(TAG, "IO Error: ${e.message}")
                }
            }
        }

        setContent {
            SharePasteO2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EncryptAndShareUI(
                        text = text,
                        textFormat = textFormat,
                        customPrivatebinHost = customPrivatebinHost
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
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
