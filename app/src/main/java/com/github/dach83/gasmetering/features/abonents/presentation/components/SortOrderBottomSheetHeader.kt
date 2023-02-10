package com.github.dach83.gasmetering.features.abonents.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SortOrderBottomSheetHeader() {
    Text(
        text = "Sort order",
        modifier = Modifier.padding(24.dp),
    )
    Divider()
}
