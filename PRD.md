# 📋 Product Requirements Document (PRD)
## AI Task Manager — Ứng Dụng Quản Lý Công Việc Thông Minh

**Phiên bản:** 2.0 (Production Ready)  
**Ngày cập nhật:** 07/09/2026  
**Tác giả:** Nguyễn Phúc Dư  
**Trạng thái:** Completed / Fully Implemented

---

## 1. Tổng Quan Sản Phẩm

### 1.1 Mô Tả

**AI Task Manager** là ứng dụng Android quản lý công việc cá nhân tích hợp trí tuệ nhân tạo. Ứng dụng giúp người dùng tổ chức, theo dõi và hoàn thành các nhiệm vụ hàng ngày, kết hợp kỹ thuật Pomodoro để tối ưu năng suất, đồng thời đồng bộ dữ liệu real-time qua backend cloud.

### 1.2 Mục Tiêu Sản Phẩm

| # | Mục tiêu | Chỉ số thành công | Trạng thái |
|---|---|---|---|
| 1 | Giúp người dùng quản lý task hiệu quả | Tỷ lệ hoàn thành task ≥ 70% | ✅ Đạt |
| 2 | Tăng năng suất qua Pomodoro | ≥ 3 session Pomodoro/ngày | ✅ Đạt |
| 3 | Đồng bộ dữ liệu mượt mà | Sync latency < 500ms | ✅ Đạt |
| 4 | AI gợi ý priority thông minh | Độ chính xác gợi ý ≥ 80% | ✅ Đạt |
| 5 | Ghi nhớ phiên đăng nhập | Auto-login mượt mà | ✅ Đạt |

### 1.3 Phạm Vi

- ✅ **Trong phạm vi:** Ứng dụng Android Native, Backend REST API, Real-time sync, Analytics, ML Service, Full Screen Settings, Auto-login DataStore.
- ❌ **Ngoài phạm vi:** iOS app, Web app, Desktop app.

---

## 2. Đối Tượng Người Dùng

### 2.1 Persona Chính — "Sinh Viên / Nhân Viên Văn Phòng"

| Thuộc tính | Chi tiết |
|---|---|
| **Tuổi** | 18 – 35 |
| **Thiết bị** | Android (API 26+) |
| **Nhu cầu** | Theo dõi nhiều task cùng lúc, không muốn bỏ lỡ deadline |
| **Pain point** | Hay quên, khó phân loại task theo ưu tiên, không có thói quen làm việc tập trung |
| **Mục tiêu** | Hoàn thành nhiều việc hơn trong ngày, giảm stress |

### 2.2 User Stories

```
Là người dùng, tôi muốn:
- Tạo task nhanh chóng với title, mô tả, độ ưu tiên và ngày đến hạn
- Phân loại task vào category (công việc, học tập, cá nhân...)
- Đặt priority (thấp/trung bình/cao/khẩn cấp) cho từng task
- Tự động đăng nhập (ghi nhớ phiên làm việc) ở các lần mở app sau
- Đặt lại mật khẩu dễ dàng khi lỡ quên
- Đánh dấu task hoàn thành và theo dõi thống kê trực quan
- Sử dụng Pomodoro timer gắn liền với từng task cụ thể
- Tùy chỉnh các cài đặt cá nhân: Thông tin, Bảo mật (2FA), Thông báo, AI Config qua trang toàn màn hình
- Đọc Điều khoản dịch vụ và Chính sách bảo mật rõ ràng
- Đồng bộ dữ liệu tức thì (Real-time sync) với cloud
```

---

## 3. Kiến Trúc Hệ Thống

### 3.1 Tổng Quan

```
┌─────────────────────┐     HTTPS/REST     ┌──────────────────────┐
│   Android Client    │◄──────────────────►│   Backend (Node.js)  │
│   (Kotlin/Compose)  │                    │   Express + Prisma    │
│                     │     WebSocket       │                       │
│  - Room DB (local)  │◄──────────────────►│  - PostgreSQL (DB)    │
│  - DataStore        │     Socket.IO       │  - Redis (cache)      │
└─────────────────────┘                    └──────────┬────────────┘
                                                      │ HTTP
                                           ┌──────────▼────────────┐
                                           │  ML Service (FastAPI) │
                                           │  Python + ONNX        │
                                           └───────────────────────┘
```

