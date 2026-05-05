import * as analyticsRepo from './analytics.repository';

export const getAnalytics = async (userId: string) => {
    const [
        totalTasks,
        completedTasks,
        tasksByPriority,
        tasksByCategory,
        recentActivity
    ] = await Promise.all ([
        analyticsRepo.countTasks(userId),
        analyticsRepo.countCompletedTasks(userId),
        analyticsRepo.getTasksByPriority(userId),
        analyticsRepo.getTasksByCategory(userId),
        analyticsRepo.getRecentTasks(userId)
    ]);

    const completionRate = totalTasks > 0 
    ? Math.round((completedTasks / totalTasks) * 100) 
    : 0;

    return {
        overview: {
            totalTasks,
            completedTasks,
            pendingTasks: totalTasks - completedTasks,
            completionRate
        },
        tasksByPriority: tasksByPriority.map(p => ({
            priority: p.priority,
            label: ['Thấp', 'Trung bình', 'Cao', 'Khẩn cấp'][p.priority],
            count: p._count.id
        })),
        tasksByCategory,
        recentActivity
    };
};

export const getPomodoroStats = async (userId: string) => {
  const [total, completed, sessions] = await Promise.all([
    analyticsRepo.countPomodoros(userId),
    analyticsRepo.countCompletedPomodoros(userId),
    analyticsRepo.getRecentPomodoros(userId)
  ]);

  const totalMinutes = sessions.reduce((acc, s) => acc + s.duration, 0);

  return {
    totalSessions: total,
    completedSessions: completed,
    totalMinutes,
    recentSessions: sessions
  };
};
