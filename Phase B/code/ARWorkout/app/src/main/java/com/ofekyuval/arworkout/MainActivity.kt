package com.ofekyuval.arworkout

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.ofekyuval.arworkout.screens.BottomNavigationScreens
import com.ofekyuval.arworkout.screens.Screens
import com.ofekyuval.arworkout.ui.theme.ARWorkoutTheme
import com.ofekyuval.arworkout.ui.theme.BluePrimary
import com.ofekyuval.arworkout.ui.theme.BlueSecondary
import com.ofekyuval.arworkout.viewmodels.GlassesViewModel
import com.ofekyuval.arworkout.viewmodels.WorkoutsViewModel
import kotlinx.coroutines.MainScope

val mainScope = MainScope()
class MainActivity : ComponentActivity() {
    private lateinit var requestOverlayPermissionLauncher: ActivityResultLauncher<Intent>
    private val glassesViewModel: GlassesViewModel by viewModels<GlassesViewModel>()
    private val workoutsViewModel: WorkoutsViewModel by viewModels<WorkoutsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestOverlayPermissionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val msg = if (Settings.canDrawOverlays(this)) {
                getString(R.string.now_you_can_preview_glasses)
            } else {
                getString(R.string.you_must_allow_permission_for_glasses_preview)
            }
            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
        }
        glassesViewModel.initSdk(this)
        workoutsViewModel.loadWorkoutHistoryFromSP(this)

        setContent {
            ARWorkoutTheme {
                MainScreen()
            }
        }
    }

    fun requestOverlayPermission() {
        if (Settings.canDrawOverlays(this)) return
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        requestOverlayPermissionLauncher.launch(intent)
    }

    companion object {
        const val SP_NAME = "ARWorkout"
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val screens = arrayOf(BottomNavigationScreens.Home, BottomNavigationScreens.History)
    var selectedScreenIndex by remember { mutableIntStateOf(0) }
    val navControllers = Array(screens.size) { rememberNavController() }
    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            NavigationBar(
                modifier = Modifier.height(60.dp),
                containerColor = Color.White
            ) {
                screens.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = BluePrimary, indicatorColor = BlueSecondary),
                        icon = {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(id = screen.icon),
                                contentDescription = screen.label,
                                tint = if (selectedScreenIndex == index) Color.Black else Color.Black.copy(alpha = 0.3f)
                            )
                        },
                        selected = selectedScreenIndex == index,
                        onClick = {
                            if (navControllers.any { it.currentDestination?.route == Screens.ActiveWorkout.route }) {
                                Toast.makeText(context, context.getString(R.string.workout_is_currently_active), Toast.LENGTH_SHORT).show()
                                return@NavigationBarItem
                            }
                            if (selectedScreenIndex == index) {
                                navControllers[selectedScreenIndex].navigate(screens[selectedScreenIndex].route) {
                                    popUpTo(screens[selectedScreenIndex].route)
                                    launchSingleTop = true
                                }
                            } else {
                                selectedScreenIndex = index
                            }
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        }
                    )
                }
            }
        },
    ) { paddingValues ->
        when (screens[selectedScreenIndex]) {
            BottomNavigationScreens.Home -> HomeNavGraph(modifier = Modifier.padding(paddingValues), navController = navControllers[selectedScreenIndex])
            BottomNavigationScreens.History -> HistoryNavGraph(modifier = Modifier.padding(paddingValues), navController = navControllers[selectedScreenIndex])
        }
    }
}

@Preview
@Composable
private fun AppPrev() {
    ARWorkoutTheme {
        MainScreen()
    }
}