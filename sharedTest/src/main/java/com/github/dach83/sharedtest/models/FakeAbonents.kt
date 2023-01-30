package com.github.dach83.sharedtest.models

import com.github.dach83.gasmetering.features.abonents.domain.model.Abonent
import com.github.dach83.gasmetering.features.abonents.domain.model.ReadingsDate
import java.util.*

val fakeAbonent1 = Abonent(
    id = "97141",
    address = "Oregon, 44 Cedar Avenue",
    readings = TreeMap<ReadingsDate, Double>().apply {
        put(ReadingsDate(2022, 11), 53.961)
        put(ReadingsDate(2022, 12), 62.031)
    }
)

val fakeAbonent2 = Abonent(
    id = "19886",
    address = "Wilmington, 420 Paper St",
    readings = TreeMap<ReadingsDate, Double>().apply {
        put(ReadingsDate(2022, 11), 51.869)
        put(ReadingsDate(2022, 12), 57.473)
    }
)

val fakeAbonents = listOf(
    fakeAbonent1,
    fakeAbonent2
)
