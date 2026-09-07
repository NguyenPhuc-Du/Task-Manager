package com.example.task_manager.presentation.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsScreen(
    title: String,
    isPrivacyPolicy: Boolean = false,
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.Bold) },
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

            // Header Banner Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isPrivacyPolicy) Icons.Default.Security else Icons.Default.Description,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Cập nhật lần cuối: 07/09/2026",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            if (isPrivacyPolicy) {
                TermsSection(
                    sectionNumber = "1",
                    heading = "Thu thập thông tin",
                    body = "Chúng tôi thu thập các thông tin cá nhân cơ bản như Địa chỉ Email và Tên hiển thị khi bạn đăng ký tài khoản để cung cấp dịch vụ quản lý công việc và đồng bộ hóa giữa các thiết bị."
                )

                TermsSection(
                    sectionNumber = "2",
                    heading = "Bảo mật dữ liệu",
                    body = "Mật khẩu của bạn được mã hóa một chiều bằng thuật toán Bcrypt bảo mật cao. Dữ liệu công việc của bạn được lưu trữ an toàn trong cơ sở dữ liệu và chỉ có bạn mới có quyền truy cập."
                )

                TermsSection(
                    sectionNumber = "3",
                    heading = "Chia sẻ thông tin",
                    body = "Chúng tôi cam kết không bán, trao đổi hoặc chia sẻ thông tin cá nhân của bạn cho bất kỳ bên thứ ba nào ngoại trừ các trường hợp phục vụ tính năng AI theo yêu cầu của bạn."
                )

                TermsSection(
                    sectionNumber = "4",
                    heading = "Quyền của người dùng",
                    body = "Bạn có quyền yêu cầu trích xuất, chỉnh sửa hoặc xóa vĩnh viễn tài khoản và toàn bộ dữ liệu cá nhân của mình bất kỳ lúc nào thông qua cài đặt ứng dụng."
                )
            } else {
                TermsSection(
                    sectionNumber = "1",
                    heading = "Quyền sử dụng dịch vụ",
                    body = "Khi sử dụng ứng dụng AI Task Manager, bạn đồng ý tuân thủ các quy định dịch vụ và sử dụng ứng dụng cho các mục đích hợp pháp, không vi phạm pháp luật hiện hành."
                )

                TermsSection(
                    sectionNumber = "2",
                    heading = "Tài khoản người dùng",
                    body = "Bạn có trách nhiệm bảo mật thông tin đăng nhập tài khoản của mình. Mọi hoạt động phát sinh dưới tài khoản của bạn sẽ do bạn chịu trách nhiệm hoàn toàn."
                )

                TermsSection(
                    sectionNumber = "3",
                    heading = "Tính năng AI & Dịch vụ",
                    body = "Các gợi ý và tính năng hỗ trợ từ AI được cung cấp nhằm mục đích trợ giúp tối ưu hóa công việc. Người dùng tự chịu trách nhiệm đối với các quyết định dựa trên các đề xuất này."
                )

                TermsSection(
                    sectionNumber = "4",
                    heading = "Thay đổi điều khoản",
                    body = "Chúng tôi có quyền điều chỉnh nội dung điều khoản này bất kỳ lúc nào để phù hợp với sự phát triển của ứng dụng. Thay đổi sẽ có hiệu lực ngay khi được cập nhật."
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun TermsSection(
    sectionNumber: String,
    heading: String,
    body: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Điều $sectionNumber: $heading",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
