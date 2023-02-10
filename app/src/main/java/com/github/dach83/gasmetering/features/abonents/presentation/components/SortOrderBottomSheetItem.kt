package com.github.dach83.gasmetering.features.abonents.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.dach83.gasmetering.features.abonents.domain.model.AbonentSortKey
import com.github.dach83.gasmetering.features.abonents.domain.model.AbonentSortOrder
import com.github.dach83.gasmetering.features.abonents.domain.model.SortDirection

@Composable
fun SortOrderBottomSheetItem(
    sortKey: AbonentSortKey,
    sortDirection: SortDirection,
    selected: Boolean,
    onSortChange: (AbonentSortOrder) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clickable {
                val newSortDirection = if (selected) {
                    sortDirection.reverse()
                } else {
                    SortDirection.ASCENDING
                }
                val newSortOrder = AbonentSortOrder(
                    key = sortKey,
                    direction = newSortDirection,
                )
                onSortChange(newSortOrder)
            },
    ) {
        Icon(
            imageVector = sortDirection.icon,
            tint = if (selected) Color.Black else Color.Transparent,
            contentDescription = "",
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = stringResource(id = sortKey.title))
    }
}
