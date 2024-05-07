package com.ofekyuval.arworkout.utils

import androidx.annotation.DrawableRes
import com.ofekyuval.arworkout.glasses.activities.RestActivity
import com.ofekyuval.arworkout.glasses.workout.Exercise
import com.ofekyuval.arworkout.glasses.workout.Workout
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutHistory(val name: String, val exercises: Array<ExerciseHistory>, @DrawableRes val img: Int, val startDate: String) {
    companion object {
        fun fromWorkout(workout: Workout): WorkoutHistory {
            val exerciseHistory = workout.exercises.filter { it.wasStarted() && it.activity != RestActivity.instance }.map {
                ExerciseHistory(
                    name = it.name,
                    currentReps = it.currentReps,
                    totalReps = it.totalReps,
                    score = it.getScore(),
                    intensityArray = it.pastIntensity.toTypedArray()
                )
            }
            return WorkoutHistory(
                name = workout.name,
                exercises = exerciseHistory.toTypedArray(),
                img = workout.img,
                startDate = workout.getStartDate()
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WorkoutHistory

        if (name != other.name) return false
        if (!exercises.contentEquals(other.exercises)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + exercises.contentHashCode()
        return result
    }
}


@Serializable
data class ExerciseHistory(val name: String, val currentReps: Int, val totalReps: Int, val score: Int, val intensityArray: Array<Exercise.IntensityHistory>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExerciseHistory

        if (name != other.name) return false
        if (currentReps != other.currentReps) return false
        if (totalReps != other.totalReps) return false
        if (score != other.score) return false
        if (!intensityArray.contentEquals(other.intensityArray)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + totalReps.hashCode()
        result = 31 * result + score.hashCode()
        result = 31 * result + intensityArray.contentHashCode()
        return result
    }
}