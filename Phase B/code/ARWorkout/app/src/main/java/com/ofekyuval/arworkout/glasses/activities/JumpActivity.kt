package com.ofekyuval.arworkout.glasses.activities

import android.util.Log
import androidx.annotation.FloatRange
import com.ofekyuval.arworkout.glasses.GlassesTheme
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
open class JumpActivity protected constructor() : IActivity {
    companion object {
        val instance by lazy { JumpActivity() }
    }
    @Transient override val TAG = "JumpActivity"
    @Transient override val imgGif = GlassesTheme.imgGifJump
    @Transient override val threshold = -15f..-6f
    @Transient override var activitiesPerformed = arrayListOf<ActivityDetails>()
    @Transient override var amountOfLookBack = 3
    @Transient override var centerRate = 0.7f
    @Transient override var slowestActivityDurationSec = 2f
    @Transient override var fastestActivityDurationSec = 0.4f

    protected fun isJump(avg: Float, startTs: Long, endTs: Long): JumpDetails? {
        if (avg !in threshold) return null
        val durationSec = (endTs - startTs) / 1000f / 2f
        val height = 0.5f * 9.807f * durationSec * durationSec
        return JumpDetails(
            height = height,
            startTs = startTs,
            endTs = endTs,
            durationSec = durationSec,
            intensity = (height / 0.5f).coerceIn(0f, 1f)
        )
    }
    override fun isPerformed(avg: Float, startTs: Long, endTs: Long): ActivityDetails? {
        val jumpDetails = isJump(avg, startTs, endTs) ?: return null
        Log.d(TAG, "-----$TAG----- $jumpDetails")
        activitiesPerformed.add(jumpDetails)
        return jumpDetails
    }

    data class JumpDetails(
        val height: Float,
        override val startTs: Long,
        override val endTs: Long,
        override val durationSec: Float,
        @FloatRange(from = 0.0, to = 1.0) override val intensity: Float
    ): ActivityDetails(startTs, endTs, durationSec, intensity)
}