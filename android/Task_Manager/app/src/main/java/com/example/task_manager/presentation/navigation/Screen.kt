package com.example.task_manager.presentation.ui.navigation

sealed class Screen(val route: String) {
    object Login: Screen("login")
    object Register: Screen("register")
    object ForgotPassword: Screen("forgot_password")
    object Home: Screen("home")
    object TaskList: Screen("task_list")
    object TaskDetail: Screen("task_detail/{taskId}") {
        fun createRoute(taskId: String) = "task_detail/$taskId"
    }

    object CreateTask: Screen("create_task")
    object Pomodoro: Screen("pomodoro")
    object Settings: Screen("settings")
    object PersonalInfo: Screen("personal_info")
    object Security: Screen("security")
    object NotificationSettings: Screen("notification_settings")
    object AiConfig: Screen("ai_config")
    object PomodoroSettings: Screen("pomodoro_settings")
    object TermsAndConditions: Screen("terms_and_conditions")
    object PrivacyPolicy: Screen("privacy_policy")
}