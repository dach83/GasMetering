package com.github.dach83.gasmetering.features.abonents.presentation

import android.net.Uri
import com.github.dach83.gasmetering.R
import com.github.dach83.gasmetering.fake.FakeLoadAbonents
import com.github.dach83.gasmetering.features.abonents.presentation.state.AbonentsUiState
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
        assertThat(uiState).isEqualTo(AbonentsUiState.Loading(progress = 0))
    }

    @Test
    fun `successful load excel file updates state to loaded`() = runTest {
        val sut = createAbonentsViewModel()

        sut.loadExcelFile(fakeExcelUri)

        advanceUntilIdle()
        val uiState = sut.uiState.first()
        assertThat(uiState).isEqualTo(AbonentsUiState.Loaded)
    }

    @Test
    fun `unsuccessful load excel file updates state to error`() = runTest {
        val sut = createAbonentsViewModel()
        fakeLoadAbonents.errorMode()

        sut.loadExcelFile(fakeExcelUri)

        advanceUntilIdle()
        val uiState = sut.uiState.first()
        assertThat(uiState).isEqualTo(AbonentsUiState.Error(R.string.error_message))
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
    fun `start empty search displays no abonents`() = runTest {
        val sut = createAbonentsViewModel()
        sut.loadExcelFile(fakeExcelUri)

        sut.startSearch(searchQuery = "")

        advanceUntilIdle()
        val abonents = sut.filteredAbonents.first()
        assertThat(abonents).isEmpty()
    }

    @Test
    fun `cancel search displays all abonents`() = runTest {
        val sut = createAbonentsViewModel()
        sut.loadExcelFile(fakeExcelUri)
        sut.startSearch()

        sut.cancelSearch()

        advanceUntilIdle()
        val abonents = sut.filteredAbonents.first()
        assertThat(abonents).containsExactlyElementsIn(fakeAbonents)
    }

    private fun createAbonentsViewModel() = AbonentsViewModel(
        loadAbonents = fakeLoadAbonents
    )
}