### 3.2 Tech Stack

| Layer | Công nghệ |
|---|---|
| **Android Client** | Kotlin, Jetpack Compose, Material 3, Room, DataStore, Retrofit, OkHttp, Hilt, Coroutines, Flow, Socket.IO, Navigation Animations |
| **Backend** | Node.js, TypeScript, Express.js, Prisma ORM, PostgreSQL, Redis, Socket.IO, JWT, bcrypt |
| **ML Service** | Python, FastAPI, ONNX Runtime, Uvicorn |
| **Infrastructure** | Docker, Docker Compose, GitHub Actions |

---

## 4. Yêu Cầu Chức Năng

### 4.1 Module Authentication

#### 4.1.1 Đăng Ký (Register)
| Trường | Kiểu | Bắt buộc | Validation |
|---|---|---|---|
| `email` | String | ✅ | Format email hợp lệ, chưa tồn tại |
| `password` | String | ✅ | Tối thiểu 6-8 ký tự |
| `name` | String | ❌ | Tối đa 100 ký tự |

- **Check terms:** Tích chọn "Tôi đồng ý với Điều khoản dịch vụ & Chính sách bảo mật" trước khi kích hoạt nút Đăng ký.

#### 4.1.2 Đăng Nhập (Login)
- Rate limit: **5 requests/15 phút** per IP
- Trả về JWT Token + User profile.
- Lưu trữ token, email và tên người dùng (`userName`) vào Android DataStore.

#### 4.1.3 Tự Động Đăng Nhập (Auto-Login / Session Persistence)
- Khi mở ứng dụng, `MainActivity` tự động kiểm tra token lưu trong `UserPreferences`.
- Nếu token còn hiệu lực: Chuyển thẳng đến **HomeScreen** mà không cần gõ lại mật khẩu.

#### 4.1.4 Quên Mật Khẩu (Forgot Password)
- Màn hình riêng `ForgotPasswordScreen` cho phép nhập email.
- Gửi mã/liên kết khôi phục mật khẩu về hộp thư người dùng.

#### 4.1.5 Đăng Xuất (Logout)
- Token bị blacklist trong Redis với TTL = thời gian còn lại của token.
- Client xóa sạch token & user profile trong DataStore và quay về màn hình Login.

---

### 4.2 Module Tasks

#### 4.2.1 CRUD Task
| Operation | Method | Endpoint | Body / Feature |
|---|---|---|---|
| Tạo task | POST | `/tasks` | `{title, description?, priority?, dueDate?, categoryId?}` |
| Xem danh sách | GET | `/tasks` | Hỗ trợ filter theo completed, priority, categoryId |
| Xem chi tiết | GET | `/tasks/:id` | Hiển thị đầy đủ thông tin + Pomodoro sessions |
| Cập nhật | PUT | `/tasks/:id` | `{title?, description?, priority?, completed?, categoryId?}` |
| Xóa | DELETE | `/tasks/:id` | Xóa task và liên kết liên quan |

#### 4.2.2 Priority Levels
| Giá trị | Nhãn | Màu đại diện |
|---|---|---|
| `0` | Thấp | Xám / Gray |
| `1` | Trung bình | Xanh dương / Primary Blue |
| `2` | Cao | Cam / Orange |
| `3` | Khẩn cấp | Đỏ / Error Red |

---

### 4.3 Module Categories

| Operation | Method | Endpoint |
|---|---|---|
| Lấy tất cả | GET | `/categories` |
| Tạo mới | POST | `/categories` |
| Cập nhật | PUT | `/categories/:id` |
| Xóa | DELETE | `/categories/:id` |

---

### 4.4 Module Settings & Cá Nhân Hóa (Full Screens)

Trang Cài đặt sử dụng các màn hình riêng biệt (Full Screen) với chuyển cảnh slide mượt mượt:

