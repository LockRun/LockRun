package com.tteoli.lockrunapp.ui_components.buttons

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    @StringRes id: Int? = null,
    text: String = "",
    onClick: () -> Unit
){
    Box(
        modifier = modifier.fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .background(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color(0xFFD8F3EC),
                        Color(0xFFE8F4EA),
                        Color(0xFFCDE4FB),
                        Color(0xFFC5CBF8),
                        Color(0xFFC9E7F4),
                        Color(0xFFEBEDEC),
                        Color(0xFFC5D7F9),
                        Color(0xFFB9C7F5)
                    )
                )
            )
            .clickable(

            ) { onClick() }
            .padding(vertical = 14.dp, horizontal = 40.dp),
        contentAlignment = Alignment.Center

    ){
        Text(
            text = text,
            color = Color.Black,
            fontSize = 24.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PrimaryButtonPreview(){

    PrimaryButton(text = "안녕하세요"){

    }
}