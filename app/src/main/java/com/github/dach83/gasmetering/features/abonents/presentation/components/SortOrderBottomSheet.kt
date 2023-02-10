package com.github.dach83.gasmetering.features.abonents.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
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
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle

@Destination(style = DestinationStyle.BottomSheet::class)
@Composable
fun SortOrderBottomSheet(
    sortOrder: AbonentSortOrder,
    resultNavigator: ResultBackNavigator<AbonentSortOrder>,
) {
    Column {
        SortOrderHeader()
        Spacer(modifier = Modifier.height(24.dp))
        SortOrderItem(
            sortKey = AbonentSortKey.ID,
            sortDirection = sortOrder.direction,
            selected = sortOrder.key == AbonentSortKey.ID,
            onSortChange = resultNavigator::navigateBack,
        )
        SortOrderItem(
            sortKey = AbonentSortKey.ADDRESS,
            sortDirection = sortOrder.direction,
            selected = sortOrder.key == AbonentSortKey.ADDRESS,
            onSortChange = resultNavigator::navigateBack,
        )
        SortOrderItem(
            sortKey = AbonentSortKey.LAST_METERING_DATE,
            sortDirection = sortOrder.direction,
            selected = sortOrder.key == AbonentSortKey.LAST_METERING_DATE,
            onSortChange = resultNavigator::navigateBack,
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SortOrderHeader() {
    Text(
        text = "Sort order",
        modifier = Modifier.padding(24.dp),
    )
    Divider()
}

@Composable
fun SortOrderItem(
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
