package alt.nainapps.sharepaste.intents

import alt.nainapps.sharepaste.common.EncryptAndShareUI
import alt.nainapps.sharepaste.intents.ui.theme.SharePasteO2Theme
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.preference.PreferenceManager
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

const val TAG = "ShareAttachActivity"

class ShareAttachActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        assert(intent?.action == Intent.ACTION_SEND) {
            "This activity is expected to be intent filtered to Intent.ACTION_SEND"
        }
        var attach: String? = null
        var attachName: String? = null
        var attachSize: String? = "unknown size"
        val customPrivatebinHost = PreferenceManager.getDefaultSharedPreferences(
            this
        ).getString("privatebin_host_url", null)

        // If there is some extra text, receive it.
        val text = intent.getStringExtra(Intent.EXTRA_TEXT).orEmpty()

        // If there's a file attached, we should read it and covert it's contents to dataUri.
        val contentUri: Uri? = intent.getParcelableExtra<Parcelable>(
            Intent.EXTRA_STREAM
        ) as? Uri

        contentUri?.let { contentUri ->
            try {
                val mimeType = contentResolver.getType(contentUri).also {
                    Log.i(TAG, "Attach file type: $it")
                } ?: ""

                /*
                 * Get the file's content URI from the incoming Intent,
                 * then query the server app to get the file's display name
                 * and size.
                 */
                contentResolver.query(contentUri, null, null, null, null)?.use { cursor ->
                    /*
                     * Get the column indexes of the data in the Cursor,
                     * move to the first row in the Cursor, get the data,
                     * and display it.
                     */
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                    cursor.moveToFirst()
                    attachName = cursor.getString(nameIndex)
                    attachSize = bytesToHumanReadableSize(cursor.getLong(sizeIndex))
                }

                contentResolver.openInputStream(contentUri)?.use { inputStream ->
                    // inputStream is guaranteed to be non-null here
                    // Process the input stream
                    Log.i(TAG, "Processing input stream...")
                    // Convert InputStream to Base64 String
                    inputStreamToBase64String(inputStream).let {
                        // Construct the Data URI
                        attach = "data:$mimeType;base64,$it"
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

        setContent {
            SharePasteO2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            textAlign = TextAlign.Center,
                            text = "Attachment: $attachName ($attachSize)"
                        )
                        // Note: TODO(" We should maybe warn when too big file size above ")
                        EncryptAndShareUI(
                            text = text,
                            attach = attach,
                            attachName = attachName,
                            customPrivatebinHost = customPrivatebinHost
                        )
                    }
                }
            }
        }
    }
}

// https://stackoverflow.com/a/68822715
fun bytesToHumanReadableSize(bytes: Long) = when {
    bytes >= 1 shl 30 -> "%.1f GB".format(bytes.toDouble() / (1 shl 30))
    bytes >= 1 shl 20 -> "%.1f MB".format(bytes.toDouble() / (1 shl 20))
    bytes >= 1 shl 10 -> "%.0f kB".format(bytes.toDouble() / (1 shl 10))
    else -> "$bytes bytes"
}

@OptIn(ExperimentalEncodingApi::class)
fun inputStreamToBase64String(inputStream: InputStream): String? = try {
    Base64.encode(inputStream.readBytes())
} catch (e: Exception) {
    Log.e(TAG, "Encoding Base64 Error: ${e.message}")
    null // Handle exception appropriately
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
