package com.github.dach83.gasmetering.features.abonents.domain.model

data class MeteringDate(
    val year: Int,
    val month: Int
) {

    operator fun compareTo(other: MeteringDate): Int {
        return numberOfMonths() - other.numberOfMonths()
    }

    private fun numberOfMonths(): Int {
        return year * MONTH_IN_YEAR + month
    }

    companion object {
        private const val MONTH_IN_YEAR = 12
    }
}
