package alt.nainapps.sharepaste.common.units

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExpandableOptionsCard(
    modifier: Modifier = Modifier,
    title: String = "More Options",
    defaultExpanded: Boolean = false,
    content: @Composable() (ColumnScope.() -> Unit)
) {
    var isExpanded by rememberSaveable { mutableStateOf(defaultExpanded) }
    Card(
        onClick = { isExpanded = !isExpanded },
        modifier = modifier,
        elevation = CardDefaults.outlinedCardElevation(),
        colors = CardDefaults.outlinedCardColors()
    ) {
        val textModifier = Modifier.padding(4.dp)
        Row(
            modifier
                .padding(4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                title,
                fontSize = 18.sp,
                modifier = textModifier
            )
            if (!isExpanded) {
                Text(
                    "Expand",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = textModifier,
                )
            } else {
                Text(
                    "Collapse",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = textModifier
                )
            }
        }
        if (isExpanded) {
            HorizontalDivider()
            content()
        }
    }
}