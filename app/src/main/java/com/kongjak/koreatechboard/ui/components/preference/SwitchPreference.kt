package com.kongjak.koreatechboard.ui.components.preference

import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import com.kongjak.koreatechboard.ui.common.NoRippleInteractionSource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwitchPreference(
    modifier: Modifier = Modifier,
    title: String,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val coroutineScope = rememberCoroutineScope()
    var componentWidth = 0
    var componentHeight = 0

    BasicPreference(
        modifier = modifier
            .indication(interactionSource, rememberRipple())
            .onGloballyPositioned {
                componentWidth = it.size.width
                componentHeight = it.size.height
            },
        title = title,
        onClick = {
            if (enabled) onCheckedChange(!checked)
        },
        content = {
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                Switch(
                    checked = checked,
                    enabled = enabled,
                    interactionSource = NoRippleInteractionSource(),
                    onCheckedChange = {
                        onCheckedChange(it)
                        coroutineScope.launch {
                            val centerX = componentWidth
                            val centerY = componentHeight / 2
                            val press = PressInteraction.Press(
                                Offset(centerX.toFloat(), centerY.toFloat())
                            )
                            interactionSource.emit(press)
                            interactionSource.emit(PressInteraction.Release(press))
                        }
                    }
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwitchPreference(
    modifier: Modifier = Modifier,
    title: String,
    summary: String,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val coroutineScope = rememberCoroutineScope()
    var componentWidth = 0
    var componentHeight = 0

    BasicPreference(
        modifier = modifier
            .indication(interactionSource, rememberRipple())
            .onGloballyPositioned {
                componentWidth = it.size.width
                componentHeight = it.size.height
            },
        title = title,
        summary = summary,
        onClick = {
            if (enabled) onCheckedChange(!checked)
        },
        content = {
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                Switch(
                    checked = checked,
                    enabled = enabled,
                    interactionSource = NoRippleInteractionSource(),
                    onCheckedChange = {
                        onCheckedChange(it)
                        coroutineScope.launch {
                            val centerX = componentWidth
                            val centerY = componentHeight / 2
                            val press = PressInteraction.Press(
                                Offset(centerX.toFloat(), centerY.toFloat())
                            )
                            interactionSource.emit(press)
                            interactionSource.emit(PressInteraction.Release(press))
                        }
                    }
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwitchPreference(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val coroutineScope = rememberCoroutineScope()
    var componentWidth = 0
    var componentHeight = 0

    BasicPreference(
        modifier = modifier
            .indication(interactionSource, rememberRipple())
            .onGloballyPositioned {
                componentWidth = it.size.width
                componentHeight = it.size.height
            },
        icon = icon,
        title = title,
        onClick = {
            if (enabled) onCheckedChange(!checked)
        },
        content = {
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                Switch(
                    checked = checked,
                    enabled = enabled,
                    interactionSource = NoRippleInteractionSource(),
                    onCheckedChange = {
                        onCheckedChange(it)
                        coroutineScope.launch {
                            val centerX = componentWidth
                            val centerY = componentHeight / 2
                            val press = PressInteraction.Press(
                                Offset(centerX.toFloat(), centerY.toFloat())
                            )
                            interactionSource.emit(press)
                            interactionSource.emit(PressInteraction.Release(press))
                        }
                    }
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwitchPreference(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    summary: String,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val coroutineScope = rememberCoroutineScope()
    var componentWidth = 0
    var componentHeight = 0

    BasicPreference(
        modifier = modifier
            .indication(interactionSource, rememberRipple())
            .onGloballyPositioned {
                componentWidth = it.size.width
                componentHeight = it.size.height
            },
        icon = icon,
        title = title,
        summary = summary,
        onClick = {
            if (enabled) onCheckedChange(!checked)
        },
        content = {
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                Switch(
                    checked = checked,
                    enabled = enabled,
                    interactionSource = NoRippleInteractionSource(),
                    onCheckedChange = {
                        onCheckedChange(it)
                        coroutineScope.launch {
                            val centerX = componentWidth
                            val centerY = componentHeight / 2
                            val press = PressInteraction.Press(
                                Offset(centerX.toFloat(), centerY.toFloat())
                            )
                            interactionSource.emit(press)
                            interactionSource.emit(PressInteraction.Release(press))
                        }
                    }
                )
            }
        }
    )
}

@Preview
@Composable
fun SwitchPreferencePreview() {
    SwitchPreference(modifier = Modifier, title = "Title", summary = "Summary", checked = false) {
    }
}
