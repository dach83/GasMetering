package com.github.dach83.gasmetering.features.abonents.presentation

import android.net.Uri
import com.github.dach83.gasmetering.fake.FakeLoadAbonents
import com.github.dach83.gasmetering.models.fakeAbonents
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

    private val fakeExcelUri: Uri = mockk()
    private val fakeLoadAbonents = FakeLoadAbonents()

    @Test
    fun `check initial state`() {
        val sut = createAbonentsViewModel()
        val expected = AbonentsUiState.INITIAL
        assertThat(sut.uiState).isEqualTo(expected)
    }

    @Test
    fun `start loading excel file updates state to loading`() {
        val sut = createAbonentsViewModel()

        sut.loadExcelFile(fakeExcelUri)

        assertThat(sut.uiState.isLoading).isTrue()
    }

    @Test
    fun `successful load excel file updates state to loaded`() = runTest {
        val sut = createAbonentsViewModel()

        sut.loadExcelFile(fakeExcelUri)
        advanceUntilIdle()

        assertThat(sut.uiState.isLoading).isFalse()
        assertThat(sut.uiState.errorMessage).isNull()
        assertThat(sut.uiState.abonents).containsExactlyElementsIn(fakeAbonents)
    }

    @Test
    fun `unsuccessful load excel file updates state to error`() = runTest {
        val sut = createAbonentsViewModel()
        fakeLoadAbonents.errorMode()

        sut.loadExcelFile(fakeExcelUri)
        advanceUntilIdle()

        assertThat(sut.uiState.isLoading).isFalse()
        assertThat(sut.uiState.errorMessage).isNotNull()
        assertThat(sut.uiState.abonents).isEmpty()
    }

    private fun createAbonentsViewModel() = AbonentsViewModel(
        loadAbonents = fakeLoadAbonents
    )
}
