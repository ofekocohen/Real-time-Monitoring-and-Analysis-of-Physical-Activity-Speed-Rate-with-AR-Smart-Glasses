package com.ofekyuval.arworkout.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.everysight.evskit.android.Evs
import com.ofekyuval.arworkout.Constants
import com.ofekyuval.arworkout.R
import com.ofekyuval.arworkout.glasses.screens.WorkoutSummaryScreen
import com.ofekyuval.arworkout.ui.components.MyCard
import com.ofekyuval.arworkout.ui.components.MyDescription
import com.ofekyuval.arworkout.ui.components.MySubTitle
import com.ofekyuval.arworkout.ui.components.MyTitle
import com.ofekyuval.arworkout.ui.components.ScreenWithGlasses
import com.ofekyuval.arworkout.ui.theme.cardColors
import com.ofekyuval.arworkout.utils.WorkoutHistory

@Composable
fun WorkoutSummaryScreen(navController: NavController = rememberNavController(), workout: WorkoutHistory?) {
    LaunchedEffect(Unit) {
        Evs.instance().screens().removeScreen(WorkoutSummaryScreen)
        Evs.instance().screens().addScreen(WorkoutSummaryScreen)
    }
    DisposableEffect(Unit) {
        onDispose {
            Evs.instance().screens().removeScreen(WorkoutSummaryScreen)
        }
    }
    ScreenWithGlasses {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            MyTitle(text = stringResource(R.string.workout_summary))
            if (workout == null) {
                Text(stringResource(R.string.cannot_load_this_workout))
            } else {
                if (workout.exercises.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        MyDescription(
                            modifier = Modifier.align(Alignment.Center),
                            text = stringResource(R.string.no_exercises),
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        itemsIndexed(workout.exercises) { i, exercise ->
                            MyCard(
                                containerColor = cardColors[i % cardColors.size]
                            ) {
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(vertical = 10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(15.dp)
                                ) {
                                    MySubTitle(
                                        text = exercise.name
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceAround
                                    ) {
                                        ExerciseAttribute(
                                            title = stringResource(R.string.reps),
                                            value = exercise.currentReps
                                        )
                                        VerticalDivider(modifier = Modifier.height(30.dp))
                                        ExerciseAttribute(
                                            title = stringResource(R.string.score),
                                            value = exercise.score
                                        )
                                    }
                                }
                            }
                        }
                        item {}
                    }
                }
            }
        }
    }
}

@Composable
private fun ExerciseAttribute(
    title: String,
    value: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        MyDescription(text = title)
        Text(
            text = value.toString(),
            style = TextStyle(
                fontSize = 20.sp
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ExerciseAttributePrev() {
    ExerciseAttribute(
        title = "Score",
        value = 88
    )
}

@Preview(showBackground = true)
@Composable
private fun WorkoutScreenPrev() {
    WorkoutSummaryScreen(workout = WorkoutHistory.fromWorkout(Constants.availableWorkouts.first()))
}