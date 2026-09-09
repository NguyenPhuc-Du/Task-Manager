import { Request, Response, NextFunction } from 'express';
import { AiService } from './ai.service';

export const predictTaskPriority = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { title, description, dueDate } = req.body;
    if (!title) {
      return res.status(400).json({ error: 'Title is required' });
    }

    const result = await AiService.predictPriority({ title, description, dueDate });
    return res.json(result);
  } catch (error) {
    return next(error);
  }
};

export const getTaskInsights = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { tasks } = req.body;
    if (!Array.isArray(tasks)) {
      return res.status(400).json({ error: 'Tasks array is required' });
    }

    const result = await AiService.getInsights({ tasks });
    return res.json(result);
  } catch (error) {
    return next(error);
  }
};
