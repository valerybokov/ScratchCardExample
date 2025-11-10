package com.scratchcardexample.feature.activation

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
internal fun MessageDialog(
    @StringRes
    titleId: Int,
    description: String,
    onConfirmation: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val title = stringResource(titleId)
    val proceed = stringResource(R.string.ok)

    AlertDialog(
        title = {
            Text(text = title)
        },
        text = {
            Text(text = description)
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onConfirmation
            ) {
                Text(text = proceed)
            }
        },
    )
}