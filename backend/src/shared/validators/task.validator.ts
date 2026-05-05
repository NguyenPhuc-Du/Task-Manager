import { z } from 'zod';

export const createTaskSchema = z.object({
    title: z.string().min(1, 'Tiêu đề không được để trống'),
    description: z.string().optional(),
    priority: z.number().int().min(0).max(3).default(0),
    dueDate: z.string().datetime().optional(),
    categoryId: z.string().optional()
});

export const updateTaskSchema = createTaskSchema.partial();

export const taskFilterSchema = z.object({
    completed: z.enum(['true', 'false']).optional(),
    priority: z.string().optional(),
    categoryId: z.string().optional(),
    sortBy: z.enum(['createdAt', 'dueDate', 'priority']).optional(),
    order: z.enum(['asc', 'desc']).optional()
});