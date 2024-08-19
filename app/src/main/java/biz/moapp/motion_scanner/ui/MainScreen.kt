package biz.moapp.motion_scanner.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import biz.moapp.motion_scanner.navigation.Nav

@Composable
fun MainScreen(modifier: Modifier, navController : NavController){

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){

        Text(text = "MainScreen", modifier = modifier)
        Button(onClick = { navController.navigate(Nav.CameraScreen.name) }) {
            Text(text = "カメラ起動")
        }
    }


}