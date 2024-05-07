package com.ofekyuval.arworkout.glasses.activities

import com.ofekyuval.arworkout.glasses.sprites.ImageGif

class RestActivity private constructor() : IActivity {
    companion object {
        val instance by lazy { RestActivity() }
    }
    override val TAG: String = "RestActivity"
    override val imgGif = ImageGif(arrayOf(), -1L)
    override val threshold: ClosedRange<Float> = 0f..0f
    override var activitiesPerformed: ArrayList<ActivityDetails> = arrayListOf()
    override var amountOfLookBack: Int = -1
    override var centerRate: Float = -1f
    override var slowestActivityDurationSec: Float = -1f
    override var fastestActivityDurationSec: Float = -1f

    override fun isPerformed(avg: Float, startTs: Long, endTs: Long) = null
    override fun getIntensity() = -1
    override fun isIntensityValid() = false
    override fun reset() {}
}
