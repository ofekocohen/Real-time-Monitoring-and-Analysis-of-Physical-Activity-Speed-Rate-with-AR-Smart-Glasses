package com.ofekyuval.arworkout.viewmodels

import UIKit.app.utils.AutoBrightnessGainProvider
import UIKit.services.AppErrorCode
import UIKit.services.IEvsAppEvents
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.everysight.evskit.android.Evs
import com.ofekyuval.arworkout.BuildConfig
import com.ofekyuval.arworkout.glasses.screens.SelectWorkoutScreen

class GlassesViewModel: ViewModel() {
    private val TAG = "GlassesViewModel"

    fun initSdk(context: Context) {
        if (Evs.wasInitialized()) return
        Evs.init(context)
        val options = HashSet<String>()
        if (BuildConfig.DEBUG) options.add("supportSimulator")
        Evs.instance().startExt(options)

        Evs.instance().display().autoBrightness().setProvider(AutoBrightnessGainProvider())
        Evs.instance().display().autoBrightness().enable(true)

        Evs.startDefaultLogger()
        Evs.instance().registerAppEvents(appEvents)
        with(Evs.instance().comm()){
            if(hasConfiguredDevice()) connect()
        }

        Evs.instance().screens().addScreen(SelectWorkoutScreen)
    }

    override fun onCleared() {
        super.onCleared()
        Evs.instance().stop()
    }

    val appEvents = object : IEvsAppEvents {
        override fun onError(errCode: AppErrorCode, description: String) {}
        override fun onReady() {
            Log.d(TAG, "onReady: ")
            Evs.instance().display().turnDisplayOn()
            Evs.instance().unregisterAppEvents(this)
        }
        override fun onUnReady() {
        }
    }
}