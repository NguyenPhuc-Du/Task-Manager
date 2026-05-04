import { Request, Response, NextFunction } from 'express';
import { verifyToken } from '../../shared/utils/jwt.utils';
import redis from '../../config/redis';

export interface AuthRequest extends Request {
    userId?: string;
}

export const authenticate = async (
    req: AuthRequest,
    res: Response,
    next: NextFunction
) => {
    try {
        const token = req.headers.authorization?.split(' ')[1]; // Extract token from "Bearer <token>"
        if (!token) {
            return res.status(401).json({ message: 'Không có token'});
        }

        const blacklisted = await redis.get('blacklist:' + token);
        if (blacklisted) {
            return res.status(401).json({ message: 'Token đã hết hạn' });
        }

        const decoded = verifyToken(token);

        req.userId = decoded.userId;
        next();

    } catch (error) {
        return res.status(401).json({ message: 'Token không hợp lệ' });
    }
};