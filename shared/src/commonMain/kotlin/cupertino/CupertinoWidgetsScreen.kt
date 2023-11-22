@file:OptIn(ExperimentalCupertinoApi::class, ExperimentalLayoutApi::class)
/*
 * Copyright (c) 2023 Compose Cupertino project and open source contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cupertino

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.cupertino.CupertinoActionSheet
import io.github.alexzhirkevich.cupertino.CupertinoActionSheetNative
import io.github.alexzhirkevich.cupertino.CupertinoActivityIndicator
import io.github.alexzhirkevich.cupertino.CupertinoAlertDialog
import io.github.alexzhirkevich.cupertino.CupertinoAlertDialogNative
import io.github.alexzhirkevich.cupertino.CupertinoBottomSheetScaffold
import io.github.alexzhirkevich.cupertino.CupertinoButton
import io.github.alexzhirkevich.cupertino.CupertinoButtonDefaults
import io.github.alexzhirkevich.cupertino.CupertinoButtonSize
import io.github.alexzhirkevich.cupertino.CupertinoDatePicker
import io.github.alexzhirkevich.cupertino.CupertinoDatePickerState
import io.github.alexzhirkevich.cupertino.CupertinoDateTimePicker
import io.github.alexzhirkevich.cupertino.CupertinoDateTimePickerState
import io.github.alexzhirkevich.cupertino.CupertinoDropdownMenu
import io.github.alexzhirkevich.cupertino.CupertinoIcon
import io.github.alexzhirkevich.cupertino.CupertinoIconButton
import io.github.alexzhirkevich.cupertino.CupertinoNavigationBar
import io.github.alexzhirkevich.cupertino.CupertinoNavigationBarItem
import io.github.alexzhirkevich.cupertino.CupertinoPicker
import io.github.alexzhirkevich.cupertino.CupertinoPickerState
import io.github.alexzhirkevich.cupertino.CupertinoRangeSlider
import io.github.alexzhirkevich.cupertino.CupertinoScaffold
import io.github.alexzhirkevich.cupertino.CupertinoSearchTextField
import io.github.alexzhirkevich.cupertino.CupertinoSearchTextFieldDefaults
import io.github.alexzhirkevich.cupertino.CupertinoSegmentedControl
import io.github.alexzhirkevich.cupertino.CupertinoSegmentedControlTab
import io.github.alexzhirkevich.cupertino.CupertinoSlider
import io.github.alexzhirkevich.cupertino.CupertinoSwitch
import io.github.alexzhirkevich.cupertino.CupertinoText
import io.github.alexzhirkevich.cupertino.CupertinoTimePicker
import io.github.alexzhirkevich.cupertino.CupertinoTimePickerState
import io.github.alexzhirkevich.cupertino.CupertinoTopAppBar
import io.github.alexzhirkevich.cupertino.ExperimentalCupertinoApi
import io.github.alexzhirkevich.cupertino.adaptive.icons.AdaptiveIcons
import io.github.alexzhirkevich.cupertino.adaptive.icons.Add
import io.github.alexzhirkevich.cupertino.adaptive.icons.Settings
import io.github.alexzhirkevich.cupertino.adaptive.icons.Share
import io.github.alexzhirkevich.cupertino.cancel
import io.github.alexzhirkevich.cupertino.default
import io.github.alexzhirkevich.cupertino.destructive
import io.github.alexzhirkevich.cupertino.icons.CupertinoIcons
import io.github.alexzhirkevich.cupertino.icons.outlined.*
import io.github.alexzhirkevich.cupertino.icons.filled.*
import io.github.alexzhirkevich.cupertino.isNavigationBarTransparent
import io.github.alexzhirkevich.cupertino.isTopBarTransparent
import io.github.alexzhirkevich.cupertino.rememberCupertinoBottomSheetScaffoldState
import io.github.alexzhirkevich.cupertino.rememberCupertinoDatePickerState
import io.github.alexzhirkevich.cupertino.rememberCupertinoDateTimePickerState
import io.github.alexzhirkevich.cupertino.rememberCupertinoPickerState
import io.github.alexzhirkevich.cupertino.rememberCupertinoSearchTextFieldState
import io.github.alexzhirkevich.cupertino.rememberCupertinoTimePickerState
import io.github.alexzhirkevich.cupertino.section.CupertinoLabelIcon
import io.github.alexzhirkevich.cupertino.section.CupertinoSectionDefaults
import io.github.alexzhirkevich.cupertino.section.SectionScope
import io.github.alexzhirkevich.cupertino.section.SectionStyle
import io.github.alexzhirkevich.cupertino.section.label
import io.github.alexzhirkevich.cupertino.section.section
import io.github.alexzhirkevich.cupertino.section.sectionContainerBackground
import io.github.alexzhirkevich.cupertino.section.sectionTitle
import io.github.alexzhirkevich.cupertino.section.switch
import io.github.alexzhirkevich.cupertino.theme.CupertinoColors
import io.github.alexzhirkevich.cupertino.theme.CupertinoTheme
import io.github.alexzhirkevich.cupertino.theme.SystemBlue
import io.github.alexzhirkevich.cupertino.theme.SystemGreen
import io.github.alexzhirkevich.cupertino.theme.SystemIndigo
import io.github.alexzhirkevich.cupertino.theme.SystemOrange
import io.github.alexzhirkevich.cupertino.theme.SystemPurple
import io.github.alexzhirkevich.cupertino.theme.SystemRed
import io.github.alexzhirkevich.cupertino.theme.systemBlue
import io.github.alexzhirkevich.cupertino.theme.systemGreen
import io.github.alexzhirkevich.cupertino.theme.systemIndigo
import io.github.alexzhirkevich.cupertino.theme.systemOrange
import io.github.alexzhirkevich.cupertino.theme.systemPurple
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


@OptIn(ExperimentalCupertinoApi::class)
@Composable
fun CupertinoWidgetsScreen(
    component: CupertinoWidgetsComponent
) {

    val lazyListState = rememberLazyListState()
    val sheetListState = rememberLazyListState()

    val scaffoldState = rememberCupertinoBottomSheetScaffoldState(
//        skipPartiallyExpanded = true,
        partialExpandFraction = .65f,
        collapseOnClickOutside = true,
    )

    val coroutineScope = rememberCoroutineScope()

    CupertinoBottomSheetScaffold(
        sheetTopBar = {
            CupertinoTopAppBar(
                title = {
                    Text("Bottom Sheet")
                },
                navigationIcon = {
                    CupertinoButton(
                        colors = CupertinoButtonDefaults.plainButtonColors(),
                        onClick = {
                            coroutineScope.launch {
                                scaffoldState.collapse()
                            }
                        }
                    ){
                        Text("Cancel")
                    }
                },
                isTransparent = sheetListState.isTopBarTransparent
            )
        },
        sheetContent = { pv ->

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                state = sheetListState,
                contentPadding = pv + CupertinoSectionDefaults.PaddingValues
            ) {
                items(300) {
                    Text(
                        text = "Sheet lazy list item $it",
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
            }
        },
        scaffoldState = scaffoldState,
        topBar = {
            CupertinoTopAppBar(
                isTransparent = lazyListState.isTopBarTransparent(
                    CupertinoSearchTextFieldDefaults.PaddingValues.calculateTopPadding()
                ),
                actions = {
                    CupertinoIconButton(
                        onClick = component::onThemeClicked
                    ) {
                        AnimatedContent(component.isDark.value) {
                            if (it) {
                                CupertinoIcon(
                                    imageVector = CupertinoIcons.Default.SunMax,
                                    contentDescription = null
                                )
                            } else {
                                CupertinoIcon(
                                    imageVector = CupertinoIcons.Default.MoonStars,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                },
                title = {
                    CupertinoText("Compose Cupertino")
                }
            )
        },
        bottomBar = {
            var tab by remember {
                mutableStateOf(0)
            }
            CupertinoNavigationBar(
                isTransparent = lazyListState.isNavigationBarTransparent,
            ) {
                CupertinoNavigationBarItem(
                    selected = tab == 0,
                    onClick = { tab = 0 },
                    icon = {
                        CupertinoIcon(
                            imageVector = CupertinoIcons.Filled.Person,
                            contentDescription = null
                        )
                    },
                    label = {
                        CupertinoText("Profile")
                    }
                )
                CupertinoNavigationBarItem(
                    selected = tab == 1,
                    onClick = { tab = 1 },
                    icon = {
                        CupertinoIcon(
                            imageVector = CupertinoIcons.Filled.Gearshape,
                            contentDescription = null
                        )
                    },
                    label = {
                        CupertinoText("Settings")
                    }
                )
            }
        }
    ) { pv ->

        val toggleState = remember {
            mutableStateOf(false)
        }

        val pickerValues = remember {
            listOf(
                "January", "February",
                "March", "April",
                "May", "June", "July", "August", "Semptember",
                "October", "November", "December"
            )
        }

        val pickerState = rememberCupertinoPickerState()
        val timePickerState = rememberCupertinoTimePickerState()
        val datePickerState = rememberCupertinoDatePickerState()
        val dateTimePickerState = rememberCupertinoDateTimePickerState()

        var selectedPickerTab by remember {
            mutableStateOf(0)
        }

        val searchState = rememberCupertinoSearchTextFieldState(
            scrollableState = lazyListState,
            blockScrollWhenFocusedAndEmpty = true
        )

        LazyColumn(
            state = lazyListState,
            contentPadding = pv,
            modifier = Modifier
                .fillMaxSize()
                .sectionContainerBackground()
                .nestedScroll(searchState.nestedScrollConnection)
        ) {

            item {
                var value by remember {
                    mutableStateOf("")
                }
                CupertinoSearchTextField(
                    value = value,
                    state = searchState,
                    onValueChange = {
                        value = it
                    }
                )
            }

            section {
                switch(
                    checked = component.isInvertLayoutDirection.value,
                    onCheckedChange = component::onInvertLayoutDirection
                ){
                    Text("Toggle layout direction")
                }
            }

            section(
                title = {
                    CupertinoText(
                        text = "Buttons".sectionTitle(),
                        modifier = Modifier.padding(it)
                    )
                }
            ) {
                buttons(onColorsChanged = component::onAccentColorChanged)
                switchAndProgressBar()
            }
//
            labelsWithIcons(
                onSheetClicked = {
                    coroutineScope.launch {
                        if (scaffoldState.skipPartiallyExpanded) {
                            scaffoldState.expand()
                        } else {
                            scaffoldState.partialExpand()
                        }
                    }
                },
                onNavigateToAdaptive = component::onNavigateToAdaptive,
                onNavigateToIcons = component::onNavigateToIcons,
            )

            section(
                title = {
                    CupertinoText(
                        modifier = Modifier.padding(it),
                        text = "Dialogs".sectionTitle(),
                    )
                },
                caption = {
                    CupertinoText(
                        text = "Native dialogs will use UIAlertController on iOS and Compose Cupertino analogs on other platforms",
                        modifier = Modifier.padding(it)
                    )
                }
            ) {
                dialogs()
                sheets()
                dropdown()
            }

            item {
                CupertinoSegmentedControl(
                    selectedTabIndex = selectedPickerTab,
                ) {
                    val tabs = listOf(
                        "Picker",
                        "Time",
                        "Date",
                        "Date & Time"
                    )

                    tabs.forEachIndexed { index, s ->
                        CupertinoSegmentedControlTab(
                            isSelected = index == selectedPickerTab,
                            onClick = {
                                selectedPickerTab = index
                            }
                        ) {
                            CupertinoText(s)
                        }
                    }
                }
            }


            when (selectedPickerTab) {
                0 -> picker(pickerValues, pickerState)
                1 -> timePicker(timePickerState)
                2 -> datePicker(datePickerState)
                3 -> dateTimePicker(dateTimePickerState)
            }

            sections(
                toggle = toggleState,
            )
        }
    }
}

@Composable
private operator fun PaddingValues.plus(other : PaddingValues) : PaddingValues{
    val layoutDirection = LocalLayoutDirection.current

    return PaddingValues(
        top = calculateTopPadding() + other.calculateTopPadding(),
        bottom = calculateBottomPadding() + other.calculateBottomPadding(),
        start = calculateStartPadding(layoutDirection) + other.calculateStartPadding(layoutDirection),
        end = calculateEndPadding(layoutDirection) + other.calculateEndPadding(layoutDirection)
    )
}

@OptIn(ExperimentalCupertinoApi::class)
fun LazyListScope.picker(
    pickerValues : List<String>, pickerState : CupertinoPickerState
) {
    section(
        title = {
            CupertinoText(
                modifier = Modifier.padding(it),
                text = "Wheel Pickers".sectionTitle()
            )
        },
        caption = {
            CupertinoText(
                modifier = Modifier.padding(it),
//                text = "Selected: ${pickerValues[pickerState.selectedItemIndex]}",

                //For infinite picker this should be used instead:

                text = "Selected: ${pickerValues[pickerState.selectedItemIndex(pickerValues.size)]}"
            )
        }
    ) {

        item {

            Column {
                CupertinoPicker(
                    state = pickerState,
                    items = pickerValues,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = CupertinoTheme.colorScheme.secondarySystemGroupedBackground
                ) {
                    CupertinoText(it)
                }
            }
        }
    }
}

fun LazyListScope.timePicker(
    state : CupertinoTimePickerState
){
    section(
        title = {
            CupertinoText(
                text = "Compose Time Picker".sectionTitle(),
                modifier = Modifier.padding(it)
            )
        },
        caption = {
            Text(
                modifier = Modifier.padding(it),
                text = "${state.hour} : ${state.minute}"
            )
        }
    ) {
        item {
            CupertinoTimePicker(
                modifier = Modifier.fillMaxWidth(),
                state = state
            )
        }
    }
}

fun LazyListScope.datePicker(
    state: CupertinoDatePickerState
){
    section(
        title = {
            CupertinoText(
                text = "Compose Date Picker".sectionTitle(),
                modifier = Modifier.padding(it)
            )
        },
        caption = {
            Text(
                modifier = Modifier.padding(it),
                text = Instant
                    .fromEpochMilliseconds(state.selectedDateMillis)
                    .toLocalDateTime(TimeZone.UTC)
                    .toString()
            )
        }
    ) {
        item {

            CupertinoDatePicker(
                modifier = Modifier.fillMaxWidth(),
                state = state
            )
        }
    }
}


@OptIn(ExperimentalCupertinoApi::class)
fun LazyListScope.dateTimePicker(
    state : CupertinoDateTimePickerState
){
    section(
        title = {
            CupertinoText(
                text = "Compose Date Time Picker".sectionTitle(),
                modifier = Modifier.padding(it)
            )
        },
        caption = {
            Text(
                modifier = Modifier.padding(it),
                text = Instant
                    .fromEpochMilliseconds(state.selectedDateTimeMillis)
                    .toLocalDateTime(TimeZone.UTC)
                    .toString()
            )
        }
    ) {
        item {
            CupertinoDateTimePicker(
                modifier = Modifier.fillMaxWidth(),
                state = state
            )
        }
    }
}



private fun SectionScope.switchAndProgressBar() {
    item { pv ->
        Row(
            modifier = Modifier.padding(pv),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            var active1 by remember {
                mutableStateOf(true)
            }

            var active2 by remember {
                mutableStateOf(false)
            }
            CupertinoSwitch(
                checked = active1,
                onCheckedChange = {
                    active1 = it
                }
            )
            CupertinoSwitch(
                checked = active2,
                onCheckedChange = {
                    active2 = it
                }
            )
            CupertinoSwitch(
                checked = true,
                enabled = false,
                onCheckedChange = {}
            )
            CupertinoSwitch(
                checked = false,
                enabled = false,
                onCheckedChange = {}
            )

            CupertinoActivityIndicator()
        }
    }

    item { pv ->
        Row(
            modifier = Modifier.padding(pv),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            var b by remember {
                mutableStateOf(.5f)
            }
            CupertinoSlider(
                modifier = Modifier.weight(1f),
                value = b,
                onValueChange = {
                    b = it
                }
            )

            Text(b.toString().take(4))
        }
    }

    item { pv ->
        Row(
            modifier = Modifier.padding(pv),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            var b by remember {
                mutableStateOf(0f..1f)
            }
            CupertinoRangeSlider(
                modifier = Modifier.weight(1f),
                value = b,
                steps = 9,
                onValueChange = {
                    b = it
                }
            )

            Text("${b.start.toString().take(4)} - ${b.endInclusive.toString().take(4)}")
        }
    }

//    item {
//        Box(
//            modifier = Modifier.padding(it),
//        ) {
//            var b by remember {
//                mutableStateOf(.5f)
//            }
//            CupertinoSlider(
//                value = b,
//                onValueChange = {
//                    b = it
//                },
//                steps = 5
//            )
//        }
//    }
//    item {
//        Box(
//            modifier = Modifier.padding(it),
//        ) {
//            var b by remember {
//                mutableStateOf(.25f..0.75f)
//            }
//            CupertinoRangeSlider(
//                value = b,
//                onValueChange = {
//                    b = it
//                },
//            )
//        }
//    }
//    item {
//        Box(
//            modifier = Modifier.padding(it),
//        ) {
//            var b by remember {
//                mutableStateOf(.25f..0.75f)
//            }
//            CupertinoRangeSlider(
//                value = b,
//                onValueChange = {
//                    b = it
//                },
//                steps = 5
//            )
//        }
//    }
}
private fun SectionScope.buttons(
    onColorsChanged : (light : Color, dark : Color) -> Unit
) {


    item {
        Row(
            modifier = Modifier.padding(it),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CupertinoIconButton(
                onClick = {
                    onColorsChanged(
                        CupertinoColors.systemBlue(false),
                        CupertinoColors.systemBlue(true)
                    )
                },
                colors = CupertinoButtonDefaults.tintedButtonColors(
                    contentColor = CupertinoColors.SystemBlue
                )
            ) {
                CupertinoIcon(
                    imageVector = CupertinoIcons.Default.Paintpalette,
                    contentDescription = null
                )
            }
            CupertinoIconButton(
                onClick = {
                    onColorsChanged(
                        CupertinoColors.systemGreen(false),
                        CupertinoColors.systemGreen(true)
                    )
                },
                colors = CupertinoButtonDefaults.tintedButtonColors(
                    contentColor = CupertinoColors.SystemGreen
                )
            ) {
                CupertinoIcon(
                        imageVector = CupertinoIcons.Default.Paintpalette,
                    contentDescription = null
                )
            }
            CupertinoIconButton(
                onClick = {
                    onColorsChanged(
                        CupertinoColors.systemPurple(false),
                        CupertinoColors.systemPurple(true)
                    )
                },
                colors = CupertinoButtonDefaults.tintedButtonColors(
                    contentColor = CupertinoColors.SystemPurple
                )
            ) {
                CupertinoIcon(
                        imageVector = CupertinoIcons.Default.Paintpalette,
                    contentDescription = null
                )
            }

            CupertinoIconButton(
                onClick = {
                    onColorsChanged(
                        CupertinoColors.systemOrange(false),
                        CupertinoColors.systemOrange(true)
                    )
                },
                colors = CupertinoButtonDefaults.tintedButtonColors(
                    contentColor = CupertinoColors.SystemOrange
                )
            ) {
                CupertinoIcon(
                        imageVector = CupertinoIcons.Default.Paintpalette,
                    contentDescription = null
                )
            }
            CupertinoIconButton(
                onClick = {
                    onColorsChanged(
                        CupertinoColors.systemIndigo(false),
                        CupertinoColors.systemIndigo(true)
                    )
                },
                colors = CupertinoButtonDefaults.tintedButtonColors(
                    contentColor = CupertinoColors.SystemIndigo
                )
            ) {
                CupertinoIcon(
                        imageVector = CupertinoIcons.Default.Paintpalette,
                    contentDescription = null
                )
            }
        }
    }


    item {
        Row(
            modifier = Modifier.padding(it),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CupertinoButton(
                colors = CupertinoButtonDefaults.plainButtonColors(),
                onClick = {}
            ) {
                CupertinoText("Plain")
            }
            CupertinoIconButton(
                onClick = {},
            ) {
                CupertinoIcon(
                    imageVector = AdaptiveIcons.Outlined.Share,
                    contentDescription = null
                )
            }
            CupertinoIconButton(
                onClick = {},
                colors = CupertinoButtonDefaults.filledButtonColors()
            ) {
                CupertinoIcon(
                    imageVector = AdaptiveIcons.Outlined.Add,
                    contentDescription = null
                )
            }
            CupertinoIconButton(
                onClick = {},
                colors = CupertinoButtonDefaults.grayButtonColors()
            ) {
                CupertinoIcon(
                    imageVector = AdaptiveIcons.Outlined.Settings,
                    contentDescription = null
                )
            }
            CupertinoIconButton(
                onClick = {},
                enabled = false,
            ) {
                CupertinoIcon(
                    imageVector = AdaptiveIcons.Outlined.Add,
                    contentDescription = null
                )
            }

        }
    }

    item {
        Row(
            modifier = Modifier.padding(it),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CupertinoButton(
                colors = CupertinoButtonDefaults.filledButtonColors(),
                onClick = {}
            ) {
                CupertinoText("Filled")
            }

            CupertinoButton(
                colors = CupertinoButtonDefaults.grayButtonColors(),
                onClick = {}
            ) {
                CupertinoText("Gray")
            }

            CupertinoButton(
                colors = CupertinoButtonDefaults.tintedButtonColors(),
                onClick = {}
            ) {
                CupertinoText("Tinted")
            }
        }
    }

    item {
        Row(
            modifier = Modifier.padding(it),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CupertinoButton(
                colors = CupertinoButtonDefaults.plainButtonColors(),
                onClick = {},
                enabled = false
            ) {
                CupertinoText("Plain Disabled")
            }

            CupertinoButton(
                colors = CupertinoButtonDefaults.filledButtonColors(),
                onClick = {},
                enabled = false
            ) {
                CupertinoText("Disabled")
            }
        }
    }

    item {
        Row(
            modifier = Modifier.padding(it),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CupertinoButton(
                colors = CupertinoButtonDefaults.filledButtonColors(),
                size = CupertinoButtonSize.Small,
                onClick = {}
            ) {
                CupertinoText("Small")
            }

            CupertinoButton(
                colors = CupertinoButtonDefaults.filledButtonColors(),
                size = CupertinoButtonSize.Medium,
                onClick = {}
            ) {
                CupertinoText("Medium")
            }

            CupertinoButton(
                colors = CupertinoButtonDefaults.filledButtonColors(),
                size = CupertinoButtonSize.Large,
                onClick = {}
            ) {
                CupertinoText("Large")
            }
        }
    }
}

private fun SectionScope.dialogs(){
    item {

        var alertVisible by remember {
            mutableStateOf(false)
        }
        var nativeAlertVisible by remember {
            mutableStateOf(false)
        }

        if (alertVisible) {
            CupertinoAlertDialog(
                onDismissRequest = {
                    alertVisible = false
                },
                title = {
                    CupertinoText("Alert Dialog")
                },
                message = {
                    CupertinoText("Alert dialog message")
                }
            ) {
                destructive(
                    onClick = {
                        alertVisible = false
                    }
                ) {
                    CupertinoText("Cancel")
                }
                default(
                    onClick = {
                        alertVisible = false
                    }
                ) {
                    CupertinoText("OK")
                }
            }
        }
        if (nativeAlertVisible) {
            CupertinoAlertDialogNative(
                onDismissRequest = {
                    nativeAlertVisible = false
                },
                title = "Alert Dialog",
                message = "Alert dialog message"
            ) {
                destructive(
                    onClick = {
                        nativeAlertVisible = false
                    },
                    title = "Cancel"
                )
                default(
                    onClick = {
                        nativeAlertVisible = false
                    },
                    title = "OK"
                )
            }
        }

        Row(
            modifier = Modifier.padding(it),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CupertinoButton(
                colors = CupertinoButtonDefaults.tintedButtonColors(),
                onClick = {
                    alertVisible = true
                }
            ) {
                CupertinoText("Alert")
            }
            CupertinoButton(
                colors = CupertinoButtonDefaults.tintedButtonColors(),
                onClick = {
                    nativeAlertVisible = true
                }
            ) {
                CupertinoText("Native Alert")
            }
        }
    }
}

private fun SectionScope.sheets(){
    item {

        var sheetVisible by remember {
            mutableStateOf(false)
        }
        var nativeSheetVisible by remember {
            mutableStateOf(false)
        }

            CupertinoActionSheet(
                visible = sheetVisible,
                onDismissRequest = {
                    sheetVisible = false
                },
                title = {
                    CupertinoText("Action Sheet")
                },
                message = {
                    CupertinoText("This is a message of the action sheet")
                },
            ) {
                default(
                    onClick = {
                        sheetVisible = false
                    }
                ) {
                    CupertinoText("OK")
                }
                destructive(
                    onClick = {
                        sheetVisible = false
                    }
                ) {
                    CupertinoText("Delete")
                }

                cancel(
                    onClick = {
                        sheetVisible = false
                    }
                ) {
                    CupertinoText("Cancel")
                }
            }
            CupertinoActionSheetNative(
                visible = nativeSheetVisible,
                onDismissRequest = {
                    nativeSheetVisible = false
                },
                title = "Action Sheet",
                message = "This is a message of the action sheet"
            ) {
                default(
                    onClick = {
                        nativeSheetVisible = false
                    },
                    title = "OK"
                )
                destructive(
                    onClick = {
                        nativeSheetVisible = false
                    },
                    title = "Delete"
                )

                cancel(
                    onClick = {
                        nativeSheetVisible = false
                    },
                    title = "Cancel"
                )
            }

        Row(
            modifier = Modifier.padding(it),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CupertinoButton(
                colors = CupertinoButtonDefaults.tintedButtonColors(),
                onClick = {
                    sheetVisible = true
                }
            ) {
                CupertinoText("Action Sheet")
            }
            CupertinoButton(
                colors = CupertinoButtonDefaults.tintedButtonColors(),
                onClick = {
                    nativeSheetVisible = true
                }
            ) {
                CupertinoText("Native Action Sheet")
            }
        }
    }
}


private fun SectionScope.dropdown() {


    item { pv ->

        var dropdownVisible by remember {
            mutableStateOf(false)
        }

        var pickerSheetVisible by remember {
            mutableStateOf(false)
        }
        CupertinoActionSheet(
            visible = pickerSheetVisible,
            onDismissRequest = {
                pickerSheetVisible = false
            },
            title = {
                CupertinoText("Cupertino Picker Sheet")
            },
            message = {
                CupertinoText("Pickers are the most used case for such sheets but you can place below any content you want")
            },
            buttons = {
                default(
                    onClick = {
                        pickerSheetVisible = false
                    },
                ) {
                    CupertinoText("Confirm")
                }
                cancel(
                    onClick = {
                        pickerSheetVisible = false
                    },
                ) {
                    CupertinoText("Cancel")
                }
            },
            content = {
                CupertinoDatePicker(
                    rememberCupertinoDatePickerState()
                )
            }
        )


        Row(
            modifier = Modifier.padding(pv),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            CupertinoButton(
                colors = CupertinoButtonDefaults.tintedButtonColors(),
                onClick = {
                    pickerSheetVisible = true
                }
            ) {
                CupertinoText("Picker Sheet")
            }

            //Menu bar should be in the box with anchor to align correctly
            Box {
                CupertinoButton(
                    onClick = {
                        dropdownVisible = !dropdownVisible
                    }
                ) {
                    CupertinoText("Dropdown")
                }

                CupertinoDropdownMenu(
                    expanded = dropdownVisible,
                    onDismissRequest = {
                        dropdownVisible = false
                    },
                    title = {
                        CupertinoText(
                            modifier = Modifier.padding(it),
                            text = "Menu"
                        )
                    }
                ) {
                    button(
                        onClick = {
                            dropdownVisible = false
                        },
                        icon = {
                            CupertinoIcon(
                                imageVector = CupertinoIcons.Default.SquareAndArrowUp,
                                contentDescription = null
                            )
                        }
                    ) {
                        CupertinoText("Share")
                    }
                    button(
                        onClick = {
                            dropdownVisible = false
                        },
                        icon = {
                            CupertinoIcon(
                                imageVector = CupertinoIcons.Default.Bookmark,
                                contentDescription = null
                            )
                        }
                    ) {
                        CupertinoText("Add to Favorites")
                    }
                    divider()
                    button(
                        onClick = {
                            dropdownVisible = false

                        },
                        contentColor = { CupertinoColors.SystemRed },
                        icon = {
                            CupertinoIcon(
                                imageVector = CupertinoIcons.Default.Trash,
                                contentDescription = null
                            )
                        }
                    ) {
                        CupertinoText("Delete")
                    }
                }
            }
        }
    }
}

private fun LazyListScope.labelsWithIcons(
    onSheetClicked : () -> Unit,
    onNavigateToAdaptive : () -> Unit,
    onNavigateToIcons : () -> Unit,
){
    section(
        style = SectionStyle.InsetGrouped,
        title = {
            CupertinoText(
                modifier = Modifier.padding(it),
                text = "Labels with icons".sectionTitle()
            )
        },
        caption = {
            CupertinoText(
                text = "Clickable labels with icons and adjusted separator padding",
                modifier = Modifier.padding(it)
            )
        }
    ){
        label(
            icon = {
                CupertinoLabelIcon(
                    imageVector = CupertinoIcons.Default.Heart,
                    contentDescription = null,
                    containerColor = CupertinoColors.SystemRed
                )
            },
            caption = {
                Text("Two")
            },
            onClick = onNavigateToIcons
        ) {
            CupertinoText("SF Symbols")
        }
        label(
            icon = {
                CupertinoLabelIcon(
                    imageVector = CupertinoIcons.Default.Iphone,
                    containerColor = CupertinoColors.SystemBlue
                )
            },
            caption = {
                Text("One")
            },
            onClick = onNavigateToAdaptive
        ) {
            CupertinoText("Adaptive Widgets")
        }

        label(
            icon = {
                CupertinoLabelIcon(
                    imageVector = CupertinoIcons.Default.Calendar,
                    contentDescription = null,
                    containerColor = CupertinoColors.SystemIndigo
                )
            },
            caption = {
                Text("Three")
            },
            onClick = onSheetClicked
        ) {
            CupertinoText("Bottom Sheet")
        }
    }
}
private fun LazyListScope.sections(
    toggle : MutableState<Boolean>,
){

    SectionStyle.values().forEach { style ->

        section(

            style = style,
            title = {
                CupertinoText(
                    modifier = Modifier.padding(it),
                    text = "Section ${style.name}".sectionTitle(),
                )
            },
            caption = {
                CupertinoText(
                    modifier = Modifier.padding(it),
                    text = "Sections can be used as a separate widget or as a part of a lazy list where each row is a separate lazy list item"
                )
            }
        ) {
            item {
                CupertinoText(
                    text = "Simple text",
                    modifier = Modifier.padding(it)
                )
            }
            label(
                onClick = {}
            ) {
                CupertinoText("Clickable label")
            }
            switch(
                checked = toggle.value,
                onCheckedChange = {
                    toggle.value = it
                }
            ) {
                CupertinoText("Toggle")
            }
        }
    }
}