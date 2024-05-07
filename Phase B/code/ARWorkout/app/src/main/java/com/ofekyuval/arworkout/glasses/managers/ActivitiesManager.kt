package com.ofekyuval.arworkout.glasses.managers

import UIKit.app.data.CalibrationStatus
import UIKit.app.data.QuaternionData
import UIKit.app.data.SensorRate
import UIKit.app.data.SensorType
import UIKit.ar.utilities.Quaternion
import UIKit.services.IEvsInertialSensorsEvents
import UIKit.services.IEvsQuaternionSensorsEvents
import android.util.Log
import com.everysight.evskit.android.Evs
import com.ofekyuval.arworkout.glasses.activities.ActivityDetails
import com.ofekyuval.arworkout.glasses.activities.ActivityType

class ActivitiesManager {

    interface IActivitiesEvents {
        fun onActivityRecognized(activityType: ActivityType, activityDetails: ActivityDetails, startTs: Long, endTs: Long)
    }

    private val TAG = "ActivitiesManager"
    private var quat = Quaternion()
    private var isCalibrated = false
    private val accGlobal = FloatArray(3)
    private var accZminusG = 0f
    private val listeners = arrayListOf<IActivitiesEvents>()

    fun onCreate() {
        with(Evs.instance().sensors()) {
            setSensorsRate(SensorRate.rate0)
            enableInertialSensors()
            registerInertialSensorsEvents(sensorEvents)
            registerQuaternionSensorsEvents(quatEvents)
        }
    }
    fun onDestroy() {
        with(Evs.instance().sensors()) {
            disableInertialSensors()
            unregisterInertialSensorsEvents(sensorEvents)
            unregisterQuaternionSensorsEvents(quatEvents)
        }
    }

    fun registerActivitiesEvents(listener: IActivitiesEvents) {
        listeners.add(listener)
    }
    fun unregisterActivitiesEvents(listener: IActivitiesEvents) {
        listeners.remove(listener)
    }

    private fun notifyIfNeeded(avg: Float, startTs: Long, endTs: Long) {
        ActivityType.entries.forEach {
            it.activity.isPerformed(avg, startTs, endTs)?.let { activityDetails ->
                listeners.forEach { l -> l.onActivityRecognized(it, activityDetails, startTs, endTs) }
            }
        }
    }

    private val sensorEvents = object : IEvsInertialSensorsEvents {
        private var sum = 0f
        private var countNeg = 0
        private var countPos = 0
        private var startActivityTs = 0L
        override fun onSensor(sensorType: SensorType, timestampNs: Long, x: Float, y: Float, z: Float) {
//            Log.d(TAG, "onSensor: ${sensorType.name}: x=$x | y=$y | z=$z")
            if (sensorType == SensorType.acc && isCalibrated) {
                quat.rotateVector(x, y, z, accGlobal)
                accZminusG = accGlobal[2] - 9.807f
                if (accZminusG > 0) {
                    if (countPos > 10) {
                        if (countNeg > 10) { // assure good avg
                            val avg = sum / countNeg
                            notifyIfNeeded(avg, startActivityTs, System.currentTimeMillis())
                        }
                        sum = 0f
                        countNeg = 0
                    }
                    countPos++
                } else {
                    if (countNeg == 0) {
                        startActivityTs = System.currentTimeMillis()
                    }
                    sum += accZminusG
                    countNeg++
                    countPos = 0
                }
            }
        }
    }

    private val quatEvents = object : IEvsQuaternionSensorsEvents {
        override fun onQuaternion(timestampMs: Long, quaternionsData: QuaternionData, calibrationStatus: CalibrationStatus) {
            isCalibrated = calibrationStatus == CalibrationStatus.Calibrated
            if (isCalibrated) {
                quat.setValues(quaternionsData.x, quaternionsData.y, quaternionsData.z, quaternionsData.w)
            } else {
                Log.d(TAG, "onQuaternion: ${calibrationStatus.name}")
            }
        }
    }

    private fun nanoToMilli(nano: Long): Long {
        return nano / 1_000_000
    }

}