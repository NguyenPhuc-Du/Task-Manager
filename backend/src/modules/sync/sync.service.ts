import { Server } from 'socket.io';

export const handleSyncRequest = async (userId: string) => {
    return { status: 'ok', timestamp: new Date(), userId };
};

export const emitToUser = (
    io: Server,
    userId: string,
    event: string,
    data: any
) => {
    io.to(`user:${userId}`).emit(event, data);
}