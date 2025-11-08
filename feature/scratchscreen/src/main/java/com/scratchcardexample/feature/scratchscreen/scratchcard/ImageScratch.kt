package com.scratchcardexample.feature.scratchscreen.scratchcard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ClipOp

import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.scratchcardexample.feature.scratchscreen.scratchcard.model.DraggedPath
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.Dp
import com.scratchcardexample.feature.scratchscreen.R

@Composable
internal fun ImageScratch(
    widthHeight: Dp,
    overlayImage: ImageBitmap,
    baseImage: ImageBitmap,
    modifier: Modifier = Modifier,
    isIntersect: Boolean = true
) {
    val currentPathState = remember { mutableStateOf(DraggedPath(path = Path())) }
    val movedOffsetState = remember { mutableStateOf<Offset?>(null) }

     Canvas(
         modifier = modifier
             .size(widthHeight)
             .pointerInput(true) {
                 detectDragGestures { change, _ ->
                     movedOffsetState.value = change.position
                 }
             },
     ) {
         val imageWidth = size.width.coerceAtMost(size.height).toInt()
         val imageSize = IntSize(
             width = imageWidth,
             height = imageWidth
         )

         drawImage(overlayImage, dstSize = imageSize)

         movedOffsetState.value?.let {
             currentPathState.value.path.addOval(oval = Rect(it, 50f))
         }

         val clipOp = if (isIntersect) ClipOp.Intersect else ClipOp.Difference

         clipPath(path = currentPathState.value.path, clipOp = clipOp) {
             drawImage(baseImage, dstSize = imageSize)
         }
    }
}

@Composable
@Preview(device = Devices.PIXEL_4)
private fun ImageScratchPreview() {
    val overlay = ImageBitmap.imageResource(R.drawable.overlay)
    val base = ImageBitmap.imageResource(R.drawable.base)

    ImageScratch(
        widthHeight = 300.dp,
        overlayImage = overlay,
        baseImage = base,
        isIntersect = true,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 16.dp)
    )
}
