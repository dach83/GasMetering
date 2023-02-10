package com.github.dach83.gasmetering.features.abonents.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.ui.graphics.vector.ImageVector

enum class SortDirection(val icon: ImageVector) {
    ASCENDING(Icons.Default.ArrowDownward) {
        override fun reverse(): SortDirection = DESCENDING
    },
    DESCENDING(Icons.Default.ArrowUpward) {
        override fun reverse(): SortDirection = ASCENDING
    },
    ;

    abstract fun reverse(): SortDirection
}
