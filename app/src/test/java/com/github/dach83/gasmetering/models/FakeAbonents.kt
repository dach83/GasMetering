package com.github.dach83.gasmetering.models

import com.github.dach83.gasmetering.features.abonents.domain.model.Abonent
import com.github.dach83.gasmetering.features.abonents.domain.model.MeteringDate

val fakeAbonent1 = Abonent(
    id = "Abonent 1",
    address = "Address 1",
    meterings = mapOf(
        MeteringDate(2022, 1) to 0.937,
        MeteringDate(2022, 2) to 8.769,
        MeteringDate(2022, 3) to 17.238,
        MeteringDate(2022, 4) to 23.292,
        MeteringDate(2022, 5) to 29.282,
        MeteringDate(2022, 6) to 35.360,
        MeteringDate(2022, 7) to 44.045,
        MeteringDate(2022, 8) to 46.103,
        MeteringDate(2022, 9) to 47.816,
        MeteringDate(2022, 10) to 52.750,
        MeteringDate(2022, 11) to 53.962,
        MeteringDate(2022, 12) to 62.032
    )
)

val fakeAbonent2 = Abonent(
    id = "Abonent 2",
    address = "Address 2",
    meterings = mapOf(
        MeteringDate(2022, 1) to 3.118,
        MeteringDate(2022, 2) to 10.884,
        MeteringDate(2022, 3) to 17.970,
        MeteringDate(2022, 4) to 20.715,
        MeteringDate(2022, 5) to 24.884,
        MeteringDate(2022, 6) to 26.614,
        MeteringDate(2022, 7) to 35.181,
        MeteringDate(2022, 8) to 40.968,
        MeteringDate(2022, 9) to 44.004,
        MeteringDate(2022, 10) to 49.964,
        MeteringDate(2022, 11) to 51.870,
        MeteringDate(2022, 12) to 57.474
    )
)

val fakeAbonents = listOf(
    fakeAbonent1,
    fakeAbonent2
)
