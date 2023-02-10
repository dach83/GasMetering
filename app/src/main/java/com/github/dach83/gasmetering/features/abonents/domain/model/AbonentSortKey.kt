package com.github.dach83.gasmetering.features.abonents.domain.model

import androidx.annotation.StringRes
import com.github.dach83.gasmetering.R

enum class AbonentSortKey(@StringRes val title: Int) : Comparator<Abonent> {
    ID(R.string.sort_abonents_by_id) {
        override fun compare(abonent1: Abonent, abonent2: Abonent): Int =
            abonent1.id.compareTo(abonent2.id, ignoreCase = true)
    },
    ADDRESS(R.string.sort_abonents_by_address) {
        override fun compare(abonent1: Abonent, abonent2: Abonent): Int =
            abonent1.address.compareTo(abonent2.address, ignoreCase = true)
    },
    LAST_METERING_DATE(R.string.sort_abonents_by_last_metering_date) {
        override fun compare(abonent1: Abonent, abonent2: Abonent): Int {
            val date1 = abonent1.readings.lastEntry()?.key
            val date2 = abonent2.readings.lastEntry()?.key
            return if (date1 != null && date2 != null) {
                date1.compareTo(date2)
            } else {
                0
            }
        }
    },
}
