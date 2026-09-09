package com.example.task_manager.presentation.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.task_manager.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToPersonalInfo: () -> Unit = {},
    onNavigateToSecurity: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToAiConfig: () -> Unit = {},
    onNavigateToPomodoroSettings: () -> Unit = {},
    onNavigateToTerms: () -> Unit = {},
    onNavigateToPrivacy: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    var showThemeDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }

    val displayName = uiState.userName?.ifBlank { null } ?: stringResource(R.string.default_user)
    val displayEmail = uiState.userEmail?.ifBlank { null } ?: "user@example.com"
    val initialLetter = (uiState.userName?.firstOrNull() ?: uiState.userEmail?.firstOrNull() ?: 'U').uppercaseChar().toString()

    LaunchedEffect(uiState.isLoggedOut) {
        if (uiState.isLoggedOut) onLogout()
    }

    // --- QUICK SELECTION DIALOGS ---

    // Theme Dialog
    if (showThemeDialog) {
        var selectedTheme by remember { mutableStateOf("system") }
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Tùy chọn giao diện", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("system" to "Hệ thống (Mặc định)", "dark" to "Giao diện Tối (Dark)", "light" to "Giao diện Sáng (Light)").forEach { (key, label) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedTheme = key }
                                .padding(8.dp)
                        ) {
                            RadioButton(selected = selectedTheme == key, onClick = { selectedTheme = key })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(label, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showThemeDialog = false }) {
                    Text("Áp dụng")
                }
            },
            dismissButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }

    // Language Dialog
    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text(stringResource(R.string.select_language)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.selectLanguage("vi")
                                showLanguageDialog = false
                            }
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = uiState.currentLanguage == "vi",
                            onClick = {
                                viewModel.selectLanguage("vi")
                                showLanguageDialog = false
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.lang_vietnamese), style = MaterialTheme.typography.bodyLarge)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.selectLanguage("en")
                                showLanguageDialog = false
                            }
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = uiState.currentLanguage == "en",
                            onClick = {
                                viewModel.selectLanguage("en")
                                showLanguageDialog = false
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.lang_english), style = MaterialTheme.typography.bodyLarge)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    // --- MAIN SCREEN ---
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.settings),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToPersonalInfo() },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            initialLetter,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            displayName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            displayEmail,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Account section
            SettingsSection(title = stringResource(R.string.section_account)) {
                SettingsItem(
                    icon = Icons.Default.Person,
                    title = stringResource(R.string.personal_info),
                    onClick = onNavigateToPersonalInfo
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                )
                SettingsItem(
                    icon = Icons.Default.Lock,
                    title = stringResource(R.string.password_security),
                    onClick = onNavigateToSecurity
                )
            }

            // Legal & Info section
            SettingsSection(title = "Thông tin & Pháp lý") {
                SettingsItem(
                    icon = Icons.Default.Description,
                    title = stringResource(R.string.terms_of_service),
                    onClick = onNavigateToTerms
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                )
                SettingsItem(
                    icon = Icons.Default.Security,
                    title = stringResource(R.string.privacy_policy),
                    onClick = onNavigateToPrivacy
                )
            }

            // Preferences section
            SettingsSection(title = stringResource(R.string.section_preferences)) {
                SettingsItem(
                    icon = Icons.Default.Palette,
                    title = stringResource(R.string.theme),
                    subtitle = stringResource(R.string.theme_system),
                    onClick = { showThemeDialog = true }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                )
                SettingsItem(
                    icon = Icons.Default.Language,
                    title = stringResource(R.string.language),
                    subtitle = if (uiState.currentLanguage == "vi") stringResource(R.string.lang_vietnamese) else stringResource(R.string.lang_english),
                    onClick = { showLanguageDialog = true }
                )
            }

            // Productivity section
            SettingsSection(title = stringResource(R.string.section_productivity)) {
                SettingsItem(
                    icon = Icons.Default.Notifications,
                    title = stringResource(R.string.notifications),
                    onClick = onNavigateToNotifications
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                )
                SettingsItem(
                    icon = Icons.Default.Timer,
                    title = stringResource(R.string.pomodoro_timer),
                    subtitle = stringResource(R.string.pomodoro_setting_sub),
                    onClick = onNavigateToPomodoroSettings
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                )
                SettingsItem(
                    icon = Icons.Default.AutoAwesome,
                    title = stringResource(R.string.ai_config),
                    onClick = onNavigateToAiConfig
                )
            }

            // Logout
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.logout() }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        stringResource(R.string.logout),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.weight(1f)
                    )
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.error
                        )
                    } else {
                        Icon(
                            Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            // Version info
            Text(
                "${stringResource(R.string.app_version)}\n${stringResource(R.string.app_tagline)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            title,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(content = content)
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        if (subtitle != null) {
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.size(18.dp)
        )
    }
}