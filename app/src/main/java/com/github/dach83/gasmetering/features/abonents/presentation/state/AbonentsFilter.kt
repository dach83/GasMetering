package com.github.dach83.gasmetering.features.abonents.presentation.state

data class AbonentsFilter(
    val searchQuery: String?
) {

    companion object {
        val INITIAL = AbonentsFilter(
            searchQuery = null
        )
    }
}
