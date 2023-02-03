package com.github.dach83.gasmetering.features.abonents.domain.repository

import android.net.Uri

interface ExcelUriRepository {

    fun loadLatestExcelUri(): Uri?

    fun saveLatestExcelUri(excelUri: Uri?)
}
