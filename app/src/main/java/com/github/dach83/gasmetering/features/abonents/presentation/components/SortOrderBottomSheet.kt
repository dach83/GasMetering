package com.github.dach83.gasmetering.features.abonents.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.dach83.gasmetering.features.abonents.domain.model.AbonentSortKey
import com.github.dach83.gasmetering.features.abonents.domain.model.AbonentSortOrder
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
        SortOrderBottomSheetHeader()
        Spacer(modifier = Modifier.height(24.dp))
        SortOrderBottomSheetItem(
            sortKey = AbonentSortKey.ID,
            sortDirection = sortOrder.direction,
            selected = sortOrder.key == AbonentSortKey.ID,
            onSortChange = resultNavigator::navigateBack,
        )
        SortOrderBottomSheetItem(
            sortKey = AbonentSortKey.ADDRESS,
            sortDirection = sortOrder.direction,
            selected = sortOrder.key == AbonentSortKey.ADDRESS,
            onSortChange = resultNavigator::navigateBack,
        )
        SortOrderBottomSheetItem(
            sortKey = AbonentSortKey.LAST_METERING_DATE,
            sortDirection = sortOrder.direction,
            selected = sortOrder.key == AbonentSortKey.LAST_METERING_DATE,
            onSortChange = resultNavigator::navigateBack,
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}
