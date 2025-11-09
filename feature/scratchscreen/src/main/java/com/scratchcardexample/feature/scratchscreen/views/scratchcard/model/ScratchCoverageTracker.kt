package com.scratchcardexample.feature.scratchscreen.views.scratchcard.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import kotlin.math.roundToInt
import kotlin.math.sqrt

internal class ScratchCoverageTracker(
    /** From 50 to 200. The bigger the accurate the result */
    private val gridResolution: Int = 100,
    private val brushRadius: Float,
    private val onScratchedListener: OnScratchedListener
) {
    private val gridResolutionF: Float = gridResolution.toFloat()
    private val scratchedCells = mutableSetOf<Pair<Int, Int>>()

    fun registerTouch(offset: Offset, canvasSize: IntSize) {
        val cellWidth = canvasSize.width / gridResolutionF
        val cellHeight = canvasSize.height / gridResolutionF

        // calculate the brush radius in cells
        val radiusInCellsX = (brushRadius / cellWidth).roundToInt()
        val radiusInCellsY = (brushRadius / cellHeight).roundToInt()

        val cellX = (offset.x / cellWidth).toInt()
        val cellY = (offset.y / cellHeight).toInt()

        // mark the cells that fall under the circle
        for (x in (cellX - radiusInCellsX)..(cellX + radiusInCellsX)) {
            for (y in (cellY - radiusInCellsY)..(cellY + radiusInCellsY)) {
                if (x in 0 until gridResolution && y in 0 until gridResolution) {
                    val dx = x - cellX
                    val dy = y - cellY
                    val dist = sqrt((dx * dx + dy * dy).toFloat())
                    if (dist <= radiusInCellsX) {
                        scratchedCells.add(Pair(x, y))
                    }
                }
            }
        }

        notifyScratched()
    }

    fun getScratchedPercentage(): Float {
        val totalCells = gridResolution * gridResolutionF
        return (scratchedCells.size / totalCells) * 100f
    }

    private fun notifyScratched() {
        val percentage = getScratchedPercentage()

        onScratchedListener.onScratched(percentage)
    }

    fun reset() {
        scratchedCells.clear()
    }

    fun interface OnScratchedListener {
        fun onScratched(percentage: Float)
    }
}