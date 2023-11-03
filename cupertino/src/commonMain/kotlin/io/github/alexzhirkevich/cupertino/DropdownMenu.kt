/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.alexzhirkevich.cupertino

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitDragOrCancellation
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import io.github.alexzhirkevich.LocalContentColor
import io.github.alexzhirkevich.cupertino.section.CupertinoSectionDefaults
import io.github.alexzhirkevich.cupertino.section.Draw
import io.github.alexzhirkevich.cupertino.section.SectionScopeImpl
import io.github.alexzhirkevich.cupertino.section.SectionTokens
import io.github.alexzhirkevich.cupertino.theme.CupertinoColors
import io.github.alexzhirkevich.cupertino.theme.CupertinoTheme
import io.github.alexzhirkevich.cupertino.theme.gray
import io.github.alexzhirkevich.cupertino.theme.isDark
import io.github.alexzhirkevich.cupertino.theme.systemGray
import io.github.alexzhirkevich.cupertino.theme.systemGray2
import io.github.alexzhirkevich.cupertino.theme.systemGray3
import io.github.alexzhirkevich.cupertino.theme.systemGray4
import io.github.alexzhirkevich.cupertino.theme.systemGray5
import io.github.alexzhirkevich.cupertino.theme.systemGray6
import io.github.alexzhirkevich.cupertino.theme.systemGray7
import io.github.alexzhirkevich.cupertino.theme.systemGray8
import kotlin.math.max
import kotlin.math.min


@Composable
@ExperimentalCupertinoApi
fun CupertinoDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    scrollState: ScrollState = rememberScrollState(),
    properties: PopupProperties = PopupProperties(focusable = true),
    title: (@Composable (PaddingValues) -> Unit)? = null,
    content: CupertinoDropdownMenuScope.() -> Unit
) {

    val expandedStates = remember { MutableTransitionState(false) }
    expandedStates.targetState = expanded

    if (expandedStates.currentState || expandedStates.targetState) {
        val transformOriginState = remember { mutableStateOf(TransformOrigin.Center) }
        val density = LocalDensity.current
        val popupPositionProvider = DropdownMenuPositionProvider(
            offset,
            density
        ) { parentBounds, menuBounds ->
            transformOriginState.value = calculateTransformOrigin(parentBounds, menuBounds)
        }

        Popup(
            onDismissRequest = onDismissRequest,
            popupPositionProvider = popupPositionProvider,
            properties = properties
        ) {
            DropdownMenuContent(
                expandedStates = expandedStates,
                transformOriginState = transformOriginState,
                scrollState = scrollState,
                modifier = modifier,
                title = title,
                content = content
            )
        }
    }
}

interface CupertinoDropdownMenuScope {

    /**
     * Plain section item withoud additional controls
     * */
    fun item(
        key: Any? = null,
        minHeight : Dp = MenuDefaults.ItemMinHeight,
        hasDivider : Boolean = true,
        content: @Composable (padding : PaddingValues) -> Unit
    )

    fun button(
        onClick: () -> Unit,
        key: Any? = null,
        enabled: Boolean = true,
        contentColor : @Composable () -> Color = { Color.Unspecified },
        icon: (@Composable () -> Unit) = {},
        caption : @Composable () -> Unit = {},
        title: @Composable () -> Unit,
    )

    fun divider()
}


internal class CupertinoDropdownMenuScopeImpl : CupertinoDropdownMenuScope {

    val delegate = SectionScopeImpl()

    override fun item(
        key: Any?,
        minHeight : Dp,
        hasDivider : Boolean,
        content: @Composable (padding: PaddingValues) -> Unit
    ) {
        delegate.item(
            key = key,
            dividerPadding = if (hasDivider) 0.dp else null,
            minHeight = minHeight,
            content = content,
        )
    }

    override fun button(
        onClick: () -> Unit,
        key: Any?,
        enabled: Boolean,
        contentColor : @Composable () -> Color ,
        icon: @Composable (() -> Unit),
        caption: @Composable () -> Unit,
        title: @Composable () -> Unit
    ) {
        item(key = key) {
            val color = contentColor().takeIf { it.isSpecified } ?: LocalContentColor.current
            CompositionLocalProvider(LocalContentColor provides color) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .heightIn(min = SectionTokens.MinHeight)
                        .fillMaxWidth()
                        .clickable(
                            enabled = enabled,
                            onClick = onClick,
                            role = Role.DropdownList,
                        )
                        .padding(it)
                ) {


                    title()
                    Spacer(Modifier.weight(1f))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(SectionTokens.SplitPadding)
                    ) {
                        caption.invoke()
                        icon.invoke()
                    }
                }
            }
        }
    }

    override fun divider() {
        delegate.item(
            minHeight = MenuDefaults.DividerHeight,
            dividerPadding = null
        ) {
            Spacer(
                modifier = Modifier
                    .height(MenuDefaults.DividerHeight)
                    .fillMaxWidth()
                    .background(MenuDefaults.dividerColor())
            )
        }
    }
}

