package com.scratchcardexample.feature.scratchscreen

import android.annotation.SuppressLint
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.scratchcardexample.feature.scratchscreen.views.scratchcard.model.DraggedPath
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import com.scratchcardexample.feature.scratchscreen.views.screen.LoadingAnimation
import com.scratchcardexample.feature.scratchscreen.views.screen.ScratchScreenViewLandscape
import com.scratchcardexample.feature.scratchscreen.views.screen.ScratchScreenViewPortrait
import com.scratchcardexample.feature.scratchscreen.views.scratchcard.model.ScratchCoverageTracker

@SuppressLint("UnrememberedMutableState")
@Composable
fun ScratchScreen(
    onStart: () -> Unit,
) {
    val viewModel: ScratchScreenViewModel = hiltViewModel()
    val res = LocalResources.current

    LaunchedEffect(Unit) {
        onStart()
        viewModel.initialize(res)
    }

    BackHandler(onBack = viewModel::cancelScratching)

    val baseImage = viewModel.scratchBaseImage.value
    val overlayImage = viewModel.scratchOverlayImage.value

    if (baseImage != null && overlayImage != null)
        ScratchScreenView(
            modifier = Modifier.fillMaxSize(),
            baseImage = baseImage,
            overlayImage = overlayImage,
            draggedPath = viewModel.draggedPath,
            movedOffset = viewModel.movedOffset,
            tracker = viewModel.tracker,
            scratchCardState = viewModel.scratchCardState.value,
            onScratchClick = viewModel::tryScratchTheCard,
        )
}

@Composable
private fun ScratchScreenView(
    modifier: Modifier,
    overlayImage: ImageBitmap,
    baseImage: ImageBitmap,
    draggedPath: State<DraggedPath>,
    movedOffset: MutableState<Offset>,
    tracker: ScratchCoverageTracker,
    scratchCardState: ScratchCardState,
    onScratchClick: () -> Unit,
) {
    if (scratchCardState != ScratchCardState.Scratching) {
        val configuration = LocalConfiguration.current
        if (configuration.orientation == ORIENTATION_PORTRAIT)
            ScratchScreenViewPortrait(
                modifier = modifier,
                draggedPath = draggedPath,
                movedOffset = movedOffset,
                overlayImage = overlayImage,
                baseImage = baseImage,
                isCardScratched = scratchCardState == ScratchCardState.Scratched,
                onScratchClick = onScratchClick,
                tracker = tracker,
            )
        else
            ScratchScreenViewLandscape(
                modifier = modifier,
                draggedPath = draggedPath,
                movedOffset = movedOffset,
                overlayImage = overlayImage,
                baseImage = baseImage,
                isCardScratched = scratchCardState == ScratchCardState.Scratched,
                tracker = tracker,
                onScratchClick = onScratchClick
            )
    }
    else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            LoadingAnimation()
        }
    }
}

@Preview
@Composable
private fun ScratchScreenScratchedPreview() {
    ScratchScreenPreview(ScratchCardState.Scratched)
}

@Preview
@Composable
private fun ScratchScreenNotScratchedPreview() {
    ScratchScreenPreview(ScratchCardState.NotScratched)
}

@Composable
private fun ScratchScreenPreview(
    scratchCardState: ScratchCardState) {
    val overlay = ImageBitmap.imageResource(R.drawable.overlay)
    val base = ImageBitmap.imageResource(R.drawable.base)
    val draggedPath = remember { mutableStateOf(DraggedPath(path = Path())) }
    val movedOffset = remember { mutableStateOf(Offset.Unspecified) }
    val listener = ScratchCoverageTracker.OnScratchedListener {/* no-op */}

    ScratchScreenViewPortrait(
        modifier = Modifier.fillMaxSize(),
        draggedPath = draggedPath,
        movedOffset = movedOffset,
        overlayImage = overlay,
        baseImage = base,
        onScratchClick = { },
        tracker = ScratchCoverageTracker(
            brushRadius = 50f, onScratchedListener = listener
        ),
        isCardScratched = scratchCardState == ScratchCardState.Scratched,
    )
}