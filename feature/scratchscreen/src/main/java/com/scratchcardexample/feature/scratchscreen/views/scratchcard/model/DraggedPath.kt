package com.scratchcardexample.feature.scratchscreen.views.scratchcard.model

import androidx.compose.ui.graphics.Path

data class DraggedPath(
    val path: Path,
    val width: Float = 18f
)