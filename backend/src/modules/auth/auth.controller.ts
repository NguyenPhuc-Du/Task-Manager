import { Request, Response, NextFunction } from 'express';
import * as authService from './auth.service';
import { registerSchema, loginSchema } from '../../shared/validators/auth.validator';  

export const register = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const data = registerSchema.parse(req.body);
        const result = await authService.register(data.email, data.password, data.name);
        res.status(201).json(result);
    } catch (error) {
        next(error);
    }
};

export const login = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const data = loginSchema.parse(req.body) 
        const result = await authService.login(data.email, data.password);
        res.json(result);
    } catch (error) {
        next(error);
    }
};

export const logout = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const token = req.headers.authorization?.split(' ')[1]; // Extract token from "Bearer <token>"

        if (token) {
            await authService.logout(token);
            res.json({ message: 'Đăng xuất thành công' });
        }
    } catch (error) {
        next(error);
    }
};