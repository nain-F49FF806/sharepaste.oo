package alt.nainapps.sharepaste.settings

import alt.nainapps.sharepaste.R
import alt.nainapps.sharepaste.settings.ui.theme.SharePasteO2Theme
import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.textFieldPreference

class SettingsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SharePasteO2Theme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary
                            ),
                            title = {
                                Text("SharePasteO₂ Settings")
                            }
                        )
                    }
                ) { innerPadding ->
                    ProvidePreferenceLocals {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(innerPadding)
                        ) {
                            textFieldPreference(
                                key = "privatebin_host_url",
                                defaultValue = getString(R.string.default_privatebin_host_url),
                                textToValue = { it },
                                title = { Text(text = "Privatebin host (server)") },
                                summary = {
                                    Text(
                                        text = if (looksValidUrl(it)) {
                                            it
                                        } else {
                                            "Invalid looking url: " + it.ifBlank { "<empty>" }
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

fun looksValidUrl(potentialUrl: String) = Patterns.WEB_URL.matcher(potentialUrl).matches()

@Composable
fun Greeting3(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    SharePasteO2Theme {
        Greeting3("Android")
    }
}
