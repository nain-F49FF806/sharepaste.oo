package alt.nainapps.sharepaste.common

import alt.nainapps.sharepaste.common.units.ExpandableOptionsCard
import alt.nainapps.sharepaste.common.units.OptionMenu
import alt.nainapps.sharepaste.common.units.OutputLinkWithShareIcon
import alt.nainapps.sharepaste.common.units.OutputTextWithCopyIcon
import alt.nainapps.sharepaste.common.units.SwithWithOnOffIcons
import alt.nainapps.sharepaste.rsnative.PrivateBinRs
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uniffi.pbcli.PasteException
import uniffi.pbcli.PasteFormat

@Composable
fun EncryptAndShareUI(
    text: String = "",
    textFormat: PasteFormat? = null,
    attach: String? = null,
    attachName: String? = null,
    customPrivatebinHost: String? = null
) {
    var textToEncrypt by rememberSaveable { mutableStateOf(text) }
    val textFormat by rememberSaveable { mutableStateOf(textFormat) }
    val attach by rememberSaveable { mutableStateOf(attach) }
    val attachName by rememberSaveable { mutableStateOf(attachName) }
    var expiry by rememberSaveable { mutableStateOf("1day") }
    var burnOnRead by rememberSaveable { mutableStateOf(false) }
    val customPrivatebinHost by rememberSaveable { mutableStateOf(customPrivatebinHost) }
    var shareLink by rememberSaveable { mutableStateOf("") }
    var deleteLink by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var openDiscussion by rememberSaveable { mutableStateOf(false) }
    var moreOptionCardExpandrd by rememberSaveable {
        mutableStateOf(!attachName.isNullOrEmpty())
    }
    var errorString by rememberSaveable { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier =
        Modifier
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

        OptionMenu(
            label = "expiry",
            options = listOf("5min", "1hour", "1day", "1week", "1month"),
            defaultOption = expiry
        ) {
            expiry = it
            println("expiry = $expiry")
        }

        Spacer(modifier = Modifier.height(16.dp))

        SwithWithOnOffIcons(label = "Burn on read") { burnOnRead = it }

        Spacer(modifier = Modifier.height(16.dp))

        ExpandableOptionsCard(title = "More options") {
            SwithWithOnOffIcons(label = "Enable Discussions") { openDiscussion = it }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Call your encryption function here
            isLoading = true
            shareLink = ""
            deleteLink = ""
            errorString = ""
            coroutineScope.launch(Dispatchers.IO) {
                val pb = PrivateBinRs(defaultBaseUrl = customPrivatebinHost)
                val opts = pb.getOpts(
                    format = textFormat,
                    expire = expiry,
                    burn = burnOnRead,
                    discussion = openDiscussion
                )
                try {
                    val pbResponse = pb.send(textToEncrypt, opts, attach, attachName)
                    shareLink = pbResponse.toPasteUrl()
                    deleteLink = pbResponse.toDeleteUrl()
                } catch (e: PasteException) {
                    errorString = e.toString()
                }

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
            OutputLinkWithShareIcon(link = shareLink, "Share link")
        }

        if (deleteLink.isNotEmpty()) {
            OutputLinkWithShareIcon(link = deleteLink, "Early delete link", singleLine = true)
        }
        if (errorString.isNotEmpty()) {
            OutputTextWithCopyIcon(text = errorString, "Error")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EncryptAndShareUIPreview() {
    EncryptAndShareUI()
}
