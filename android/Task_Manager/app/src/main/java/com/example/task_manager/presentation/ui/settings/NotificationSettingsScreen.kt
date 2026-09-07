package com.example.task_manager.presentation.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    var taskReminder by remember { mutableStateOf(true) }
    var dailyReport by remember { mutableStateOf(true) }
    var priorityAlert by remember { mutableStateOf(true) }
    var soundVibration by remember { mutableStateOf(true) }
    var isSaved by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Cài đặt thông báo", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            Spacer(modifier = Modifier.height(8.dp))

            // Notification Toggles Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Tùy chọn nhận thông báo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }

                    NotificationToggleItem(
                        title = "Nhắc nhở công việc sắp tới",
                        description = "Gửi thông báo trước khi deadline công việc diễn ra 15 phút.",
                        checked = taskReminder,
                        onCheckedChange = {
                            taskReminder = it
                            isSaved = false
                        }
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

                    NotificationToggleItem(
                        title = "Báo cáo tổng kết hàng ngày",
                        description = "Tổng kết danh sách công việc đã hoàn thành vào lúc 20:00 mỗi tối.",
                        checked = dailyReport,
                        onCheckedChange = {
                            dailyReport = it
                            isSaved = false
                        }
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

                    NotificationToggleItem(
                        title = "Cảnh báo độ ưu tiên cao",
                        description = "Thông báo ngay lập tức đối với các nhiệm vụ được đánh dấu quan trọng.",
                        checked = priorityAlert,
                        onCheckedChange = {
                            priorityAlert = it
                            isSaved = false
                        }
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

                    NotificationToggleItem(
                        title = "Âm thanh & Rung",
                        description = "Phát âm thanh báo hiệu và rung thiết bị khi nhận thông báo mới.",
                        checked = soundVibration,
                        onCheckedChange = {
                            soundVibration = it
                            isSaved = false
                        }
                    )
                }
            }

            if (isSaved) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("✓ Đã lưu cài đặt thông báo thành công!", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(16.dp))
                }
            }

            Button(
                onClick = { isSaved = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Lưu cài đặt thông báo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun NotificationToggleItem(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
