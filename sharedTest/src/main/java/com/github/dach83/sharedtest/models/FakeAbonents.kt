package com.github.dach83.sharedtest.models

import com.github.dach83.gasmetering.features.abonents.domain.model.Abonent
import com.github.dach83.gasmetering.features.abonents.domain.model.ReadingsDate

val fakeAbonent1 = Abonent(
    id = "97141",
    address = "Oregon, 44 Cedar Avenue",
    readings = mapOf(
        ReadingsDate(2022, 11) to 53.961,
        ReadingsDate(2022, 12) to 62.031
    )
)

val fakeAbonent2 = Abonent(
    id = "19886",
    address = "Wilmington, 420 Paper St",
    readings = mapOf(
        ReadingsDate(2022, 11) to 51.869,
        ReadingsDate(2022, 12) to 57.473
    )
)

val fakeAbonents = listOf(
    fakeAbonent1,
    fakeAbonent2
)
