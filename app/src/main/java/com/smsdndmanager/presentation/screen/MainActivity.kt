package com.smsdndmanager.presentation.screen

import android.Manifest
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.smsdndmanager.presentation.theme.SmsDndManagerTheme
import com.smsdndmanager.service.MessageNotificationListenerService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmsDndManagerTheme {
                MainApp()
            }
        }
    }
}

sealed class Screen(val route: String, val title: String) {
    data object Numbers : Screen("numbers", "Authorized Numbers")
    data object Logs : Screen("logs", "Activity Log")
    data object Test : Screen("test", "Test")
    data object Settings : Screen("settings", "Settings")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {
    val navController = rememberNavController()
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf(Screen.Numbers, Screen.Logs, Screen.Test, Screen.Settings)
    val context = LocalContext.current
    
    // Check and request permissions
    var hasPermissions by remember { mutableStateOf(checkAllPermissions(context)) }
    
    if (!hasPermissions) {
        PermissionScreen(
            onPermissionsGranted = { hasPermissions = true }
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(items[selectedItem].title) }
            )
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        icon = {
                            when (screen) {
                                Screen.Numbers -> Icon(Icons.Default.Person, contentDescription = null)
                                Screen.Logs -> Icon(Icons.Default.List, contentDescription = null)
                                Screen.Test -> Icon(Icons.Default.Build, contentDescription = null)
                                Screen.Settings -> Icon(Icons.Default.Settings, contentDescription = null)
                            }
                        },
                        label = { Text(screen.title) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavigationGraph(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

@Composable
fun NavigationGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Numbers.route,
        modifier = modifier
    ) {
        composable(Screen.Numbers.route) {
            AuthorizedNumbersScreen()
        }
        composable(Screen.Logs.route) {
            LogsScreen()
        }
        composable(Screen.Test.route) {
            TestScreen()
        }
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}

private fun checkAllPermissions(context: Context): Boolean {
    val hasSmsPermission = ContextCompat.checkSelfPermission(
        context, Manifest.permission.RECEIVE_SMS
    ) == PackageManager.PERMISSION_GRANTED
    
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val hasDndAccess = notificationManager.isNotificationPolicyAccessGranted
    
    val hasNotificationAccess = hasNotificationListenerAccess(context)
    
    return hasSmsPermission && hasDndAccess && hasNotificationAccess
}

private fun hasNotificationListenerAccess(context: Context): Boolean {
    val cn = ComponentName(context, MessageNotificationListenerService::class.java)
    val flat = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
    return flat != null && flat.contains(cn.flattenToString())
}
