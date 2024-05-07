package com.ofekyuval.arworkout.glasses.screens

import UIKit.app.Screen
import UIKit.app.data.EvsColor
import UIKit.app.resources.Font
import UIKit.widgets.Text

object SelectWorkoutScreen: Screen("SelectWorkoutScreen") {
    private val txtAppName = Text("txtAppName")
    private val txtSelectWorkout = Text("txtSelectWorkout")

    private val marginTop = 50f

    override fun onCreate() {
        super.onCreate()
        txtAppName.apply {
            setText("ARWorkout")
            setResource(Font.StockFont.Small)
            setCenter(this@SelectWorkoutScreen.getWidth()/2f)
            setForegroundColor(EvsColor.Yellow)
            setY(marginTop)
            add(this)
        }

        txtSelectWorkout.apply {
            setText("Select Workout")
            setResource(Font.StockFont.Large)
            setCenter(this@SelectWorkoutScreen.getWidth()/2f)
            setForegroundColor(EvsColor.White)
            setY(this@SelectWorkoutScreen.getHeight()/2f-getHeight()/2f)
            add(this)
        }
    }
}