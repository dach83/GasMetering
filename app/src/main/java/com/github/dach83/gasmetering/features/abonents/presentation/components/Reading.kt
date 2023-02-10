package com.github.dach83.gasmetering.features.abonents.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.dach83.gasmetering.features.abonents.domain.model.ReadingsDate

@Composable
fun Reading(
    date: ReadingsDate,
    value: Double,
) {
    Column {
        ReadingValue(value)
        Spacer(modifier = Modifier.height(2.dp))
        ReadingDate(date)
    }
}
