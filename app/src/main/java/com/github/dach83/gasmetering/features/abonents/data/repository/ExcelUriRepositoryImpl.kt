package com.github.dach83.gasmetering.features.abonents.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.content.edit
import com.github.dach83.gasmetering.features.abonents.domain.repository.ExcelUriRepository

class ExcelUriRepositoryImpl(context: Context) : ExcelUriRepository {

    private val sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    override fun loadLatestExcelUri(): Uri? = if (sharedPref.contains(LATEST_EXCEL_URI)) {
        val uriString = sharedPref.getString(LATEST_EXCEL_URI, "")
        Uri.parse(uriString)
    } else {
        null
    }

    override fun saveLatestExcelUri(excelUri: Uri?) {
        if (excelUri != null) {
            sharedPref.edit {
                putString(LATEST_EXCEL_URI, excelUri.toString())
            }
        }
    }

    companion object {
        private const val SHARED_PREF_NAME = "excelUriPref"
        private const val LATEST_EXCEL_URI = "latestExcelUri"
    }
}
