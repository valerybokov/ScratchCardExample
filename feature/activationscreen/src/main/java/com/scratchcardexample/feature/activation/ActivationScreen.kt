package com.scratchcardexample.feature.activation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
        modifier = Modifier.fillMaxSize(),
        activationState = viewModel.state,
        onActivateClick = viewModel::activate,
        onMessageDialogDismiss = viewModel::reset,
        onMessageDialogConfirmation = viewModel::reset
    )
}

@Composable
private fun ActivationScreenView(
    modifier: Modifier,
    activationState: State<ActivationState>,
    onActivateClick: () -> Unit,
    onMessageDialogDismiss: () -> Unit,
    onMessageDialogConfirmation: () -> Unit,
) {
    Box(modifier = modifier,
        contentAlignment = Alignment.Center) {
        when (activationState.value) {
            ActivationState.Default ->
                Button(
                    onClick = onActivateClick,
                    modifier = Modifier.widthIn(160.dp)
                ) {
                    Text(text = stringResource(R.string.activate))
                }
            ActivationState.ReadingTheCode -> {

            }
            is ActivationState.Success -> {
                MessageDialog(
                    titleId = R.string.success,
                    description = stringResource(R.string.success_args, (activationState.value as ActivationState.Success).code),
                    onConfirmation = onMessageDialogConfirmation,
                    onDismissRequest = onMessageDialogDismiss,
                )
            }
            else -> {
                val tmp = activationState.value as ActivationState.Error
                MessageDialog(
                    titleId = R.string.error,
                    description = stringResource(tmp.value),
                    onConfirmation = onMessageDialogConfirmation,
                    onDismissRequest = onMessageDialogDismiss,
                )
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun ActivationScreenPreview() {
    ActivationScreenView(
        modifier = Modifier.fillMaxSize(),
        activationState = mutableStateOf<ActivationState>(ActivationState.Default),
        onActivateClick = { },
        onMessageDialogDismiss = { },
        onMessageDialogConfirmation = { },
    )
}