package com.github.dach83.gasmetering.features.abonents.domain.model

import java.util.*

typealias Readings = TreeMap<ReadingsDate, Double>

data class Abonent(
    val id: String,
    val address: String,
    val readings: Readings
) {
    fun contains(searchQuery: String, ignoreCase: Boolean = true): Boolean {
        return id.contains(searchQuery, ignoreCase) || address.contains(searchQuery, ignoreCase)
    }
}
