package com.github.dach83.gasmetering.features.abonents.presentation.components

import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import com.github.dach83.gasmetering.features.abonents.presentation.state.AbonentsUiState

@Composable
fun LoadingProgressIndicator(uiState: AbonentsUiState) {
    if (uiState is AbonentsUiState.Loading) {
        LinearProgressIndicator(progress = uiState.progress)
    }
}
