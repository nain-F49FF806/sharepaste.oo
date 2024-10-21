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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
    var timesClicked by rememberSaveable { mutableIntStateOf(0) }
    Card(
        onClick = {
            isExpanded = !isExpanded
            if (isExpanded) {
                timesClicked += 1
            }
        },
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
                modifier = textModifier,
            )
            if (timesClicked == 5) {
                Egg()
            }
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
                    modifier = textModifier,
                )
            }
        }
        if (isExpanded) {
            HorizontalDivider()
            content()
        }
    }
}

@Composable
fun Egg() {
    var timesNubbed by rememberSaveable { mutableStateOf(0) }

    fun moan(timesNubbed: Int): String {
        return when {
            timesNubbed < 5 -> "More" + "More".repeat(timesNubbed / 2)
            timesNubbed < 7 -> "MoreMoreMore"
            timesNubbed < 9 -> "\uD83E\uDD29"
            (timesNubbed > 9) and (timesNubbed < 12) -> "\uD83D\uDCA6"
            timesNubbed == 12 -> "\uD83D\uDE2D"
            (timesNubbed > 25) and (timesNubbed < 35) -> "\uD83E\uDD95"
            timesNubbed > 35 -> "\uD83E\uDD88"
            else -> "☺\uFE0F"
        }
    }
    TextButton(onClick = { timesNubbed += 1 }) { Text(moan(timesNubbed)) }
}