package com.github.dach83.gasmetering.features.abonents.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AbonentSortOrder(
    val key: AbonentSortKey,
    val direction: SortDirection,
) : Parcelable, Comparator<Abonent> {

    override fun compare(abonent1: Abonent?, abonent2: Abonent?): Int = when (direction) {
        SortDirection.ASCENDING -> key.compare(abonent1, abonent2)
        SortDirection.DESCENDING -> key.compare(abonent2, abonent1)
    }

    companion object {
        val DEFAULT = AbonentSortOrder(
            key = AbonentSortKey.ID,
            direction = SortDirection.ASCENDING,
        )
    }
}
