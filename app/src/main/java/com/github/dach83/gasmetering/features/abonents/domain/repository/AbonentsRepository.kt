package com.github.dach83.gasmetering.features.abonents.domain.repository

import android.net.Uri
import com.github.dach83.gasmetering.features.abonents.domain.model.Abonent

interface AbonentsRepository {

    suspend fun loadAbonents(
        excelUri: Uri,
        onLoading: suspend (progress: Float, abonents: List<Abonent>) -> Unit
    )
}
