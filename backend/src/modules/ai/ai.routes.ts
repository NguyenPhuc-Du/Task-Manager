import { Router } from 'express';
import { authenticate } from '../auth/auth.middleware';
import * as aiController from './ai.controller';

const router = Router();

router.use(authenticate);

router.post('/predict', aiController.predictTaskPriority);
router.post('/insights', aiController.getTaskInsights);

export default router;
