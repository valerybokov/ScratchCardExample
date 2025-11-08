package com.example.scratchcardexample.feature.scratchscreen.scratchcard.model

import androidx.compose.ui.graphics.Path

data class DraggedPath(
    val path: Path,
    val width: Float = 18f
)