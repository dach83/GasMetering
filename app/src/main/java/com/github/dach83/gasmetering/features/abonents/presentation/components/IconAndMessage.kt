package com.github.dach83.gasmetering.features.abonents.presentation.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun IconAndMessage(
    @DrawableRes iconId: Int,
    @StringRes textId: Int,
    onClick: () -> Unit = {},
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onClick() }
                .padding(56.dp),
        ) {
            Image(
                painter = painterResource(id = iconId),
                contentDescription = "",
                modifier = Modifier.size(100.dp),
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id = textId),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}
