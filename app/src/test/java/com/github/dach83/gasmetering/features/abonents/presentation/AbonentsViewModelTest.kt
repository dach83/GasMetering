package com.github.dach83.gasmetering.features.abonents.presentation

import android.net.Uri
import com.github.dach83.gasmetering.rule.CoroutineRule
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AbonentsViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

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

    @Test
    fun `successful load excel file updates ui state to loaded`() = runTest {
        val sut = AbonentsViewModel()
        val expected = AbonentsUiState(
            isLoading = false
        )

        sut.loadExcelFile(excelUri)
        advanceUntilIdle()

        assertThat(sut.uiState).isEqualTo(expected)
    }
}
