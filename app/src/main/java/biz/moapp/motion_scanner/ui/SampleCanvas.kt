package biz.moapp.motion_scanner.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Composable
fun SampleCanvas(modifier :Modifier){
    BoxWithConstraints {
        val width = maxWidth
        val height = maxHeight

        Canvas(modifier = modifier.fillMaxSize()) {

            val list = listOf(
                Pair(Offset(734.9671F, 550.7924F), Offset(391.27032F, 583.2485F),),
                Pair(Offset(734.9671F, 550.7924F), Offset(1073.8956F, 654.9725F),),
                Pair(Offset(391.27032F, 583.2485F), Offset(218.27956F, 1015.70435F),),


                )
            val pose = listOf(
                Offset(0F, 0F),
                Offset(734.9671F, 550.7924F),
                Offset(391.27032F, 583.2485F),
                Offset(1073.8956F, 654.9725F),
                Offset(218.27956F, 1015.70435F),
                Offset(903.83704F, 754.676F),
                Offset(322.18152F, 842.5973F),
                Offset(1146.1635F, 609.6432F),
                Offset(176.17755F, 1065.838F),
                Offset((width/2).toPx(), (height/2).toPx())
            )

            pose.forEach { value ->
                drawCircle(
                    color = Color.Red,
                    radius = 8f,
                    center = Offset(value.x, value.y)
                )
            }
        }
    }
}