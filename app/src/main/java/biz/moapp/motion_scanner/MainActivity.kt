package biz.moapp.motion_scanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import biz.moapp.motion_scanner.ui.BaseScreen
import biz.moapp.motion_scanner.ui.CameraViewModel
import biz.moapp.motion_scanner.ui.theme.Motion_scannerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val localCameraViewModel = compositionLocalOf<CameraViewModel> { error("No CameraViewModel provided") }
        setContent {
            Motion_scannerTheme {
                val parentViewModel: CameraViewModel = viewModel()
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                    CompositionLocalProvider(localCameraViewModel provides parentViewModel) {
                        BaseScreen()
                    }
                }
            }
        }
    }
}