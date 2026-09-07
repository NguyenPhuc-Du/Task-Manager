# 📱 Android Task Manager — Kiến Trúc & Luồng Hoạt Động

## Tổng Quan Kiến Trúc

Project sử dụng **Clean Architecture + MVVM** với **Hilt** cho Dependency Injection.

```mermaid
graph TB
    subgraph Presentation["🖥️ PRESENTATION LAYER"]
        UI["Screens (Compose UI)"]
        VM["ViewModels"]
    end

    subgraph Domain["🧠 DOMAIN LAYER"]
        UC["Use Cases"]
        REPO_I["Repository Interfaces"]
        MODEL["Domain Models"]
    end

    subgraph Data["💾 DATA LAYER"]
        REPO_IMPL["Repository Implementations"]
        subgraph Local["Local"]
            DB["Room Database"]
            DAO["DAOs"]
            ENTITY["Entities"]
        end
        subgraph Remote["Remote"]
            API["Retrofit API Services"]
            DTO["DTOs"]
            INTERCEPT["Interceptors"]
        end
        DS["DataStore Preferences"]
    end

    subgraph DI["⚙️ DI LAYER"]
        MODULES["Hilt Modules"]
    end

    UI --> VM
    VM --> UC
    UC --> REPO_I
    REPO_I -.-> REPO_IMPL
    REPO_IMPL --> DAO
    REPO_IMPL --> API
    REPO_IMPL --> DS
    DAO --> DB
    DB --> ENTITY
    API --> DTO
    API --> INTERCEPT
    MODULES -.-> |provides| DB
    MODULES -.-> |provides| API
    MODULES -.-> |provides| DS
```

---

## 📁 Chi Tiết Từng File

### 🏠 Root — Entry Points

#### `MainApplication.kt` ✅ Đã implement
| Thuộc tính | Chi tiết |
|---|---|
| **Chức năng** | Entry point của ứng dụng, khởi tạo Hilt DI container |
| **Annotation** | `@HiltAndroidApp` — trigger Hilt code generation |
| **Luồng** | Android OS → `MainApplication.onCreate()` → Hilt khởi tạo dependency graph |

#### `MainActivity.kt` ✅ Đã implement
| Thuộc tính | Chi tiết |
|---|---|
| **Chức năng** | Activity chính, host Jetpack Compose UI |
| **Annotation** | `@AndroidEntryPoint` — cho phép inject dependencies |
| **Luồng** | `MainApplication` → `MainActivity.onCreate()` → `setContent { Scaffold }` |
| **Lưu ý** | Hiện chỉ hiển thị "Hello Android!", chưa tích hợp NavGraph |

---

### 💾 Data Layer — Local (Room Database)

#### `data/local/entity/TaskEntity.kt` ✅ Đã implement
| Thuộc tính | Chi tiết |
|---|---|
| **Table** | `tasks` |
| **Chức năng** | Định nghĩa bảng tasks trong SQLite |
| **Fields** | `id` (PK), `title`, `description?`, `priority`, `completed`, `dueDate?`, `categoryId`, `userId`, `createdAt`, `updatedAt` |

#### `data/local/entity/CategoryEntity.kt` ✅ Đã implement
| Thuộc tính | Chi tiết |
|---|---|
| **Table** | `categories` |
| **Chức năng** | Định nghĩa bảng categories |
| **Fields** | `id` (PK), `name`, `color?`, `userId`, `createdAt` |

#### `data/local/entity/PomodoroEntity.kt` ✅ Đã implement
| Thuộc tính | Chi tiết |
|---|---|
| **Table** | `pomodoros` |
| **Chức năng** | Định nghĩa bảng pomodoro sessions |
| **Fields** | `id` (PK), `duration`, `completed`, `taskId`, `userId`, `createdAt` |

