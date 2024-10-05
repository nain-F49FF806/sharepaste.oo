package alt.nainapps.sharepaste.launcher

import alt.nainapps.sharepaste.common.EncryptAndShareUI
import alt.nainapps.sharepaste.launcher.ui.theme.SharePasteO2Theme
import alt.nainapps.sharepaste.settings.SettingsUI
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.preference.PreferenceManager

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val customPrivatebinHost = PreferenceManager.getDefaultSharedPreferences(
            this
        ).getString("privatebin_host_url", null)

        val bottomNavBarItems = listOf(
            BottomNavBarItem(
                title = "Share",
                selectedIcon = Icons.Filled.Create,
                unselectedIcon = Icons.Outlined.Create
            ),
            BottomNavBarItem(
                title = "Settings",
                selectedIcon = Icons.Filled.Settings,
                unselectedIcon = Icons.Outlined.Settings
            ),

        )
        setContent {
            SharePasteO2Theme {
                var selectedNavItemIndex by rememberSaveable { mutableIntStateOf(0) }
                    Scaffold (
                        topBar = {
                            if (selectedNavItemIndex == 1) {
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
                        },
                        bottomBar = {
                            NavigationBar {
                                bottomNavBarItems.forEachIndexed { index, item ->
                                    NavigationBarItem(
                                        selected = (selectedNavItemIndex == index),
                                        onClick = {selectedNavItemIndex = index},
                                        icon = {
                                            Icon(
                                                imageVector = if (selectedNavItemIndex == index) item.selectedIcon else item.unselectedIcon,
                                                contentDescription = item.title
                                            )
                                        },
                                        label = { Text(item.title) }
                                    )
                                }
                            }
                        }
                    ) { paddingValues ->
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.padding(paddingValues).fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            if (selectedNavItemIndex == 0) {
                                EncryptAndShareUI(
                                    customPrivatebinHost = customPrivatebinHost,
                                )
                            } else if (selectedNavItemIndex == 1) {
                                SettingsUI()
                            }
                        }
                    }


            }
        }
    }
}

data class BottomNavBarItem (
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

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
