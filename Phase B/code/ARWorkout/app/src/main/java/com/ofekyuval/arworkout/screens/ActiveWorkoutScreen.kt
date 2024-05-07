package com.ofekyuval.arworkout.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.everysight.evskit.android.Evs
import com.everysight.evskit.android.compose.viewmodels.EvsGlassesViewModel
import com.ofekyuval.arworkout.R
import com.ofekyuval.arworkout.glasses.workout.Workout
import com.ofekyuval.arworkout.ui.components.MyDescription
import com.ofekyuval.arworkout.ui.components.MyDialog
import com.ofekyuval.arworkout.ui.components.MyTitle
import com.ofekyuval.arworkout.ui.components.ScreenWithGlasses

@Composable
fun ActiveWorkoutScreen(navController: NavController = rememberNavController(), workout: Workout?, glassesViewModel: EvsGlassesViewModel = viewModel()) {
    var openDiscardWorkoutAlertDialog by remember { mutableStateOf(false) }
    var openEndWorkoutAlertDialog by remember { mutableStateOf(false) }
    var _workout by remember { mutableStateOf(workout) }
    BackHandler {
        openDiscardWorkoutAlertDialog = true
    }
    LaunchedEffect(workout) {
        if (workout == null) {
            navController.popBackStack()
            navController.navigate(Screens.WorkoutSummary.route)
        } else {
            _workout = workout
        }
    }
    Box {
        ScreenWithGlasses(bottomPadding = 0.dp, spacedBy = 0.dp) {
            if (_workout == null) {
                Text("Cannot load this workout")
            } else {
                Column(modifier = Modifier.padding(top = 40.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    MyTitle(text = _workout!!.name)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            modifier = Modifier.alpha(0.4f),
                            imageVector = Icons.Default.Alarm,
                            contentDescription = "clock"
                        )
                        MyDescription(text = _workout!!.getDuration())
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        val color = if (glassesViewModel.isReady) Color(0xFF399C37) else Color(0xFF861414)
                        Icon(
                            modifier = Modifier.alpha(0.4f),
                            imageVector = Icons.Default.Circle,
                            tint = color,
                            contentDescription = "clock"
                        )
                        Text(
                            modifier = Modifier.alpha(0.7f),
                            text = if (glassesViewModel.isReady) stringResource(R.string.active) else stringResource(R.string.not_active),
                            color = color
                        )
                    }
                }
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.align(Alignment.BottomEnd),
                        painter = painterResource(id = _workout!!.img),
                        contentDescription = null
                    )
                }
            }
        }

        EndWorkoutButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp),
            onClick = { openEndWorkoutAlertDialog = true }
        )

        if (openDiscardWorkoutAlertDialog) {
            MyDialog(
                onDismissRequest = { openDiscardWorkoutAlertDialog = false },
                onConfirmation = {
                    openDiscardWorkoutAlertDialog = false
                    Evs.instance().screens().removeTopmostScreen()
                    navController.popBackStack()
                },
                dialogTitle = stringResource(id = R.string.discard_workout),
                dialogText = stringResource(id = R.string.are_you_sure),
            )
        }
        if (openEndWorkoutAlertDialog) {
            MyDialog(
                onDismissRequest = { openEndWorkoutAlertDialog = false },
                onConfirmation = {
                    openEndWorkoutAlertDialog = false
                    workout?.stopWorkout()
                },
                dialogTitle = stringResource(id = R.string.end_workout),
                dialogText = stringResource(id = R.string.are_you_sure),
            )
        }

    }
}

@Preview
@Composable
fun EndWorkoutButton(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    ElevatedButton(
        modifier = modifier,
        colors = ButtonColors(containerColor = Color(0xFFEC5F5F), contentColor = Color.White, disabledContainerColor = Color.Gray, Color.Black),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.end_workout_capital),
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WorkoutScreenPrev() {
    ActiveWorkoutScreen(workout = Workout("Active Workout Name Title", R.drawable.boy, arrayOf()))
}