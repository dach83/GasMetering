package com.github.dach83.gasmetering.fake

import android.net.Uri
import com.github.dach83.gasmetering.features.abonents.domain.model.Abonent
import com.github.dach83.gasmetering.features.abonents.domain.usecase.LoadAbonents

class FakeLoadAbonents : LoadAbonents {

    private var abonents: List<Abonent> = emptyList()
    private var error: Exception? = null

    override suspend fun invoke(excelUri: Uri): List<Abonent> {
        error?.let { throw it }
        return abonents
    }

    fun successMode() {
        error = null
    }

    fun errorMode() {
        error = java.lang.Exception()
    }
}
