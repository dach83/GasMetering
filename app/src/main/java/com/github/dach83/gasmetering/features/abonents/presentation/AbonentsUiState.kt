package com.github.dach83.gasmetering.features.abonents.presentation

import androidx.annotation.StringRes

data class AbonentsUiState(
    val loadingProgress: Byte?,
    val searchQuery: String?,
    @StringRes val errorMessage: Int?
) {

    fun loading(progress: Byte = 0) = copy(
        loadingProgress = progress
    )

    fun loaded() = copy(
        loadingProgress = null
    )

    fun error(@StringRes errorMessage: Int) = copy(
        loadingProgress = null,
        errorMessage = errorMessage
    )

    fun search(searchQuery: String?) = copy(
        searchQuery = searchQuery
    )

    companion object {
        val INITIAL = AbonentsUiState(
            loadingProgress = null,
            searchQuery = null,
            errorMessage = null
        )
    }
}