#### `data/local/dao/TaskDao.kt` ✅ Đã implement
| Thuộc tính | Chi tiết |
|---|---|
| **Chức năng** | Data Access Object cho bảng `tasks` |
| **Operations** | `getAllTasks()` → `Flow<List>`, `getTaskById()`, `insertTask()`, `insertTasks()`, `updateTask()`, `deleteTask()`, `deleteAllTasks()` |
| **Đặc biệt** | `getAllTasks()` trả về `Flow` — tự động emit khi data thay đổi |

#### `data/local/dao/CategoryDao.kt` ✅ Đã implement
| Thuộc tính | Chi tiết |
|---|---|
| **Chức năng** | DAO cho bảng `categories` |
| **Operations** | `getAllCategories()` → `Flow<List>`, `insertCategory()`, `insertCategories()`, `deleteCategory()`, `deleteAllCategories()` |

#### `data/local/dao/PomodoroDao.kt` ✅ Đã implement
| Thuộc tính | Chi tiết |
|---|---|
| **Chức năng** | DAO cho bảng `pomodoros` |
| **Operations** | `getAllPomodoros()` → `Flow<List>`, `insertPomodoro()`, `deleteAllPomodoros()` |

#### `data/local/AppDatabase.kt` ✅ Đã implement
| Thuộc tính | Chi tiết |
|---|---|
| **Chức năng** | Room Database chính, quản lý 3 bảng |
| **Entities** | `TaskEntity`, `CategoryEntity`, `PomodoroEntity` |
| **Version** | 1 |
| **Expose** | `taskDao()`, `categoryDao()`, `pomodoroDao()` |

---

### 💾 Data Layer — Remote (Retrofit + OkHttp)

#### `data/remote/dto/TaskDto.kt` ✅ Đã implement
| Thuộc tính | Chi tiết |
|---|---|
| **Chức năng** | Data Transfer Objects cho Task API |
| **Classes** | `TaskDto` (response), `CreateTaskRequest` (POST body), `UpdateTaskRequest` (PUT body) |
| **Luồng** | Backend JSON → Gson → `TaskDto` → map to `Task` domain model |

#### `data/remote/dto/UserDto.kt` ✅ Đã implement
| Thuộc tính | Chi tiết |
|---|---|
| **Chức năng** | DTOs cho Auth API |
| **Classes** | `UserDto` (user info), `RegisterRequest`, `LoginRequest`, `AuthResponse` (chứa `UserDto` + `token`) |

> [!NOTE]
> `SyncApiService.kt` tham chiếu đến `CategoryDto` nhưng class này **chưa được tạo** trong `UserDto.kt` hay file riêng. Cần bổ sung.

#### `data/remote/api/TaskApiService.kt` ✅ Đã implement
| Thuộc tính | Chi tiết |
|---|---|
| **Chức năng** | Retrofit interface cho Task CRUD |
| **Endpoints** | `GET /tasks`, `GET /tasks/{id}`, `POST /tasks`, `PUT /tasks/{id}`, `DELETE /tasks/{id}` |
| **Base URL** | `http://10.0.2.2:3000/` (Android emulator → localhost) |

#### `data/remote/api/AuthApiService.kt` ✅ Đã implement
| Thuộc tính | Chi tiết |
|---|---|
| **Chức năng** | Retrofit interface cho Authentication |
| **Endpoints** | `POST /auth/register`, `POST /auth/login`, `POST /auth/logout` |

#### `data/remote/api/SyncApiService.kt` ✅ Đã implement
| Thuộc tính | Chi tiết |
|---|---|
| **Chức năng** | Retrofit interface cho đồng bộ dữ liệu |
| **Endpoints** | `GET /tasks` (sync tasks), `GET /categories` (sync categories) |

#### `data/remote/interceptor/AuthInterceptor.kt` ✅ Đã implement
| Thuộc tính | Chi tiết |
|---|---|
| **Chức năng** | Tự động gắn JWT token vào header mỗi HTTP request |
| **Luồng** | Request → đọc token từ `UserPreferences` (DataStore) → thêm `Authorization: Bearer <token>` → forward request |
| **Lưu ý** | Dùng `runBlocking` để đọc Flow trong synchronous context (OkHttp interceptor) |

