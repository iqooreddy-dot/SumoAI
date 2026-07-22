package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AddHabitDialog
import com.example.ui.components.AddTaskDialog
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.HabitsScreen
import com.example.ui.screens.TasksScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskTrackerApp(
    viewModel: TaskViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var isAddTaskDialogOpen by remember { mutableStateOf(false) }
    var isAddHabitDialogOpen by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (uiState.selectedTab) {
                            0 -> "Overview"
                            1 -> "My Tasks"
                            2 -> "Habits Tracker"
                            3 -> "Analytics"
                            else -> "Task & Habit Tracker"
                        },
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                NavigationBarItem(
                    selected = uiState.selectedTab == 0,
                    onClick = { viewModel.selectTab(0) },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                    label = { Text("Dashboard") },
                    modifier = Modifier.testTag("nav_tab_dashboard")
                )
                NavigationBarItem(
                    selected = uiState.selectedTab == 1,
                    onClick = { viewModel.selectTab(1) },
                    icon = { Icon(Icons.Default.ListAlt, contentDescription = "Tasks") },
                    label = { Text("Tasks") },
                    modifier = Modifier.testTag("nav_tab_tasks")
                )
                NavigationBarItem(
                    selected = uiState.selectedTab == 2,
                    onClick = { viewModel.selectTab(2) },
                    icon = { Icon(Icons.Default.CheckCircle, contentDescription = "Habits") },
                    label = { Text("Habits") },
                    modifier = Modifier.testTag("nav_tab_habits")
                )
                NavigationBarItem(
                    selected = uiState.selectedTab == 3,
                    onClick = { viewModel.selectTab(3) },
                    icon = { Icon(Icons.Default.BarChart, contentDescription = "Analytics") },
                    label = { Text("Analytics") },
                    modifier = Modifier.testTag("nav_tab_analytics")
                )
            }
        },
        floatingActionButton = {
            if (uiState.selectedTab == 0 || uiState.selectedTab == 1) {
                FloatingActionButton(
                    onClick = { isAddTaskDialogOpen = true },
                    modifier = Modifier.testTag("fab_add_task")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Task")
                }
            } else if (uiState.selectedTab == 2) {
                FloatingActionButton(
                    onClick = { isAddHabitDialogOpen = true },
                    modifier = Modifier.testTag("fab_add_habit")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Habit")
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.selectedTab) {
                0 -> DashboardScreen(
                    uiState = uiState,
                    onToggleTask = viewModel::toggleTask,
                    onCheckInHabit = viewModel::checkInHabit,
                    onNavigateToTasks = { viewModel.selectTab(1) },
                    onNavigateToHabits = { viewModel.selectTab(2) },
                    onOpenAddTaskDialog = { isAddTaskDialogOpen = true },
                    onOpenAddHabitDialog = { isAddHabitDialogOpen = true }
                )
                1 -> TasksScreen(
                    uiState = uiState,
                    onSearchQueryChange = viewModel::setSearchQuery,
                    onCategorySelect = viewModel::setCategoryFilter,
                    onPrioritySelect = viewModel::setPriorityFilter,
                    onToggleHideCompleted = viewModel::toggleHideCompleted,
                    onToggleTask = viewModel::toggleTask,
                    onDeleteTask = viewModel::deleteTask,
                    onOpenAddTaskDialog = { isAddTaskDialogOpen = true }
                )
                2 -> HabitsScreen(
                    uiState = uiState,
                    onCheckInHabit = viewModel::checkInHabit,
                    onDeleteHabit = viewModel::deleteHabit,
                    onOpenAddHabitDialog = { isAddHabitDialogOpen = true }
                )
                3 -> AnalyticsScreen(
                    uiState = uiState
                )
            }
        }

        if (isAddTaskDialogOpen) {
            AddTaskDialog(
                onDismiss = { isAddTaskDialogOpen = false },
                onAddTask = { title, desc, category, priority, dueDate ->
                    viewModel.addTask(title, desc, category, priority, dueDate)
                }
            )
        }

        if (isAddHabitDialogOpen) {
            AddHabitDialog(
                onDismiss = { isAddHabitDialogOpen = false },
                onAddHabit = { title, category, targetDays ->
                    viewModel.addHabit(title, category, targetDays)
                }
            )
        }
    }
}
