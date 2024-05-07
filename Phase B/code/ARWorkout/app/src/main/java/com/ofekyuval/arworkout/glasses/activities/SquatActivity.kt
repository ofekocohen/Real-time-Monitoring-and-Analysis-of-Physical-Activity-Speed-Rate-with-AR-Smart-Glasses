package com.ofekyuval.arworkout.glasses.activities

import android.util.Log
import com.ofekyuval.arworkout.glasses.GlassesTheme
import kotlinx.serialization.Transient

open class SquatActivity: IActivity {
    companion object {
        val instance by lazy { SquatActivity() }
    }
    @Transient override val TAG = "SquatActivity"
    @Transient override val imgGif = GlassesTheme.imgGifSquat
    @Transient override val threshold = -3.9f..-2.3f
    @Transient override var activitiesPerformed = arrayListOf<ActivityDetails>()
    @Transient override var amountOfLookBack = 3
    @Transient override var centerRate = 1.5f
    @Transient override var slowestActivityDurationSec = 4f
    @Transient override var fastestActivityDurationSec = 1f

    override fun isPerformed(avg: Float, startTs: Long, endTs: Long): ActivityDetails? {
        if (avg !in threshold) return null
        val durationSec = (endTs-startTs)/1000f
        val activityDetails = ActivityDetails(
            startTs = startTs,
            endTs = endTs,
            durationSec = durationSec,
            intensity = (durationSec/3f).coerceIn(0f, 1f)
        )
        Log.d("OFEK", "-----$TAG----- $activityDetails")
        activitiesPerformed.add(activityDetails)
        return activityDetails
    }
}