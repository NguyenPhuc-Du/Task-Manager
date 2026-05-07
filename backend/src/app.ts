import express from 'express';
import cors from 'cors';
import helmet from 'helmet';
import { PrismaClient } from '@prisma/client';

import authRoutes from './modules/auth/auth.routes';
import taskRoutes from './modules/tasks/task.routes';
import categoryRoutes from './modules/categories/category.routes';
import analyticsRoutes from './modules/analytics/analytics.routes';

import { errorHandler } from './shared/middleware/error.middleware';
import { apiRateLimit } from './shared/middleware/rateLimit.middleware';
import { logger } from './shared/middleware/logger.middleware';

import { createServer } from 'http';
import { Server } from 'socket.io';
import { initGateway } from './modules/sync/sync.gateway';

// Initialize Prisma
export const prisma = new PrismaClient();

// Initialize Express app
const app = express();
export const httpServer = createServer(app);

// Initialize Socket.IO server
export const io = new Server(httpServer, {
  cors: {
    origin: '*', // Allow requests from any origin (for development)
    methods: ['GET', 'POST']
  }
});

initGateway(io);

// Middleware
app.use(helmet()); // Set security-related HTTP headers
app.use(cors()); // Enable CORS for all routes (allow requests from any origin)
app.use(logger); // Log HTTP requests to the console
app.use(express.json()); // Parse JSON body
app.use(apiRateLimit); // Apply rate limiting to all API routes

// ─── Health check ─────────────────────────────────────
app.get('/health', (req, res) => {
  res.json({ status: 'ok', timestamp: new Date() });
});

// Routes
app.use('/auth', authRoutes);
app.use('/tasks', taskRoutes);
app.use('/categories', categoryRoutes);
app.use('/analytics', analyticsRoutes);
// Error handling 
app.use(errorHandler);

export default app;