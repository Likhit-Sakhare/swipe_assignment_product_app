package com.likhit.swipeassignment.presentation.product_add.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun CustomProductTextField(
    text: String,
    hint: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    var isFocused by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .padding(vertical = 8.dp)
            .padding(end = 16.dp)
    ){
        if(text.isEmpty() && !isFocused){
            Text(
                text = hint,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            modifier = Modifier
                .onFocusChanged {
                    isFocused = it.isFocused
                },
            textStyle = TextStyle(
                color = if(isSystemInDarkTheme()) Color.White else Color.Black
            ),
            cursorBrush = if(isSystemInDarkTheme()) SolidColor(Color.White) else SolidColor(Color.Black),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
        )
    }
}