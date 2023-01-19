package com.github.dach83.gasmetering.features.abonents.presentation

data class AbonentsUiState(
    val isLoading: Boolean
) {
    fun loading() = copy(
        isLoading = true
    )

    fun loaded() = copy(
        isLoading = false
    )

    companion object {
        val INITIAL = AbonentsUiState(
            isLoading = false
        )
    }
}
