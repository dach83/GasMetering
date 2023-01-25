package com.github.dach83.gasmetering.features.abonents.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.dach83.gasmetering.R
import com.github.dach83.gasmetering.core.presentation.navigation.NavigationTransitions
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlin.math.roundToInt

@RootNavGraph(start = true)
@Destination(style = NavigationTransitions::class)
@Composable
fun AbonentsScreen(
    viewModel: AbonentsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val filter by viewModel.filter.collectAsState()

    val toolbarHeight = 64.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    var toolbarOffsetHeightPx by remember { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // try to consume before LazyColumn to collapse toolbar if needed, hence pre-scroll
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx + delta
                toolbarOffsetHeightPx = if (filter.searchEnabled) {
                    0f // in search mode, the toolbar doesn't collapse
                } else {
                    newOffset.coerceIn(-toolbarHeightPx, 0f)
                }
                // here's the catch: let's pretend we consumed 0 in any case, since we want
                // LazyColumn to scroll anyway for good UX
                // We're basically watching scroll without taking it
                return Offset.Zero
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        AbonentList(
            toolbarHeight = toolbarHeight
        )
        SearchToolbar(
            enabled = filter.searchEnabled,
            searchQuery = filter.searchQuery,
            onStartSearch = viewModel::startSearch,
            onCancelSearch = viewModel::cancelSearch,
            onFolderClick = {},
            modifier = Modifier
                .height(toolbarHeight)
                .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.roundToInt()) }
        )
    }
}

@Composable
fun AbonentList(toolbarHeight: Dp) {
    LazyColumn(
        contentPadding = PaddingValues(top = toolbarHeight),
        modifier = Modifier.fillMaxSize()
    ) {
        items(50) {
            AbonentInfo(it)
        }
    }
}

@Composable
fun AbonentInfo(num: Int) {
    Text(text = "Abonent $num")
}

@Composable
fun SearchToolbar(
    enabled: Boolean,
    searchQuery: String,
    onStartSearch: (String) -> Unit,
    onCancelSearch: () -> Unit,
    onFolderClick: () -> Unit,
    modifier: Modifier = Modifier,
    surface: Color = MaterialTheme.colorScheme.surface,
    surfaceVariant: Color = MaterialTheme.colorScheme.surfaceVariant,
    onSurfaceVariant: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(color = onSurfaceVariant),
    shape: Shape = MaterialTheme.shapes.extraLarge
) {
    val backgroundColor = if (enabled) surfaceVariant else surface
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
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(shape = shape)
            .background(color = surfaceVariant)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onStartSearch("")
            }
    ) {
        Icon( // leading icon
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
                    onClick = onCancelSearch
                )
                .padding(8.dp)
                .size(24.dp)

        )
        BasicTextField( // search query editor
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
                    Text( // placeholder
                        text = stringResource(id = R.string.search_placeholder),
                        style = textStyle
                    )
                }
            }
        )
        if (enabled.not()) {
            Icon( // trailing icon
                imageVector = Icons.Default.FolderOpen,
                contentDescription = "Open document",
                tint = onSurfaceVariant,
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onFolderClick)
                    .padding(8.dp)
                    .size(24.dp)

            )
        }
    }
}
