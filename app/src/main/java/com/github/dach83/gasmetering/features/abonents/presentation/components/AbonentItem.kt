package com.github.dach83.gasmetering.features.abonents.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.dach83.gasmetering.core.presentation.ui.theme.DarkSwamp
import com.github.dach83.gasmetering.features.abonents.domain.model.Abonent

@Composable
fun AbonentItem(
    abonent: Abonent,
    onAbonentClick: (Abonent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onAbonentClick(abonent) },
    ) {
        Spacer(modifier = Modifier.height(6.dp))
        AbonentAddress(abonent)
        Spacer(modifier = Modifier.height(4.dp))
        LastReadingAndChart(abonent.readings)
        Spacer(modifier = Modifier.height(7.dp))
        Divider(color = DarkSwamp.copy(.1f))
    }
}
