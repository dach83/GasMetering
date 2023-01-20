package com.github.dach83.gasmetering.features.abonents.presentation

import androidx.annotation.StringRes

data class AbonentsUiState(
    val isLoading: Boolean,
    val loadingProgress: Byte,
    @StringRes val errorMessage: Int?
) {
    fun loading(progress: Byte = 0) = copy(
        isLoading = true,
        loadingProgress = progress
    )

    fun loaded() = copy(
        isLoading = false
    )

    fun error(@StringRes errorMessage: Int) = copy(
        isLoading = false,
        errorMessage = errorMessage
    )

    companion object {
        val INITIAL = AbonentsUiState(
            isLoading = false,
            loadingProgress = 0,
            errorMessage = null
        )
    }
}
