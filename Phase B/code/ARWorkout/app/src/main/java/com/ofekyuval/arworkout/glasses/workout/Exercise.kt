package com.ofekyuval.arworkout.glasses.workout

import com.ofekyuval.arworkout.glasses.activities.IActivity
import com.ofekyuval.arworkout.glasses.activities.RestActivity
import kotlinx.serialization.Serializable

@Serializable
class Exercise(val name: String, val activity: IActivity, val durationMs: Long, var totalReps: Int) {
    var currentReps = 0
        private set
    var startTs = 0L
        private set

    @Serializable
    data class IntensityHistory(val timestamp: Long, val intensity: Int)
    val pastIntensity = arrayListOf<IntensityHistory>()

    fun onUpdate(){
        if (isFinish()) return
        if (startTs == 0L) {
            startTs = System.currentTimeMillis()
        }
        val intensityHistory = IntensityHistory(System.currentTimeMillis(), activity.getIntensity())
        pastIntensity.add(intensityHistory)
        currentReps = activity.activitiesPerformed.size
    }

    fun wasStarted() = startTs != 0L
    fun isFinish() = wasStarted() && (currentReps >= totalReps || System.currentTimeMillis() >= getFinishTime())
    fun getFinishTime() =  startTs + durationMs

    fun getLabelIndicator(): String {
        countDown()?.let { return it }
        if (activity == RestActivity.instance) {
            return "Rest Time"
        }
        if (!activity.isIntensityValid()) {
            return "Let's go!"
        }
        if (pastIntensity.size >= 10) {
            val lastTenElements = pastIntensity.takeLast(10)
            val allInRange = lastTenElements.all { it.intensity in 4..7 }
            val allToSlow = lastTenElements.all { it.intensity < 4 }
            val allToFast = lastTenElements.all { it.intensity > 7 }

            if (allInRange) {
                return "Great Job!"
            }
            if (allToSlow) {
                return "Go Faster!"
            }
            if (allToFast) {
                return "Slow Down"
            }
        }
        return ""
    }

    fun getScore(): Int {
        if (pastIntensity.isEmpty()) return 0
        val countInRange = pastIntensity.count { it.intensity in 4..7 }
        return (countInRange.toDouble() / pastIntensity.size * 100).toInt()
    }

    fun getIntensity() = if (activity.isIntensityValid()) activity.getIntensity() else null
    fun reset() {
        startTs = 0L
        currentReps = 0
        activity.reset()
    }

    private fun countDown(): String? {
        val elapsedTime = (getFinishTime() - System.currentTimeMillis()) / 1000f
        return when (elapsedTime) {
            in 4f..5f -> "5"
            in 3f..4f -> "4"
            in 2f..3f -> "3"
            in 1f..2f -> "2"
            in 0f..1f -> "1"
            else -> null
        }
    }

}