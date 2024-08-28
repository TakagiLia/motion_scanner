package biz.moapp.motion_scanner.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

@Composable
fun PoseOverlay(pose: Pose, imageWidth: Int, imageHeight: Int,) {

    val context = LocalContext.current
    var state by remember { mutableStateOf(false) }

    if (state) {
        LaunchedEffect(Unit) {
            Log.d("--check Toast","Toast showed")
            Toast.makeText(context, "Touch! ", Toast.LENGTH_SHORT).show()
            state = false
        }
    }

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
                    val x = landmark.position.x * canvasWidth / imageWidth
                    val y = landmark.position.y * canvasHeight / imageHeight
                    if(landmark.landmarkType == PoseLandmark.LEFT_WRIST){
                     if(landmark.landmarkType == PoseLandmark.LEFT_WRIST && y >500){
                         state = true
                    }
                    Log.d("--check state"," state:${state}")
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
                    start = Offset(startLandmark.position.x * (canvasWidth / imageWidth),
                        startLandmark.position.y * (canvasHeight / imageHeight)),
                    end = Offset(endLandmark.position.x * (canvasWidth / imageWidth),
                        endLandmark.position.y * (canvasHeight / imageHeight)),
                    strokeWidth = 4f
                )
            }
        }
    }
}