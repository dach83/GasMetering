package com.github.dach83.gasmetering.features.abonents.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.dach83.gasmetering.features.abonents.domain.model.Readings
import com.github.dach83.gasmetering.features.abonents.domain.model.ReadingsBarChart
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

@Composable
fun VolumesBarChart(readings: Readings) {
    val barsCount = 12
    val volumes = ReadingsBarChart(readings).normalizeVolumes(barsCount)
    val scrollState = rememberScrollState(Int.MAX_VALUE)
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .height(40.dp)
            .horizontalScroll(scrollState),
    ) {
        volumes.forEach { volume ->
            val barColor = if (volume.second < 0) {
                Color.Red.copy(alpha = .3f)
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .3f)
            }
            val month = SimpleDateFormat("MMM", Locale.getDefault())
                .format(volume.first.value)
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = month,
                    style = MaterialTheme.typography.labelSmall,
                    softWrap = false,
                    modifier = Modifier.scale(0.8f),
                )
                Spacer(
                    modifier = Modifier
                        .width(16.dp)
                        .fillMaxHeight(abs(volume.second))
                        .background(color = barColor),
                )
            }
        }
    }
}
