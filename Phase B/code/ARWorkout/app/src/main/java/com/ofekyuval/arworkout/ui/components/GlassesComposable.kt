package com.ofekyuval.arworkout.ui.components

import UIKit.app.Screen
import UIKit.app.data.BatteryLevel
import android.content.Context
import android.os.Handler
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.everysight.evskit.android.Evs
import com.everysight.evskit.android.R
import com.everysight.evskit.android.compose.viewmodels.EvsGlassesEventsViewModel
import com.everysight.evskit.android.compose.viewmodels.EvsGlassesViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ofekyuval.arworkout.MainActivity
import com.ofekyuval.arworkout.mainScope
import kotlinx.coroutines.launch

@Composable
internal fun GlassesInfo(modifier: Modifier = Modifier, glassesViewModel: EvsGlassesViewModel = viewModel(), appEvsGlassesViewModel: EvsGlassesEventsViewModel = viewModel(), isClickable: Boolean = true, adjustConfigurations: String = "") {
    GlassesInfoInternal(
        modifier = modifier,
        name = glassesViewModel.name,
        isReady = glassesViewModel.isReady,
        batteryPercentage =  appEvsGlassesViewModel.batteryPercentage,
        batteryLevel = appEvsGlassesViewModel.batteryLevel,
        isCharging = appEvsGlassesViewModel.isChargerConnected,
        connectionState = glassesViewModel.connectionState,
        hasConfiguredDevice = glassesViewModel.hasConfiguredDevice,
        isClickable = isClickable,
        adjustConfigurations = adjustConfigurations
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun GlassesInfoInternal(modifier: Modifier = Modifier, name: String, isReady: Boolean, batteryPercentage: Int, batteryLevel: BatteryLevel, isCharging: Boolean, connectionState: EvsGlassesViewModel.GlassesConnectionStateEnum, hasConfiguredDevice: Boolean, isClickable: Boolean = true, adjustConfigurations: String = "") {
    val context = LocalContext.current
    val isConnected = connectionState == EvsGlassesViewModel.GlassesConnectionStateEnum.CONNECTED
    Card(
        modifier = modifier
            .height(60.dp),
        border = BorderStroke(0.5.dp, LightGray),
        shape = RoundedCornerShape(100),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        )
    ) {
        if (isReady) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 16.dp, end = 24.dp, bottom = 16.dp)
                    .combinedClickable(
                        enabled = isClickable,
                        onClick = {
                            Evs.instance().showUI("adjust:$adjustConfigurations")
                        },
                        onLongClick = {
                            showDialog(context, null, "Forget This Device", "Are you sure?", onOkAction = {
                                Evs.instance().comm().setDeviceInfo(null, null)
                                Evs.instance().showUI("configure")
                            })
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.Start)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.glasses_status),
                        contentDescription = stringResource(id = R.string.glasses),
                        tint = if (isConnected) Color.Black else Gray,
                        modifier = Modifier.clickable {
                            showGlassesPreview(context)
                        }
                    )
                    Text(name, fontSize = 18.sp, fontWeight = FontWeight(500))
                }
                Divider(
                    modifier = Modifier
                        .width(2.dp)
                        .height(25.dp),
                    thickness = 2.dp,
                    color = LightGray
                )
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("${batteryPercentage}%", fontSize = 18.sp, fontWeight = FontWeight(500))
                    BatteryImageComposable(isCharging = isCharging, batteryLevel = batteryLevel)
                }
            }
        }
        else {
            Box(contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .combinedClickable(
                        enabled = isClickable,
                        onClick = {
                            if (hasConfiguredDevice) {
                                showDialog(context, null, "Forget This Device", "Are you sure?", onOkAction = {
                                    Evs.instance().comm().setDeviceInfo(null, null)
                                    Evs.instance().showUI("configure")
                                })
                            } else {
                                Evs.instance().showUI("configure")
                            }
                        }
                    )
            ) {
                Text(
                    stringResource(
                        if (hasConfiguredDevice) {
                            when (connectionState) {
                                EvsGlassesViewModel.GlassesConnectionStateEnum.CONNECTED -> R.string.verifying
                                EvsGlassesViewModel.GlassesConnectionStateEnum.CONNECTING -> R.string.connecting
                                EvsGlassesViewModel.GlassesConnectionStateEnum.DISCONNECTED -> R.string.not_connected
                                EvsGlassesViewModel.GlassesConnectionStateEnum.FAILED_TO_CONNECT -> R.string.failed_to_connect
                            }
                        } else {
                            R.string.configure_glasses
                        }
                    ),
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight(500),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview
@Composable
private fun Connected() {
    GlassesInfoInternal(
        modifier = Modifier.fillMaxWidth(),
        name = "EV00123",
        isReady = true,
        batteryPercentage = 95,
        batteryLevel = BatteryLevel.High,
        isCharging = true,
        connectionState = EvsGlassesViewModel.GlassesConnectionStateEnum.CONNECTED,
        hasConfiguredDevice = true
    )
}

@Preview
@Composable
private fun Connected60() {
    GlassesInfoInternal(
        modifier = Modifier.fillMaxWidth(),
        name = "EV00123",
        isReady = true,
        batteryPercentage = 60,
        batteryLevel = BatteryLevel.Medium,
        isCharging = false,
        connectionState = EvsGlassesViewModel.GlassesConnectionStateEnum.CONNECTED,
        hasConfiguredDevice = true
    )
}

@Preview
@Composable
private fun Connected10() {
    GlassesInfoInternal(
        modifier = Modifier.fillMaxWidth(),
        name = "EV00123",
        isReady = true,
        batteryPercentage = 10,
        batteryLevel = BatteryLevel.Critical,
        isCharging = false,
        connectionState = EvsGlassesViewModel.GlassesConnectionStateEnum.CONNECTED,
        hasConfiguredDevice = true
    )
}

@Preview
@Composable
private fun Connecting() {
    GlassesInfoInternal(
        modifier = Modifier.fillMaxWidth(),
        name = "EV00123",
        isReady = false,
        batteryPercentage = 0,
        batteryLevel = BatteryLevel.Unknown,
        isCharging = false,
        connectionState = EvsGlassesViewModel.GlassesConnectionStateEnum.CONNECTING,
        hasConfiguredDevice = true
    )
}

@Preview
@Composable
private fun Disconnected() {
    GlassesInfoInternal(
        modifier = Modifier.fillMaxWidth(),
        name = "EV00123",
        isReady = false,
        batteryPercentage = 0,
        batteryLevel = BatteryLevel.Unknown,
        isCharging = false,
        connectionState = EvsGlassesViewModel.GlassesConnectionStateEnum.DISCONNECTED,
        hasConfiguredDevice = true
    )
}

@Preview
@Composable
private fun NotConfigured() {
    GlassesInfoInternal(
        modifier = Modifier.fillMaxWidth(),
        name = "",
        isReady = false,
        batteryPercentage = 0,
        batteryLevel = BatteryLevel.Unknown,
        isCharging = false,
        connectionState = EvsGlassesViewModel.GlassesConnectionStateEnum.DISCONNECTED,
        hasConfiguredDevice = false
    )
}

private fun showDialog(context:Context, iconId:Int?, title:String, msg:String, onOkAction:()->Unit = {},onCancelAction:()->Unit = {}) {
    with(MaterialAlertDialogBuilder(context, R.style.EvsAlertDialogTheme)) {
        setTitle(title)
        setMessage(msg)
        if (iconId != null) setIcon(iconId)
        setPositiveButton(android.R.string.yes) { _, _ ->
            Handler().postDelayed({
                onOkAction()
            }, 0)
        } // A null listener allows the button to dismiss the dialog and take no further action.
        setNegativeButton(android.R.string.no) {_, _->
            onCancelAction()
        }
        setOnCancelListener {
        }
        setCancelable(false)
        show()
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
private fun showGlassesPreview(context: Context) {
    if (Settings.canDrawOverlays(context)) {
        Evs.instance().showUI("preview")
        mainScope.launch {
            (Evs.instance().screens().getTopmostScreen() as? Screen)?.let {
                it.getElements().firstOrNull()?.invalidate()
            }
        }
    } else {
        val mainActivity = context as? MainActivity
        if (mainActivity == null) {
            Toast.makeText(context, "You don't have permission", Toast.LENGTH_SHORT).show()
        } else {
            mainActivity.requestOverlayPermission()
        }
    }
}