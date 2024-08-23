package biz.moapp.motion_scanner.ui

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.pose.Pose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CameraViewModel: ViewModel() {

    private val _pose = MutableStateFlow<Pose?>(null)
    val pose: StateFlow<Pose?> = _pose.asStateFlow()
    fun setPose(pose :Pose?){
        _pose.value = pose
    }

    private val _animationVisible = MutableStateFlow(false) // 初期値は true
    val animationVisible: StateFlow<Boolean> = _animationVisible.asStateFlow()
    fun setAnimationVisible(flag: Boolean = false){
        _animationVisible.value = flag
    }

}