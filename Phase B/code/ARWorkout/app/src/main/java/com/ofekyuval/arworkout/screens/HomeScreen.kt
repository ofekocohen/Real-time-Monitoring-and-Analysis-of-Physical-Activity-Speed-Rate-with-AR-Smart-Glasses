package com.ofekyuval.arworkout.screens

import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.everysight.evskit.android.Evs
import com.everysight.evskit.android.compose.viewmodels.EvsGlassesViewModel
import com.ofekyuval.arworkout.Constants
import com.ofekyuval.arworkout.MainActivity
import com.ofekyuval.arworkout.R
import com.ofekyuval.arworkout.ui.components.MyTitle
import com.ofekyuval.arworkout.ui.components.ScreenWithGlasses
import com.ofekyuval.arworkout.ui.components.WorkoutCard
import com.ofekyuval.arworkout.ui.theme.cardColors
import com.ofekyuval.arworkout.viewmodels.WorkoutsViewModel

@Composable
fun HomeScreen(navController: NavController = rememberNavController(), glassesViewModel: EvsGlassesViewModel = viewModel(), workoutsViewModel: WorkoutsViewModel = viewModel()) {
    val context = LocalContext.current
    ScreenWithGlasses {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            MyTitle(text = stringResource(R.string.workouts))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                itemsIndexed(Constants.availableWorkouts) { i, workout ->
                    WorkoutCard(
                        workout = workout,
                        imageVector = Icons.Default.Alarm,
                        descText = workout.getDuration(),
                        buttonText = "Start",
                        buttonColor = Color(0xFF399C37),
                        containerColor = cardColors[i % cardColors.size]
                    ) {
                        workoutsViewModel.startWorkout(context, workout)
                        navController.navigate(Screens.ActiveWorkout.route)
                    }
                }
                item {}
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    HomeScreen()
}