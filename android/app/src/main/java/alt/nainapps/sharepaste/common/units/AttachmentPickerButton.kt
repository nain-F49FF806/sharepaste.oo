package alt.nainapps.sharepaste.common.units

import alt.nainapps.sharepaste.utils.bytesToHumanReadableSize
import alt.nainapps.sharepaste.utils.inputStreamToBase64String
import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.io.FileNotFoundException
import java.io.IOException

@Composable
fun AttachmentPickerButton(prevAttachment: Attachment? = null, onAttach: (Attachment?) -> Unit) {
    var attachment: Attachment? = null
    val contentResolver = LocalContext.current.contentResolver
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { fileUri: Uri? ->
            fileUri?.let { attachment = getAttachmentFromFileUri(contentResolver, it) }
            attachment?.let { onAttach(it) }
        }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        if (prevAttachment != null) {
            OutlinedTextField(
                "${prevAttachment.name} (${prevAttachment.size})",
                {},
                readOnly = true,
                label = { Text("Attachment") },
                placeholder = { Text(text = "Attach file") },
                trailingIcon = { TextButton(onClick = { onAttach(null) }) { Text("Clear") } },
                // trailingIcon = { TextButton(onClick = { launcher.launch("*/*"); }) { Text("Change") } }
            )
        } else {
            TextButton(onClick = { launcher.launch("*/*") }) {
                Text(text = "Add attachment")
            }
        }
    }
}

const val TAG = "Attachment"

data class Attachment(
    // data uri containing attachment data
    val data: String,
    val name: String?,
    // Size of attachment pre base64-encoding
    val size: String?
)

fun getAttachmentFromFileUri(contentResolver: ContentResolver, fileUri: Uri): Attachment? {
    val attachData: String
    var attachName = "unknown name"
    var attachSize = "unknown size"
    var attachment: Attachment? = null
    try {
        val mimeType = contentResolver.getType(fileUri).also {
            Log.i(TAG, "Attach file type: $it")
        } ?: ""

        contentResolver.query(fileUri, null, null, null, null)?.use { cursor ->
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

        contentResolver.openInputStream(fileUri)?.use { inputStream ->
            // inputStream is guaranteed to be non-null here
            // Process the input stream
            Log.i(TAG, "Processing input stream...")
            // Convert InputStream to Base64 String
            inputStreamToBase64String(inputStream).let {
                // Construct the Data URI
                attachData = "data:$mimeType;base64,$it"
            }
            attachment = Attachment(attachData, attachName, attachSize)
        }

    } catch (e: FileNotFoundException) {
        // Handle the case where the file was not found
        Log.e(TAG, "File not found: ${e.message}")
    } catch (e: IOException) {
        // Handle general IO errors, including permission issues
        Log.e(TAG, "IO Error: ${e.message}")
    }
    return attachment
}
