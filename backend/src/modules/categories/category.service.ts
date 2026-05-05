import { id } from 'zod/locales';
import * as categoryRepo from './category.repository';

export const getCategories = async (userId: string) => {
    return categoryRepo.findAllByUser(userId);
};

export const createCategory = async (usrId: string, data: any) => {
    return categoryRepo.create(usrId, data);
};

export const updateCategory = async (id: string, userId: string, data: any) => {
    const category = await categoryRepo.findById(id, userId);
    if (!category) {
        throw new Error('Không tìm thấy category');
    }

    return categoryRepo.update(id, data);
};

export const deleteCategory = async (id: string, userId: string) => {
    const category = await categoryRepo.findById(id, userId);
    if (!category) {
        throw new Error('Không tìm thấy category');
    }

    return categoryRepo.remove(id);
};