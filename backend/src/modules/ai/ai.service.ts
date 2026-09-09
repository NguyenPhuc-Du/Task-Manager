const ML_SERVICE_URL = process.env.ML_SERVICE_URL || 'http://localhost:8000';

export interface PredictTaskRequest {
  title: string;
  description?: string;
  dueDate?: string;
}

export interface PredictTaskResponse {
  priority: number;
  category: string;
  reasoning: string;
}

export interface TaskInsightRequest {
  tasks: PredictTaskRequest[];
}

export interface TaskInsightResponse {
  summary: string;
  recommendedFocusTaskId?: string;
}

export class AiService {
  /**
   * Forward task priority prediction request to FastAPI ML Service
   */
  static async predictPriority(payload: PredictTaskRequest): Promise<PredictTaskResponse> {
    try {
      const response = await fetch(`${ML_SERVICE_URL}/api/v1/ai/predict-priority`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        throw new Error(`ML Service returned HTTP status ${response.status}`);
      }

      return (await response.json()) as PredictTaskResponse;
    } catch (error: any) {
      console.warn('ML Service offline/failed, falling back to local heuristic:', error.message);
      // Fallback local classification if ML service is unreachable
      const titleLower = payload.title.toLowerCase();
      let priority = 1;
      let category = 'Công việc';

      if (titleLower.includes('gấp') || titleLower.includes('khẩn') || titleLower.includes('hôm nay')) {
        priority = 3;
      } else if (titleLower.includes('tiền') || titleLower.includes('thanh toán')) {
        category = 'Tài chính';
        priority = 2;
      }

      return {
        priority,
        category,
        reasoning: 'Tự động phân loại bằng hệ thống dự phòng (ML Service Offline).',
      };
    }
  }

  /**
   * Forward task insights request to FastAPI ML Service
   */
  static async getInsights(payload: TaskInsightRequest): Promise<TaskInsightResponse> {
    try {
      const response = await fetch(`${ML_SERVICE_URL}/api/v1/ai/suggest-insights`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        throw new Error(`ML Service returned HTTP status ${response.status}`);
      }

      return (await response.json()) as TaskInsightResponse;
    } catch (error: any) {
      console.warn('ML Service offline/failed, returning default insights:', error.message);
      return {
        summary: `Bạn có ${payload.tasks.length} công việc cần xử lý. Hãy tập trung ưu tiên việc quan trọng trước.`,
      };
    }
  }
}
