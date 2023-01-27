package com.github.dach83.gasmetering.features.abonents.domain.model

import java.util.*

data class Abonent(
    val id: String,
    val address: String,
    val readings: TreeMap<ReadingsDate, Double>
) {
    fun contains(searchQuery: String): Boolean {
        return id.contains(searchQuery) || address.contains(searchQuery)
    }
}
