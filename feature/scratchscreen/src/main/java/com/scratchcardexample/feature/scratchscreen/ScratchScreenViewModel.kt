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
import androidx.lifecycle.viewModelScope
import com.scratchcardexample.feature.scratchscreen.views.scratchcard.model.DraggedPath
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.scratchcardexample.core.domain.repository.ScratchCodeRepository
import com.scratchcardexample.core.domain.usecases.GenerateCodeUseCase
import com.scratchcardexample.feature.scratchscreen.views.scratchcard.BRUSH_RADIUS
import com.scratchcardexample.feature.scratchscreen.views.scratchcard.model.ScratchCoverageTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class ScratchScreenViewModel @Inject constructor(
    private val repo: ScratchCodeRepository,
    private val generateCode: GenerateCodeUseCase,
): ViewModel() {
    private val _scratchOverlayImage = mutableStateOf<ImageBitmap?>(null)
    private val _scratchBaseImage = mutableStateOf<ImageBitmap?>(null)

    val scratchOverlayImage: State<ImageBitmap?> = _scratchOverlayImage
    val scratchBaseImage: State<ImageBitmap?> = _scratchBaseImage

    private val _draggedPath: MutableState<DraggedPath> =
        mutableStateOf(DraggedPath(path = Path()))

    val draggedPath: State<DraggedPath> = _draggedPath

    val movedOffset: MutableState<Offset> =
        mutableStateOf(Offset.Unspecified)

    private val listener = ScratchCoverageTracker.OnScratchedListener { percentage ->
        if (percentage > 75f) {
            tryScratchTheCard()
        }
    }
    internal val tracker = ScratchCoverageTracker(brushRadius = BRUSH_RADIUS, onScratchedListener = listener)

    private val _scratchCardState = MutableStateFlow(ScratchCardState.Initialising)

    val scratchCardState: StateFlow<ScratchCardState> = combine(repo.isCardActivated(), _scratchCardState) { isCardActivated, scratchCardState ->
        val state: ScratchCardState
        if (scratchCardState == ScratchCardState.Initialising) {
            state = if (isCardActivated) ScratchCardState.Scratched else ScratchCardState.NotScratched
            _scratchCardState.value = state
        }
        else
            state = scratchCardState

        state
    }
    .stateIn(viewModelScope, SharingStarted.Lazily, ScratchCardState.Initialising)

    private var _loadImagesJob: Job? = null

    fun initialize(res: Resources) {
        if (_scratchOverlayImage.value != null || _loadImagesJob != null)
            return

        _loadImagesJob = viewModelScope.launch {
            // Decoding bitmaps is CPU heavy, keep it off the main thread.
            val (overlay, base) = withContext(Dispatchers.Default) {
                ImageBitmap.imageResource(res, R.drawable.overlay) to
                    ImageBitmap.imageResource(res, R.drawable.base)
            }
            _scratchOverlayImage.value = overlay
            _scratchBaseImage.value = base
        }
    }

    private var _scratchingJob : Job? = null

    fun tryScratchTheCard() {
        if (_scratchCardState.value == ScratchCardState.NotScratched) {
            _scratchCardState.value = ScratchCardState.Scratching

            _scratchingJob = viewModelScope.launch {
                val code = generateCode()
                draggedPath.value.path.reset()
                movedOffset.value = Offset.Unspecified
                tracker.reset()
                _scratchCardState.value = ScratchCardState.Scratched
                ensureActive()
                repo.saveCode(code)
            }
        }
    }

    /** Cancels an unfinished scratch, e.g. when the user closes the screen. */
    private fun cancelScratching() {
        _scratchingJob?.cancel()
        _scratchingJob = null

        if (_scratchCardState.value == ScratchCardState.Scratching)
            _scratchCardState.value = ScratchCardState.NotScratched
    }

    override fun onCleared() {
        super.onCleared()
        cancelScratching()

        _scratchOverlayImage.value = null
        _scratchBaseImage.value = null
    }
}

enum class ScratchCardState {
    Initialising,
    NotScratched,
    Scratching,
    Scratched,
}