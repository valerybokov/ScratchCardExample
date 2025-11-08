package com.scratchcardexample.feature.scratchscreen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@NonRestartableComposable
@Composable
internal fun StartScratchButton(onScratchClick: () -> Unit) {
    Button(
        onClick = onScratchClick,
        modifier = Modifier
            .widthIn(180.dp)
            .padding(top = 32.dp, start = 32.dp, end = 32.dp)
    ) {
        Text(text = stringResource(R.string.scratch))
    }
}