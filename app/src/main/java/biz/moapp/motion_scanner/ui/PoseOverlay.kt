package biz.moapp.motion_scanner.ui

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

@Composable
fun PoseOverlay(pose: Pose, imageWidth: Int, imageHeight: Int,) {
    Canvas(modifier = Modifier.fillMaxSize()) {

        /**ランドマークの反転**/
        val leftShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
        val rightShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        val rightElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
        val leftElbow =  pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)
        val leftHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)
        val rightHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val leftWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)
        val rightWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)

        val poseMap = mapOf(
            PoseLandmark.LEFT_SHOULDER to leftShoulder,
            PoseLandmark.RIGHT_SHOULDER to rightShoulder,
            PoseLandmark.LEFT_ELBOW to leftElbow,
            PoseLandmark.RIGHT_ELBOW to rightElbow,
            PoseLandmark.LEFT_HIP to leftHip,
            PoseLandmark.RIGHT_HIP to rightHip,
            PoseLandmark.LEFT_WRIST to leftWrist,
            PoseLandmark.RIGHT_WRIST to rightWrist,
        )

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

            /**体幹 反転Ver**/
            Pair(leftShoulder, rightShoulder),
            Pair(leftShoulder, leftHip),
            Pair(rightShoulder, rightHip),
            Pair(leftHip, rightHip),

            /**腕（左側）反転Ver**/
            Pair(leftShoulder, leftElbow),
            Pair(leftElbow, leftWrist),

            /**腕（右側））反転Ver**/
            Pair(rightShoulder, rightElbow),
            Pair(rightElbow, rightWrist),

            /**脚（左側）**/
//            Pair(PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_KNEE),
//            Pair(PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_ANKLE),
            /**脚（右側）**/
//            Pair(PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_KNEE),
//            Pair(PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_ANKLE)
        )

        val canvasWidth = size.width
        val canvasHeight = size.height
        Log.d("--size","canvasWidth：${canvasWidth}\n" +
                "canvasHeight：${canvasHeight}\n" +
                "imageWidth：${imageWidth}\n" +
                "imageHeight：${imageHeight}\n" +
                "WeightRatio: ${canvasWidth / imageWidth}\n" +
                "HeightRatio: ${canvasHeight / imageHeight}\n" +
                "NOSE X: ${pose.getPoseLandmark(PoseLandmark.NOSE)?.position3D?.x}\n" +
                "NOSE Y: ${pose.getPoseLandmark(PoseLandmark.NOSE)?.position3D?.y}\n" +
                "NOSE Z: ${pose.getPoseLandmark(PoseLandmark.NOSE)?.position3D?.z}\n")

        /**ランドマークの描画**/
        poseMap.values.forEach {  landmark ->
            val x = landmark?.position3D?.x ?: 0f
            val y = landmark?.position3D?.y ?: 0f
            drawCircle(
                color = Color.Red,
                radius = 8f,
                center = Offset(x, y)
            )
        }

        /**線の描画**/
        connections.forEach{ connection ->
            val start = connection.first
            val end = connection.second

            val startLandmark = poseMap.get(start?.landmarkType)
            val endLandmark = poseMap.get(end?.landmarkType)
            if(startLandmark != null && endLandmark != null){
                drawLine(
                    color = Color.Red,
                    start = Offset(
                        startLandmark.position3D.x * (canvasWidth / imageWidth),
                        startLandmark.position3D.y * (canvasHeight / imageHeight)),
                    end = Offset(
                        endLandmark.position3D.x * (canvasWidth / imageWidth),
                        endLandmark.position3D.y * (canvasHeight / imageHeight)),
                    strokeWidth = 4f
                )
            }
        }
    }
}