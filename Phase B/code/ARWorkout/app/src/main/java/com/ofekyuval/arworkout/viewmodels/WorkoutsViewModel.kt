package com.ofekyuval.arworkout.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.everysight.evskit.android.Evs
import com.ofekyuval.arworkout.MainActivity.Companion.SP_NAME
import com.ofekyuval.arworkout.glasses.screens.WorkoutScreen
import com.ofekyuval.arworkout.glasses.workout.Workout
import com.ofekyuval.arworkout.utils.WorkoutHistory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WorkoutsViewModel : ViewModel() {
    private val mainScope = MainScope()
    var currentWorkout: Workout? by mutableStateOf(null)
    var workoutsHistory = mutableStateListOf<WorkoutHistory>()
    var isLoadingWorkoutsHistory by mutableStateOf(false)
    val gson = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    fun startWorkout(context: Context, workout: Workout) {
        this.currentWorkout = workout
        val workoutScreen = WorkoutScreen(workout)
        Evs.instance().screens().addScreen(workoutScreen)
        workout.startWorkout()
        workout.register(object : Workout.IWorkoutEvents {
            override fun onWorkoutFinish() {
                saveWorkoutHistoryToSP(context)
                currentWorkout = null

                val workoutListener = this
                mainScope.launch {
                    workout.unregister(workoutListener)
                    delay(2000)
                    Evs.instance().screens().removeScreen(workoutScreen)
                }

            }
        })
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun loadWorkoutHistoryFromSP(context: Context) {
        isLoadingWorkoutsHistory = workoutsHistory.isEmpty()
        GlobalScope.launch(Dispatchers.IO) {
            val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
            val defaultStr = gson.encodeToString(listOf<WorkoutHistory>())
            val jsonStr = sp.getString("workout_history", defaultStr) ?: defaultStr
            val workoutsHistoryFromSP = gson.decodeFromString<List<WorkoutHistory>>(jsonStr)
            withContext(Dispatchers.Main) {
                if (workoutsHistoryFromSP != workoutsHistory) {
                    workoutsHistory.clear()
                    workoutsHistory.addAll(workoutsHistoryFromSP)
                }
                isLoadingWorkoutsHistory = false
            }
        }
    }

    private fun saveWorkoutHistoryToSP(context: Context) {
        if (currentWorkout != null) {
            val workoutHistory = WorkoutHistory.fromWorkout(currentWorkout!!)
            if (!workoutsHistory.contains(workoutHistory)) {
                workoutsHistory.add(0, workoutHistory)
            }
        }
        val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        val jsonStr = gson.encodeToString(workoutsHistory.toList())
        sp.edit().apply {
            putString("workout_history", jsonStr)
            apply()
        }
    }

    fun clearHistory(context: Context) {
        workoutsHistory.clear()
        workoutsHistory.addAll(listOf())
        saveWorkoutHistoryToSP(context)
    }
}