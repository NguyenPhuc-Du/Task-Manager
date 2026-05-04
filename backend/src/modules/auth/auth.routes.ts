import { Router } from 'express';
import * as authController from './auth.controller';
import { authenticate } from './auth.middleware';

const router = Router();

router.post('/register', authController.register);
router.post('/login', authController.login);
router.post('/logout', authenticate, authController.logout);

export default router;