@Composable
internal fun DropdownMenuContent(
    expandedStates: MutableTransitionState<Boolean>,
    transformOriginState: MutableState<TransformOrigin>,
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
    title: (@Composable (PaddingValues) -> Unit)?,
    content: CupertinoDropdownMenuScope.() -> Unit
) {
    // Menu open/close animation.
    val transition = updateTransition(expandedStates, "DropDownMenu")

    val scale by transition.animateFloat(
        transitionSpec = {
            if (false isTransitioningTo true) {
                // Dismissed to expanded
                tween(
                    durationMillis = InTransitionDuration,
                    easing = LinearOutSlowInEasing
                )
            } else {
                // Expanded to dismissed.
                tween(
                    durationMillis = 1,
                    delayMillis = OutTransitionDuration - 1
                )
            }
        }
    ) {
        if (it) {
            // Menu is expanded.
            1f
        } else {
            // Menu is dismissed.
            0.8f
        }
    }

    val alpha by transition.animateFloat(
        transitionSpec = {
            if (false isTransitioningTo true) {
                // Dismissed to expanded
                tween(durationMillis = 30)
            } else {
                // Expanded to dismissed.
                tween(durationMillis = OutTransitionDuration)
            }
        }
    ) {
        if (it) {
            // Menu is expanded.
            1f
        } else {
            // Menu is dismissed.
            0f
        }
    }

    val shape = MenuDefaults.shape


    Surface(
        modifier = Modifier
            .padding(vertical = MenuDefaults.VerticalPadding)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
                transformOrigin = transformOriginState.value
                this.shape = shape
                clip = true
                shadowElevation = MenuDefaults.Elevation.toPx()
            }.width(MenuDefaults.ItemWidth),
        color = MenuDefaults.containerColor(),
    ) {
        val scope = remember(content) { CupertinoDropdownMenuScopeImpl().apply(content) }

        Column(
            modifier = modifier
                .width(IntrinsicSize.Max)
                .verticalScroll(scrollState),
        ){
            if (title != null){
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    ProvideTextStyle(
                        CupertinoTheme.typography.footnote.copy(
                            textAlign = TextAlign.Center,
                        )
                    ) {
                        CompositionLocalProvider(
                            LocalContentColor provides CupertinoTheme.colorScheme.tertiaryLabel
                        ) {
                            title(MenuDefaults.TitlePadding)
                        }
                    }
                }
                Separator()
            }
            ProvideTextStyle(
                CupertinoTheme.typography.body.copy(
                    fontWeight = FontWeight.Light
                )
            ) {
                scope.delegate.Draw()
            }
        }
    }
}

//@Composable
//internal fun DropdownMenuItemContent(
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier,
//    enabled: Boolean = true,
//    contentPadding: PaddingValues = MenuDefaults.DropdownMenuItemContentPadding,
//    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
//    content: @Composable RowScope.() -> Unit
//) {
//    // TODO(popam, b/156911853): investigate replacing this Row with ListItem
//    Row(
//        modifier = modifier
//            .clickable(
//                enabled = enabled,
//                onClick = onClick,
//                indication = LocalIndication.current,
//                interactionSource = interactionSource,
//            )
//            .fillMaxWidth()
//            // Preferred min and max width used during the intrinsic measurement.
//            .sizeIn(
//                minWidth = DropdownMenuItemDefaultMinWidth,
//                maxWidth = DropdownMenuItemDefaultMaxWidth,
//                minHeight = DropdownMenuItemDefaultMinHeight
//            )
//            .padding(contentPadding),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        val typography = CupertinoTheme.typography
//        ProvideTextStyle(typography.body) {
//            val contentAlpha = if (enabled) 1f else CupertinoButtonDefaults.PressedPlainButonAlpha
//
//            CompositionLocalProvider(LocalContentColor provides LocalContentColor.current.copy(
//                alpha = contentAlpha
//            )) {
//                content()
//            }
//        }
//    }
//}

/**
 * Contains default values used for [DropdownMenuItem].
 */
object MenuDefaults {



    val ItemMinHeight = SectionTokens.MinHeight
    val ItemWidth = 270.dp

    val DividerHeight = 6.dp

    val Elevation = .5.dp

    val VerticalPadding = 6.dp

    val TitlePadding = PaddingValues(8.dp)

