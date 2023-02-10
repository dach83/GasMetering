package com.github.dach83.gasmetering.features.abonents.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.dach83.gasmetering.core.presentation.ui.theme.DarkSwamp

@Composable
fun ReadingValue(value: Double) {
    val (intPart, decPart) = "%09.3f".format(value).split(".", ",")
    Row {
        Surface(shadowElevation = 2.dp) {
            Text(
                text = intPart,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = DarkSwamp,
                        shape = RoundedCornerShape(2.dp),
                    )
                    .padding(horizontal = 4.dp, vertical = 2.dp),
            )
        }
        Spacer(modifier = Modifier.width(2.dp))
        Surface(shadowElevation = 2.dp) {
            Text(
                text = decPart,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color.Red,
                        shape = RoundedCornerShape(2.dp),
                    )
                    .padding(horizontal = 4.dp, vertical = 2.dp),
            )
        }
    }
}
