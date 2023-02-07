package com.github.dach83.gasmetering.features.abonents.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyle

@Destination(style = DestinationStyle.BottomSheet::class)
@Composable
fun SortOrderBottomSheet() {
    Column {
        SortOrderHeader()
        Spacer(modifier = Modifier.height(24.dp))
        SortOrderItem(text = "Abonent", ascending = true, selected = true)
        SortOrderItem(text = "Address", ascending = false, selected = true)
        SortOrderItem(text = "Last metering date", ascending = false, selected = false)
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SortOrderHeader() {
    Text(
        text = "Sort",
        modifier = Modifier.padding(24.dp)
    )
    Divider()
}

@Composable
fun SortOrderItem(
    text: String,
    ascending: Boolean,
    selected: Boolean
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Icon(
            imageVector = if (ascending) {
                Icons.Default.ArrowDownward
            } else {
                Icons.Default.ArrowUpward
            },
            tint = if (selected) Color.Black else Color.Transparent,
            contentDescription = ""
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text)
    }
}
