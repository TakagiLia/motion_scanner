package biz.moapp.motion_scanner.ui

import android.content.ContentValues.TAG
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import java.io.File
import kotlin.coroutines.resume

@Composable
fun CameraScreen(modifier: Modifier, navController : NavController, cameraViewModel:CameraViewModel){
    var hasCameraPermission by remember { mutableStateOf(false) }
//    val viewModel = localC

    Column(
        modifier = modifier.fillMaxSize(),
        ){

        /**パーミッションリクエストの結果を受け取る launcher を作成**/
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { granted ->
                hasCameraPermission = granted
            }
        )

        /**カメラパーミッションをリクエスト**/
        LaunchedEffect(Unit) {
            launcher.launch(android.Manifest.permission.CAMERA)
        }
        if(hasCameraPermission){
            CameraPreview(cameraViewModel = cameraViewModel)
        }else{
            Text(text = "Non Camera Permission", modifier = modifier)
        }
    }
}