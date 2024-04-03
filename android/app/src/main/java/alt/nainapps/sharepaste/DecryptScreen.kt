package alt.nainapps.sharepaste

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun DecryptScreen() {
    var inputText by rememberSaveable { mutableStateOf("") }
    var encryptedText by rememberSaveable { mutableStateOf("") }
    var decryptionKey by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Enter paste to decrypt") },
            minLines = 3
        )
        OutlinedTextField(
            value = decryptionKey,
            onValueChange = { decryptionKey = it },
            label = { Text("Decryption #Key") },
        )
        Button(onClick = {
            isLoading = true
            coroutineScope.launch(Dispatchers.Default) {
                val plainPaste = inputText //swiftDecrypt(inputText, decryptionKey)
                encryptedText = plainPaste
                isLoading = false
            }
        }) {
            if (isLoading) {
                CircularProgressIndicator(
                    Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Decrypt")
            }
        }
        if (encryptedText.isNotEmpty()) {
            OutlinedTextField(
                value = encryptedText,
                onValueChange = {}, // No action on value change
                label = { Text("Decrypted Paste") },
                readOnly = true, // Makes the text field read-only
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.None)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDecryptScreen() {
    DecryptScreen()
}