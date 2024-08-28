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

        val connections = listOf(
            /**体幹**/
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
        )

        /**必要なランドマークだけ保持**/
        val poseList = listOf(
            pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER),
            pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER),
            pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW),
            pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW),
            pose.getPoseLandmark(PoseLandmark.LEFT_WRIST),
            pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST),
            pose.getPoseLandmark(PoseLandmark.LEFT_HIP),
            pose.getPoseLandmark(PoseLandmark.RIGHT_HIP),
        )

        val canvasWidth = size.width
        val canvasHeight = size.height

        /**ランドマークの描画**/
        poseList.forEach { landmark ->
            landmark?.let {
                if (landmark.inFrameLikelihood > 0.5f) { // 信頼度が低い場合は描画しない
                    val x = landmark.position3D.x * canvasWidth / imageWidth
                    val y = landmark.position3D.y * canvasHeight / imageHeight
                    drawCircle(
                        color = Color.Red,
                        radius = 8f,
                        center = Offset(x, y)
                    )
                }
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