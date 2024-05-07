package com.ofekyuval.arworkout.glasses.components

import UIKit.app.data.EvsColor
import UIKit.widgets.Polygon
import UIKit.widgets.Rect
import UIKit.widgets.UIElement
import UIKit.widgets.UIElementsGroup
import kotlin.math.min
import kotlin.math.roundToInt

class IntensityView: UIElementsGroup("IntensityView") {
    private val rectsCount = 10
    private val spacedBy = 20f
    private val rects: Array<Rect> = Array(rectsCount) {
        Rect().apply {
            setFillColor(EvsColor.White)
            setRoundedCorners(Float.MAX_VALUE)
            add(this)
        }
    }
    private val triangle = Polygon()

    private val mainRegionStartIndex = (rects.size/3f).roundToInt()
    private val mainRegionEndIndex = rects.size-mainRegionStartIndex
    private val dividers: Array<Rect> = Array(2) {
        Rect().apply {
            setFillColor(EvsColor.Yellow)
            setRoundedCorners(Float.MAX_VALUE)
            add(this)
        }
    }

    private var selectedRect: Rect? = null

    override fun onCreate() {
        super.onCreate()
        placeElements()

        add(triangle)
        deselect()
        select(6)
    }

    override fun setDimensions(x: Float, y: Float, width: Float, height: Float): UIElement {
        super.setDimensions(x, y, width, height)
        placeElements()
        return this
    }

    private fun placeElements() {
        triangle.apply {
            val size = min(this@IntensityView.getWidth() * 0.1f, this@IntensityView.getHeight() * 0.22f)
            setWidthHeight(size, size)
            clear()
            addMany(
                xs = floatArrayOf(0f, getWidth(), getWidth()/2f),
                ys = floatArrayOf(0f, 0f, getHeight()),
                3
            )
            add(this)
        }

        val paddingBetweenTriangleAndRects = 20f
        val heightFactor = getHeight()*0.8f

        rects.forEachIndexed { i, rect ->
            rect.apply {
                setWidth((this@IntensityView.getWidth()-(rectsCount-1)*spacedBy)/rectsCount)
                setHeight(heightFactor-triangle.getHeight()-paddingBetweenTriangleAndRects)
                setX((getWidth()+spacedBy)*i)
                setY((this@IntensityView.getHeight()-getHeight())/2f)
            }
        }


        dividers.first().apply {
            setWidth(spacedBy/4f)
            setHeight(heightFactor)
            setX(rects[mainRegionStartIndex].getX() - spacedBy/2f - getWidth()/2f)
            setY((this@IntensityView.getHeight()-getHeight())/2f)
        }
        dividers.last().apply {
            setWidth(spacedBy/4f)
            setHeight(heightFactor)
            setX(rects[mainRegionEndIndex].getX() - spacedBy/2f - getWidth()/2f)
            setY((this@IntensityView.getHeight()-getHeight())/2f)
        }
    }

    fun select(index: Int?) {
        if (index == null) {
            deselect()
            return
        }
        val index = index.coerceIn(rects.indices)
        val selectedColor = if (index in mainRegionStartIndex..<mainRegionEndIndex) EvsColor.Green else EvsColor.Red
        selectedRect?.setFillColor(EvsColor.White)
        selectedRect = rects[index]
        selectedRect!!.setFillColor(selectedColor)

        triangle
            .setFillColor(selectedColor)
            .setX(selectedRect!!.getX()+selectedRect!!.getWidth()/2f-triangle.getWidth()/2f)
            .setVisibility(true)
    }
    fun deselect() {
        selectedRect?.setFillColor(EvsColor.White)
        triangle.setVisibility(false)
    }
}