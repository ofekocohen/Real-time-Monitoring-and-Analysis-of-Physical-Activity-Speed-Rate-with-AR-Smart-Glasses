package com.ofekyuval.arworkout.glasses.activities

import androidx.annotation.FloatRange
import com.ofekyuval.arworkout.glasses.sprites.ImageGif
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

interface IActivity {
    val TAG: String
    val threshold: ClosedRange<Float>
    val imgGif: ImageGif
    var activitiesPerformed : ArrayList<ActivityDetails>
    var amountOfLookBack : Int
    var centerRate: Float
    var slowestActivityDurationSec: Float
    var fastestActivityDurationSec: Float
    fun isPerformed(avg: Float, startTs: Long, endTs: Long): ActivityDetails?
    fun getIntensity(): Int {
        if (activitiesPerformed.isEmpty()) return 0
        val startIndex = if(activitiesPerformed.size > amountOfLookBack){activitiesPerformed.size-1 - amountOfLookBack} else 0
        val currentTimeMillis = System.currentTimeMillis()
        val durationInSec = (currentTimeMillis - activitiesPerformed[startIndex].startTs) / 1000f
        val amountOfJumps = activitiesPerformed.size - startIndex
        val rate = durationInSec / amountOfJumps
        val sensitivity = if (rate - centerRate > 0) {
            (slowestActivityDurationSec-centerRate)/5f
        } else {
            (centerRate-fastestActivityDurationSec)/5f
        }.coerceAtLeast(0.001f)
        return 5 - ((rate - centerRate) / sensitivity).roundToInt()
    }
    fun isIntensityValid() = activitiesPerformed.size >= amountOfLookBack
    fun reset() {
        activitiesPerformed.clear()
    }
}

@Serializable
open class ActivityDetails(
    open val startTs: Long,
    open val endTs: Long,
    open val durationSec: Float,
    @FloatRange(from = 0.0, to = 1.0) open val intensity: Float
)

enum class ActivityType(val activity: IActivity) {
    JUMP(JumpActivity.instance),
    STAR_JUMP(StarJumpActivity.instance),
    REST(RestActivity.instance),
    SQUAT(SquatActivity.instance),
//        PULL_UP(-3f..-3f)
}