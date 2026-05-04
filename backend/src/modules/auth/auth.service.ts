import { verify } from 'crypto';
import { prisma } from '../../app';
import redis from '../../config/redis';
import { hashPassword, comparePassword } from '../../shared/utils/hash.utils';
import { generateToken, verifyToken } from '../../shared/utils/jwt.utils';

export const register = async (email: string, password: string, name?: string) => {
    const existing = await prisma.user.findUnique({ where: { email } });

    if (existing) {
        throw new Error('Email đã được sử dụng');
    }

    const hashedPassword = await hashPassword(password);
    const user = await prisma.user.create({
        data: { email, password: hashedPassword, name }, 
        select: { id:true, email: true, name: true, createdAt: true }
    });

    const token = generateToken(user.id);

    return { user, token };
};

export const login = async (email: string, password: string) => {
    const user = await prisma.user.findUnique({ where: { email }});

    if (!user) {
        throw new Error('Email hoặc mật khẩu không đúng');
    }

    const valid = await comparePassword(password, user.password);

    if (!valid) {
        throw new Error('Email hoặc mật khẩu không đúng');
    }

    const token = generateToken(user.id);

    return {
        user: { id: user.id, email: user.email, name: user.name },
        token
    }
};

export const logout = async (token: string) => {
    const decode = verifyToken(token);
    const exp = (decode as any).exp;
    const ttl = exp - Math.floor(Date.now() / 1000);

    if (ttl > 0)
    {
        await redis.setEx(`blacklist:${token}`, ttl, '1');
    }
};