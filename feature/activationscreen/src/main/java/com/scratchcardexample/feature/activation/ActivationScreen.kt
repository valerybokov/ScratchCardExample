package com.scratchcardexample.feature.activation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

@Composable
fun ActivationScreen(
    onStart: () -> Unit,
) {
    val viewModel: ActivationScreenViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        onStart()
    }
    ActivationScreenView(
        Modifier.fillMaxSize(),
        viewModel::activate
    )
}

@Composable
private fun ActivationScreenView(
    modifier: Modifier,
    onActivateClick: () -> Unit) {
    Box(modifier = modifier,
        contentAlignment = Alignment.Center) {
        Button(onClick = onActivateClick,
            modifier = Modifier.widthIn(160.dp)) {
            Text(text = stringResource(R.string.activate))
        }
    }
}

@Preview
@Composable
private fun ActivationScreenPreview() {
    ActivationScreenView(
        modifier = Modifier.fillMaxSize(),
        onActivateClick = { },
    )
}