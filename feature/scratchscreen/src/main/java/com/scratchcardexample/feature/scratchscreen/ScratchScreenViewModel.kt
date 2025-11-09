package com.scratchcardexample.feature.scratchscreen

import android.content.res.Resources
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.imageResource
import androidx.lifecycle.ViewModel
import com.scratchcardexample.feature.scratchscreen.scratchcard.model.DraggedPath
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class ScratchScreenViewModel @Inject constructor(): ViewModel() {
    private val _scratchOverlayImage = mutableStateOf<ImageBitmap?>(null)
    private val _scratchBaseImage = mutableStateOf<ImageBitmap?>(null)

    val scratchOverlayImage: State<ImageBitmap?> = _scratchOverlayImage
    val scratchBaseImage: State<ImageBitmap?> = _scratchBaseImage

    private val _draggedPath: MutableState<DraggedPath> =
        mutableStateOf(DraggedPath(path = Path()))

    val draggedPath: State<DraggedPath> = _draggedPath

    val movedOffset: MutableState<Offset> =
        mutableStateOf(Offset.Unspecified)

    private val _isScratched: MutableState<Boolean> =
        mutableStateOf(false)

    val isScratched: State<Boolean> = _isScratched

    fun init(res: Resources) {
        if (_scratchOverlayImage.value == null) {
            _scratchOverlayImage.value = ImageBitmap.imageResource(res, R.drawable.overlay)
            _scratchBaseImage.value = ImageBitmap.imageResource(res, R.drawable.base)
        }
    }

    fun tryScratchTheCard() {
        if (!_isScratched.value) {
            draggedPath.value.path.reset()
            movedOffset.value = Offset.Unspecified
            _isScratched.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()

        _scratchOverlayImage.value = null
        _scratchBaseImage.value = null
    }
}