    val shape : CornerBasedShape
        @Composable get() = CupertinoSectionDefaults.Shape
    /**
     * Default padding used for [DropdownMenuItem].
     */
    val DropdownMenuItemContentPadding = PaddingValues(
        horizontal = DropdownMenuItemHorizontalPadding,
        vertical = 0.dp
    )

    @Composable
    fun containerColor(): Color = CupertinoColors.systemGray7

    @Composable
    fun dividerColor(): Color = if (isDark())
        CupertinoColors.systemGray6 else  CupertinoColors.systemGray4
//    = CupertinoTheme.colorScheme.secondarySystemGroupedBackground
}

// Size defaults.
internal val MenuVerticalMargin = 48.dp
private val DropdownMenuItemHorizontalPadding = 16.dp

// Menu open/close animation.
internal const val InTransitionDuration = 120
internal const val OutTransitionDuration = 75

internal fun calculateTransformOrigin(
    parentBounds: IntRect,
    menuBounds: IntRect
): TransformOrigin {
    val pivotX = when {
        menuBounds.left >= parentBounds.right -> 0f
        menuBounds.right <= parentBounds.left -> 1f
        menuBounds.width == 0 -> 0f
        else -> {
            val intersectionCenter =
                (
                        max(parentBounds.left, menuBounds.left) +
                                min(parentBounds.right, menuBounds.right)
                        ) / 2
            (intersectionCenter - menuBounds.left).toFloat() / menuBounds.width
        }
    }
    val pivotY = when {
        menuBounds.top >= parentBounds.bottom -> 0f
        menuBounds.bottom <= parentBounds.top -> 1f
        menuBounds.height == 0 -> 0f
        else -> {
            val intersectionCenter =
                (
                        max(parentBounds.top, menuBounds.top) +
                                min(parentBounds.bottom, menuBounds.bottom)
                        ) / 2
            (intersectionCenter - menuBounds.top).toFloat() / menuBounds.height
        }
    }
    return TransformOrigin(pivotX, pivotY)
}

// Menu positioning.

/**
 * Calculates the position of a Material [DropdownMenu].
 */
@Immutable
internal data class DropdownMenuPositionProvider(
    val contentOffset: DpOffset,
    val density: Density,
    val onPositionCalculated: (IntRect, IntRect) -> Unit = { _, _ -> }
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        // The min margin above and below the menu, relative to the screen.
        val verticalMargin = with(density) { MenuVerticalMargin.roundToPx() }
        // The content offset specified using the dropdown offset parameter.
        val contentOffsetX = with(density) {
            contentOffset.x.roundToPx() * (if (layoutDirection == LayoutDirection.Ltr) 1 else -1)
        }
        val contentOffsetY = with(density) { contentOffset.y.roundToPx() }

        // Compute horizontal position.
        val leftToAnchorLeft = anchorBounds.left + contentOffsetX
        val rightToAnchorRight = anchorBounds.right - popupContentSize.width + contentOffsetX
        val rightToWindowRight = windowSize.width - popupContentSize.width
        val leftToWindowLeft = 0
        val x = if (layoutDirection == LayoutDirection.Ltr) {
            sequenceOf(
                leftToAnchorLeft,
                rightToAnchorRight,
                // If the anchor gets outside of the window on the left, we want to position
                // toDisplayLeft for proximity to the anchor. Otherwise, toDisplayRight.
                if (anchorBounds.left >= 0) rightToWindowRight else leftToWindowLeft
            )
        } else {
            sequenceOf(
                rightToAnchorRight,
                leftToAnchorLeft,
                // If the anchor gets outside of the window on the right, we want to position
                // toDisplayRight for proximity to the anchor. Otherwise, toDisplayLeft.
                if (anchorBounds.right <= windowSize.width) leftToWindowLeft else rightToWindowRight
            )
        }.firstOrNull {
            it >= 0 && it + popupContentSize.width <= windowSize.width
        } ?: rightToAnchorRight

        // Compute vertical position.
        val topToAnchorBottom = maxOf(anchorBounds.bottom + contentOffsetY, verticalMargin)
        val bottomToAnchorTop = anchorBounds.top - popupContentSize.height + contentOffsetY
        val centerToAnchorTop = anchorBounds.top - popupContentSize.height / 2 + contentOffsetY
        val bottomToWindowBottom = windowSize.height - popupContentSize.height - verticalMargin
        val y = sequenceOf(
            topToAnchorBottom,
            bottomToAnchorTop,
            centerToAnchorTop,
            bottomToWindowBottom
        ).firstOrNull {
            it >= verticalMargin &&
                    it + popupContentSize.height <= windowSize.height - verticalMargin
        } ?: bottomToAnchorTop

        onPositionCalculated(
            anchorBounds,
            IntRect(x, y, x + popupContentSize.width, y + popupContentSize.height)
        )
        return IntOffset(x, y)
    }
}