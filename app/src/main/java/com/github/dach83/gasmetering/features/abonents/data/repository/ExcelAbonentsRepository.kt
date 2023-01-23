package com.github.dach83.gasmetering.features.abonents.data.repository

import android.content.Context
import android.net.Uri
import com.github.dach83.gasmetering.features.abonents.domain.model.Abonent
import com.github.dach83.gasmetering.features.abonents.domain.repository.AbonentsRepository
import javax.inject.Inject

class ExcelAbonentsRepository @Inject constructor(
    private val context: Context
) : AbonentsRepository {

    override suspend fun loadAbonents(
        excelUri: Uri,
        onLoading: suspend (progress: Byte, abonents: List<Abonent>) -> Unit
    ) {
    }
}
