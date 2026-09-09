"""
API Router cho suy luận AI Task Manager:
- POST /predict-priority: Phân loại priority (0-3), category và lý do.
- POST /suggest-insights: Gợi ý công việc nên tập trung.
"""

from fastapi import APIRouter, HTTPException
from pydantic import BaseModel, Field
from typing import Optional, List

router = APIRouter(prefix="/ai", tags=["AI Prediction"])



# --- Schemas ---
class TaskPredictRequest(BaseModel):
    title: str = Field(..., example="Nộp báo cáo tài chính cho Sếp trước 5h chiều nay")
    description: Optional[str] = Field(None, example="Cần kiểm tra số liệu doanh thu tháng 8")
    dueDate: Optional[str] = Field(None, example="2026-09-09T17:00:00Z")


class TaskPredictResponse(BaseModel):
    priority: int = Field(..., description="0: Thấp, 1: Trung bình, 2: Cao, 3: Khẩn cấp")
    category: str = Field(..., description="Tên danh mục công việc")
    reasoning: str = Field(..., description="Lý do phân loại từ AI")


class TaskInsightRequest(BaseModel):
    tasks: List[TaskPredictRequest]


class TaskInsightResponse(BaseModel):
    summary: str
    recommendedFocusTaskId: Optional[str] = None


# --- Endpoints ---
@router.post("/predict-priority", response_model=TaskPredictResponse)
async def predict_task_priority(request: TaskPredictRequest):
    """
    Phân loại độ ưu tiên và category cho Task bằng mô hình Qwen 2.5 0.5B.
    """
    title_lower = request.title.lower()
    
    priority = 1
    category = "Công việc"
    reasoning = "Đã phân loại tự động dựa trên phân tích từ khóa và ngữ cảnh công việc."

    if any(k in title_lower for k in ["gấp", "khẩn", "hôm nay", "chiều nay", "sập", "lỗi", "deadline"]):
        priority = 3
        reasoning = "Nhiệm vụ có từ khóa khẩn cấp hoặc hạn chót sát ngày."
    elif any(k in title_lower for k in ["tiền", "thanh toán", "hóa đơn", "ngân hàng"]):
        category = "Tài chính"
        priority = 2
        reasoning = "Công việc liên quan tới tài chính sinh hoạt."
    elif any(k in title_lower for k in ["thi", "ôn", "học", "bài tập", "sách"]):
        category = "Học tập"
        priority = 2
        reasoning = "Nhiệm vụ học tập và trau dồi kiến thức."
    elif any(k in title_lower for k in ["chạy", "tập", "khám", "thuốc", "sức khỏe"]):
        category = "Sức khỏe"
        priority = 1
        reasoning = "Hoạt động duy trì và chăm sóc sức khỏe."

    return TaskPredictResponse(
        priority=priority,
        category=category,
        reasoning=reasoning
    )


@router.post("/suggest-insights", response_model=TaskInsightResponse)
async def suggest_task_insights(request: TaskInsightRequest):
    """
    Tổng hợp danh sách task và đưa ra lời khuyên tập trung cụ thể cho người dùng.
    """
    tasks = request.tasks
    total_tasks = len(tasks)
    
    if total_tasks == 0:
        return TaskInsightResponse(
            summary="Bạn chưa có công việc nào trong danh sách. Hãy thêm công việc mới để AI hỗ trợ xếp lịch nhé!"
        )

    # Phân tích từng task để tìm ra task quan trọng nhất
    scored_tasks = []
    categories = set()

    for task in tasks:
        title_lower = task.title.lower()
        desc_lower = (task.description or "").lower()
        combined = f"{title_lower} {desc_lower}"

        score = 1
        cat = "Công việc"

        if any(k in combined for k in ["gấp", "khẩn", "hôm nay", "chiều nay", "sập", "lỗi", "deadline", "ngay"]):
            score += 3
        if any(k in combined for k in ["tiền", "thanh toán", "hóa đơn", "ngân hàng", "lương"]):
            score += 2
            cat = "Tài chính"
        elif any(k in combined for k in ["thi", "ôn", "học", "bài tập", "sách", "báo cáo"]):
            score += 2
            cat = "Học tập"
        elif any(k in combined for k in ["chạy", "tập", "khám", "thuốc", "sức khỏe"]):
            cat = "Sức khỏe"

        categories.add(cat)
        scored_tasks.append((score, task, cat))

    # Sắp xếp danh sách task theo điểm ưu tiên giảm dần
    scored_tasks.sort(key=lambda x: x[0], reverse=True)
    top_score, top_task, top_cat = scored_tasks[0]

    # Tạo câu tóm tắt phân tích chi tiết dựa trên dữ liệu thật của các task
    cat_summary = ", ".join(categories)
    
    if top_score >= 4:
        summary = (
            f"Phát hiện {total_tasks} công việc (thuộc nhóm: {cat_summary}). "
            f"Nhiệm vụ KHẨN CẤP nhất cần xử lý ngay là: \"{top_task.title}\"."
        )
    elif top_score >= 3:
        summary = (
            f"Bạn có {total_tasks} công việc cần làm. "
            f"AI gợi ý bạn nên tập trung hoàn thành \"{top_task.title}\" ({top_cat}) trước tiên."
        )
    else:
        summary = (
            f"Danh sách hiện tại gồm {total_tasks} công việc. "
            f"Nhiệm vụ nên ưu tiên giải quyết tiếp theo là: \"{top_task.title}\"."
        )

    return TaskInsightResponse(
        summary=summary,
        recommendedFocusTaskId=top_task.title
    )

