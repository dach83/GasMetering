package com.github.dach83.gasmetering.features.abonents.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.github.dach83.gasmetering.R
import com.github.dach83.gasmetering.features.abonents.domain.model.Abonent
import com.github.dach83.gasmetering.features.abonents.domain.model.AbonentSortOrder
import com.github.dach83.gasmetering.features.abonents.presentation.state.AbonentsUiState

@Composable
fun AbonentsScreenBody(
    uiState: AbonentsUiState,
    abonents: List<Abonent>,
    sortOrder: AbonentSortOrder,
    toolbarHeight: Dp,
    searchEnabled: Boolean,
    onOpenDocClick: () -> Unit,
    onAbonentClick: (Abonent) -> Unit,
    onSortClick: () -> Unit,
) {
    when (uiState) {
        AbonentsUiState.NoExcelUri ->
            IconAndMessage(
                iconId = R.drawable.openfolder,
                textId = R.string.open_exel_doc,
                onClick = onOpenDocClick,
            )

        is AbonentsUiState.Error ->
            IconAndMessage(
                iconId = R.drawable.warning,
                textId = uiState.message,
                onClick = onOpenDocClick,
            )

        else ->
            AbonentList(
                sortOrder = sortOrder,
                abonents = abonents,
                onAbonentClick = onAbonentClick,
                onSortClick = onSortClick,
                toolbarHeight = toolbarHeight,
                searchEnabled = searchEnabled,
            )
    }
}
