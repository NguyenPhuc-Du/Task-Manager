import { Request, Response, NextFunction } from 'express';
import redis from '../../config/redis';

interface RateLimitOptions {
    windowSec: number;
    max: number;
    message?: string;
    keyPrefix?: string;
}

export const rateLimit = (options: RateLimitOptions) => {
    return async (req: Request, res: Response, next: NextFunction) => {
        const key = `${options.keyPrefix || 'rate-limit'}:${req.ip}`;
        const count = await redis.incr(key);

        if (count === 1) {
            await redis.expire(key, options.windowSec);
        }

        if (count > options.max) {
            return res.status(429).json({
                error: options.message || 'Quá nhiều request, vui lòng thử lại sau',
                retryAfter: options.windowSec
            });
        }

        next();
    };
};

// Preset rate limiters
export const authRateLimit = rateLimit({
    windowSec: 60,
    max: 5,
    message: 'Quá nhiều lần đăng nhập, thử lại sau 1 phút',
    keyPrefix: 'rate-limit:auth'
});

export const apiRateLimit = rateLimit({
    windowSec: 60,
    max: 100,
    keyPrefix: 'rate-limit:api'
});