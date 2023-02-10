package com.github.dach83.gasmetering.features.abonents.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.github.dach83.gasmetering.features.abonents.domain.model.ReadingsDate
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReadingDate(date: ReadingsDate) {
    Text(
        text = SimpleDateFormat("MMMM, yyyy", Locale.getDefault()).format(date.value),
        style = MaterialTheme.typography.labelSmall,
    )
}
