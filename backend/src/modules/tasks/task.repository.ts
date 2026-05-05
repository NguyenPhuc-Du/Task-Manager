import { prisma } from '../../app';

export const findAllTasks = async (userId: string, filters: any) => {
    const { completed, priority, categoryId, sortBy = 'createdAt', order = 'desc' } = filters;

    return prisma.task.findMany({
        where: {
            userId,
            ...(completed != undefined && { completed: completed === 'true'}),
            ...(priority !== undefined && { priority: Number(priority) }),
            ...(categoryId && { categoryId })
        },
        orderBy: {
            [sortBy]: order
        },
        include: {
            category: true
        }
    });
};

export const findById = async (id: string, userId: string) => {
    return prisma.task.findFirst({
        where: { id, userId },
        include: {
            category: true
        }
    });
};

export const create = async (userId: string, data: any) => {
    return prisma.task.create({
        data: {
            ...data, userId
        },
        include: {
            category: true
        }
    });
};

export const update = async (id: string, userId: string, data: any) => {
    return prisma.task.update({
        where: { id }, 
        data,
        include: {
            category: true
        }
    });
};

export const remove = async (id: string) => {
    return prisma.task.delete({
        where: { id }
    })
};