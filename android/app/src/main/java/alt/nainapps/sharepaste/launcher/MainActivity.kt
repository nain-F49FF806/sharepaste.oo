package alt.nainapps.sharepaste.launcher

import alt.nainapps.sharepaste.common.EncryptAndShareUI
import alt.nainapps.sharepaste.launcher.ui.theme.SharePasteO2Theme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.preference.PreferenceManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val customPrivatebinHost = PreferenceManager.getDefaultSharedPreferences(
            this
        ).getString("privatebin_host_url", null)

        setContent {
            SharePasteO2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EncryptAndShareUI(
                        customPrivatebinHost = customPrivatebinHost
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun GreetingPreview2() {
    SharePasteO2Theme {
        Greeting2("Android")
    }
}
