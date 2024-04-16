package alt.nainapps.sharepaste.common.units

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalTextInputService


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> OptionMenu(
    label: String,
    options: List<T>,
    defaultOption: T? = null,
    onSelection: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(defaultOption ?: options[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        CompositionLocalProvider(
            // This disables keyboard functionality for this block
            LocalTextInputService provides null
        ) {
            OutlinedTextField(
                // The `menuAnchor` modifier must be passed to the text field for correctness.
                modifier = Modifier.menuAnchor(),
                value = selectedOption.toString(),
                // Use the drop down menus on click instead to know value.
                // This onValueChange here seems to not reliably change.
                onValueChange = { },
                readOnly = true,
                singleLine = true,
                label = { Text(label) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
            )
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.toString(), style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        selectedOption = option
                        expanded = false
                        onSelection(selectedOption)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun TryMenu() {
    val options = listOf("Cupcake", "Donut", "Eclair", "Froyo", "Gingerbread")
    OptionMenu("Dessert", options = options, onSelection = {} )
}