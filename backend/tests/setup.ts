import { prisma } from '../src/app';
import redis from '../src/config/redis';
import { afterAll, beforeAll } from 'vitest';

beforeAll(async () => {
    const volatilePatterns = ['rate-limit:*', 'blacklist:*', 'tasks:*'];
    for (const pattern of volatilePatterns) {
        const keys = await redis.keys(pattern);
        if (keys.length > 0) await redis.del(keys);
    }

    // Clear the database before running tests 
    await prisma.pomodoroSession.deleteMany();
    await prisma.task.deleteMany();
    await prisma.category.deleteMany();
    await prisma.user.deleteMany();
});

afterAll(async () => {
  // Clear the database after running tests
  await prisma.pomodoroSession.deleteMany();
  await prisma.task.deleteMany();
  await prisma.category.deleteMany();
  await prisma.user.deleteMany();
  await prisma.$disconnect();
  await redis.quit();
});