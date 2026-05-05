import { Response, NextFunction } from 'express';
import { AuthRequest } from '../auth/auth.middleware';
import * as categoryService from './category.service';
import { createCategorySchema, updateCategorySchema } from '../../shared/validators/category.validator';
import ca from 'zod/v4/locales/ca.js';

export const getCategories = async (req: AuthRequest, res: Response, next: NextFunction) => {
    try {
        const categories = await categoryService.getCategories(req.userId!);
        res.json(categories);
    } catch (error) {
        next(error);
    }
};

export const createCategory = async (req: AuthRequest, res: Response, next: NextFunction) => {
    try {
        const data = createCategorySchema.parse(req.body);
        const category = await categoryService.createCategory(req.userId!, data);
        res.status(201).json(category);
    }
    catch (error) {
        next(error);
    }
};

export const updateCategory = async (req: AuthRequest, res: Response, next: NextFunction) => {
    try {
        const data = updateCategorySchema.parse(req.body);
        const category = await categoryService.updateCategory(req.params.id, req.userId!, data);
        res.json(category);
    } 
    catch (error) {
        next(error);
    }
};

export const deleteCategory = async (req: AuthRequest, res: Response, next: NextFunction) => {
    try {
        await categoryService.deleteCategory(req.params.id, req.userId!);
        res.json({ message: 'Xóa category thành công' });
    }
    catch (error) {
        next(error);
    }
};