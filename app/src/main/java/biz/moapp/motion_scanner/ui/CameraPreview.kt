package biz.moapp.motion_scanner.ui


import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.graphics.ImageFormat
import android.util.Log
import android.util.Size
import android.util.SparseIntArray
import android.view.Surface
import androidx.annotation.OptIn
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import biz.moapp.motion_scanner.MotionAnalyzeUseCase
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalGetImage::class)
@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    cameraViewModel : CameraViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember{ ProcessCameraProvider.getInstance(context)}
    var imageCapture: ImageCapture? = null
    lateinit var cameraExecutor: ExecutorService
    val pose by cameraViewModel.pose.collectAsState()
    var imageWidth = 0
    var imageHeight = 0
    val density = LocalDensity.current

    BoxWithConstraints {
        val width = maxWidth
        val height = maxHeight
        imageWidth = with(density){ maxWidth.toPx().toInt() }
        imageHeight = with(density){ maxHeight.toPx().toInt() }

        Box {
            AndroidView(
                factory = { ctx ->
                    PreviewView(ctx).apply {

                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()

                            cameraExecutor = Executors.newSingleThreadExecutor()

                            /**プレビューのユースケースを作成**/
                            val preview = Preview.Builder().build().also {
                                it.setSurfaceProvider(this.surfaceProvider)
                            }

                            /**撮影のユースケースを作成**/
                            imageCapture = ImageCapture.Builder().build()
                            /**解析のユースケース**/
                            val imageAnalyzer = ImageAnalysis.Builder()
                                .setTargetResolution(Size(PreviewView(ctx).width, PreviewView(ctx).height))
                                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build()

                            imageAnalyzer.setAnalyzer(
                                cameraExecutor,
                                ImageAnalysis.Analyzer { imageProxy ->
                                    /**カメラセンサーが生成した画像とデバイスの自然な向きとの間の回転角度を表す
                                     * ImageInfo：画像のメタデータ入っているオブジェクト
                                     * rotationDegrees：カメラセンサーの向きに基づいた回転角度（値は変わらない？？）
                                     *  回転角度を変更したい場合はandroid.graphics.Matrixを利用する
                                     * **/
                                    imageProxy.width
                                    val rotationDegrees = imageProxy.imageInfo.rotationDegrees

                                    MotionAnalyzeUseCase(cameraViewModel).analyze(imageProxy)
                                })

                            /**前面のカメラを選択**/
                            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                            try {
                                /**バインドされているユースケースがあれば、すべて解除する**/
                                cameraProvider.unbindAll()
                                /**プレビュー、撮影ユースケースをカメラにバインドする**/
                                cameraProvider.bindToLifecycle(
                                    lifecycleOwner, cameraSelector, preview, imageCapture, imageAnalyzer
                                )

                            } catch (exc: Exception) {
                                Log.e(TAG, "ユースケースのバインディングに失敗", exc)
                            }
                        }, ContextCompat.getMainExecutor(this.context))


                    }
                }
            )
            pose?.let { PoseOverlay(it, imageWidth,imageHeight ) }
        }

    }
}