package com.example.task_manager

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.task_manager.data.datastore.UserPreferences
import com.example.task_manager.presentation.navigation.NavGraph
import com.example.task_manager.presentation.ui.navigation.Screen
import com.example.task_manager.ui.theme.Task_ManagerTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

class LocalizedContext(
    base: Context,
    configuration: Configuration
) : ContextWrapper(base) {
    private val localizedContext: Context = base.createConfigurationContext(configuration)

    override fun getResources(): Resources {
        return localizedContext.resources
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val currentLanguage by userPreferences.language.collectAsState(initial = "vi")
            val tokenState by userPreferences.token.collectAsState(initial = "INITIALIZING")

            val locale = remember(currentLanguage) { Locale(currentLanguage) }
            val currentConfig = LocalConfiguration.current
            val configuration = remember(currentLanguage, currentConfig) {
                Configuration(currentConfig).apply {
                    setLocale(locale)
                }
            }
            val baseContext = LocalContext.current
            val localizedContext = remember(currentLanguage, baseContext) {
                LocalizedContext(baseContext, configuration)
            }

            CompositionLocalProvider(
                LocalContext provides localizedContext,
                LocalConfiguration provides configuration
            ) {
                Task_ManagerTheme {
                    if (tokenState == "INITIALIZING") {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    } else {
                        val startDestination = if (!tokenState.isNullOrBlank()) {
                            Screen.Home.route
                        } else {
                            Screen.Login.route
                        }
                        val navController = rememberNavController()
                        NavGraph(
                            navController = navController,
                            startDestination = startDestination
                        )
                    }
                }
            }
        }
    }
}