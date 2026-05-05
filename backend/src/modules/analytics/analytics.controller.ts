import {  Response, NextFunction } from 'express';
import { AuthRequest } from '../auth/auth.middleware';
import * as analyticsService from './analytics.service';

export const getAnalytics = async (req: AuthRequest, res: Response, next: NextFunction) => {
    try {
        const data = await analyticsService.getAnalytics(req.userId!);
        res.json(data);
    } 
    catch (err) {
        next(err);
    }
};

export const getPomodoroStats = async (req: AuthRequest, res: Response, next: NextFunction) => {
    try {
        const data = await analyticsService.getPomodoroStats(req.userId!);
        res.json(data);
    }
    catch (err) {
        next(err);
    }
};