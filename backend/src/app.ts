import express from 'express';
import cors from 'cors';
import helmet from 'helmet';
import morgan from 'morgan';
import { PrismaClient } from '@prisma/client';

import authRoutes from './modules/auth/auth.routes';
import taskRoutes from './modules/tasks/task.routes';

// Initialize Prisma
export const prisma = new PrismaClient();

// Initialize Express app
const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(cors()); // Enable CORS for all routes (allow requests from any origin)
app.use(helmet()); // Set security-related HTTP headers
app.use(morgan('dev')); // Log HTTP requests to the console
app.use(express.json()); // Parse JSON body

// ─── Health check ─────────────────────────────────────
app.get('/health', (req, res) => {
  res.json({ status: 'ok', timestamp: new Date() });
});

// Routes
app.use('/auth', authRoutes);
app.use('/tasks', taskRoutes);

// Error handling 
app.use((err: any, req: express.Request, res: express.Response, next: express.NextFunction) => {
  console.error(err.stack);
  res.status(err.status || 500).json({
    error: err.message || 'Internal Server Error'
  });
});

// Start server
app.listen(PORT, () => {
  console.log(`Server is running on PORT ${PORT}`)
});

export default app;