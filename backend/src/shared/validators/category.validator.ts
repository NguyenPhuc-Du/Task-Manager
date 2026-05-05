import { z } from 'zod';

export const createCategorySchema = z.object({
    name: z.string().min(1, 'Không được để trống tên category'),
    color: z.string().optional()
});

export const updateCategorySchema = createCategorySchema.partial();