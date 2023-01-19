package com.github.dach83.gasmetering.features.abonents.presentation

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AbonentsViewModelTest {

    @Test
    fun `check initial ui state`() {
        val sut = AbonentsViewModel()
        val expected = AbonentsUiState.INITIAL

        val actual = sut.uiState

        assertThat(actual).isEqualTo(expected)
    }
}
