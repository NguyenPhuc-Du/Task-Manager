package com.example.task_manager.presentation.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.task_manager.presentation.ui.navigation.Screen
import com.example.task_manager.presentation.ui.auth.ForgotPasswordScreen
import com.example.task_manager.presentation.ui.auth.LoginScreen
import com.example.task_manager.presentation.ui.auth.RegisterScreen
import com.example.task_manager.presentation.ui.home.HomeScreen
import com.example.task_manager.presentation.ui.pomodoro.PomodoroScreen
import com.example.task_manager.presentation.ui.settings.AiConfigScreen
import com.example.task_manager.presentation.ui.settings.NotificationSettingsScreen
import com.example.task_manager.presentation.ui.settings.PersonalInfoScreen
import com.example.task_manager.presentation.ui.settings.PomodoroSettingsScreen
import com.example.task_manager.presentation.ui.settings.SecurityScreen
import com.example.task_manager.presentation.ui.settings.SettingsScreen
import com.example.task_manager.presentation.ui.settings.TermsScreen
import com.example.task_manager.presentation.ui.task.CreateTaskScreen
import com.example.task_manager.presentation.ui.task.TaskDetailScreen
import com.example.task_manager.presentation.ui.task.TaskListScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { 300 },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -300 },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -300 },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { 300 },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        }
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onNavigateToForgotPassword = { navController.navigate(Screen.ForgotPassword.route) },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onNavigateToTerms = { navController.navigate(Screen.TermsAndConditions.route) },
                onNavigateToPrivacy = { navController.navigate(Screen.PrivacyPolicy.route) },
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackToLogin = { navController.popBackStack() }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToTaskList = { navController.navigate(Screen.TaskList.route) },
                onNavigateToPomodoro = { navController.navigate(Screen.Pomodoro.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(Screen.TaskList.route) {
            TaskListScreen(
                onNavigateToCreateTask = { navController.navigate(Screen.CreateTask.route) },
                onNavigateToTaskDetail = { taskId ->
                    navController.navigate(Screen.TaskDetail.createRoute(taskId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.TaskDetail.route) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId") ?: return@composable
            TaskDetailScreen(
                taskId = taskId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.CreateTask.route) {
            CreateTaskScreen(
                onBack = { navController.popBackStack() },
                onTaskCreated = { navController.popBackStack() }
            )
        }

        composable(Screen.Pomodoro.route) {
            PomodoroScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onNavigateToPersonalInfo = { navController.navigate(Screen.PersonalInfo.route) },
                onNavigateToSecurity = { navController.navigate(Screen.Security.route) },
                onNavigateToNotifications = { navController.navigate(Screen.NotificationSettings.route) },
                onNavigateToAiConfig = { navController.navigate(Screen.AiConfig.route) },
                onNavigateToPomodoroSettings = { navController.navigate(Screen.PomodoroSettings.route) },
                onNavigateToTerms = { navController.navigate(Screen.TermsAndConditions.route) },
                onNavigateToPrivacy = { navController.navigate(Screen.PrivacyPolicy.route) },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.PersonalInfo.route) {
            PersonalInfoScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Security.route) {
            SecurityScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.NotificationSettings.route) {
            NotificationSettingsScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.AiConfig.route) {
            AiConfigScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.PomodoroSettings.route) {
            PomodoroSettingsScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.TermsAndConditions.route) {
            TermsScreen(
                title = "Điều khoản dịch vụ",
                isPrivacyPolicy = false,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.PrivacyPolicy.route) {
            TermsScreen(
                title = "Chính sách bảo mật",
                isPrivacyPolicy = true,
                onBack = { navController.popBackStack() }
            )
        }
    }
}