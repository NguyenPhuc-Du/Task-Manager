import { io } from '../../app';
import { emitToUser } from '../sync/sync.service';
import redis from '../../config/redis';
import * as taskRepo from './task.repository';

const getCacheKey = (userId: string, filters?: Record<string, unknown>) => {
    if (!filters || Object.keys(filters).length === 0) {
        return `tasks:${userId}:all`;
    }

    const normalizedFilters = Object.entries(filters)
        .filter(([, value]) => value !== undefined && value !== null && value !== '')
        .sort(([a], [b]) => a.localeCompare(b));

    const filterQuery = new URLSearchParams(
        normalizedFilters.map(([key, value]) => [key, String(value)])
    ).toString();

    return `tasks:${userId}:${filterQuery || 'all'}`;
};

export const getTasks = async (userId: string, filters: any) => {
    const cacheKey = getCacheKey(userId, filters);
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
    const keys = await redis.keys(`tasks:${userId}:*`);
    if (keys.length > 0) await redis.del(keys);
    emitToUser(io, userId, 'task:updated', task);

    return task;
};

export const createTask = async (userId: string, data: any) => {
    const task = await taskRepo.create(userId, data);
    const keys = await redis.keys(`tasks:${userId}:*`);
    if (keys.length > 0) await redis.del(keys);
    emitToUser(io, userId, 'task:created', task);

    return task;
};

export const deleteTask = async (id: string, userId: string) => {
    await getTaskById(id, userId);

    const task = await taskRepo.remove(id);
    const keys = await redis.keys(`tasks:${userId}:*`);
    if (keys.length > 0) await redis.del(keys);
    emitToUser(io, userId, 'task:deleted', { id });
};