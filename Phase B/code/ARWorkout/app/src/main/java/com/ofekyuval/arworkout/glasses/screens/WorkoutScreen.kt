@file:OptIn(ExperimentalUnsignedTypes::class)

package com.ofekyuval.arworkout.glasses.screens

import UIKit.app.Screen
import UIKit.app.data.EvsColor
import UIKit.app.data.ScreenRenderRate
import UIKit.app.resources.Font
import UIKit.widgets.Rect
import UIKit.widgets.Text
import android.util.Log
import com.ofekyuval.arworkout.glasses.activities.ActivityDetails
import com.ofekyuval.arworkout.glasses.activities.ActivityType
import com.ofekyuval.arworkout.glasses.components.IntensityView
import com.ofekyuval.arworkout.glasses.managers.ActivitiesManager
import com.ofekyuval.arworkout.glasses.sprites.ImageGif
import com.ofekyuval.arworkout.glasses.workout.Exercise
import com.ofekyuval.arworkout.glasses.workout.Workout

class WorkoutScreen(private val workout: Workout): Screen("WorkoutScreen") {
    private val TAG = "WorkoutScreen"

    private val txtTime = Text("txtTime")
    private val txtExerciseName = Text("txtExerciseName")
    private val txtTitle = Text("txtTitle")
    private val txtReps = Text("txtReps")
    private val intensityView = IntensityView()
    private var imgActivity = ImageGif(arrayOf(), -1L)

    private val marginTop = 50f

    private val activitiesManager = ActivitiesManager()
    private var startTimeMs = 0L
    private var reps = 0

    override fun onCreate() {
        super.onCreate()
        setScreenRenderRate(ScreenRenderRate.animation)
        Rect().apply {
            setWidthHeight(this@WorkoutScreen.getWidth(), this@WorkoutScreen.getHeight())
            setPenThickness(2)
            setForegroundColor(EvsColor.Yellow)
//            add(this)
        }
        txtTime.apply {
            setXY(0f, marginTop)
            setResource(Font.StockFont.Medium)
            setText("00:00")
            add(this)
        }
        txtExerciseName.apply {
            setXY(0f, txtTime.getY()+txtTime.getHeight())
            setResource(Font.StockFont.Small)
            setText("")
            add(this)
        }
        txtTitle.apply {
            setY(marginTop)
            setCenter(this@WorkoutScreen.getWidth()/2f)
            setResource(Font.StockFont.Large)
            setText("Great Job!")
            add(this)
        }
        txtReps.apply {
            setCenter(this@WorkoutScreen.getWidth()/2f)
            setResource(Font.StockFont.Large)
            setText("$reps/10")
            add(this)
        }
        intensityView.apply {
            setWidth(this@WorkoutScreen.getWidth()*0.8f)
            setHeight(135f)
            setX(this@WorkoutScreen.getWidth()/2f-getWidth()/2f)
            setY(this@WorkoutScreen.getHeight()-getHeight())
            this@WorkoutScreen.add(this)
        }
        imgActivity.apply {
            setX(this@WorkoutScreen.getWidth()-getWidth())
            setY(marginTop)
            this@WorkoutScreen.add(this)
        }

        activitiesManager.onCreate()
        workout.register(workoutEvents)

        startTimeMs = System.currentTimeMillis()
    }

    override fun onResume() {
        super.onResume()
        activitiesManager.registerActivitiesEvents(activitiesEvents)
    }

    override fun onPause() {
        super.onPause()
        activitiesManager.unregisterActivitiesEvents(activitiesEvents)
    }

    private val activitiesEvents = object : ActivitiesManager.IActivitiesEvents {
        override fun onActivityRecognized(activityType: ActivityType, activityDetails: ActivityDetails, startTs: Long, endTs: Long) {

        }
    }
    private val workoutEvents = object : Workout.IWorkoutEvents {
        override fun onNewExerciseStarted(currentExercise: Exercise?) {
            super.onNewExerciseStarted(currentExercise)
            if (currentExercise == null) {
                imgActivity.setVisibility(false)
                txtExerciseName.setVisibility(false)
            } else {
                remove(imgActivity)
                imgActivity = currentExercise.activity.imgGif
                imgActivity.play()
                add(imgActivity)
                txtExerciseName.setText(currentExercise.name)
            }
        }
        override fun onWorkoutFinish() {
            super.onWorkoutFinish()
        }
    }

    override fun onUpdateUI(timestampMs: Long) {
        super.onUpdateUI(timestampMs)
        workout.onUpdate()
        if (workout.isWorkoutEnded()) {
            txtTitle.setText("Finish!")
        } else {
            intensityView.select(workout.getIntensity())
            txtTitle.setText(workout.getLabel())
            txtReps.setText("${workout.currentExercise?.currentReps?:0}/${workout.currentExercise?.totalReps?:0}")
            txtTime.setText(formatTimestamp((workout.currentExercise?.getFinishTime()?:System.currentTimeMillis()) - System.currentTimeMillis()))
            txtReps.setY(getHeight() / 2f - txtReps.getHeight() / 2f)
            imgActivity.setXY(this@WorkoutScreen.getWidth()-imgActivity.getWidth()-50, marginTop)
            Log.d(TAG, "onUpdateUI: intensity=${workout.getIntensity()} | txtTitle=${txtTitle.getText()} | txtReps=${txtReps.getText()} | txtTime=${txtTime.getText()}")
        }

        val isInRest = workout.isInRest()
        intensityView.setVisibility(!isInRest)
        txtReps.setVisibility(!isInRest)
        imgActivity.setVisibility(!isInRest)
        txtTitle.setForegroundColor(if (isInRest) EvsColor.Green else EvsColor.White)

    }

    override fun onDestroy() {
        super.onDestroy()
        activitiesManager.onDestroy()
        workout.unregister(workoutEvents)
    }

    private fun formatTimestamp(timestampMs: Long): String {
        val totalSeconds = timestampMs / 1000
        val hours = totalSeconds / 3600
        val remainingSeconds = totalSeconds % 3600
        val minutes = remainingSeconds / 60
        val seconds = remainingSeconds % 60 + 1

        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }

    fun Double.roundTo(digits: Int): Float {
        return "%.${digits}f".format(this).toFloat()
    }
    fun Float.roundTo(digits: Int): Float {
        return "%.${digits}f".format(this).toFloat()
    }
}