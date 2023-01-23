package com.github.dach83.gasmetering.features.abonents.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dach83.gasmetering.R
import com.github.dach83.gasmetering.features.abonents.domain.model.Abonent
import com.github.dach83.gasmetering.features.abonents.domain.usecase.LoadAbonents
import com.github.dach83.gasmetering.features.abonents.presentation.state.AbonentsFilter
import com.github.dach83.gasmetering.features.abonents.presentation.state.AbonentsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AbonentsViewModel @Inject constructor(
    private val loadAbonents: LoadAbonents
) : ViewModel() {

    private val mutableUiState = MutableStateFlow<AbonentsUiState>(AbonentsUiState.INITIAL)
    val uiState = mutableUiState.asStateFlow()

    private val mutableFilter = MutableStateFlow(AbonentsFilter.INITIAL)
    val filter = mutableFilter.asStateFlow()

    private val abonents = MutableStateFlow<List<Abonent>>(emptyList())
    val filteredAbonents: Flow<List<Abonent>> =
        combine(abonents, filter) { abonents, filter ->
            when (filter.searchQuery) {
                "" -> emptyList()
                else -> abonents
            }
        }

    private var loadingJob: Job? = null

    fun loadExcelFile(excelUri: Uri) {
        mutableUiState.value = AbonentsUiState.Loading(progress = 0)
        loadingJob?.cancel()
        loadingJob = viewModelScope.launch {
            try {
                loadAbonents(excelUri) { progress, abonents ->
                    mutableUiState.value = AbonentsUiState.Loading(progress)
                    this@AbonentsViewModel.abonents.emit(abonents)
                }
                mutableUiState.value = AbonentsUiState.Loaded
            } catch (cause: Exception) {
                mutableUiState.value = AbonentsUiState.Error(R.string.error_message)
            }
        }
    }

    fun startSearch(searchQuery: String = "") {
        mutableFilter.update { it.copy(searchQuery = searchQuery) }
    }

    fun cancelSearch() {
        mutableFilter.update { it.copy(searchQuery = null) }
    }
}
