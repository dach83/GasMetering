package com.github.dach83.gasmetering.fake

import android.net.Uri
import com.github.dach83.gasmetering.features.abonents.domain.model.Abonent
import com.github.dach83.gasmetering.features.abonents.domain.repository.AbonentsRepository
import com.github.dach83.gasmetering.models.fakeAbonents

class FakeAbonentsRepository : AbonentsRepository {

    private var abonents: List<Abonent> = fakeAbonents
    private var error: Exception? = null

    override suspend fun loadAbonents(
        excelUri: Uri,
        onLoading: suspend (progress: Byte, abonents: List<Abonent>) -> Unit
    ) {
        error?.let { throw it }
        onLoading(100, abonents)
    }

    fun errorMode() {
        error = Exception()
    }
}
