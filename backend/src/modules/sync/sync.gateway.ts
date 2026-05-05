import { Server, Socket } from 'socket.io';
import { verifyToken } from '../../shared/utils/jwt.utils';
import * as syncService from './sync.service';

export const initGateway = (io: Server) => {
    // Middleware to authenticate socket connections
    io.use((socket: Socket, next) => {
        try {
            const token = socket.handshake.auth.token
                            || socket.handshake.query.token as string;

            if (!token) {
                return next(new Error('Không có token'));
            }

            const decode = verifyToken(token);
            socket.data.userId = decode.userId; // Save userId in socket data for later use
            next();
        }
        catch(err) {
            next(new Error('Token không hợp lệ'));
        }
    });

    io.on('connection', (socket: Socket) => {
        const userId = socket.data.userId;
        console.log(`User ${userId} connected to sync gateway`);

        //Join a room specific to the user for targeted emissions
        socket.join(`user:${userId}`);

        // Handle sync request from client
        socket.on('sync_request', async (data) => {
            const result = await syncService.handleSyncRequest(userId);
            socket.emit('sync_response', result);
        });

        socket.on('disconnect', () => {
            console.log(`User ${userId} disconnected from sync gateway`);
        });
    });
}