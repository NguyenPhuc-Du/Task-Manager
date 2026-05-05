import redis from '../../config/redis';
import * as taskRepo from './task.repository';

const getCacheKey = (userId: string) => `tasks: ${userId}`;

export const getTasks = async (userId: string, filters: any) => {
    const cacheKey = getCacheKey(userId);
    const cached = await redis.get(cacheKey);

    if (cached) {
        return JSON.parse(cached);
    }

    const tasks = await taskRepo.findAllTasks(userId, filters);
    await redis.setEx(cacheKey, 300, JSON.stringify(tasks));

    return tasks;
};

export const getTaskById = async (id: string, userId: string) => {
    const tasks = await taskRepo.findById(id, userId);

    if (!tasks) {
        throw new Error('Không tìm thấy task');
    }

    return tasks;
};

export const updateTask = async (id: string, userId: string, data: any) => {
    await getTaskById(id, userId);

    const task = await taskRepo.update(id, userId, data);
    await redis.del(getCacheKey(userId));

    return task;
};

export const createTask = async (userId: string, data: any) => {
    const task = await taskRepo.create(userId, data);
    await redis.del(getCacheKey(userId));

    return task;
};

export const deleteTask = async (id: string, userId: string) => {
    getTaskById(id, userId);

    const task = await taskRepo.remove(id);
    await redis.del(getCacheKey(userId));
};