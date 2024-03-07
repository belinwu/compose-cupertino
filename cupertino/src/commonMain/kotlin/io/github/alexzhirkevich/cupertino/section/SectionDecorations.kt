package io.github.alexzhirkevich.cupertino.section

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.LocalContentColor
import io.github.alexzhirkevich.cupertino.CupertinoDivider
import io.github.alexzhirkevich.cupertino.CupertinoIcon
import io.github.alexzhirkevich.cupertino.CupertinoIconDefaults
import io.github.alexzhirkevich.cupertino.ProvideTextStyle
import io.github.alexzhirkevich.cupertino.icons.CupertinoIcons
import io.github.alexzhirkevich.cupertino.icons.outlined.ChevronBackward
import io.github.alexzhirkevich.cupertino.icons.outlined.ChevronForward
import io.github.alexzhirkevich.cupertino.theme.CupertinoTheme

@Composable
internal fun SectionTitle(
    style: SectionStyle,
    state: SectionState?,
    lazy : Boolean,
    content: @Composable () -> Unit
) {

    val additionalPadding = when {
        !lazy -> PaddingValues(0.dp)
        style == SectionStyle.InsetGrouped -> PaddingValues(
            horizontal = CupertinoSectionTokens.HorizontalPadding
        )
        style == SectionStyle.Sidebar -> PaddingValues(
            end = CupertinoSectionTokens.HorizontalPadding
        )
        else -> PaddingValues(0.dp)
    }

    val basePadding = PaddingValues(
        start = if (!lazy && style == SectionStyle.Sidebar )
            0.dp
        else CupertinoSectionTokens.HorizontalPadding,
        end = CupertinoSectionTokens.HorizontalPadding,
        bottom = if (style == SectionStyle.Sidebar)
            CupertinoSectionTokens.InlinePadding * 2
        else CupertinoSectionTokens.InlinePadding,
    )

    CompositionLocalProvider(
        LocalContentColor provides CupertinoSectionDefaults.titleColor(style),
        LocalSectionStyle provides style
    ) {
        val tapModifier = if (state != null && style == SectionStyle.Sidebar && state.canCollapse)
            Modifier
                .fillMaxWidth()
                .clickable(
                    onClickLabel = if (state.isCollapsed) "Expand" else "Hide",
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = state::toggle,
                    indication = null
                )
        else Modifier

        ProvideTextStyle(CupertinoSectionDefaults.titleTextStyle(style)) {
            Row(
                modifier = tapModifier
                    .padding(basePadding)
                    .padding(additionalPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                content()

                if (style == SectionStyle.Sidebar && state != null && state.canCollapse) {

                    val isLtr = LocalLayoutDirection.current == LayoutDirection.Ltr

                    val rotation by animateFloatAsState(
                        targetValue = when {
                            state.isCollapsed -> 0f
                            isLtr -> 90f
                            else -> -90f
                        },
                        animationSpec = spring(
                            stiffness = Spring.StiffnessMediumLow,
                        )
                    )

                    CupertinoIcon(
                        imageVector = if (isLtr)
                            CupertinoIcons.Default.ChevronForward
                        else CupertinoIcons.Default.ChevronBackward,
                        contentDescription = "Collapse",
                        modifier = Modifier
                            .size(CupertinoIconDefaults.SmallSize)
                            .graphicsLayer {
                                rotationZ = rotation
                            },
                        tint = CupertinoTheme.colorScheme.accent
                    )
                }
            }
        }
    }
}

@Composable
internal fun SectionCaption(
    style: SectionStyle,
    lazy : Boolean,
    content: @Composable () -> Unit
) {

    val addCorner = if (style.inset && style.grouped && lazy)
        CupertinoSectionTokens.HorizontalPadding else 0.dp

    CompositionLocalProvider(
        LocalContentColor provides CupertinoSectionDefaults.captionColor(style),
        LocalSectionStyle provides style
    ) {
        ProvideTextStyle(CupertinoSectionDefaults.captionTextStyle(style)) {
            Box(
                Modifier.padding(
                    PaddingValues(
                        horizontal = CupertinoSectionTokens.HorizontalPadding + addCorner,
                        vertical = CupertinoSectionTokens.InlinePadding
                    )
                )
            ) {
                content()
            }
        }
    }
}

@Composable
internal fun SectionDivider(
    style: SectionStyle,
    modifier: Modifier = Modifier
) {
    if (style.inset && style.grouped)
        return

    CupertinoDivider(
        modifier = modifier.padding(
            start = if (style.grouped)
                0.dp else CupertinoSectionTokens.HorizontalPadding
        )
    )
}
