package com.github.dach83.gasmetering.features.abonents.presentation.state

import com.github.dach83.gasmetering.features.abonents.domain.model.AbonentSortOrder

data class AbonentsFilter(
    val searchEnabled: Boolean,
    val searchQuery: String,
    val sortOrder: AbonentSortOrder,
) {
    companion object {
        val INITIAL = AbonentsFilter(
            searchEnabled = false,
            searchQuery = "",
            sortOrder = AbonentSortOrder.DEFAULT,
        )
    }
}
