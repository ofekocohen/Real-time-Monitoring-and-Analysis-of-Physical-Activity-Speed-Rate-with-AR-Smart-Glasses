package com.ofekyuval.arworkout.glasses.workout

import android.util.Log
import androidx.annotation.DrawableRes
import com.ofekyuval.arworkout.glasses.activities.RestActivity
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@Serializable
class Workout(val name: String, @DrawableRes val img: Int, val exercises: Array<Exercise>) {
    @Transient private val TAG = "Workout"
    @Transient private var currentExerciseIndex = 0
    @Transient private var currentExerciseStartTime = 0L
    val currentExercise: Exercise?
        get() = if (exercises.isEmpty()) null else exercises[currentExerciseIndex.coerceIn(exercises.indices)]
    private var startTs = 0L
    private val listeners = hashSetOf<IWorkoutEvents>()
    private val durationMs: Long = exercises.sumOf { it.durationMs }
    private var isNotifyFinish = false

    interface IWorkoutEvents {
        fun onNewExerciseStarted(currentExercise: Exercise?) {}
        fun onWorkoutFinish() {}
    }

    fun register(listener: IWorkoutEvents) {
        listeners.add(listener)
    }
    fun unregister(listener: IWorkoutEvents) {
        listeners.remove(listener)
    }

    fun onUpdate() {
        if (isNotifyFinish || isWorkoutEnded()) {
            return
        }
        if (currentExercise?.isFinish() == true) {
            Log.d(TAG, "next exercise")
            onFinishExercise()
        }
        if (!isWorkoutEnded()){
            currentExercise?.onUpdate()
        }
    }

    private fun onFinishExercise() {
        currentExerciseIndex += 1
        if (isWorkoutEnded()) {
            stopWorkout()
        } else {
            currentExercise?.reset()
            currentExerciseStartTime = System.currentTimeMillis()
            listeners.forEach { it.onNewExerciseStarted(currentExercise) }
        }
    }
    fun startWorkout() {
        Log.d(TAG, "start workout")
        currentExerciseIndex = 0
        currentExerciseStartTime = 0L
        isNotifyFinish = false
        if (exercises.isNotEmpty()) {
            exercises.forEach { it.reset() }
            listeners.forEach { it.onNewExerciseStarted(currentExercise) }
        }

        startTs = System.currentTimeMillis()
    }
    fun stopWorkout() {
        Log.d(TAG, "end workout")
        if (!isNotifyFinish) {
            isNotifyFinish = true
            listeners.forEach { it.onWorkoutFinish() }
        }
    }
    fun isWorkoutEnded() = currentExerciseIndex > exercises.lastIndex

    fun getLabel(): String {
        return currentExercise?.getLabelIndicator()?:""
    }

    fun getIntensity(): Int? {
        if (isWorkoutEnded()){
            return 0
        }
        return currentExercise?.getIntensity()
    }

    fun getDuration() = buildString {
        val _durationMs = durationMs + 1000L
        (TimeUnit.MILLISECONDS.toHours(_durationMs)).let { if ( it != 0L) append("$it h ") }
        (TimeUnit.MILLISECONDS.toMinutes(_durationMs) % 60).let { if ( it != 0L) append("$it min ") }
        (TimeUnit.MILLISECONDS.toSeconds(_durationMs) % 60).let { if ( it != 0L) append("$it sec") }
    }
    fun getStartDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = Date(startTs)
        return dateFormat.format(date)
    }

    fun isInRest() = currentExercise?.activity == RestActivity.instance

}