package com.example.data

import kotlinx.coroutines.flow.Flow

class TaskRepository(
    private val taskDao: TaskDao,
    private val habitDao: HabitDao
) {
    val allTasks: Flow<List<TaskEntity>> = taskDao.getAllTasks()
    val allHabits: Flow<List<HabitEntity>> = habitDao.getAllHabits()

    suspend fun insertTask(task: TaskEntity): Long = taskDao.insertTask(task)

    suspend fun updateTask(task: TaskEntity) = taskDao.updateTask(task)

    suspend fun deleteTask(task: TaskEntity) = taskDao.deleteTask(task)

    suspend fun toggleTaskCompletion(task: TaskEntity) {
        taskDao.updateTaskCompletion(task.id, !task.isCompleted)
    }

    suspend fun insertHabit(habit: HabitEntity): Long = habitDao.insertHabit(habit)

    suspend fun updateHabit(habit: HabitEntity) = habitDao.updateHabit(habit)

    suspend fun deleteHabit(habit: HabitEntity) = habitDao.deleteHabit(habit)

    suspend fun checkInHabit(habit: HabitEntity) {
        val now = System.currentTimeMillis()
        val updatedStreak = habit.currentStreak + 1
        val newBestStreak = maxOf(habit.bestStreak, updatedStreak)
        val updatedHabit = habit.copy(
            currentStreak = updatedStreak,
            bestStreak = newBestStreak,
            lastCompletedDateMillis = now
        )
        habitDao.updateHabit(updatedHabit)
    }
}