#### `data/remote/interceptor/LoggingInterceptor.kt` ✅ Đã implement
| Thuộc tính | Chi tiết |
|---|---|
| **Chức năng** | Log toàn bộ HTTP request/response body (debug) |
| **Level** | `BODY` — log headers + body |

---

### 💾 Data Layer — Repository & DataStore

#### `data/datastore/UserPreferences.kt` ✅ Đã implement
| Thuộc tính | Chi tiết |
|---|---|
| **Chức năng** | Quản lý user session (token, userId, email) bằng DataStore |
| **Lưu trữ** | `jwt_token`, `user_id`, `user_email` |
| **Operations** | `saveUser(token, userId, email)`, `clearUser()` |
| **Expose** | `token: Flow<String?>`, `userId: Flow<String?>`, `userEmail: Flow<String?>` |

#### `data/repository/TaskRepositoryImpl.kt` ❌ Chưa implement
| Thuộc tính | Chi tiết |
|---|---|
| **Chức năng dự kiến** | Implement `TaskRepository` interface, kết hợp local (Room) + remote (Retrofit) |
| **Luồng dự kiến** | UseCase → `TaskRepositoryImpl` → API call + cache vào Room |

#### `data/repository/AuthRepositoryImpl.kt` ❌ Chưa implement
| Thuộc tính | Chi tiết |
|---|---|
| **Chức năng dự kiến** | Implement `AuthRepository` interface, xử lý login/register + lưu token |

---

### 🧠 Domain Layer — Models

#### `domain/model/Task.kt` ✅ Đã implement
| Fields | `id`, `title`, `description?`, `priority`, `completed`, `dueDate?`, `categoryId?`, `userId`, `createdAt`, `updatedAt` |
|---|---|
| **Chức năng** | Domain model cho Task — không phụ thuộc framework |

#### `domain/model/Category.kt` ✅ Đã implement
| Fields | `id`, `name`, `color?`, `userId`, `createdAt` |
|---|---|

#### `domain/model/User.kt` ✅ Đã implement
| Fields | `id`, `email`, `name?`, `token` |
|---|---|

---

### 🧠 Domain Layer — Repository Interfaces

#### `domain/repository/TaskRepository.kt` ✅ Đã implement
| Thuộc tính | Chi tiết |
|---|---|
| **Chức năng** | Contract cho Task data operations |
| **Methods** | `getTasks()`, `getTaskById()`, `createTask()`, `updateTask()`, `deleteTask()`, `syncTasks()` |

#### `domain/repository/AuthRepository.kt` ✅ Đã implement
| Thuộc tính | Chi tiết |
|---|---|
| **Chức năng** | Contract cho Auth operations |
| **Methods** | `register()`, `login()`, `logout()`, `getToken()`, `isLoggedIn()` |

---

### 🧠 Domain Layer — Use Cases

#### `domain/usecase/task/GetTasksUseCase.kt` ✅ Đã implement
| Thuộc tính | Chi tiết |
|---|---|
| **Input** | Không |
| **Output** | `Flow<List<Task>>` |
| **Luồng** | ViewModel gọi `invoke()` → `TaskRepository.getTasks()` → emit liên tục khi data thay đổi |

#### `domain/usecase/task/CreateTaskUseCase.kt` ✅ Đã implement
| Thuộc tính | Chi tiết |
|---|---|
| **Input** | `title`, `description?`, `priority`, `dueDate?`, `categoryId?` |
| **Output** | `Task` |
| **Validation** | Kiểm tra `title` không được blank |
| **Luồng** | ViewModel → validate → `TaskRepository.createTask()` → trả về Task mới |

#### `domain/usecase/task/UpdateTaskUseCase.kt` ✅ Đã implement
| Thuộc tính | Chi tiết |
|---|---|
| **Input** | `id`, `title?`, `description?`, `priority?`, `completed?`, `categoryId?` |
| **Output** | `Task` |
| **Luồng** | ViewModel → `TaskRepository.updateTask()` → trả về Task đã cập nhật |

