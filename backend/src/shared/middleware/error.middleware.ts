import { Request, Response, NextFunction } from 'express';
import { ZodError } from 'zod';

export const errorHandler = (
  err: any,
  req: Request,
  res: Response,
  next: NextFunction
) => {
  // Zod validation error
  if (err instanceof ZodError) {
    return res.status(400).json({
      error: 'Dữ liệu không hợp lệ',
      details: err.issues.map(e => ({
        field: e.path.join('.'),
        message: e.message
      }))
    });
  }

  // Prisma errors
  if (err.code === 'P2002') {
    return res.status(409).json({ error: 'Dữ liệu đã tồn tại' });
  }
  if (err.code === 'P2025') {
    return res.status(404).json({ error: 'Không tìm thấy dữ liệu' });
  }

  // Custom errors
  if (err.message?.includes('Không tìm thấy')) {
    return res.status(404).json({ error: err.message });
  }
  if (err.message?.includes('đã được sử dụng')) {
    return res.status(409).json({ error: err.message });
  }
  if (err.message?.includes('không đúng')) {
    return res.status(401).json({ error: err.message });
  }

  // Default
  console.error(err.stack);
  res.status(500).json({ error: 'Internal Server Error' });
};