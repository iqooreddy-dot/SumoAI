package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class TaskUiState(
    val tasks: List<TaskEntity> = emptyList(),
    val habits: List<HabitEntity> = emptyList(),
    val selectedCategory: TaskCategory? = null,
    val searchQuery: String = "",
    val filterPriority: TaskPriority? = null,
    val hideCompleted: Boolean = false,
    val selectedTab: Int = 0 // 0: Dashboard, 1: Tasks, 2: Habits, 3: Analytics
)

data class FilterOptions(
    val category: TaskCategory? = null,
    val searchQuery: String = "",
    val filterPriority: TaskPriority? = null,
    val hideCompleted: Boolean = false,
    val selectedTab: Int = 0
)

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = TaskRepository(database.taskDao(), database.habitDao())
        
        // Seed initial sample data if database is empty
        viewModelScope.launch {
            repository.allTasks.firstOrNull().let { list ->
                if (list.isNullOrEmpty()) {
                    seedSampleData()
                }
            }
        }
    }

    private val _filterOptions = MutableStateFlow(FilterOptions())

    val uiState: StateFlow<TaskUiState> = combine(
        repository.allTasks,
        repository.allHabits,
        _filterOptions
    ) { tasks, habits, filters ->
        val filteredTasks = tasks.filter { task ->
            val matchesCategory = filters.category == null || task.category == filters.category
            val matchesQuery = filters.searchQuery.isBlank() ||
                    task.title.contains(filters.searchQuery, ignoreCase = true) ||
                    task.description.contains(filters.searchQuery, ignoreCase = true)
            val matchesPriority = filters.filterPriority == null || task.priority == filters.filterPriority
            val matchesCompletion = !filters.hideCompleted || !task.isCompleted
            matchesCategory && matchesQuery && matchesPriority && matchesCompletion
        }

        TaskUiState(
            tasks = filteredTasks,
            habits = habits,
            selectedCategory = filters.category,
            searchQuery = filters.searchQuery,
            filterPriority = filters.filterPriority,
            hideCompleted = filters.hideCompleted,
            selectedTab = filters.selectedTab
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TaskUiState()
    )

    fun selectTab(tabIndex: Int) {
        _filterOptions.update { it.copy(selectedTab = tabIndex) }
    }

    fun setCategoryFilter(category: TaskCategory?) {
        _filterOptions.update { it.copy(category = category) }
    }

    fun setSearchQuery(query: String) {
        _filterOptions.update { it.copy(searchQuery = query) }
    }

    fun setPriorityFilter(priority: TaskPriority?) {
        _filterOptions.update { it.copy(filterPriority = priority) }
    }

    fun toggleHideCompleted() {
        _filterOptions.update { it.copy(hideCompleted = !it.hideCompleted) }
    }

    fun toggleTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.toggleTaskCompletion(task)
        }
    }

    fun addTask(title: String, description: String, category: TaskCategory, priority: TaskPriority, dueDateMillis: Long) {
        viewModelScope.launch {
            repository.insertTask(
                TaskEntity(
                    title = title,
                    description = description,
                    category = category,
                    priority = priority,
                    dueDateMillis = dueDateMillis
                )
            )
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun addHabit(title: String, category: TaskCategory, targetDaysPerWeek: Int) {
        viewModelScope.launch {
            repository.insertHabit(
                HabitEntity(
                    title = title,
                    category = category,
                    targetDaysPerWeek = targetDaysPerWeek
                )
            )
        }
    }

    fun checkInHabit(habit: HabitEntity) {
        viewModelScope.launch {
            repository.checkInHabit(habit)
        }
    }

    fun deleteHabit(habit: HabitEntity) {
        viewModelScope.launch {
            repository.deleteHabit(habit)
        }
    }

    private suspend fun seedSampleData() {
        val sampleTasks = listOf(
            TaskEntity(
                title = "Design UI Wireframes",
                description = "Create Material 3 layouts for the new Android project",
                category = TaskCategory.WORK,
                priority = TaskPriority.HIGH,
                isCompleted = false
            ),
            TaskEntity(
                title = "Morning Workout & Stretch",
                description = "30 mins cardio and core exercises",
                category = TaskCategory.HEALTH,
                priority = TaskPriority.MEDIUM,
                isCompleted = true
            ),
            TaskEntity(
                title = "Review Weekly Budget",
                description = "Track expenses and save receipts",
                category = TaskCategory.FINANCE,
                priority = TaskPriority.MEDIUM,
                isCompleted = false
            ),
            TaskEntity(
                title = "Kotlin Coroutines Study",
                description = "Read Flow and StateFlow official documentation",
                category = TaskCategory.STUDY,
                priority = TaskPriority.LOW,
                isCompleted = false
            )
        )

        val sampleHabits = listOf(
            HabitEntity(
                title = "Drink 2L Water",
                category = TaskCategory.HEALTH,
                targetDaysPerWeek = 7,
                currentStreak = 5,
                bestStreak = 12
            ),
            HabitEntity(
                title = "Read 15 mins",
                category = TaskCategory.STUDY,
                targetDaysPerWeek = 5,
                currentStreak = 3,
                bestStreak = 8
            ),
            HabitEntity(
                title = "Daily Code Commit",
                category = TaskCategory.WORK,
                targetDaysPerWeek = 5,
                currentStreak = 7,
                bestStreak = 15
            )
        )

        sampleTasks.forEach { repository.insertTask(it) }
        sampleHabits.forEach { repository.insertHabit(it) }
    }
}
