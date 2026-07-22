package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: TaskCategory = TaskCategory.GENERAL,
    val targetDaysPerWeek: Int = 7,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val lastCompletedDateMillis: Long? = null,
    val createdAtMillis: Long = System.currentTimeMillis()
)
