import { Router } from 'express';
import * as authController from './auth.controller';
import { authenticate } from './auth.middleware';
import { authRateLimit } from '../../shared/middleware/rateLimit.middleware';

const router = Router();

router.post('/register', authController.register);
router.post('/login', authRateLimit, authController.login);
router.post('/logout', authenticate, authController.logout);

export default router;