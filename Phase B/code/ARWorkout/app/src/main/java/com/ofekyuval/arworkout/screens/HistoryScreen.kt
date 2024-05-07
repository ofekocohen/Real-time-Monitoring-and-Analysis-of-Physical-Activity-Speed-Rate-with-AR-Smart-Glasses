package com.ofekyuval.arworkout.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.everysight.evskit.android.compose.viewmodels.EvsGlassesViewModel
import com.ofekyuval.arworkout.R
import com.ofekyuval.arworkout.glasses.workout.Workout
import com.ofekyuval.arworkout.ui.components.MyDescription
import com.ofekyuval.arworkout.ui.components.MyDialog
import com.ofekyuval.arworkout.ui.components.MyTitle
import com.ofekyuval.arworkout.ui.components.ScreenWithGlasses
import com.ofekyuval.arworkout.ui.components.WorkoutCard
import com.ofekyuval.arworkout.ui.theme.BluePrimary
import com.ofekyuval.arworkout.ui.theme.cardColors
import com.ofekyuval.arworkout.viewmodels.WorkoutsViewModel

@Composable
fun HistoryScreen(navController: NavController = rememberNavController(), glassesViewModel: EvsGlassesViewModel = viewModel(), workoutsViewModel: WorkoutsViewModel = viewModel()) {
    val context = LocalContext.current
    val haptics = LocalHapticFeedback.current
    var isEditMode: Boolean by remember { mutableStateOf(false) }
    var openClearDialog by remember { mutableStateOf(false) }
    var workoutIndexToRemove: Int? by remember { mutableStateOf(null) }
    ScreenWithGlasses {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MyTitle(text = stringResource(R.string.history))
                    AnimatedVisibility(workoutsViewModel.workoutsHistory.isNotEmpty()) {
                        AnimatedVisibility(!isEditMode) {
                            Text(text = stringResource(R.string.edit), modifier = Modifier.clickable {
                                isEditMode = true
                            })
                        }
                        AnimatedVisibility(isEditMode) {
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(text = stringResource(R.string.clear), modifier = Modifier.clickable {
                                    openClearDialog = true
                                })
                                Text(text = stringResource(R.string.done), color = BluePrimary, modifier = Modifier.clickable {
                                    isEditMode = false
                                })
                            }
                        }
                    }
                }
                if (workoutsViewModel.isLoadingWorkoutsHistory) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center),)
                    }
                } else if (workoutsViewModel.workoutsHistory.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        MyDescription(
                            modifier = Modifier.align(Alignment.Center),
                            text = stringResource(R.string.no_workouts),
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        itemsIndexed(workoutsViewModel.workoutsHistory) { i, workoutHistory ->
                            Box {
                                WorkoutCard(
                                    workout = Workout(workoutHistory.name, workoutHistory.img, arrayOf()),
                                    imageVector = Icons.Default.CalendarToday,
                                    descText = workoutHistory.startDate,
                                    buttonText = "View",
                                    buttonColor = Color.Black,
                                    containerColor = cardColors[i % cardColors.size],
                                ) {
                                    if (!isEditMode) {
                                        navController.navigate(Screens.WorkoutSummary.route + "/$i")
                                    } else {
                                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                        workoutIndexToRemove = i
                                    }
                                }
                                if (isEditMode) {
                                    Icon(
                                        modifier = Modifier.align(Alignment.TopEnd),
                                        imageVector = Icons.Filled.RemoveCircle,
                                        contentDescription = context.getString(R.string.remove),
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                        item {}
                    }
                }
            }

            if (openClearDialog) {
                MyDialog(
                    onDismissRequest = { openClearDialog = false },
                    onConfirmation = {
                        openClearDialog = false
                        workoutsViewModel.clearHistory(context)
                    },
                    dialogTitle = stringResource(R.string.clear_all_history),
                    dialogText = stringResource(id = R.string.are_you_sure),
                )
            }

            if (workoutIndexToRemove != null) {
                MyDialog(
                    onDismissRequest = { workoutIndexToRemove = null },
                    onConfirmation = {
                        workoutsViewModel.workoutsHistory.removeAt(workoutIndexToRemove!!)
                        workoutIndexToRemove = null
                    },
                    dialogTitle = stringResource(R.string.delete_workout),
                    dialogText = stringResource(id = R.string.are_you_sure),
                )
            }


        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    HistoryScreen()
}