#### `domain/usecase/task/DeleteTaskUseCase.kt` ✅ Đã implement
| Thuộc tính | Chi tiết |
|---|---|
| **Input** | `id: String` |
| **Luồng** | ViewModel → `TaskRepository.deleteTask(id)` |

#### `domain/usecase/sync/SyncTasksUseCase.kt` ✅ Đã implement
| Thuộc tính | Chi tiết |
|---|---|
| **Chức năng** | Đồng bộ tasks từ server về local |
| **Luồng** | ViewModel → `TaskRepository.syncTasks()` → pull từ API → lưu vào Room |

---

### ⚙️ DI Layer — Hilt Modules

#### `di/AppModule.kt` ✅ Đã implement
| Thuộc tính | Chi tiết |
|---|---|
| **Provides** | `DataStore<Preferences>` (Singleton) |
| **Tên file** | `user_prefs` |
| **Luồng** | Hilt inject `DataStore` → `UserPreferences` → `AuthInterceptor` / Repositories |

#### `di/NetworkModule.kt` ✅ Đã implement
| Thuộc tính | Chi tiết |
|---|---|
| **Base URL** | `http://10.0.2.2:3000/` (emulator → localhost:3000) |
| **Provides** | `OkHttpClient` (với Auth + Logging interceptors), `Retrofit`, `AuthApiService`, `TaskApiService`, `SyncApiService` |
| **Luồng** | Hilt → tạo `OkHttpClient` (inject interceptors) → tạo `Retrofit` → tạo API services |

#### `di/DatabaseModule.kt` ✅ Đã implement
| Thuộc tính | Chi tiết |
|---|---|
| **DB Name** | `aitaskmanager.db` |
| **Provides** | `AppDatabase` (Singleton), `TaskDao`, `CategoryDao`, `PomodoroDao` |
| **Luồng** | Hilt → `Room.databaseBuilder()` → expose DAOs |

---

### 🖥️ Presentation Layer — Tất cả ❌ Chưa implement

| File | Chức năng dự kiến |
|---|---|
| `ui/auth/LoginScreen.kt` | Màn hình đăng nhập (email + password) |
| `ui/auth/RegisterScreen.kt` | Màn hình đăng ký tài khoản |
| `ui/auth/AuthViewModel.kt` | Quản lý state auth, gọi AuthRepository |
| `ui/home/HomeScreen.kt` | Màn hình chính, dashboard tổng quan |
| `ui/home/HomeViewModel.kt` | Load dữ liệu tổng hợp cho home |
| `ui/task/TaskListScreen.kt` | Danh sách tasks, filter/sort |
| `ui/task/TaskDetailScreen.kt` | Chi tiết 1 task |
| `ui/task/CreateTaskScreen.kt` | Form tạo task mới |
| `ui/task/TaskViewModel.kt` | CRUD tasks qua Use Cases |
| `ui/pomodoro/PomodoroScreen.kt` | Timer Pomodoro |
| `ui/pomodoro/PomodoroViewModel.kt` | Quản lý countdown + sessions |
| `ui/settings/SettingsScreen.kt` | Cài đặt ứng dụng |
| `ui/settings/SettingsViewModel.kt` | Load/save settings |
| `ui/navigation/NavGraph.kt` | Định nghĩa navigation routes |
| `ui/navigation/Screen.kt` | Sealed class các screen routes |

### 🔔 Service Layer

| File | Trạng thái | Chức năng dự kiến |
|---|---|---|
| `service/NotificationHelper.kt` | ❌ Chưa implement | Push notification cho task reminders, pomodoro alerts |

---

## 🔄 Luồng Hoạt Động Chính

### 1. Luồng Khởi Động App

```mermaid
sequenceDiagram
    participant OS as Android OS
    participant App as MainApplication
    participant Hilt as Hilt DI
    participant MA as MainActivity
    participant Nav as NavGraph

    OS->>App: onCreate()
    App->>Hilt: Khởi tạo dependency graph
    Hilt->>Hilt: Build AppModule + NetworkModule + DatabaseModule
    OS->>MA: onCreate()
    MA->>MA: enableEdgeToEdge()
    MA->>Nav: setContent { NavGraph }
    Nav->>Nav: Check isLoggedIn?
    alt Đã login
        Nav->>Nav: Navigate → HomeScreen
    else Chưa login
        Nav->>Nav: Navigate → LoginScreen
    end
```

