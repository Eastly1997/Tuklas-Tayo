package com.lakbay.tuklas.core.designsystem.component

import androidx.compose.ui.res.stringResource

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.lakbay.tuklas.core.designsystem.R

/**
 * Controlled search input. [onClear] overrides the default onQueryChange("") action.
 * Clearing retains input focus; the Search IME action dismisses the keyboard.
 * 56dp is the minimum height so large accessibility fonts can grow naturally.
 */
@Composable
fun TuklasSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = TuklasSearchDefaults.placeholder,
    enabled: Boolean = true,
    onClear: (() -> Unit)? = null,
) {
    val interactions = remember { MutableInteractionSource() }
    val focused by interactions.collectIsFocusedAsState()
    val keyboard = LocalSoftwareKeyboardController.current
    val colors = MaterialTheme.colorScheme
    // A thin, transient pill ring plus the visible caret distinguish keyboard focus.
    val focusModifier = if (enabled && focused) {
        Modifier.border(1.dp, colors.primary.copy(alpha = 0.5f), TuklasSearchDefaults.shape)
    } else Modifier
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.heightIn(min = TuklasSearchDefaults.minHeight).then(focusModifier)
            .semantics { contentDescription = placeholder },
        enabled = enabled,
        singleLine = true,
        shape = TuklasSearchDefaults.shape,
        textStyle = TuklasSearchDefaults.textStyle,
        placeholder = { Text(placeholder, style = TuklasSearchDefaults.textStyle) },
        leadingIcon = {
            Icon(painterResource(R.drawable.ic_search), contentDescription = null, modifier = Modifier.size(TuklasSearchDefaults.iconSize))
        },
        trailingIcon = if (query.isNotEmpty()) {
            {
                IconButton(
                    onClick = { if (onClear != null) onClear() else onQueryChange("") },
                    enabled = enabled,
                ) {
                    Icon(painterResource(R.drawable.ic_clear), contentDescription = stringResource(R.string.search_clear),
                        modifier = Modifier.size(TuklasSearchDefaults.iconSize))
                }
            }
        } else null,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { keyboard?.hide() }),
        interactionSource = interactions,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = TuklasSearchDefaults.containerColor,
            unfocusedContainerColor = TuklasSearchDefaults.containerColor,
            disabledContainerColor = TuklasSearchDefaults.containerColor.copy(alpha = 0.38f),
            focusedTextColor = colors.onSurface,
            unfocusedTextColor = colors.onSurface,
            focusedPlaceholderColor = colors.onSurfaceVariant,
            unfocusedPlaceholderColor = colors.onSurfaceVariant,
            focusedLeadingIconColor = colors.onSurfaceVariant,
            unfocusedLeadingIconColor = colors.onSurfaceVariant,
            focusedTrailingIconColor = colors.onSurfaceVariant,
            unfocusedTrailingIconColor = colors.onSurfaceVariant,
            cursorColor = colors.primary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
        ),
    )
}