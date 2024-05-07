package com.ofekyuval.arworkout.glasses.sprites

import UIKit.app.resources.ImgSrc
import UIKit.widgets.Image
import com.everysight.evskit.android.Evs

class ImageGif(val images: Array<ImgSrc>, val delayFrameMs: Long): Image() {
    private var currIndex = 0
    private var playTs = 0L
    var isInfiniteAnimation = true
        private set
    var isPlaying = false
        private set

    init {
        if (Evs.wasInitialized() && images.isNotEmpty()) {
            setResource(images[0])
        }
    }

    override fun onBeforeDraw(timestampMs: Long) {
        super.onBeforeDraw(timestampMs)
        if (isPlaying && timestampMs - playTs >= delayFrameMs) {
            playTs = timestampMs
            nextFrame()
        }
    }

    fun play(isInfiniteAnimation: Boolean = true) {
        if (isPlaying) stop()
        if (images.isEmpty()) return
        this.isInfiniteAnimation = isInfiniteAnimation
        playTs = System.currentTimeMillis()
        isPlaying = true
    }
    fun stop() {
        isPlaying = false
        currIndex = 0
        playTs = 0L
    }
    private fun nextFrame() {
        if (images.size > ++currIndex) {
            setResource(images[currIndex])
        } else if (isInfiniteAnimation) {
            currIndex = 0
            setResource(images[currIndex])
        } else {
            stop()
        }
    }
}