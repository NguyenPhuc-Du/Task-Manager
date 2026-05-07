import { describe, it, expect, beforeAll } from 'vitest';
import request from 'supertest';
import app from '../src/app';
import redis from '../src/config/redis';

let token: string;
let taskId: string;
let categoryId: string;

beforeAll(async () => {
  // Delete rate limit keys to avoid hitting limits during tests
  const keys = await redis.keys('rate-limit:*');
  if (keys.length > 0) await redis.del(keys);

  //    Register user for testing
  await request(app)
    .post('/auth/register')  // đúng route
    .send({ email: 'testtask@test.com', password: '123456' });

  //    Login to get token
  const res = await request(app)
    .post('/auth/login')
    .send({ email: 'testtask@test.com', password: '123456' });

  token = res.body.token;

  // Create a category for testing
  const catRes = await request(app)
    .post('/categories')
    .set('Authorization', `Bearer ${token}`)
    .send({ name: 'Test Category', color: '#FF0000' });

  categoryId = catRes.body.id;
});

describe('Tasks API', () => {

  describe('POST /tasks', () => {
    it('should create task successfully', async () => {
      const res = await request(app)
        .post('/tasks')
        .set('Authorization', `Bearer ${token}`)
        .send({
          title: 'Test Task',
          description: 'Test Description',
          priority: 2
        });

      expect(res.status).toBe(201);
      expect(res.body.title).toBe('Test Task');
      expect(res.body.priority).toBe(2);
      taskId = res.body.id;
    });

    it('should create task with category', async () => {
      const res = await request(app)
        .post('/tasks')
        .set('Authorization', `Bearer ${token}`)
        .send({
          title: 'Task with Category',
          priority: 1,
          categoryId
        });

      expect(res.status).toBe(201);
      expect(res.body.categoryId).toBe(categoryId);
    });

    it('should fail without title', async () => {
      const res = await request(app)
        .post('/tasks')
        .set('Authorization', `Bearer ${token}`)
        .send({ priority: 1 });

      expect(res.status).toBe(400);
    });

    it('should fail without token', async () => {
      const res = await request(app)
        .post('/tasks')
        .send({ title: 'No Auth Task' });

      expect(res.status).toBe(401);
    });
  });

  describe('GET /tasks', () => {
    it('should get all tasks', async () => {
      const res = await request(app)
        .get('/tasks')
        .set('Authorization', `Bearer ${token}`);

      expect(res.status).toBe(200);
      expect(Array.isArray(res.body)).toBe(true);
      expect(res.body.length).toBeGreaterThan(0);
    });

    it('should filter by priority', async () => {
      const res = await request(app)
        .get('/tasks?priority=2')
        .set('Authorization', `Bearer ${token}`);

      expect(res.status).toBe(200);
      res.body.forEach((task: any) => {
        expect(task.priority).toBe(2);
      });
    });
  });

  describe('GET /tasks/:id', () => {
    it('should get task by id', async () => {
      const res = await request(app)
        .get(`/tasks/${taskId}`)
        .set('Authorization', `Bearer ${token}`);

      expect(res.status).toBe(200);
      expect(res.body.id).toBe(taskId);
    });

    it('should fail with invalid id', async () => {
      const res = await request(app)
        .get('/tasks/invalid-id')
        .set('Authorization', `Bearer ${token}`);

      expect(res.status).toBe(404);
    });
  });

  describe('PUT /tasks/:id', () => {
    it('should update task', async () => {
      const res = await request(app)
        .put(`/tasks/${taskId}`)
        .set('Authorization', `Bearer ${token}`)
        .send({ completed: true, priority: 3 });

      expect(res.status).toBe(200);
      expect(res.body.completed).toBe(true);
      expect(res.body.priority).toBe(3);
    });
  });

  describe('DELETE /tasks/:id', () => {
    it('should delete task', async () => {
      const res = await request(app)
        .delete(`/tasks/${taskId}`)
        .set('Authorization', `Bearer ${token}`);

      expect(res.status).toBe(204);
    });

    it('should fail getting deleted task', async () => {
      const res = await request(app)
        .get(`/tasks/${taskId}`)
        .set('Authorization', `Bearer ${token}`);

      expect(res.status).toBe(404);
    });
  });

});