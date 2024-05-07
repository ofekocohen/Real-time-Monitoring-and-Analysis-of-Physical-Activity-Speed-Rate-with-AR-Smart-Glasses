package com.ofekyuval.arworkout

import androidx.compose.runtime.mutableStateListOf
import com.ofekyuval.arworkout.glasses.activities.JumpActivity
import com.ofekyuval.arworkout.glasses.activities.RestActivity
import com.ofekyuval.arworkout.glasses.activities.SquatActivity
import com.ofekyuval.arworkout.glasses.activities.StarJumpActivity
import com.ofekyuval.arworkout.glasses.workout.Exercise
import com.ofekyuval.arworkout.glasses.workout.Workout

class Constants {
    companion object {
        val availableWorkouts = mutableStateListOf(
            Workout("Jumping Burn-Fat", R.drawable.girl, arrayOf(
                Exercise("Rest", RestActivity.instance, 9999L, 1),
                Exercise("Start-Jump", StarJumpActivity.instance, 30000L, 10),
                Exercise("Rest", RestActivity.instance, 15000L, 1),
                Exercise("Jump", SquatActivity.instance, 30000L, 15),
                Exercise("Rest", RestActivity.instance, 15000L, 1),
                Exercise("Start-Jump", StarJumpActivity.instance, 15000L, 10),
                Exercise("Rest", RestActivity.instance, 10000L, 1),
                Exercise("Start-Jump", StarJumpActivity.instance, 15000L, 10),
                Exercise("Rest", RestActivity.instance, 10000L, 1),
                Exercise("Start-Jump", StarJumpActivity.instance, 15000L, 10),
                Exercise("Rest", RestActivity.instance, 30000L, 1),
                Exercise("Start-Jump", StarJumpActivity.instance, 15000L, 10),
                Exercise("Rest", RestActivity.instance, 10000L, 1),
                Exercise("Start-Jump", StarJumpActivity.instance, 15000L, 10),
            )),
            Workout("Rope Workout", R.drawable.boy, arrayOf(
                Exercise("Rest", RestActivity.instance, 9999L, 1),
                Exercise("Rope Jump", JumpActivity.instance, 30000L, 30),
                Exercise("Rest", RestActivity.instance, 30000L, 1),
                Exercise("Rope Jump", JumpActivity.instance, 30000L, 30),
                Exercise("Rest", RestActivity.instance, 30000L, 1),
                Exercise("Rope Jump", JumpActivity.instance, 30000L, 30),
                Exercise("Rest", RestActivity.instance, 60000L, 1),
                Exercise("Rope Jump", JumpActivity.instance, 60000L, 60),
                Exercise("Rest", RestActivity.instance, 60000L, 1),
                Exercise("Rope Jump", JumpActivity.instance, 60000L, 60),
                Exercise("Rest", RestActivity.instance, 30000L, 1),
                Exercise("Rope Jump", JumpActivity.instance, 30000L, 30),
                Exercise("Rest", RestActivity.instance, 30000L, 1),
                Exercise("Rope Jump", JumpActivity.instance, 30000L, 30),
            )),
        )
    }
}