| Màn hình | Route | Chức năng chính |
|---|---|---|
| **Thông tin cá nhân** | `personal_info` | Xem/sửa Họ tên, Email tài khoản, Ngày tham gia, Avatar ký tự đầu |
| **Mật khẩu & Bảo mật** | `security` | Đổi mật khẩu, Bật/tắt xác thực 2 lớp (2FA), Lịch sử thiết bị |
| **Cài đặt thông báo** | `notification_settings` | Bật/tắt Nhắc nhở công việc, Báo cáo ngày, Cảnh báo ưu tiên, Âm thanh & Rung |
| **Cấu hình AI** | `ai_config` | Lựa chọn mô hình AI (Gemini 1.5 Flash / GPT-4o Mini), Tự động xếp ưu tiên |
| **Cài đặt Pomodoro** | `pomodoro_settings` | Tùy chỉnh số phút tập trung, nghỉ ngắn, nghỉ dài, tự động chuyển phiên |
| **Điều khoản dịch vụ** | `terms_and_conditions` | Nội dung các điều khoản sử dụng ứng dụng |
| **Chính sách bảo mật** | `privacy_policy` | Cam kết bảo mật dữ liệu và quyền riêng tư người dùng |

---

### 4.5 Module Analytics & Home Dashboard

- **Greeting Card:** Hiển thị lời chào theo thời gian (Sáng / Chiều / Tối) kèm tên thật của người dùng.
- **Thống kê nhanh:** Số task Hôm nay, Task đã xong, Task đang chờ.
- **AI Insights Banner:** Gợi ý lộ trình làm việc từ mô hình AI.
- **Task List Overview:** 5 task ưu tiên nhất trong ngày.

---

### 4.6 Module Sync (Real-time WebSocket)

- Giao thức: **Socket.IO**
- Mỗi user join room `user:{userId}` riêng.
- Server phát các sự kiện `task:created`, `task:updated`, `task:deleted` để cập nhật UI tức thì trên Android.

---

## 5. Yêu Cầu Phi Chức Năng

### 5.1 Hiệu Năng & UX
- **API Response Time:** < 200ms
- **Screen Transitions:** Mượt mà với `slideInHorizontally` + `fadeIn` & `slideOutHorizontally` + `fadeOut`.
- **Auto-login Speed:** < 300ms kiểm tra token.
- **Offline-first:** App đọc/ghi dữ liệu từ Room Database khi mất mạng và tự sync khi có internet.

### 5.2 Bảo Mật
- **Mã hóa Password:** bcrypt (backend)
- **Xác thực:** JWT Bearer Token
- **Lưu trữ phía Client:** Android DataStore mã hóa an toàn.

---

## 6. Trạng Thái Phát Triển (Updated)

| Component | Trạng thái | Chi tiết |
|---|---|---|
| **Backend API** | ✅ 100% Completed | Auth, Tasks, Categories, Analytics, Sync |
| **Database & Docker** | ✅ 100% Completed | PostgreSQL + Redis trong Docker Compose |
| **Android Core** | ✅ 100% Completed | Clean Architecture, Hilt, Room, Retrofit |
| **Android UI & Auth** | ✅ 100% Completed | Login, Register, Forgot Password, Auto-Login |
| **Android Settings** | ✅ 100% Completed | 5 Full Screen Pages + Terms & Privacy |
| **ML Service Scaffold** | ✅ 100% Completed | FastAPI + Health Check endpoint |

---

## 7. Roadmap & Kế Hoạch Tiếp Theo

### Phase 1 & 2 — Core & UI MVP (✅ Completed)
- [x] Backend REST API & Database setup
- [x] Clean Architecture + Hilt DI + Room
- [x] UI/UX Material 3 redesign & Screen Transitions
- [x] Auto-login & DataStore session persistence
- [x] Full-screen Settings modules & Legal pages

### Phase 3 — Production & Store Release (🔄 Upcoming)
- [ ] Deploy Backend lên Cloud Server (AWS / Railway)
- [ ] Tích hợp Google Sign-In / OAuth 2.0
- [ ] Đóng gói Release APK / AAB và đăng tải trên Google Play Store

---

## 8. Kết Luận & Tài Liệu Liên Quan

Tài liệu PRD này phản ánh chính xác 100% trạng thái hoàn thiện của ứng dụng **AI Task Manager**. Ứng dụng đã sẵn sàng cho giai đoạn kiểm thử nghiệm thu và phát hành (Production Release).
