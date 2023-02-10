package com.github.dach83.gasmetering.features.abonents.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.github.dach83.gasmetering.features.abonents.domain.model.Abonent

@Composable
fun AbonentAddress(abonent: Abonent) {
    Text(
        text = "${abonent.id}, ${abonent.address}",
        style = MaterialTheme.typography.labelLarge,
    )
}
