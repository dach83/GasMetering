package com.github.dach83.gasmetering.features.abonents.data.repository

import com.github.dach83.gasmetering.features.abonents.domain.model.ReadingsDate

sealed class ColumnHeader() {

    object Abonent : ColumnHeader()

    object Address : ColumnHeader()

    class Readings(val date: ReadingsDate) : ColumnHeader()
}
