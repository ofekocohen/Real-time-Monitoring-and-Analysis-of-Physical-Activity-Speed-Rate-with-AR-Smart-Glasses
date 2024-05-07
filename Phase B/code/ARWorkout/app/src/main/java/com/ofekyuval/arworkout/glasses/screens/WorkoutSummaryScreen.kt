package com.ofekyuval.arworkout.glasses.screens

import UIKit.app.Screen
import UIKit.app.data.EvsColor
import UIKit.app.resources.Font
import UIKit.widgets.Text
import UIKit.widgets.UIElement
import com.everysight.evskit.android.Evs

object WorkoutSummaryScreen: Screen("SelectWorkoutScreen") {
    private val txtAppName = Text("txtAppName")
    private val txtSelectWorkout = Text("txtSelectWorkout")
    private val txtSelectWorkout2 = Text("txtSelectWorkout2")

    private val marginTop = 50f

    override fun onCreate() {
        super.onCreate()
        txtAppName.apply {
            setText("ARWorkout")
            setResource(Font.StockFont.Small)
            setCenter(this@WorkoutSummaryScreen.getWidth()/2f)
            setForegroundColor(EvsColor.Yellow)
            setY(marginTop)
            add(this)
        }

        txtSelectWorkout.apply {
            setText("Checkout Summary")
            setResource(Font.StockFont.Large)
            setCenter(this@WorkoutSummaryScreen.getWidth()/2f)
            setForegroundColor(EvsColor.White)
            add(this)
        }
        txtSelectWorkout2.apply {
            setText("on the app")
            setResource(Font.StockFont.Medium)
            setCenter(this@WorkoutSummaryScreen.getWidth()/2f)
            setForegroundColor(EvsColor.White)
            add(this)
        }
        txtSelectWorkout.setY(getHeight()/2f-txtSelectWorkout.getHeight()/2f-txtSelectWorkout2.getHeight()/2f)
        txtSelectWorkout2.setY(txtSelectWorkout.getY()+txtSelectWorkout.getHeight()+5f)
    }
}