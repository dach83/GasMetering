package com.github.dach83.gasmetering.features.abonents.domain.model

data class MeterReading(
    val year: Int,
    val month: Byte,
    val value: Float
)
