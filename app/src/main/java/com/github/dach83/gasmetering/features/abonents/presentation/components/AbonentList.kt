package com.github.dach83.gasmetering.features.abonents.presentation.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.dach83.gasmetering.features.abonents.domain.model.Abonent
import com.github.dach83.gasmetering.features.abonents.domain.model.AbonentSortOrder

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AbonentList(
    abonents: List<Abonent>,
    onAbonentClick: (Abonent) -> Unit,
    sortOrder: AbonentSortOrder,
    onSortClick: () -> Unit,
    searchEnabled: Boolean,
    toolbarHeight: Dp,
) {
    LazyColumn(
        contentPadding = PaddingValues(top = toolbarHeight),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
    ) {
        if (!searchEnabled) {
            item {
                SortOrder(sortOrder, onSortClick)
            }
        }
        items(
            items = abonents,
            key = { it.id },
        ) {
            AbonentItem(
                abonent = it,
                onAbonentClick = onAbonentClick,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .animateItemPlacement(tween(300)),
            )
        }
    }
}
