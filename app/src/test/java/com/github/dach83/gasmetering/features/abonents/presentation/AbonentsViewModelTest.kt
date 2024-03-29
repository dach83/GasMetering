package com.github.dach83.gasmetering.features.abonents.presentation

import android.net.Uri
import com.github.dach83.gasmetering.R
import com.github.dach83.gasmetering.fake.FakeAbonentsRepository
import com.github.dach83.gasmetering.fake.FakeExcelUriRepository
import com.github.dach83.gasmetering.features.abonents.presentation.state.AbonentsUiState
import com.github.dach83.gasmetering.rule.CoroutineRule
import com.github.dach83.sharedtest.models.emptySearchQuery
import com.github.dach83.sharedtest.models.fakeAbonents
import com.github.dach83.sharedtest.models.fakeSearchQuery
import com.github.dach83.sharedtest.models.fakeSearchResult
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
    private val fakeAbonentsRepository = FakeAbonentsRepository()
    private val fakeExcelUriRepository = FakeExcelUriRepository()

    @Test
    fun `check initial state`() = runTest {
        val sut = createAbonentsViewModel()
        val uiState = sut.uiState
        assertThat(uiState).isEqualTo(AbonentsUiState.INITIAL)
    }

    @Test
    fun `start loading excel file updates state to loading`() = runTest {
        val sut = createAbonentsViewModel()

        sut.loadExcelFile(fakeExcelUri)

        val uiState = sut.uiState
        assertThat(uiState).isEqualTo(AbonentsUiState.Loading(progress = 0f))
    }

    @Test
    fun `successful load excel file updates state to loaded`() = runTest {
        val sut = createAbonentsViewModel()

        sut.loadExcelFile(fakeExcelUri)

        advanceUntilIdle()
        val uiState = sut.uiState
        assertThat(uiState).isEqualTo(AbonentsUiState.Loaded)
    }

    @Test
    fun `unsuccessful load excel file updates state to error`() = runTest {
        val sut = createAbonentsViewModel()
        fakeAbonentsRepository.errorMode()

        sut.loadExcelFile(fakeExcelUri)

        advanceUntilIdle()
        val uiState = sut.uiState
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
    fun `start search displays no abonents`() = runTest {
        val sut = createAbonentsViewModel()
        sut.loadExcelFile(fakeExcelUri)

        sut.startSearch(searchQuery = emptySearchQuery)

        advanceUntilIdle()
        val abonents = sut.filteredAbonents.first()
        assertThat(abonents).isEmpty()
    }

    @Test
    fun `correct search displays correct abonents`() = runTest {
        val sut = createAbonentsViewModel()
        sut.loadExcelFile(fakeExcelUri)

        sut.startSearch(searchQuery = fakeSearchQuery)

        advanceUntilIdle()
        val abonents = sut.filteredAbonents.first()
        assertThat(abonents).containsExactlyElementsIn(fakeSearchResult)
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
        abonentsRepository = fakeAbonentsRepository,
        excelUriRepository = fakeExcelUriRepository
    )
}
