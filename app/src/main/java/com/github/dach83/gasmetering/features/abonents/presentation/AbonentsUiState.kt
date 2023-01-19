package com.github.dach83.gasmetering.features.abonents.presentation

data class AbonentsUiState(
    val isLoading: Boolean
) {
    companion object {
        val INITIAL = AbonentsUiState(
            isLoading = false
        )
    }
}
