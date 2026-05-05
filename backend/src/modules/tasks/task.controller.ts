import { Response, NextFunction} from 'express';
import { AuthRequest } from '../auth/auth.middleware';
import * as taskService from './task.service';
import { createTaskSchema, updateTaskSchema, taskFilterSchema } from '../../shared/validators/task.validator';

export const getTasks = async (req: AuthRequest, res: Response, next: NextFunction) => {
    try {
        const filters = taskFilterSchema.parse(req.query);
        const tasks = await taskService.getTasks(req.userId!, filters);
        res.json(tasks);
    }
    catch (err) {
        next(err);
    }
};

export const getTaskById = async (req: AuthRequest, res: Response, next: NextFunction) => {
    try {
        const task = await taskService.getTaskById(req.params.id, req.userId!);
        res.json(task);
    }
    catch (err) {
        next(err);
    }
};

export const createTask = async (req: AuthRequest, res: Response, next: NextFunction) => {
    try {
        const taskData = createTaskSchema.parse(req.body);
        const newTask = await taskService.createTask(req.userId!, taskData);
        res.status(201).json(newTask);
    }
    catch (err) {
        next(err);
    }
};

export const updateTask = async (req: AuthRequest, res: Response, next: NextFunction) => {
    try {
        const taskData = updateTaskSchema.parse(req.body);
        const updatedTask = await taskService.updateTask(req.params.id, req.userId!, taskData);
        res.json(updatedTask);
    }
    catch (err) {
        next(err);
    }
};

export const deleteTask = async (req: AuthRequest, res: Response, next: NextFunction) => {
    try {
        await taskService.deleteTask(req.params.id, req.userId!);
        res.status(204).send();
    }
    catch (err) {
        next(err);
    }
};