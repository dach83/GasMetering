package com.github.dach83.gasmetering.features.abonents.presentation.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.github.dach83.gasmetering.R

@Composable
fun SearchToolbar(
    enabled: Boolean,
    searchQuery: String,
    onStartSearch: (String) -> Unit,
    onCancelSearch: () -> Unit,
    onOpenDocClick: () -> Unit,
    modifier: Modifier = Modifier,
    surface: Color = MaterialTheme.colorScheme.surface,
    surfaceVariant: Color = MaterialTheme.colorScheme.surfaceVariant,
    onSurfaceVariant: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(color = onSurfaceVariant),
    shape: Shape = MaterialTheme.shapes.extraLarge,
) {
    val backgroundBrush = if (enabled) {
        SolidColor(surfaceVariant)
    } else {
        Brush.verticalGradient(listOf(surface, surface, Color.Transparent, Color.Transparent))
    }
    val leadingIcon = if (enabled) Icons.Default.ArrowBack else Icons.Default.Search
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    var textField by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    textField = textField.copy(text = searchQuery)

    // When the "Back" button is pressed, the search mode ends first.
    BackHandler(enabled = enabled) {
        onCancelSearch()
    }

    // When the search mode is started, the edit field gets focus.
    LaunchedEffect(key1 = enabled) {
        focusRequester.requestFocus()
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(brush = backgroundBrush)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(shape = shape)
            .background(color = surfaceVariant)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
            ) {
                onStartSearch("")
            },
    ) {
        Icon(
            // leading icon
            imageVector = leadingIcon,
            contentDescription = "",
            tint = onSurfaceVariant,
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp)
                .clip(CircleShape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled,
                    onClick = onCancelSearch,
                )
                .padding(8.dp)
                .size(24.dp),

        )
        BasicTextField(
            // search query editor
            value = textField,
            onValueChange = {
                if (enabled) {
                    onStartSearch(it.text)
                    textField = it
                }
            },
            enabled = enabled,
            textStyle = textStyle,
            singleLine = true,
            modifier = Modifier
                .weight(10f)
                .focusRequester(focusRequester),
            decorationBox = { innerTextField ->
                if (enabled) {
                    innerTextField()
                } else {
                    Text(
                        // placeholder
                        text = stringResource(id = R.string.search_placeholder),
                        style = textStyle,
                    )
                }
            },
        )
        if (enabled.not()) {
            Icon(
                // trailing icon
                imageVector = Icons.Default.FolderOpen,
                contentDescription = "Open document",
                tint = onSurfaceVariant,
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onOpenDocClick)
                    .padding(8.dp)
                    .size(24.dp),

            )
        }
    }
}
