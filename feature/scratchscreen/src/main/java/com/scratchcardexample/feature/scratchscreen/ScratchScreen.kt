package com.scratchcardexample.feature.scratchscreen

import android.annotation.SuppressLint
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun ScratchScreen(
    onStart: () -> Unit,
) {
    val viewModel: ScratchScreenViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        onStart()
    }

    ScratchScreenView(
        modifier = Modifier.fillMaxSize(),
        onScratchClick = viewModel::tryActivateScratchCard,
    )
}

@Composable
private fun ScratchScreenView(
    modifier: Modifier,
    onScratchClick: () -> Unit) {
    val configuration = LocalConfiguration.current
    if (configuration.orientation == ORIENTATION_PORTRAIT)
        ScratchScreenViewPortrait(
            modifier = modifier,
            onScratchClick = onScratchClick
        )
    else
        ScratchScreenViewLandscape(
            modifier = modifier,
            onScratchClick = onScratchClick
        )
}

@Preview
@Composable
private fun ScratchScreenPreview() {
    ScratchScreenViewPortrait(
        modifier = Modifier.fillMaxSize(),
        onScratchClick = { },
    )
}