### 2. Luồng Đăng Nhập

```mermaid
sequenceDiagram
    participant UI as LoginScreen
    participant VM as AuthViewModel
    participant Repo as AuthRepositoryImpl
    participant API as AuthApiService
    participant DS as UserPreferences

    UI->>VM: login(email, password)
    VM->>Repo: login(email, password)
    Repo->>API: POST /auth/login {email, password}
    API-->>Repo: AuthResponse {user, token}
    Repo->>DS: saveUser(token, userId, email)
    Repo-->>VM: User
    VM-->>UI: Update UI State → Navigate to Home
```

### 3. Luồng CRUD Task

```mermaid
sequenceDiagram
    participant UI as TaskListScreen
    participant VM as TaskViewModel
    participant UC as GetTasksUseCase
    participant Repo as TaskRepositoryImpl
    participant API as TaskApiService
    participant DAO as TaskDao
    participant Auth as AuthInterceptor

    UI->>VM: Observe tasks
    VM->>UC: invoke()
    UC->>Repo: getTasks()
    Repo->>DAO: getAllTasks()
    DAO-->>UI: Flow<List<Task>> (auto-update)

    Note over UI,Auth: Tạo Task Mới
    UI->>VM: createTask(title, desc, priority)
    VM->>Repo: createTask(...)
    Repo->>API: POST /tasks {body}
    API->>Auth: intercept → add Bearer token
    Auth->>API: Forward request
    API-->>Repo: TaskDto
    Repo->>DAO: insertTask(entity)
    DAO-->>UI: Flow emit update
```

### 4. Luồng Sync Dữ Liệu

```mermaid
sequenceDiagram
    participant VM as ViewModel
    participant UC as SyncTasksUseCase
    participant Repo as TaskRepositoryImpl
    participant API as SyncApiService
    participant DAO as TaskDao

    VM->>UC: invoke()
    UC->>Repo: syncTasks()
    Repo->>API: GET /tasks
    API-->>Repo: List<TaskDto>
    Repo->>Repo: Map TaskDto → TaskEntity
    Repo->>DAO: insertTasks(entities)
    DAO-->>VM: Flow emit latest data
```

---

## 📊 Trạng Thái Implement

```mermaid
pie title Trạng Thái Implement (37 files)
    "✅ Đã implement" : 22
    "❌ Chưa implement" : 15
```

| Layer | Đã implement | Chưa implement |
|---|---|---|
| Root | 2/2 | — |
| Data / Local | 7/7 | — |
| Data / Remote | 7/7 | — |
| Data / Repository | 0/2 | `TaskRepositoryImpl`, `AuthRepositoryImpl` |
| Data / DataStore | 1/1 | — |
| Domain / Model | 3/3 | — |
| Domain / Repository | 2/2 | — |
| Domain / UseCase | 5/5 | — |
| DI | 3/3 | — |
| Presentation | 0/15 | Tất cả Screen + ViewModel + Navigation |
| Service | 0/1 | `NotificationHelper` |

---

## ⚠️ Vấn Đề Cần Lưu Ý

> [!WARNING]
> **`CategoryDto` chưa tồn tại** — `SyncApiService.kt` import `CategoryDto` nhưng class này chưa được tạo. Cần thêm vào `data/remote/dto/`.

> [!IMPORTANT]
> **2 Repository Implementations chưa code** — `TaskRepositoryImpl` và `AuthRepositoryImpl` là cầu nối giữa Domain và Data layer. Không có chúng, app chưa thể hoạt động.

> [!NOTE]
> **Toàn bộ Presentation layer chưa implement** — 15 files Screen/ViewModel/Navigation đều chỉ là stub. Đây là phần cần làm nhiều nhất tiếp theo.
