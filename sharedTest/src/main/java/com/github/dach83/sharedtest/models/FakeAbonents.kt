package com.github.dach83.sharedtest.models

import com.github.dach83.gasmetering.features.abonents.domain.model.Abonent
import com.github.dach83.gasmetering.features.abonents.domain.model.ReadingsDate
import java.text.SimpleDateFormat
import java.util.*

val testDate1 = SimpleDateFormat("yyyy-MM", Locale.US).parse("2022-11")!!
val testDate2 = SimpleDateFormat("yyyy-MM", Locale.US).parse("2022-12")!!

val fakeAbonent1 = Abonent(
    id = "97141",
    address = "Oregon, 44 Cedar Avenue",
    readings = TreeMap<ReadingsDate, Double>().apply {
        put(ReadingsDate(testDate1), 53.961)
        put(ReadingsDate(testDate2), 62.031)
    }
)

val fakeAbonent2 = Abonent(
    id = "19886",
    address = "Wilmington, 420 Paper St",
    readings = TreeMap<ReadingsDate, Double>().apply {
        put(ReadingsDate(testDate1), 51.869)
        put(ReadingsDate(testDate2), 57.473)
    }
)

val fakeAbonents = listOf(
    fakeAbonent1,
    fakeAbonent2
)
