import { Router } from 'express';
import { authenticate } from '../auth/auth.middleware';
import * as analyticsController from './analytics.controller';

const router = Router();

router.use(authenticate);

router.get('/', analyticsController.getAnalytics);
router.get('/pomodoro', analyticsController.getPomodoroStats);

export default router