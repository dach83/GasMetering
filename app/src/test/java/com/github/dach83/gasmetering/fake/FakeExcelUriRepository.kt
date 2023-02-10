package com.github.dach83.gasmetering.fake

import android.net.Uri
import com.github.dach83.gasmetering.features.abonents.domain.repository.ExcelUriRepository

class FakeExcelUriRepository : ExcelUriRepository {

    var savedUri: Uri? = null
        private set

    override fun loadLatestExcelUri(): Uri? {
        return savedUri
    }

    override fun saveLatestExcelUri(excelUri: Uri?) {
        savedUri = excelUri
    }
}
