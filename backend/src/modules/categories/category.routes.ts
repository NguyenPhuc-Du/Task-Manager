import { Router } from 'express';
import * as categoryController from './category.controller';
import { authenticate } from '../auth/auth.middleware';

const router =Router();

router.use(authenticate);

router.get('/', categoryController.getCategories);
router.post('/', categoryController.createCategory);
router.put('/:id', categoryController.updateCategory);
router.delete('/:id', categoryController.deleteCategory);

export default router;