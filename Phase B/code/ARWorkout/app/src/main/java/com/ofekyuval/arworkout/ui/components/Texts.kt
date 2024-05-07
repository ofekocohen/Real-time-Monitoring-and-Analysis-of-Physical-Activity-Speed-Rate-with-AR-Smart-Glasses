package com.ofekyuval.arworkout.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun MyTitle(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        style = TextStyle(
            fontWeight = FontWeight.Black,
            fontSize = 32.sp
        )
    )
}

@Composable
fun MySubTitle(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    )
}

@Composable
fun MyDescription(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier.alpha(0.4f),
        text = text
    )
}

@Composable
fun MyButton(modifier: Modifier = Modifier, text: String, color: Color = Color.Black, imageVector: ImageVector? = null, onClick: ()->Unit) {
    OutlinedButton(
        modifier = modifier,
        border = BorderStroke(1.dp, color),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        onClick = onClick
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (imageVector != null) {
                Icon(imageVector = imageVector, contentDescription = "Icon", tint = color)
            }
            Text(
                text = text,
                style = TextStyle(
                    color = color,
                    fontSize = 18.sp
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MyTitlePrev() {
    MyTitle(text = "MyTitle")
}

@Preview(showBackground = true)
@Composable
private fun MySubTitlePrev() {
    MySubTitle(text = "MySubTitle")
}

@Preview(showBackground = true)
@Composable
private fun MyButtonPrev() {
    MyButton(text = "MyButton") {}
}
