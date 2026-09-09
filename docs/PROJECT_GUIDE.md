# 📚 TỔNG HỢP KIẾN THỨC BÁO CÁO ĐỒ ÁN: AI TASK MANAGER

Tài liệu này tổng hợp toàn bộ hệ thống kiến thức, kiến trúc công nghệ và ví dụ minh họa thực tế từ dự án **AI Task Manager**. Tài liệu được biên soạn nhằm giúp bạn nắm vững bản chất hệ thống để báo cáo đồ án hoặc trả lời phỏng vấn kỹ thuật.

---

## 📑 MỤC LỤC
1. [Cấu Trúc Tổng Quan Đồ Án (System Architecture)](#1-cấu-trúc-tổng-quan-đồ-án)
2. [Kiến Thức Lập Trình Android Native (Kotlin & Jetpack Compose)](#2-kiến-thức-android-native)
3. [Kiến Thức Lập Trình Backend (Node.js, TypeScript & Prisma)](#3-kiến-thức-backend)
4. [Kiến Thức AI & ML Service (Python & FastAPI)](#4-kiến-thức-ai--ml-service)
5. [Kiến Thức Docker & DevOps (Docker Compose & Infrastructure)](#5-kiến-thức-docker--devops)

---

## 1. CẤU TRÚC TỔNG QUAN ĐỒ ÁN

### 1.1 Sơ Đồ Kiến Trúc Hệ Thống (3-Tier Microservices Architecture)

```
┌────────────────────────────────────────────────────────────────────────┐
│                        1. CLIENT LAYER (Mobile App)                    │
│   Android App (Kotlin Native + Jetpack Compose + Room Local Database)  │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ HTTPS (REST API) & WebSocket (Socket.IO)
┌───────────────────────────────────▼────────────────────────────────────┐
│                        2. BACKEND LAYER (API Gateway)                  │
│   Node.js + Express + TypeScript + Prisma ORM + Redis Cache            │
└─────────┬─────────────────────────────────────────────────┬────────────┘
          │ Internal HTTP                                   │ Database Drivers
┌─────────▼──────────────┐                         ┌────────▼────────────┐
│ 3. ML SERVICE (AI)     │                         │ 4. DATABASE LAYER   │
│ Python + FastAPI       │                         │ PostgreSQL Database │
└────────────────────────┘                         └─────────────────────┘
```

### 1.2 Luồng Dữ Liệu Hoạt Động (Data Flow)
1. **Người dùng tạo Task trên Android App**: App lưu tạm vào **Room Database** (Offline-first) ➔ Gửi REST API lên **Node.js Backend**.
2. **Xử lý Backend**: Backend gọi sang **Python ML Service** để tự động phân tích tiêu đề task ➔ gán **Độ ưu tiên (`priority`)** và **Danh mục (`category`)**.
3. **Lưu trữ & Đồng bộ**: Backend lưu vào **PostgreSQL**, xóa cache **Redis**, và phát sự kiện **Socket.IO** để tất cả thiết bị cùng tài khoản cập nhật giao diện real-time.

---

## 2. KIẾN THỨC ANDROID NATIVE

### 2.1 Các Khái Niệm Cốt Lõi
* **Clean Architecture**: Chia ứng dụng thành 3 lớp riêng biệt:
  * `data`: Quản lý nguồn dữ liệu (Room DB, Retrofit API, Repositories).
  * `domain`: Chứa các Business Logic thuần túy (Models, UseCases).
  * `presentation`: Chứa giao diện người dùng (Compose Screens, ViewModels).
* **Jetpack Compose**: Bộ công cụ xây dựng giao diện khai báo (Declarative UI) hiện đại của Android.
* **Hilt Dependency Injection (DI)**: Tự động quản lý và tiêm phụ thuộc (như Database, Retrofit Instance) giúp code dễ bảo trì và test.
* **StateFlow & Coroutines**: Xử lý bất đồng bộ và quản lý trạng thái UI theo thời gian thực mà không làm treo luồng chính (Main Thread).

### 2.2 Ví Dụ Code Thực Tế: Cấu hình Sắp xếp Task & Query Database (Room DAO)
File: `android/Task_Manager/app/src/main/java/com/example/task_manager/data/local/dao/TaskDao.kt`

```kotlin
package com.example.task_manager.data.local.dao

import androidx.room.*
import com.example.task_manager.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    // Truy vấn sắp xếp task chưa hoàn thành lên trước, ưu tiên theo độ ưu tiên AI giảm dần (3 -> 0)
    @Query("SELECT * FROM tasks ORDER BY completed ASC, priority DESC, createdAt DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTask(id: String)
}
```

**Giải thích ví dụ:**
- `@Dao`: Đánh dấu interface truy vấn dữ liệu Room.
- `Flow<List<TaskEntity>>`: Khi dữ liệu trong bảng `tasks` thay đổi (thêm/sửa/xóa), `Flow` tự động phát dữ liệu mới ra UI mà không cần gọi lại hàm load.
- `completed ASC, priority DESC`: Giúp task chưa hoàn thành và task có độ ưu tiên AI cao nhất luôn nằm ở đầu danh sách.

---

## 3. KIẾN THỨC BACKEND

### 3.1 Các Khái Niệm Cốt Lõi
* **Express.js + TypeScript**: Node.js framework giúp dựng RESTful API nhanh chóng, kết hợp TypeScript để kiểm soát kiểu dữ liệu chặt chẽ.
* **Prisma ORM**: Công cụ giao tiếp với Database PostgreSQL bằng type-safety, thay thế cho việc viết SQL thô.
* **Redis Caching**: Lưu tạm danh sách task trong RAM để giảm tải truy vấn vào PostgreSQL, tăng tốc độ phản hồi API < 50ms.
* **Socket.IO (Real-time Sync)**: Giao thức WebSocket giúp gửi thông báo từ Server xuống Client ngay lập tức khi dữ liệu thay đổi.

### 3.2 Ví Dụ Code Thực Tế: Quản lý Truy Vấn & Caching Redis
File: `backend/src/modules/tasks/task.repository.ts`

```typescript
import { prisma } from '../../app';

export const findAllTasks = async (userId: string, filters: any) => {
    const { completed, priority, categoryId, sortBy, order = 'desc' } = filters;

    // Tiêu chuẩn sắp xếp ưu tiên AI priority
    const defaultOrderBy = [
        { completed: 'asc' },
        { priority: 'desc' },
        { createdAt: 'desc' }
    ];

    return prisma.task.findMany({
        where: {
            userId,
            ...(completed != undefined && { completed: completed === 'true'}),
            ...(priority !== undefined && { priority: Number(priority) }),
            ...(categoryId && { categoryId })
        },
        orderBy: sortBy ? { [sortBy]: order } : defaultOrderBy,
        include: {
            category: true // Join bảng category
        }
    });
};
```

**Giải thích ví dụ:**
- `prisma.task.findMany()`: Hàm truy vấn lấy danh sách task từ PostgreSQL.
- `...(completed != undefined && { ... })`: Kỹ thuật conditional object spreading để chỉ lọc điều kiện khi client truyền lên query params.
- `include: { category: true }`: Tự động JOIN với bảng danh mục để lấy tên category.

---

## 4. KIẾN THỨC AI & ML SERVICE

### 4.1 Các Khái Niệm Cốt Lõi
* **FastAPI (Python)**: Framework hiệu năng cao xây dựng AI microservices bất đồng bộ (`async/await`).
* **Pydantic Schemas**: Kiểm tra tính hợp lệ của dữ liệu đầu vào/đầu ra (Input/Output Validation).
* **Rule-based & NLP Hybrid Classification**: Kết hợp phân tích ngữ cảnh từ khóa tiếng Việt (như *"gấp"*, *"sập"*, *"tiền"*, *"thi"*) và mô hình NLP để xếp hạng độ ưu tiên task.
* **Fallback Pattern**: Nếu AI Service offline, Backend Node.js tự nhảy sang thuật toán dự phòng để ứng dụng không bao giờ bị gián đoạn.

### 4.2 Ví Dụ Code Thực Tế: Endpoint Phân Tích & Gợi Ý AI Insights
File: `ml-service/app/api/predict.py`

```python
from fastapi import APIRouter
from pydantic import BaseModel, Field
from typing import Optional, List

router = APIRouter(prefix="/ai", tags=["AI Prediction"])

class TaskPredictRequest(BaseModel):
    title: str = Field(..., example="Nộp báo cáo tài chính gấp trước 5h chiều")
    description: Optional[str] = None

class TaskInsightResponse(BaseModel):
    summary: str
    recommendedFocusTaskId: Optional[str] = None

@router.post("/suggest-insights", response_model=TaskInsightResponse)
async def suggest_task_insights(request: TaskInsightRequest):
    tasks = request.tasks
    if not tasks:
        return TaskInsightResponse(summary="Chưa có công việc nào trong danh sách.")

    # Phân tích điểm ưu tiên dựa trên từ khóa ngữ cảnh
    scored_tasks = []
    for task in tasks:
        score = 1
        combined = f"{task.title.lower()} {(task.description or '').lower()}"
        if any(k in combined for k in ["gấp", "khẩn", "sập", "lỗi", "deadline"]):
            score += 3
        scored_tasks.append((score, task))

    # Sắp xếp lấy task quan trọng nhất
    scored_tasks.sort(key=lambda x: x[0], reverse=True)
    top_task = scored_tasks[0][1]

    return TaskInsightResponse(
        summary=f"Nhiệm vụ KHẨN CẤP nhất bạn cần làm ngay là: '{top_task.title}'.",
        recommendedFocusTaskId=top_task.title
    )
```

**Giải thích ví dụ:**
- `BaseModel`: Đảm bảo request gửi lên đúng cấu trúc dữ liệu JSON.
- `scored_tasks.sort(key=..., reverse=True)`: Thuật toán tính điểm trọng số cho từng task để tìm ra task quan trọng nhất và đưa ra gợi ý thông minh cho người dùng.

---

## 5. KIẾN THỨC DOCKER & DEVOPS

### 4.1 Các Khái Niệm Cốt Lõi
* **Containerization (Đóng gói)**: Đóng gói toàn bộ source code, môi trường (Node.js, Python, PostgreSQL) vào các container độc lập, chạy giống nhau trên mọi máy tính.
* **Docker Compose**: Công cụ định nghĩa và chạy nhiều container cùng lúc chỉ bằng 1 câu lệnh (`docker-compose up -d`).
* **Environment Isolation**: Tách biệt môi trường chạy của từng dịch vụ, giúp Backend không bị xung đột phiên bản với ML Service.

### 5.2 Ví Dụ Code Thực Tế: File Điều Phối Container Docker Compose
File: `docker/docker-compose.yml`

```yaml
version: '3.8'

services:
  # 1. Cơ sở dữ liệu PostgreSQL
  postgres:
    image: postgres:15
    container_name: aitm_postgres
    environment:
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: change_me
      POSTGRES_DB: aitaskmanager
    ports:
      - "5432:5432"

  # 2. Redis Cache Server
  redis:
    image: redis:7-alpine
    container_name: aitm_redis
    ports: 
      - "6379:6379"
  
  # 3. Node.js Backend Service
  backend:
    build:
      context: ../backend
      dockerfile: ../docker/Dockerfile.backend
    container_name: aitm_backend
    ports:
      - "3000:3000"
    depends_on:
      - postgres
      - redis

  # 4. Python ML Service (FastAPI)
  ml-service:
    build:
      context: ../ml-service
      dockerfile: ../docker/Dockerfile.ml
    container_name: aitm_ml
    ports:
      - "8000:8000"
```

**Giải thích ví dụ:**
- `depends_on`: Quy định thứ tự khởi động (ví dụ: `backend` phải đợi `postgres` và `redis` khởi động xong thì mới chạy).
- `ports: - "3000:3000"`: Mapping cổng 3000 bên trong container ra cổng 3000 trên máy thật (Host Machine).

---

## 💡 CÁCH TRẢ LỜI PHỎNG VẤN / BÁO CÁO ĐỒ ÁN MẪU

* **Câu hỏi: "Hệ thống của em giải quyết bài toán gì và điểm đặc biệt là gì?"**
  * **Trả lời:** *"Dự án AI Task Manager giải quyết bài toán tối ưu năng suất cá nhân. Điểm đặc biệt là hệ thống sử dụng kiến trúc Microservices kết hợp giữa Android Native (Jetpack Compose), Backend Node.js và Python AI Service. AI sẽ tự động đọc hiểu tiêu đề task để gợi ý mức độ ưu tiên và đưa ra lời khuyên người dùng nên tập trung hoàn thành task nào trước trong ngày."*

* **Câu hỏi: "Nếu dịch vụ AI (Python) bị sập thì App Android có hoạt động được không?"**
  * **Trả lời:** *"Dạ có. Hệ thống được thiết kế theo cơ chế **Fallback Pattern**. Tại Backend Node.js (`ai.service.ts`), nếu không gọi được dịch vụ FastAPI ở cổng 8000, hệ thống sẽ tự chuyển sang thuật toán phân loại dự phòng cục bộ, đảm bảo ứng dụng Android luôn hoạt động liên tục 100% không bị crash."*
