package com.github.dach83.gasmetering.features.abonents.presentation

import android.net.Uri
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import org.junit.Test

class AbonentsViewModelTest {

    private val excelUri: Uri = mockk()

    @Test
    fun `check initial state`() {
        val sut = AbonentsViewModel()
        val expected = AbonentsUiState.INITIAL

        assertThat(sut.uiState).isEqualTo(expected)
    }

    @Test
    fun `start loading excel file updates state to loading`() {
        val sut = AbonentsViewModel()
        val expected = AbonentsUiState(
            isLoading = true
        )

        sut.loadExcelFile(excelUri)

        assertThat(sut.uiState).isEqualTo(expected)
    }
}
