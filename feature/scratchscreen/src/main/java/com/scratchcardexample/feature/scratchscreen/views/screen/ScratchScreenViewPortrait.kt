package com.scratchcardexample.feature.scratchscreen.views.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
internal fun ScratchScreenViewPortrait(
    modifier: Modifier,
    draggedPath: State<DraggedPath>,
    movedOffset: MutableState<Offset>,
    overlayImage: ImageBitmap,
    baseImage: ImageBitmap,
    onScratchClick: () -> Unit,
    isCardScratched: Boolean,
    tracker: ScratchCoverageTracker,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        val borderColor = if (isCardScratched) Color.Green else Color.Gray
        ScratchCard(
            modifier = Modifier
                .border(width = 2.dp, color = borderColor, shape = RoundedCornerShape(8.dp))
                .padding(8.dp),
            widthHeight = 300.dp,
            draggedPath = draggedPath,
            movedOffset = movedOffset,
            overlayImage = overlayImage,
            baseImage = baseImage,
            isCardScratched = isCardScratched,
            tracker = tracker,
        )

        if (!isCardScratched)
            StartScratchButton(onScratchClick)
    }
}