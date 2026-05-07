import { describe, it, expect, beforeAll } from 'vitest';
import request from 'supertest';
import app from '../src/app';
import { email } from 'zod';
import { as } from 'vitest/dist/chunks/reporters.nr4dxCkA.js';

let token: string;
let userId: string;

describe('Auth API', () => {

    describe('POST /auth/register', () => {
        it('should register successfully', async () => {
            const res = await request(app)
                .post('/auth/register')
                .send({
                    email: 'testauth@test.com',
                    password: '123456',
                    name: 'Test User'
                });
            
            expect(res.status).toBe(201);
            expect(res.body).toHaveProperty('token');
            expect(res.body.user.email).toBe('testauth@test.com');
        });

        it('should fail with duplicate email', async () => {
            const res = await request(app)
                .post('/auth/register')
                .send({
                    email: 'testauth@test.com',
                    password: '123456'
                });

            expect(res.status).toBe(409);
            expect(res.body).toHaveProperty('error');
        });

        it('should fail with invalid email', async () => {
            const res = await request(app)
                .post('/auth/register')
                .send({
                    email: 'invalid-email',
                    password: '123456'
                });

            expect(res.status).toBe(400);
        });

        it('should fail with short password', async () => {
            const res = await request(app)
                .post('/auth/register')
                .send({
                    email:'newtest@test.com',
                    password: '123'
                });

            expect(res.status).toBe(400);
        });
    });

    describe('POST /auth/login', () => {
        it('should login successfully', async () => {
            const res = await request(app)
                .post('/auth/login')
                .send({
                    email: 'testauth@test.com',
                    password: '123456'
                });

            expect(res.status).toBe(200);
            expect(res.body).toHaveProperty('token');
            token = res.body.token;
            userId = res.body.user.id;
        });

        it('should fail with wrong password', async () =>{
            const res = await request(app)
                .post('/auth/login')
                .send({
                    email: 'testauth@test.com',
                    password: 'wrongpassword'
                });

            expect(res.status).toBe(401);
        });

        it('should fail with non-existing email', async () => {
            const res = await request(app)
                .post('/auth/login')
                .send({
                    email: 'nonexistent@test.com',
                    password: '123456'
                });

            expect(res.status).toBe(401);
        });
    });

    describe('POST /auth/logout', () => {
        it('should logout successfully', async () => {
            const res = await request(app)
                .post('/auth/logout')
                .set('Authorization', `Bearer ${token}`);

            expect(res.status).toBe(200);
            expect(res.body.message).toBe('Đăng xuất thành công');
        });

        it('should fail without token', async () => {
            const res = await request(app)
                .post('/auth/logout');

            expect(res.status).toBe(401);
        });
    });
});