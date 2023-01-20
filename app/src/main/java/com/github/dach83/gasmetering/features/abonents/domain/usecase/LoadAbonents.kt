package com.github.dach83.gasmetering.features.abonents.domain.usecase

import android.net.Uri
import com.github.dach83.gasmetering.features.abonents.domain.model.Abonent

interface LoadAbonents {
    suspend operator fun invoke(
        excelUri: Uri,
        onLoading: suspend (progress: Byte, abonents: List<Abonent>) -> Unit
    )
}
