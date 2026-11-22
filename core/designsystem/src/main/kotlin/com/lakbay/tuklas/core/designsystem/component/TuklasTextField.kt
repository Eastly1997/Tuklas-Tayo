package com.lakbay.tuklas.core.designsystem.component

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

enum class TuklasTextFieldVariant { FILLED, OUTLINED }

/** Controlled string input. Supply a meaningful label and explanatory supporting text for errors.
 * Modifier focus/semantics and keyboard actions remain available to callers without owning state here.
 */
@Composable
fun TuklasTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    variant: TuklasTextFieldVariant = TuklasTextFieldVariant.OUTLINED,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    singleLine: Boolean = false,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    when (variant) {
        TuklasTextFieldVariant.FILLED -> TextField(
            value, onValueChange, modifier, enabled, readOnly, label = label, placeholder = placeholder,
            leadingIcon = leadingIcon, trailingIcon = trailingIcon, supportingText = supportingText,
            isError = isError, singleLine = singleLine, keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
        )
        TuklasTextFieldVariant.OUTLINED -> OutlinedTextField(
            value, onValueChange, modifier, enabled, readOnly, label = label, placeholder = placeholder,
            leadingIcon = leadingIcon, trailingIcon = trailingIcon, supportingText = supportingText,
            isError = isError, singleLine = singleLine, keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
        )
    }
}
