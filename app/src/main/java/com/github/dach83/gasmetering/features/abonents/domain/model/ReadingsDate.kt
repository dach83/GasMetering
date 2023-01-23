package com.github.dach83.gasmetering.features.abonents.domain.model

data class ReadingsDate(
    val year: Int,
    val month: Int
) {

    operator fun compareTo(other: ReadingsDate): Int {
        return numberOfMonths() - other.numberOfMonths()
    }

    private fun numberOfMonths(): Int {
        return year * MONTH_IN_YEAR + month
    }

    companion object {
        private const val MONTH_IN_YEAR = 12
    }
}
