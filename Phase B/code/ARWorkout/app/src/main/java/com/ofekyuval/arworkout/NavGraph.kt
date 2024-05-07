package com.ofekyuval.arworkout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ofekyuval.arworkout.screens.ActiveWorkoutScreen
import com.ofekyuval.arworkout.screens.BottomNavigationScreens
import com.ofekyuval.arworkout.screens.HistoryScreen
import com.ofekyuval.arworkout.screens.HomeScreen
import com.ofekyuval.arworkout.screens.Screens
import com.ofekyuval.arworkout.screens.WorkoutSummaryScreen
import com.ofekyuval.arworkout.viewmodels.WorkoutsViewModel

@Composable
fun HomeNavGraph(modifier: Modifier = Modifier, navController: NavHostController) {
    val workoutsViewModel: WorkoutsViewModel = viewModel()
    NavHost(modifier = modifier, navController = navController, startDestination = BottomNavigationScreens.Home.route) {
        composable(route = BottomNavigationScreens.Home.route) {
            HomeScreen(navController, workoutsViewModel = workoutsViewModel)
        }
        composable(route = Screens.ActiveWorkout.route) {
            ActiveWorkoutScreen(navController, workoutsViewModel.currentWorkout)
        }
        composable(route = Screens.WorkoutSummary.route) {
            WorkoutSummaryScreen(navController, workoutsViewModel.workoutsHistory.firstOrNull())
        }
    }
}

@Composable
fun HistoryNavGraph(modifier: Modifier = Modifier, navController: NavHostController) {
    val workoutsViewModel: WorkoutsViewModel = viewModel()
    NavHost(modifier = modifier, navController = navController, startDestination = BottomNavigationScreens.History.route) {
        composable(route = BottomNavigationScreens.History.route) {
            HistoryScreen(navController, workoutsViewModel = workoutsViewModel)
        }
        composable(route = Screens.WorkoutSummary.route + "/{workoutHistoryIndex}") {
            val workoutHistoryIndex = it.arguments?.getInt("workoutHistoryIndex")?:workoutsViewModel.workoutsHistory.lastIndex
            val workoutHistory = workoutsViewModel.workoutsHistory[workoutHistoryIndex]
            WorkoutSummaryScreen(navController, workoutHistory)
        }
    }
}