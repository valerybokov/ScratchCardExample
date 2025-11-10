package com.scratchcardexample.feature.startscreen

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun StartScreen(
    onStart: () -> Unit,
    onNavigateToScratchScreen: () -> Unit,
    onNavigateToActivationScreen: () -> Unit,
) {
    val viewModel: StartScreenViewModel = hiltViewModel()
    val scratchcardStateId by viewModel.scratchcardStateId.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        onStart()
    }

    StartScreenView(
        Modifier.fillMaxSize(),
        onNavigateToScratchScreen,
        onNavigateToActivationScreen,
        scratchcardStateId
    )
}

@Composable
private fun StartScreenView(
    modifier: Modifier,
    onNavigateToScratchScreen: () -> Unit,
    onNavigateToActivationScreen: () -> Unit,
    @StringRes
    scratchcardStateId: Int) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        Text(text = stringResource(R.string.scratchcard_state_title))

        Text(
            text = stringResource(scratchcardStateId),
            modifier = Modifier.padding(top = 16.dp, bottom = 32.dp),
            style = MaterialTheme.typography.titleMedium,
        )

        Button(onClick = onNavigateToScratchScreen,
            modifier = Modifier.fillMaxWidth(0.5f)) {
            Text(text = stringResource(R.string.go_to_scratchscreen))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onNavigateToActivationScreen,
            modifier = Modifier.fillMaxWidth(0.5f)) {
            Text(text = stringResource(R.string.go_to_activationscreen))
        }
    }
}

@Preview
@Composable
private fun StartScreenPreview() {
    StartScreenView(
        modifier = Modifier.fillMaxSize(),
        onNavigateToScratchScreen = {},
        onNavigateToActivationScreen = {},
        scratchcardStateId = R.string.card_scratched
    )
}