package com.github.dach83.gasmetering.features.abonents.presentation.state

import androidx.annotation.StringRes

sealed class AbonentsUiState {

    data class Loading(val progress: Byte) : AbonentsUiState()

    object Loaded : AbonentsUiState()

    data class Error(@StringRes val message: Int) : AbonentsUiState()

    companion object {
        val INITIAL = Loaded
    }
}
