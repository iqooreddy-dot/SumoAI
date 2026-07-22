package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TaskPriority {
    HIGH, MEDIUM, LOW
}

enum class TaskCategory {
    WORK, PERSONAL, HEALTH, FINANCE, STUDY, GENERAL
}

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val category: TaskCategory = TaskCategory.GENERAL,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val dueDateMillis: Long = System.currentTimeMillis(),
    val isCompleted: Boolean = false,
    val createdAtMillis: Long = System.currentTimeMillis()
)
