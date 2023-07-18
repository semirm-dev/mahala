package semir.mahovkic.mahala

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.microblink.blinkid.MicroblinkSDK
import dagger.hilt.android.AndroidEntryPoint
import semir.mahovkic.mahala.ui.NavGraph
import semir.mahovkic.mahala.ui.theme.MahalaTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MicroblinkSDK.setLicenseFile("license.key", this)

        setContent {
            MahalaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph()
                }
            }
        }
    }
}
