package com.github.dach83.gasmetering.features.abonents.data.repository

import android.content.Context
import android.net.Uri
import com.github.dach83.gasmetering.features.abonents.data.dispatchers.CoroutineDispatchers
import com.github.dach83.gasmetering.features.abonents.domain.model.Abonent
import com.github.dach83.gasmetering.features.abonents.domain.model.ReadingsDate
import com.github.dach83.gasmetering.features.abonents.domain.repository.AbonentsRepository
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.util.*
import javax.inject.Inject

class ExcelAbonentsRepository @Inject constructor(
    private val context: Context,
    private val dispatchers: CoroutineDispatchers
) : AbonentsRepository {

    private val abonents = mutableListOf<Abonent>()
    private var headers: List<ColumnHeader?> = emptyList()

    override suspend fun loadAbonents(
        excelUri: Uri,
        onLoading: suspend (progress: Float, abonents: List<Abonent>) -> Unit
    ) {
        withContext(dispatchers.io) {
            context.contentResolver.openInputStream(excelUri)?.use { inputStream ->
                abonents.clear()
                val workbook = WorkbookFactory.create(inputStream)
                val sheet = workbook?.getSheetAt(0)
                val totalRows = sheet?.lastRowNum ?: 0
                sheet?.forEachIndexed { rowIndex, row ->
                    if (rowIndex == 0) {
                        headers = parseHeaders(row)
                    } else {
                        val abonent = parseAbonent(row)
                        if (abonent.id.isNotEmpty()) {
                            val progress = (rowIndex + 1) / (totalRows.toFloat() + 1)
                            abonents.add(abonent)
                            withContext(dispatchers.main) {
                                onLoading(progress, abonents.toList())
                            }
                        }
                    }
                }
            }
        }
    }

    private fun parseHeaders(row: Row): List<ColumnHeader?> = row.map { cell ->
        cell.toColumnHeader()
    }

    private fun parseAbonent(row: Row): Abonent {
        var id = ""
        var address = ""
        val meterings = TreeMap<ReadingsDate, Double>()
        row.forEachIndexed { columnIndex, cell ->
            headers.getOrNull(columnIndex)?.let { header ->
                when (header) {
                    ColumnHeader.Abonent -> id = cell.toText()
                    ColumnHeader.Address -> address = cell.toText()
                    is ColumnHeader.Readings ->
                        if (cell.toDouble() > 0) {
                            meterings[header.date] = cell.toDouble()
                        }
                }
            }
        }
        return Abonent(id, address, meterings)
    }

    private fun Cell.toText(): String {
        cellType = Cell.CELL_TYPE_STRING
        return stringCellValue.trim()
    }

    private fun Cell.toDouble(): Double {
        cellType = Cell.CELL_TYPE_NUMERIC
        return numericCellValue
    }

    private fun Cell.toColumnHeader(): ColumnHeader? = when (toString().lowercase()) {
        ABONENT_COLUMN -> ColumnHeader.Abonent
        ADDRESS_COLUMN -> ColumnHeader.Address
        else -> toReadingsHeaderOrNull()
    }

    private fun Cell.toReadingsHeaderOrNull(): ColumnHeader.Readings? =
        if (cellType == Cell.CELL_TYPE_NUMERIC) {
            val readingDate = ReadingsDate(dateCellValue)
            ColumnHeader.Readings(readingDate)
        } else {
            null
        }

    companion object {
        private const val ABONENT_COLUMN = "abonent"
        private const val ADDRESS_COLUMN = "address"
    }
}
