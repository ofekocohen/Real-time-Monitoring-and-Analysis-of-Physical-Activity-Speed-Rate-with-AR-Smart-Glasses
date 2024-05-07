package com.ofekyuval.arworkout.ui.components

import UIKit.app.data.BatteryLevel
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.everysight.evskit.android.R

@Composable
internal fun BatteryImageComposable(
    modifier: Modifier = Modifier,
    isCharging: Boolean,
    batteryLevel: BatteryLevel
) {
    Image(
        painter = painterResource(
            id = when (batteryLevel) {
                BatteryLevel.High -> {
                    if (isCharging) {
                        R.drawable.battery_high_charge_horizontal
                    } else {
                        R.drawable.battery_high_horizontal
                    }
                }

                BatteryLevel.Medium -> if (isCharging) {
                        R.drawable.battery_medium_charge_horizontal
                } else {
                    R.drawable.battery_medium_horizontal
                }

                else -> if (isCharging) {
                    R.drawable.battery_low_charge_horizontal
                } else {
                    R.drawable.battery_low_horizontal
                }
            }
        ),
        contentDescription = stringResource(id = R.string.battery),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun High_Charging() {
    BatteryImageComposable(isCharging = true, batteryLevel = BatteryLevel.High)
}

@Preview(showBackground = true)
@Composable
private fun Medium_Charging() {
    BatteryImageComposable(isCharging = true, batteryLevel = BatteryLevel.Medium)
}

@Preview(showBackground = true)
@Composable
private fun Low_Charging() {
    BatteryImageComposable(isCharging = true, batteryLevel = BatteryLevel.Low)
}

@Preview(showBackground = true)
@Composable
private fun Critical_Charging() {
    BatteryImageComposable(isCharging = true, batteryLevel = BatteryLevel.Critical)
}

@Preview(showBackground = true)
@Composable
private fun High_Not_Charging() {
    BatteryImageComposable(isCharging = false, batteryLevel = BatteryLevel.High)
}

@Preview(showBackground = true)
@Composable
private fun Medium_Not_Charging() {
    BatteryImageComposable(isCharging = false, batteryLevel = BatteryLevel.Medium)
}

@Preview(showBackground = true)
@Composable
private fun Low_Not_Charging() {
    BatteryImageComposable(isCharging = false, batteryLevel = BatteryLevel.Low)
}

@Preview(showBackground = true)
@Composable
private fun Critical_Not_Charging() {
    BatteryImageComposable(isCharging = false, batteryLevel = BatteryLevel.Critical)
}