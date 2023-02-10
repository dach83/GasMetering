package com.github.dach83.gasmetering.features.abonents.presentation

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dach83.gasmetering.R
import com.github.dach83.gasmetering.features.abonents.domain.model.Abonent
import com.github.dach83.gasmetering.features.abonents.domain.model.AbonentSortOrder
import com.github.dach83.gasmetering.features.abonents.domain.repository.AbonentsRepository
import com.github.dach83.gasmetering.features.abonents.domain.repository.ExcelUriRepository
import com.github.dach83.gasmetering.features.abonents.presentation.state.AbonentsFilter
import com.github.dach83.gasmetering.features.abonents.presentation.state.AbonentsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AbonentsViewModel @Inject constructor(
    private val abonentsRepository: AbonentsRepository,
    private val excelUriRepository: ExcelUriRepository,
) : ViewModel() {

    private var loadingJob: Job? = null

    var uiState: AbonentsUiState by mutableStateOf(AbonentsUiState.INITIAL)
        private set

    private val mutableFilter = MutableStateFlow(AbonentsFilter.INITIAL)
    val filter = mutableFilter.asStateFlow()

    private val abonents = MutableStateFlow<List<Abonent>>(emptyList())
    val filteredAbonents: Flow<List<Abonent>> =
        combine(abonents, filter) { abonents, filter ->
            if (!filter.searchEnabled) {
                abonents
            } else {
                if (filter.searchQuery.isEmpty()) {
                    emptyList()
                } else {
                    delay(300)
                    abonents.filter { abonent ->
                        abonent.contains(filter.searchQuery)
                    }
                }
            }
                .sortedWith(filter.sortOrder)
        }

    init {
        val latestLoadedUri = excelUriRepository.loadLatestExcelUri()
        loadExcelFile(latestLoadedUri)
    }

    fun loadExcelFile(excelUri: Uri?) {
        if (excelUri == null) return
        excelUriRepository.saveLatestExcelUri(excelUri)

        uiState = AbonentsUiState.Loading(progress = 0f)
        loadingJob?.cancel()
        loadingJob = viewModelScope.launch {
            try {
                abonentsRepository.loadAbonents(excelUri) { progress, abonents ->
                    uiState = AbonentsUiState.Loading(progress)
                    this@AbonentsViewModel.abonents.emit(abonents)
                }
                uiState = AbonentsUiState.Loaded
            } catch (cause: Exception) {
                uiState = AbonentsUiState.Error(R.string.error_message)
            }
        }
    }

    fun startSearch(searchQuery: String = "") {
        mutableFilter.update {
            it.copy(
                searchEnabled = true,
                searchQuery = searchQuery,
            )
        }
    }

    fun cancelSearch() {
        mutableFilter.update {
            it.copy(
                searchEnabled = false,
                searchQuery = "",
            )
        }
    }

    fun changeSortOrder(newSortOrder: AbonentSortOrder) {
        mutableFilter.update {
            Log.d("@@@", "changeSortOrder: $newSortOrder")
            it.copy(sortOrder = newSortOrder)
        }
    }
}
