package com.ofekyuval.arworkout.glasses.activities

import com.ofekyuval.arworkout.glasses.GlassesTheme

class StarJumpActivity private constructor(): JumpActivity() {
    companion object {
        val instance by lazy { StarJumpActivity() }
    }
    override val TAG = "StarJumpActivity"
    override val imgGif = GlassesTheme.imgGifStarJump
    override var activitiesPerformed =  arrayListOf<ActivityDetails>()
    override var amountOfLookBack = 3
    override var centerRate = 0.9f
    override var slowestActivityDurationSec = 3f
    override var fastestActivityDurationSec = 0.6f

    private var prevJumpTs = 0L
    override fun isPerformed(avg: Float, startTs: Long, endTs: Long): ActivityDetails? {
        val jumpDetails = super.isJump(avg, startTs, endTs) ?: return null
        if (startTs - prevJumpTs < 2000 && prevJumpTs != 0L) {
            //Log.d(TAG, "2 close jumps : ${endTs - prevJumpTs}")
            prevJumpTs = 0L
            activitiesPerformed.add(jumpDetails)
        } else {
            //Log.d(TAG, "NOT STAR JUMP : ${endTs - prevJumpTs}")
            prevJumpTs = endTs
            return null
        }
        return jumpDetails
    }
}