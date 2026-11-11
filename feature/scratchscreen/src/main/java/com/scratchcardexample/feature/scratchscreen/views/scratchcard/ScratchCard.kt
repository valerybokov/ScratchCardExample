package com.scratchcardexample.feature.scratchscreen.views.scratchcard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ClipOp

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.scratchcardexample.feature.scratchscreen.views.scratchcard.model.DraggedPath
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.Dp
import com.scratchcardexample.feature.scratchscreen.R
import androidx.compose.runtime.State
import com.scratchcardexample.feature.scratchscreen.views.scratchcard.model.ScratchCoverageTracker

const val BRUSH_RADIUS = 50f

@Composable
internal fun ScratchCard(
    modifier: Modifier = Modifier,
    widthHeight: Dp,
    overlayImage: ImageBitmap,
    baseImage: ImageBitmap,
    draggedPath: State<DraggedPath>,
    movedOffset: MutableState<Offset>,
    tracker: ScratchCoverageTracker,
    isCardScratched: Boolean = false,
) {
    // The pointerInput block is started once, so read the latest value through State.
    val isScratched = rememberUpdatedState(isCardScratched)

     Canvas(
         modifier = modifier
             .size(widthHeight)
             .pointerInput(true) {
                 detectDragGestures { change, _ ->
                     val position = change.position
                     // Grow the path once per new touch position, not on every redraw.
                     if (!isScratched.value && position != movedOffset.value) {
                         draggedPath.value.path.addOval(oval = Rect(position, BRUSH_RADIUS))
                     }
                     // Updating the offset also invalidates the Canvas so the new path is drawn.
                     movedOffset.value = position
                     tracker.registerTouch(position, size)
                 }
             },
     ) {
         // Read the offset so the Canvas redraws whenever the path grows.
         movedOffset.value

         val imageWidth = size.width.coerceAtMost(size.height).toInt()
         val imageSize = IntSize(
             width = imageWidth,
             height = imageWidth
         )

         drawImage(image = overlayImage, dstSize = imageSize)

         val clipOp = if (isCardScratched) ClipOp.Difference else ClipOp.Intersect

         clipPath(path = draggedPath.value.path, clipOp = clipOp) {
             drawImage(baseImage, dstSize = imageSize)
         }
    }
}

@Composable
@Preview(device = Devices.PIXEL_4)
private fun ScratchCardPreview() {
    val overlay = ImageBitmap.imageResource(R.drawable.overlay)
    val base = ImageBitmap.imageResource(R.drawable.base)
    val currentPathState = remember { mutableStateOf(DraggedPath(path = Path())) }
    val movedOffsetState = remember { mutableStateOf(Offset.Unspecified) }
    val listener = ScratchCoverageTracker.OnScratchedListener { /* no-op */ }

    ScratchCard(
        widthHeight = 300.dp,
        overlayImage = overlay,
        draggedPath = currentPathState,
        movedOffset = movedOffsetState,
        baseImage = base,
        tracker = ScratchCoverageTracker(
            brushRadius = BRUSH_RADIUS, onScratchedListener = listener),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 16.dp)
    )
}
