package com.github.dach83.gasmetering.features.abonents.data.repository

import com.github.dach83.gasmetering.features.abonents.domain.model.MeteringDate

sealed class ColumnHeader() {

    object Abonent : ColumnHeader()

    object Address : ColumnHeader()

    class Metering(val date: MeteringDate) : ColumnHeader()
}
