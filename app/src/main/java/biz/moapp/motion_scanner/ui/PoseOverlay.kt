package biz.moapp.motion_scanner.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import biz.moapp.motion_scanner.R
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

@Composable
fun PoseOverlay(pose: Pose, imageWidth: Int, imageHeight: Int,  cameraViewModel : CameraViewModel) {
    val animationVisible by cameraViewModel.animationVisible.collectAsState()
    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.pico)
    AnimatedVisibility(
        visible = animationVisible,
        enter = expandHorizontally(),
        exit = shrinkHorizontally()
    ) {
        Image(
            modifier = Modifier
                .size(80.dp)
                .offset(90f.dp, 60f.dp),
            painter = painterResource(R.drawable.pico),
            contentDescription = ""
        )
    }
    Canvas(modifier = Modifier.fillMaxHeight(imageHeight.toFloat()).fillMaxWidth(imageWidth.toFloat())) {

        val connections = listOf(
            /**顔**/
//            Pair(PoseLandmark.LEFT_EYE_INNER, PoseLandmark.LEFT_EYE),
//            Pair(PoseLandmark.LEFT_EYE, PoseLandmark.LEFT_EYE_OUTER),
//            Pair(PoseLandmark.LEFT_EYE_OUTER, PoseLandmark.LEFT_EAR),
//            Pair(PoseLandmark.LEFT_EAR, PoseLandmark.LEFT_MOUTH),
//            Pair(PoseLandmark.LEFT_MOUTH, PoseLandmark.NOSE),
//            Pair(PoseLandmark.NOSE, PoseLandmark.RIGHT_MOUTH),
//            Pair(PoseLandmark.RIGHT_MOUTH, PoseLandmark.RIGHT_EAR),
//            Pair(PoseLandmark.RIGHT_EAR, PoseLandmark.RIGHT_EYE_OUTER),
//            Pair(PoseLandmark.RIGHT_EYE_OUTER, PoseLandmark.RIGHT_EYE),
//            Pair(PoseLandmark.RIGHT_EYE, PoseLandmark.RIGHT_EYE_INNER),

            /**体幹**/
//            Pair(PoseLandmark.NOSE, PoseLandmark.LEFT_SHOULDER),
//            Pair(PoseLandmark.NOSE, PoseLandmark.RIGHT_SHOULDER),
            Pair(PoseLandmark.LEFT_SHOULDER, PoseLandmark.RIGHT_SHOULDER),
            Pair(PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_HIP),
            Pair(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_HIP),
            Pair(PoseLandmark.LEFT_HIP, PoseLandmark.RIGHT_HIP),

            /**腕（左側）**/
            Pair(PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_ELBOW),
            Pair(PoseLandmark.LEFT_ELBOW, PoseLandmark.LEFT_WRIST),
            /**腕（右側）**/
            Pair(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_ELBOW),
            Pair(PoseLandmark.RIGHT_ELBOW, PoseLandmark.RIGHT_WRIST),

            /**脚（左側）**/
//            Pair(PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_KNEE),
//            Pair(PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_ANKLE),
            /**脚（右側）**/
//            Pair(PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_KNEE),
//            Pair(PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_ANKLE)
        )

        val canvasWidth = size.width
        val canvasHeight = size.height
//        Log.d("--size","canvasWidth：${canvasWidth}\n" +
//                "canvasHeight：${canvasHeight}\n" +
//                "imageWidth：${imageWidth}\n" +
//                "imageHeight：${imageHeight}\n" +
//                "WeightRatio: ${canvasWidth / imageWidth}\n" +
//                "HeightRatio: ${canvasHeight / imageHeight}\n" +
//                "NOSE X: ${pose.getPoseLandmark(PoseLandmark.NOSE)?.position3D?.x}\n" +
//                "NOSE Y: ${pose.getPoseLandmark(PoseLandmark.NOSE)?.position3D?.y}\n" +
//                "NOSE Z: ${pose.getPoseLandmark(PoseLandmark.NOSE)?.position3D?.z}\n")

        /**ランドマークの描画**/
        pose.allPoseLandmarks.forEach { landmark ->
                if (landmark.inFrameLikelihood > 0.5f) { // 信頼度が低い場合は描画しない
                    if(landmark.landmarkType == PoseLandmark.LEFT_SHOULDER ||
                        landmark.landmarkType == PoseLandmark.RIGHT_SHOULDER ||
                        landmark.landmarkType == PoseLandmark.LEFT_ELBOW ||
                        landmark.landmarkType == PoseLandmark.RIGHT_ELBOW
                        ) {
                        val x = landmark.position3D.x * canvasWidth / imageWidth
                        val y = landmark.position3D.y * canvasHeight / imageHeight
                        drawCircle(
                            color = Color.Red,
                            radius = 8f,
                            center = Offset(x, y)
                        )
                    }
                }
            Log.d("--animationVisible","${landmark.landmarkType}: ${landmark.position3D.x}, ${landmark.position3D.y}")

            if((landmark.landmarkType == PoseLandmark.LEFT_SHOULDER ||
                        landmark.landmarkType == PoseLandmark.RIGHT_SHOULDER ||
                        landmark.landmarkType == PoseLandmark.LEFT_ELBOW) &&
                landmark.position3D.x > 500
                && landmark.position3D.y > 500 ){
                cameraViewModel.setAnimationVisible(true)
                Log.d("--animationVisible","yes")
                drawImage(image = imageBitmap,
                    dstSize = IntSize(100, 100),
                    dstOffset = IntOffset(x = 250, y = 350)
                )
                cameraViewModel.setAnimationVisible()
            }else{
                cameraViewModel.setAnimationVisible()
            }
        }

        /**線の描画**/
        connections.forEach { connection ->
            val startLandmark = pose.getPoseLandmark(connection.first)
            val endLandmark = pose.getPoseLandmark(connection.second)
            startLandmark?.position?.describeContents()
            if(startLandmark != null && endLandmark != null){
                drawLine(
                    color = Color.Red,
                    start = Offset(startLandmark.position3D.x * (canvasWidth / imageWidth),
                        startLandmark.position3D.y * (canvasHeight / imageHeight)),
                    end = Offset(endLandmark.position3D.x * (canvasWidth / imageWidth),
                        endLandmark.position3D.y * (canvasHeight / imageHeight)),
                    strokeWidth = 4f
                )
            }
        }
    }
}