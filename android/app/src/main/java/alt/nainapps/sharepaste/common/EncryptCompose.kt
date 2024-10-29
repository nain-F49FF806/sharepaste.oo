package alt.nainapps.sharepaste.common

import alt.nainapps.sharepaste.common.units.ExpandableOptionsCard
import alt.nainapps.sharepaste.common.units.OptionMenu
import alt.nainapps.sharepaste.common.units.OutputLinkWithShareIcon
import alt.nainapps.sharepaste.common.units.OutputTextWithCopyIcon
import alt.nainapps.sharepaste.common.units.SwitchWithOnOffIcons
import alt.nainapps.sharepaste.common.units.TextModeToggleIconButton
import alt.nainapps.sharepaste.common.units.pasteFormatToggle
import alt.nainapps.sharepaste.rsnative.PrivateBinRs
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
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
    attachRawSize: String? = null,
    customPrivatebinHost: String? = null
) {
    var textToEncrypt by rememberSaveable { mutableStateOf(text) }
    var pasteFormat by rememberSaveable { mutableStateOf(textFormat ?: PasteFormat.PLAINTEXT) }
    var expiry by rememberSaveable { mutableStateOf("1day") }
    var burnOnRead by rememberSaveable { mutableStateOf(false) }
    var shareLink by rememberSaveable { mutableStateOf("") }
    var deleteLink by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var openDiscussion by rememberSaveable { mutableStateOf(false) }
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
        val density = LocalDensity.current
        var textFieldHeight: Int? by rememberSaveable { mutableStateOf(null) }
        OutlinedTextField(
            value = textToEncrypt,
            onValueChange = { textToEncrypt = it },
            label = {
                when (pasteFormat) {
                    PasteFormat.PLAINTEXT -> Text("Text to encrypt and share")
                    PasteFormat.MARKDOWN -> Text("Markdown to encrypt and share")
                    PasteFormat.SYNTAX -> Text("Code to encrypt and share")
                }
            },
            singleLine = false,
            minLines = 3,
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { textFieldHeight = it.height },
            trailingIcon = {
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = textFieldHeight?.let {
                        Modifier
                            .height(with(density) {
                                it.toDp() - OutlinedTextFieldDefaults
                                    .contentPadding()
                                    .calculateBottomPadding()
                            })
                    } ?: Modifier
                ) {
                    TextModeToggleIconButton(
                        mode = pasteFormat,
                        onCLick = { pasteFormat = pasteFormatToggle(pasteFormat) }
                    )
                }
            }
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

        SwitchWithOnOffIcons(label = "Burn on read", checked = burnOnRead) {
            burnOnRead = it
            if (burnOnRead) {
                openDiscussion = false
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ExpandableOptionsCard(
            title = "More options",
            defaultExpanded = !attachName.isNullOrEmpty()
        ) {
            SwitchWithOnOffIcons(
                label = "Enable Discussions",
                checked = openDiscussion
            ) {
                openDiscussion = it
                if (openDiscussion) {
                    burnOnRead = false
                }
            }
            if (!attachName.isNullOrEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Attachment",
                        fontWeight = FontWeight.Medium
                    )
                    Text(text = "$attachName ($attachRawSize)")
                }

            }
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
                    format = pasteFormat,
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
