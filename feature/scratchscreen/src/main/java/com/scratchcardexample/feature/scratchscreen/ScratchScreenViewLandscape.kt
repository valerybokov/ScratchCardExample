package com.scratchcardexample.feature.scratchscreen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.scratchcardexample.feature.scratchscreen.scratchcard.ImageScratch

@Composable
internal fun ScratchScreenViewLandscape(
    modifier: Modifier,
    onScratchClick: () -> Unit) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        ImageScratch(
            widthHeight = 240.dp,
            overlayImage = ImageBitmap.imageResource(R.drawable.overlay),
            baseImage = ImageBitmap.imageResource(R.drawable.base),
            modifier = Modifier
                .border(width = 2.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp))
                .padding(8.dp),
        )

        StartScratchButton(onScratchClick)
    }
}
