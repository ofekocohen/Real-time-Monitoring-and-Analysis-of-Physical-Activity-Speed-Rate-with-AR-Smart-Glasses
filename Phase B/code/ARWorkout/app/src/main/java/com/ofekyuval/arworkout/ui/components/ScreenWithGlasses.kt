package com.ofekyuval.arworkout.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ScreenWithGlasses(modifier: Modifier = Modifier, spacedBy: Dp = 40.dp, topPadding: Dp = 40.dp, bottomPadding: Dp = 0.dp, content: @Composable ColumnScope.() -> Unit) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = topPadding, bottom = bottomPadding),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.8f),
            verticalArrangement = Arrangement.spacedBy(spacedBy),
        ) {
            GlassesInfo()
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenWithGlassesPrev() {
    ScreenWithGlasses {
        Text(text = "Hello World")
    }
}