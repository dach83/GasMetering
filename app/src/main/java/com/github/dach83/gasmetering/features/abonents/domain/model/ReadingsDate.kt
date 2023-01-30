package com.github.dach83.gasmetering.features.abonents.domain.model

import java.util.*

data class ReadingsDate(
    val value: Date
) : Comparable<ReadingsDate> {

    private val numberOfMonth: Int

    init {
        val calendar = Calendar.getInstance()
        calendar.time = value
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        numberOfMonth = year * MONTH_IN_YEAR + month
    }

    override operator fun compareTo(other: ReadingsDate): Int {
        return numberOfMonth - other.numberOfMonth
    }

    companion object {
        private const val MONTH_IN_YEAR = 12
    }
}
