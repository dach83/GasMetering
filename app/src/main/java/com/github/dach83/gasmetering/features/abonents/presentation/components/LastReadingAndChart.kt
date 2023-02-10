package com.github.dach83.gasmetering.features.abonents.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.dach83.gasmetering.features.abonents.domain.model.Readings

@Composable
fun LastReadingAndChart(readings: Readings) {
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        readings.lastEntry()?.let { entry ->
            Reading(entry.key, entry.value)
        }
        Spacer(modifier = Modifier.width(4.dp))
        VolumesBarChart(readings)
    }
}
