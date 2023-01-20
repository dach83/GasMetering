package com.github.dach83.gasmetering.features.abonents.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dach83.gasmetering.R
import com.github.dach83.gasmetering.features.abonents.domain.model.Abonent
import com.github.dach83.gasmetering.features.abonents.domain.usecase.LoadAbonents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AbonentsViewModel @Inject constructor(
    private val loadAbonents: LoadAbonents
) : ViewModel() {

    private val mutableUiState = MutableStateFlow(AbonentsUiState.INITIAL)
    val uiState = mutableUiState.asStateFlow()

    private val allAbonents = MutableStateFlow<List<Abonent>>(emptyList())
    val filteredAbonents: Flow<List<Abonent>> =
        combine(allAbonents, uiState) { abonents, _ ->
            abonents
        }

    private var loadingJob: Job? = null

    fun loadExcelFile(excelUri: Uri) {
        mutableUiState.update { it.loading(progress = 0) }
        loadingJob?.cancel()
        loadingJob = viewModelScope.launch {
            try {
                loadAbonents(excelUri) { progress, abonents ->
                    mutableUiState.update { it.loading(progress) }
                    allAbonents.emit(abonents)
                }
                mutableUiState.update { it.loaded() }
            } catch (cause: Exception) {
                mutableUiState.update { it.error(R.string.app_name) }
            }
        }
    }
}
