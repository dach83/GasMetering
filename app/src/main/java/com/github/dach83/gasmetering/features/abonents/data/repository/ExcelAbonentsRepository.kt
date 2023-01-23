package com.github.dach83.gasmetering.features.abonents.data.repository

import android.content.Context
import android.net.Uri
import com.github.dach83.gasmetering.features.abonents.data.dispatchers.CoroutineDispatchers
import com.github.dach83.gasmetering.features.abonents.domain.model.Abonent
import com.github.dach83.gasmetering.features.abonents.domain.model.MeteringDate
import com.github.dach83.gasmetering.features.abonents.domain.repository.AbonentsRepository
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
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
        onLoading: suspend (progress: Int, abonents: List<Abonent>) -> Unit
    ) {
        withContext(dispatchers.io) {
            context.contentResolver.openInputStream(excelUri)?.use { inputStream ->
                abonents.clear()
                val workbook = WorkbookFactory.create(inputStream)
                val sheet = workbook?.getSheetAt(0)
                val totalRows = sheet?.indexOfLastNotEmptyRow() ?: 0
                sheet?.forEachIndexed { rowIndex, row ->
                    if (rowIndex == 0) {
                        headers = parseHeaders(row)
                    } else {
                        val abonent = parseAbonent(row)
                        if (abonent.id.isNotEmpty()) {
                            val progress = 100 * (rowIndex + 1) / (totalRows + 1)
                            abonents.add(abonent)
                            onLoading(progress, abonents.toList())
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
        val meterings = mutableMapOf<MeteringDate, Double>()
        row.forEachIndexed { columnIndex, cell ->
            headers.getOrNull(columnIndex)?.let { header ->
                when (header) {
                    ColumnHeader.Abonent -> id = cell.toText()
                    ColumnHeader.Address -> address = cell.toText()
                    is ColumnHeader.Metering -> meterings[header.date] = cell.toDouble()
                }
            }
        }
        return Abonent(id, address, meterings)
    }

    private fun Sheet.indexOfLastNotEmptyRow(): Int = indexOfLast { row ->
        row.getCell(0).toText().isNotEmpty()
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
        "abonent" -> ColumnHeader.Abonent
        "address" -> ColumnHeader.Address
        else -> toMeteringHeaderOrNull()
    }

    private fun Cell.toMeteringHeaderOrNull(): ColumnHeader.Metering? =
        if (cellType == Cell.CELL_TYPE_NUMERIC) {
            val meteringDate = toMeteringDate()
            ColumnHeader.Metering(meteringDate)
        } else {
            null
        }

    private fun Cell.toMeteringDate(): MeteringDate {
        val calendar = Calendar.getInstance()
        calendar.time = dateCellValue
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        return MeteringDate(year, month)
    }
}
