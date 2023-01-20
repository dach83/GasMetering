package com.github.dach83.gasmetering.features.abonents.presentation

import android.net.Uri
import com.github.dach83.gasmetering.fake.FakeLoadAbonents
import com.github.dach83.gasmetering.models.fakeAbonents
import com.github.dach83.gasmetering.rule.CoroutineRule
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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
    fun `check initial state`() = runTest {
        val sut = createAbonentsViewModel()
        val uiState = sut.uiState.first()
        assertThat(uiState).isEqualTo(AbonentsUiState.INITIAL)
    }

    @Test
    fun `start loading excel file updates state to loading`() = runTest {
        val sut = createAbonentsViewModel()

        sut.loadExcelFile(fakeExcelUri)

        val uiState = sut.uiState.first()
        assertThat(uiState.isLoading).isTrue()
    }

    @Test
    fun `successful load excel file updates state to loaded`() = runTest {
        val sut = createAbonentsViewModel()

        sut.loadExcelFile(fakeExcelUri)

        advanceUntilIdle()
        val uiState = sut.uiState.first()
        assertThat(uiState.isLoading).isFalse()
        assertThat(uiState.errorMessage).isNull()
    }

    @Test
    fun `successful load excel file displays all abonents`() = runTest {
        val sut = createAbonentsViewModel()

        sut.loadExcelFile(fakeExcelUri)

        advanceUntilIdle()
        val abonents = sut.filteredAbonents.first()
        assertThat(abonents).containsExactlyElementsIn(fakeAbonents)
    }

    @Test
    fun `unsuccessful load excel file updates state to error`() = runTest {
        val sut = createAbonentsViewModel()
        fakeLoadAbonents.errorMode()

        sut.loadExcelFile(fakeExcelUri)

        advanceUntilIdle()
        val uiState = sut.uiState.first()
        assertThat(uiState.isLoading).isFalse()
        assertThat(uiState.errorMessage).isNotNull()
    }

    @Test
    fun `start search updates state to searching`() = runTest {
        val sut = createAbonentsViewModel()
        sut.loadExcelFile(fakeExcelUri)

        sut.startSearch()

        advanceUntilIdle()
        val uiState = sut.uiState.first()
        assertThat(uiState.isSearching).isTrue()
    }

    @Test
    fun `start search displays empty abonents list`() = runTest {
        val sut = createAbonentsViewModel()
        sut.loadExcelFile(fakeExcelUri)

        sut.startSearch()

        advanceUntilIdle()
        val abonents = sut.filteredAbonents.first()
        assertThat(abonents).isEmpty()
    }

    private fun createAbonentsViewModel() = AbonentsViewModel(
        loadAbonents = fakeLoadAbonents
    )
}
