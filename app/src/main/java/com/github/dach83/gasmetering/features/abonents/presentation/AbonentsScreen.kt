package com.github.dach83.gasmetering.features.abonents.presentation

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.dach83.gasmetering.R
import com.github.dach83.gasmetering.core.presentation.navigation.NavigationTransitions
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination(style = NavigationTransitions::class)
@Composable
fun AbonentsScreen(
    viewModel: AbonentsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState = viewModel.uiState.collectAsState()
    val filter by viewModel.filter.collectAsState()
    val abonents = viewModel.filteredAbonents.collectAsState(initial = emptyList())
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        SearchField(
            enabled = filter.searchEnabled,
            query = filter.searchQuery,
            onStartSearch = viewModel::startSearch,
            onCancelSearch = viewModel::cancelSearch,
            onFolderClick = {}
        )
    }
}

@Composable
fun SearchField(
    enabled: Boolean,
    query: String,
    onStartSearch: (String) -> Unit,
    onCancelSearch: () -> Unit,
    onFolderClick: () -> Unit,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    surface: Color = MaterialTheme.colorScheme.surface,
    surfaceVariant: Color = MaterialTheme.colorScheme.surfaceVariant,
    onSurfaceVariant: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(color = onSurfaceVariant)
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    val searchModeColor = if (enabled) surfaceVariant else surface
    var textField by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    textField = textField.copy(text = query)

    LaunchedEffect(key1 = enabled) {
        focusRequester.requestFocus()
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(searchModeColor)
            .padding(16.dp)
            .clip(shape = shape)
            .background(color = surfaceVariant)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onStartSearch("")
            }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Icon(
            imageVector = if (enabled) {
                Icons.Default.ArrowBack
            } else {
                Icons.Default.Search
            },
            contentDescription = "",
            tint = onSurfaceVariant,
            modifier = Modifier
                .clip(CircleShape)
                .clickable(enabled = enabled, onClick = onCancelSearch)
                .padding(4.dp)
        )
        BasicTextField(
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
                .padding(horizontal = 8.dp)
                .focusRequester(focusRequester),
            decorationBox = { innerTextField ->
                if (enabled) {
                    innerTextField()
                } else {
                    Text(
                        text = stringResource(id = R.string.search_placeholder),
                        style = textStyle
                    )
                }
            }
        )
        if (!enabled) {
            Icon(
                imageVector = Icons.Default.FolderOpen,
                contentDescription = "Open document",
                tint = onSurfaceVariant,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(onClick = onFolderClick)
                    .padding(4.dp)
            )
        }
    }
}
