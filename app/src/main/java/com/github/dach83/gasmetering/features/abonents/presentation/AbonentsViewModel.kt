package com.github.dach83.gasmetering.features.abonents.presentation

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dach83.gasmetering.features.abonents.domain.usecase.LoadAbonents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AbonentsViewModel @Inject constructor(
    private val loadAbonents: LoadAbonents
) : ViewModel() {

    var uiState by mutableStateOf(AbonentsUiState.INITIAL)
        private set

    private var loadingJob: Job? = null

    fun loadExcelFile(excelUri: Uri) {
        uiState = uiState.loading()

        loadingJob?.cancel()
        loadingJob = viewModelScope.launch {
            try {
                val abonents = loadAbonents(excelUri)
                uiState = uiState.loaded(abonents)
            } catch (cause: Exception) {
                uiState = uiState.error(0)
            }
        }
    }
}
