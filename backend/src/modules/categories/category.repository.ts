import id from 'zod/v4/locales/id.js';
import { prisma } from '../../app';

export const findAllByUser = async (userId: string) => {
    return prisma.category.findMany({
        where: { userId },
        include: {
            _count: {
                select: {
                    tasks: true
                }
            }
        }
    });
};

export const findById = async (id: string, userId: string) => {
    return prisma.category.findFirst({
        where: { id, userId },
    });
};

export const create = async (userId: string, data: any) => {
    return prisma.category.create({
        data: {
            ...data,
            userId
        }
    });
};

export const update = async (id: string, data: any) => {
    return prisma.category.update({
        where: { id },
        data
    })
};

export const remove = async (id: string) => {
    return prisma.category.delete({
        where: { id }
    });
};