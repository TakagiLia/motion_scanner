package biz.moapp.motion_scanner

import android.graphics.Bitmap
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import biz.moapp.motion_scanner.ui.CameraViewModel
import com.google.android.gms.tasks.Task
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import java.nio.ByteBuffer

class MotionAnalyzeUseCase(viewModel : CameraViewModel) : ImageAnalysis.Analyzer {

    val cameraViewModel = viewModel

    /**リアルタイム検出に適したモードに設定**/
    private val options = PoseDetectorOptions.Builder()
        .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
        .build()

    /**PoseDetector のインスタンスを作成**/
    private val poseDetector = PoseDetection.getClient(options)

    @OptIn(ExperimentalGetImage::class)
    fun motionAnalyze(imageProxy: ImageProxy, onPoseDetected: (Pose?) -> Unit){

        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            val result :Task<Pose> = poseDetector.process(image)
                .addOnSuccessListener { pose ->
                    onPoseDetected(pose)
                    val allPose = pose.allPoseLandmarks
                    val nose = pose.getPoseLandmark(PoseLandmark.NOSE)
                    val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
//                    Log.d("--analyze", "motionAnalyze Move! 　Success:")
                    Log.d("--analyze", "nose position: ${nose?.position}")
//                    Log.d("--analyze", "nose landmarkType: ${nose?.landmarkType}")
                }
                .addOnFailureListener { e ->
                    Log.e("--analyze", "Error: ${e.message}",e)
                }
//                .addOnCompleteListener {
//                    imageProxy.close()
//                }
        }

    }

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image

        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val result :Task<Pose> = poseDetector.process(image)
                .addOnSuccessListener { pose ->
                    cameraViewModel.setPose(pose)
                }
                .addOnFailureListener { e ->
                    Log.e("--analyze", "Error: ${e.message}",e)
                }.addOnCompleteListener {
                    imageProxy.close()
                }
        }

    }
}