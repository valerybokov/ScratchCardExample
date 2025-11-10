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
import com.scratchcardexample.feature.scratchscreen.views.scratchcard.model.ScratchCoverageTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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
    internal val tracker = ScratchCoverageTracker(brushRadius = 50f, onScratchedListener = listener)

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
    .stateIn(viewModelScope, SharingStarted.Lazily, ScratchCardState.Scratching)

    fun initialize(res: Resources) {
        if (_scratchOverlayImage.value == null) {
            _scratchOverlayImage.value = ImageBitmap.imageResource(res, R.drawable.overlay)
            _scratchBaseImage.value = ImageBitmap.imageResource(res, R.drawable.base)
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
                repo.saveCode(code)//todo handle exceptions: cannot save the code
            }
        }
    }

    fun cancelScratching() {
        _scratchingJob?.cancel()
        _scratchingJob = null
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