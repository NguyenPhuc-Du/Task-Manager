import { Request, Response, NextFunction } from 'express';

export const logger = (req: Request, res: Response, next: NextFunction) => {
    const start = Date.now();

    res.on('finish', () => {
        const duration = Date.now() - start;
        const color = res.statusCode >= 400 ? '\x1b[31m' : '\x1b[32m'; // Red for errors, green for success
        console.log(
            `${color}[${new Date().toISOString()}] ${req.method} ${req.path} ${res.statusCode} - ${duration}ms\x1b[0m`
        );
    });

    next();
};