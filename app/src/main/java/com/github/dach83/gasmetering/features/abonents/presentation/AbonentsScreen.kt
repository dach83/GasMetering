package com.github.dach83.gasmetering.features.abonents.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.dach83.gasmetering.core.presentation.navigation.NavigationTransitions
import com.github.dach83.gasmetering.features.destinations.TakeReadingsScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(style = NavigationTransitions::class)
@Composable
fun AbonentsScreen(
    navigator: DestinationsNavigator
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Abonents screen")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            navigator.navigate(TakeReadingsScreenDestination())
        }) {
            Text(text = "Take readings")
        }
    }
}
