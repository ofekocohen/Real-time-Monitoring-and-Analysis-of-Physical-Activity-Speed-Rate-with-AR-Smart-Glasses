package com.ofekyuval.arworkout.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ofekyuval.arworkout.Constants
import com.ofekyuval.arworkout.R
import com.ofekyuval.arworkout.glasses.workout.Workout

@Composable
fun MyCard(
    modifier: Modifier = Modifier,
    containerColor: Color = Color.White,
    onClick: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            content = content
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun MyCardPrev() {
    MyCard {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "Hello World"
        )
    }
}


@Composable
fun WorkoutCard(
    modifier: Modifier = Modifier,
    workout: Workout,
    imageVector: ImageVector,
    descText: String,
    buttonText: String,
    buttonColor: Color = Color.Black,
    containerColor: Color = Color.White,
    onClick: () -> Unit
) {
    val imgData = workout.getImageData()
    MyCard(
        modifier = modifier.height(150.dp),
        onClick = onClick,
        containerColor = containerColor
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .scale(imgData.scale)
                .offset(x = imgData.xOffset, y = imgData.yOffset),
            painter = painterResource(id = workout.img),
            contentDescription = null
        )
        Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
            MySubTitle(text = workout.name)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(
                    modifier = Modifier.alpha(0.4f),
                    imageVector = imageVector,
                    contentDescription = null
                )
                MyDescription(text = descText)
            }
        }
        MyButton(
            modifier = Modifier.align(Alignment.BottomStart),
            text = buttonText,
            color = buttonColor,
            onClick = onClick
        )
    }
}

private data class ImageData(val scale: Float, val xOffset: Dp, val yOffset: Dp)
private fun Workout.getImageData(): ImageData {
    return when (img) {
        R.drawable.girl -> ImageData(scale = 1.5f, xOffset = 0.dp, yOffset = 10.dp)
        R.drawable.boy -> ImageData(scale = 2.5f, xOffset = -13.dp, yOffset = 30.dp)
        else -> ImageData(scale = 1f, xOffset = 0.dp, yOffset = 0.dp)
    }
}

@Preview
@Composable
private fun WorkoutCardPrev() {
    WorkoutCard(
        workout = Constants.availableWorkouts.first(),
        imageVector = Icons.Default.QuestionMark,
        descText = "Desc. Text",
        buttonText = "Button Text"
    ) {}
}