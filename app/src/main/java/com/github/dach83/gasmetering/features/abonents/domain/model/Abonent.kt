package com.github.dach83.gasmetering.features.abonents.domain.model

data class Abonent(
    val id: String,
    val address: String,
    val readings: Map<ReadingsDate, Double>
) {
    fun contains(searchQuery: String): Boolean {
        return id.contains(searchQuery) || address.contains(searchQuery)
    }
}
