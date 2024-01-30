/*
 * Copyright (c) 2023-2024. Compose Cupertino project and open source contributors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.github.alexzhirkevich.cupertino

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
@ExperimentalCupertinoApi
actual fun CupertinoDatePickerWheelNative(
    state: CupertinoDatePickerState,
    modifier: Modifier,
    height: Dp,
    containerColor : Color
) = CupertinoDatePickerWheel(
    state = state,
    modifier = modifier,
    height = height,
    containerColor = containerColor
)

@Composable
@ExperimentalCupertinoApi
actual fun CupertinoDatePickerPagerNative(
    state: CupertinoDatePickerState,
    modifier: Modifier,
    containerColor : Color
) = CupertinoDatePickerPager(
    state = state,
    modifier = modifier,
    containerColor = containerColor
)