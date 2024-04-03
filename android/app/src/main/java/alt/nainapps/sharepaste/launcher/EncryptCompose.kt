package alt.nainapps.sharepaste.launcher


import alt.nainapps.sharepase.rsnative.PrivateBinRs
import alt.nainapps.sharepaste.launcher.units.OptionDropdownMenu
import alt.nainapps.sharepaste.launcher.units.OutputLinkWithCopyIcon
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun EncryptAndShareUI() {
    var textToEncrypt by remember { mutableStateOf("") }
    var expiry by remember { mutableStateOf("5min") }
    var shareLink by remember { mutableStateOf("") }
    var deleteLink by remember { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = textToEncrypt,
            onValueChange = { textToEncrypt = it },
            label = { Text("Text to encrypt and share") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            minLines = 3
        )

        Spacer(modifier = Modifier.height(16.dp))

        OptionDropdownMenu(
            label = "expiry",
            options = listOf("5min", "1hour", "1day", "1week", "1month")
        ) {
            expiry = it
            println("expiry = $expiry")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Call your encryption function here
            // For demonstration, let's just set a dummy encrypted link
            isLoading = true
            coroutineScope.launch(Dispatchers.IO) {
                val pb = PrivateBinRs()
                val opts = pb.getOpts(expire = expiry)
                val pbRespose = pb.send(textToEncrypt, opts)
                shareLink = pbRespose.toUrl(pb.defaultBaseUrl)
                deleteLink = pbRespose.toDeleteUrl(pb.defaultBaseUrl)
                isLoading = false
            }

        }) {
            if (isLoading) {
                CircularProgressIndicator(
                    Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Encrypt & Share")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (shareLink.isNotEmpty()) {
            OutputLinkWithCopyIcon(link = shareLink, "Share link")
        }

        if (deleteLink.isNotEmpty()) {
            OutputLinkWithCopyIcon(link = deleteLink, "Early delete link")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EncryptAndShareUIPreview() {
    EncryptAndShareUI()
}