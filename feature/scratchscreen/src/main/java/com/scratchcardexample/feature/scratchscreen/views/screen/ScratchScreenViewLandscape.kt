package com.scratchcardexample.feature.scratchscreen.views.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.scratchcardexample.feature.scratchscreen.views.scratchcard.ScratchCard
import com.scratchcardexample.feature.scratchscreen.views.scratchcard.model.DraggedPath
import androidx.compose.runtime.State
import com.scratchcardexample.feature.scratchscreen.views.scratchcard.model.ScratchCoverageTracker

@Composable
internal fun ScratchScreenViewLandscape(
    modifier: Modifier,
    draggedPath: State<DraggedPath>,
    movedOffset: MutableState<Offset>,
    tracker: ScratchCoverageTracker,
    overlayImage: ImageBitmap,
    baseImage: ImageBitmap,
    onScratchClick: () -> Unit,
    isCardScratched: Boolean,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        ScratchCard(
            modifier = Modifier
                .border(width = 2.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp))
                .padding(8.dp),
            widthHeight = 240.dp,
            overlayImage = overlayImage,
            baseImage = baseImage,
            draggedPath = draggedPath,
            movedOffset = movedOffset,
            tracker = tracker,
            isCardScratched = isCardScratched,
        )

        StartScratchButton(onScratchClick)
    }
}
