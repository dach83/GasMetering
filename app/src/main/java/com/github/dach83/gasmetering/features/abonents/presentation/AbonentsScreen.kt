package com.github.dach83.gasmetering.features.abonents.presentation

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
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
import com.github.dach83.gasmetering.core.presentation.ui.theme.DarkSwamp
import com.github.dach83.gasmetering.features.abonents.domain.model.Abonent
import com.github.dach83.gasmetering.features.destinations.TakeReadingsScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlin.math.roundToInt
import kotlin.random.Random

@RootNavGraph(start = true)
@Destination(style = NavigationTransitions::class)
@Composable
fun AbonentsScreen(
    viewModel: AbonentsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val filter by viewModel.filter.collectAsState()
    val abonents by viewModel.filteredAbonents.collectAsState(initial = emptyList())

    val excelDocMime = arrayOf(
        "application/vnd.ms-excel",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    )
    val openDocLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = viewModel::loadExcelFile
    )

    val toolbarHeight = 64.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    var toolbarOffsetHeightPx by remember { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                // try to consume before LazyColumn to collapse toolbar if needed, hence pre-scroll
                val delta = consumed.y
                val newOffset = toolbarOffsetHeightPx + delta
                toolbarOffsetHeightPx = if (filter.searchEnabled) {
                    0f // in search mode, the toolbar doesn't collapsing
                } else {
                    newOffset.coerceIn(-toolbarHeightPx, 0f)
                }
                // here's the catch: let's pretend we consumed 0 in any case, since we want
                // LazyColumn to scroll anyway for good UX
                // We're basically watching scroll without taking it
                return super.onPostScroll(consumed, available, source)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        AbonentList(
            abonents = abonents,
            onAbonentClick = {
                navigator.navigate(TakeReadingsScreenDestination())
            },
            toolbarHeight = toolbarHeight
        )
        SearchToolbar(
            enabled = filter.searchEnabled,
            searchQuery = filter.searchQuery,
            onStartSearch = viewModel::startSearch,
            onCancelSearch = viewModel::cancelSearch,
            onFolderClick = { openDocLauncher.launch(excelDocMime) },
            modifier = Modifier
                .height(toolbarHeight)
                .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.roundToInt()) }
        )
    }
}

@Composable
fun AbonentList(
    abonents: List<Abonent>,
    onAbonentClick: (Abonent) -> Unit,
    toolbarHeight: Dp
) {
    LazyColumn(
        contentPadding = PaddingValues(top = toolbarHeight),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        items(50) {
            AbonentItem(it, onAbonentClick)
        }
    }
}

@Composable
fun AbonentItem(
    abonent: Int,
    onAbonentClick: (Abonent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(6.dp))
        AbonentAddress()
        Spacer(modifier = Modifier.height(4.dp))
        LastReadingAndChart()
        Spacer(modifier = Modifier.height(7.dp))
        Divider(color = DarkSwamp.copy(.1f))
    }
}

@Composable
private fun AbonentAddress() {
    Text(
        text = "97141, Oregon, 44 Cedar Avenue",
        style = MaterialTheme.typography.labelLarge
    )
}

@Composable
fun LastReadingAndChart() {
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Reading()
        Spacer(modifier = Modifier.width(4.dp))
        Chart()
    }
}

@Composable
private fun Reading() {
    Column {
        ReadingValue()
        Spacer(modifier = Modifier.height(2.dp))
        ReadingDate()
    }
}

@Composable
private fun ReadingDate() {
    Text(
        text = "January, 2022",
        style = MaterialTheme.typography.labelSmall
    )
}

@Composable
private fun ReadingValue() {
    Row {
        Surface(shadowElevation = 2.dp) {
            Text(
                text = "12345",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = DarkSwamp,
                        shape = RoundedCornerShape(2.dp)
                    )
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }
        Spacer(modifier = Modifier.width(2.dp))
        Surface(shadowElevation = 2.dp) {
            Text(
                text = "678",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color.Red,
                        shape = RoundedCornerShape(2.dp)
                    )
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
fun Chart() {
    val values = List(12) { Random.nextFloat() }
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
    ) {
        repeat(values.size) { ind ->
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Jan",
                    style = MaterialTheme.typography.labelSmall,
                    softWrap = false,
                    modifier = Modifier.scale(0.8f)
                )
                Spacer(
                    modifier = Modifier
                        .width(16.dp)
                        .fillMaxHeight(values[ind])
                        .background(color = DarkSwamp.copy(alpha = .3f))
                )
            }
        }
    }
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
