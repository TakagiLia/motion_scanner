package biz.moapp.motion_scanner.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import biz.moapp.motion_scanner.navigation.Nav

@Composable
fun BaseScreen(){
    val navController = rememberNavController()
    
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            NavHost(
                navController = navController, startDestination = Nav.MainScreen.name,
                enterTransition = {
                    EnterTransition.None
                },
                popEnterTransition = {
                    EnterTransition.None
                },
                exitTransition = {
                    ExitTransition.None
                },
                popExitTransition = {
                    ExitTransition.None
                },
            ) {

                    /**MainScreen**/
                    composable(route = Nav.MainScreen.name,) { backStackEntry ->
                            MainScreen(
                                Modifier.padding(innerPadding),
                                navController
                            )
                    }

                    /**MainScreen**/
                    composable(route = Nav.CameraScreen.name,) { backStackEntry ->
                            val parentEntry = remember(backStackEntry) {
                                navController.getBackStackEntry(Nav.CameraScreen.name)
                            }
                            val cameraViewModel: CameraViewModel = viewModel(viewModelStoreOwner = parentEntry)
                            CameraScreen(
                                Modifier.padding(innerPadding),
                                navController,
                                cameraViewModel
                            )
                    }
            }
    }
}