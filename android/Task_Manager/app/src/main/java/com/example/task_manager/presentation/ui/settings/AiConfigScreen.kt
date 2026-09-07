package com.example.task_manager.presentation.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiConfigScreen(
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    var selectedModel by remember { mutableStateOf("gemini") }
    var autoPriority by remember { mutableStateOf(true) }
    var autoSchedule by remember { mutableStateOf(true) }
    var autoSubtasks by remember { mutableStateOf(true) }
    var isSaved by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Cấu hình Trợ lý AI", fontWeight = FontWeight.Bold) },
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

            // AI Model Selection Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Chọn mô hình AI", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }

                    listOf(
                        "gemini" to "Gemini 1.5 Flash (Nhanh & Tối ưu hóa)",
                        "gpt" to "GPT-4o Mini (Phân tích chuyên sâu)"
                    ).forEach { (key, label) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedModel = key
                                    isSaved = false
                                }
                                .padding(vertical = 8.dp)
                        ) {
                            RadioButton(selected = selectedModel == key, onClick = {
                                selectedModel = key
                                isSaved = false
                            })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // AI Automation Toggles Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Tính năng thông minh", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

                    NotificationToggleItem(
                        title = "Tự động phân loại độ ưu tiên",
                        description = "Phân tích tiêu đề và mô tả để gợi ý độ ưu tiên (Thấp, Trung bình, Cao).",
                        checked = autoPriority,
                        onCheckedChange = {
                            autoPriority = it
                            isSaved = false
                        }
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

                    NotificationToggleItem(
                        title = "Phân tích & Tối ưu thời gian",
                        description = "Đưa ra lời khuyên phân bổ thời gian hợp lý dựa trên thói quen làm việc.",
                        checked = autoSchedule,
                        onCheckedChange = {
                            autoSchedule = it
                            isSaved = false
                        }
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

                    NotificationToggleItem(
                        title = "Gợi ý tự động chia nhỏ công việc",
                        description = "Tạo danh sách các bước nhỏ (sub-tasks) từ những mục tiêu lớn.",
                        checked = autoSubtasks,
                        onCheckedChange = {
                            autoSubtasks = it
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
                    Text("✓ Đã lưu cấu hình Trợ lý AI thành công!", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(16.dp))
                }
            }

            Button(
                onClick = { isSaved = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Lưu cấu hình AI", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
