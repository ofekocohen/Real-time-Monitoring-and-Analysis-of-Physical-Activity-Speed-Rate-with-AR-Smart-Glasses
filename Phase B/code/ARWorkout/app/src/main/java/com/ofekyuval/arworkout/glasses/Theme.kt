package com.ofekyuval.arworkout.glasses

import UIKit.app.resources.ImgSrc
import UIKit.app.resources.UIResource
import com.ofekyuval.arworkout.glasses.sprites.ImageGif

object GlassesTheme {
    val imgGifJump = ImageGif(arrayOf(
        ImgSrc("jump_0.png", ImgSrc.Slot.s1),
        ImgSrc("jump_1.png", ImgSrc.Slot.s2)
    ), 1000L)
    val imgGifStarJump = ImageGif(arrayOf(
        imgGifJump.images.first(),
        ImgSrc("star_jump_1.png", ImgSrc.Slot.s4)
    ), 1000L)
    val imgGifSquat = ImageGif(arrayOf(
        ImgSrc("squat_0.png", ImgSrc.Slot.s5),
        ImgSrc("squat_1.png", ImgSrc.Slot.s6)
    ), 1000L)

    fun resources(): Array<UIResource> {
        val resources = arrayListOf<UIResource>()
        resources.addAll(imgGifJump.images)
        resources.addAll(imgGifStarJump.images)
        resources.addAll(imgGifSquat.images)
        return resources()
    }
}