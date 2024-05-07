package com.ofekyuval.arworkout.screens

import androidx.annotation.DrawableRes
import com.ofekyuval.arworkout.R

sealed class Screens(val route: String) {
    data object ActiveWorkout: Screens(route = "active_workout_screen")
    data object WorkoutSummary: Screens(route = "workout_summary_screen")
}

sealed class BottomNavigationScreens(val route: String, @DrawableRes val icon: Int, val label: String) {
    data object Home: BottomNavigationScreens(route = "home_screen", icon = R.drawable.home, "Home")
    data object History: BottomNavigationScreens(route = "history_screen", icon = R.drawable.history, "History")
}