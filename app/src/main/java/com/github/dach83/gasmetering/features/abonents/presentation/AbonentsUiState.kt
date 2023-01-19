package com.github.dach83.gasmetering.features.abonents.presentation

import androidx.annotation.StringRes
import com.github.dach83.gasmetering.features.abonents.domain.model.Abonent

data class AbonentsUiState(
    val abonents: List<Abonent>,
    val isLoading: Boolean,
    @StringRes val errorMessage: Int?
) {
    fun loading() = copy(
        isLoading = true
    )

    fun loaded(abonents: List<Abonent>) = copy(
        abonents = abonents,
        isLoading = false
    )

    fun error(@StringRes errorMessage: Int) = copy(
        isLoading = false,
        errorMessage = errorMessage
    )

    companion object {
        val INITIAL = AbonentsUiState(
            abonents = emptyList(),
            isLoading = false,
            errorMessage = null
        )
    }
}
