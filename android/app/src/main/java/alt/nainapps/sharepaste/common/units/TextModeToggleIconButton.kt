package alt.nainapps.sharepaste.common.units

import alt.nainapps.sharepaste.R
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import uniffi.pbcli.PasteFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextModeToggleIconButton(mode: PasteFormat, onCLick: () -> Unit) {
    IconButton(
        onClick = onCLick
    ) {
        when (mode) {
            PasteFormat.PLAINTEXT -> Icon(
                imageVector = Icons.Filled.TextFields,
                contentDescription = mode.toString(),
                modifier = Modifier.size(SwitchDefaults.IconSize),
            )

            PasteFormat.MARKDOWN -> Icon(
                painter = painterResource(id = R.drawable.markdown_filled_24px),
                contentDescription = mode.toString(),
                modifier = Modifier.size(SwitchDefaults.IconSize)
            )

            PasteFormat.SYNTAX -> Icon(
                imageVector = Icons.Filled.Code,
                contentDescription = mode.toString(),
                modifier = Modifier.size(SwitchDefaults.IconSize),
            )

        }
    }

}

/// plaintext -> markdown -> code round robin.
fun pasteFormatToggle(mode: PasteFormat): PasteFormat {
    return when (mode) {
        PasteFormat.PLAINTEXT -> PasteFormat.MARKDOWN
        PasteFormat.MARKDOWN -> PasteFormat.SYNTAX
        PasteFormat.SYNTAX -> PasteFormat.PLAINTEXT
    }
}