package com.github.dach83.gasmetering.features.abonents.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.dach83.gasmetering.features.abonents.domain.model.AbonentSortOrder

@Composable
fun SortOrder(
    sortOrder: AbonentSortOrder,
    onSortClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 16.dp)
            .clickable { onSortClick() },
    ) {
        Text(
            text = stringResource(id = sortOrder.key.title),
            style = MaterialTheme.typography.labelLarge,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Icon(
            imageVector = sortOrder.direction.icon,
            contentDescription = "Sorting direction",
            modifier = Modifier.size(16.dp),
        )
    }
}
