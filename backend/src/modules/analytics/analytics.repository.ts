import { prisma } from '../../app';

export const countTasks = async (userId: string) => {
    return prisma.task.count({
        where: { userId }
    });
};

export const countCompletedTasks = async (userId: string) => {
    return prisma.task.count({
        where: {
            userId,
            completed: true
        }
    });
};

export const getTasksByPriority = async (userId: string) => {
    return prisma.task.groupBy({
        by: ['priority'],
        where: { userId },
        _count: {
            id: true
        }
    });
};

export const getTasksByCategory = async (userId: string) => {
    return prisma.task.groupBy({
        by: ['categoryId'],
        where: { userId },
        _count: {
            id: true
        }
    });
};

export const getRecentTasks = async (userId: string) => {
    return prisma.task.findMany({
        where: {
            userId,
            createdAt: {
                gte: new Date(Date.now() - 7 *24 * 60 * 60 * 1000) // Last 7 days
            }
        },
        select: {
            id: true,
            title: true,
            completed: true,
            priority: true,
            createdAt: true
        },
        orderBy: {
            createdAt: 'desc'
        }
    });
};

export const countPomodoros = async (userId: string) => {
  return prisma.pomodoroSession.count({ where: { userId } });
};

export const countCompletedPomodoros = async (userId: string) => {
  return prisma.pomodoroSession.count({ where: { userId, completed: true } });
};

export const getRecentPomodoros = async (userId: string) => {
  return prisma.pomodoroSession.findMany({
    where: { userId },
    orderBy: { createdAt: 'desc' },
    take: 10,
    include: { task: { select: { title: true } } }
  });
};