package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TaskPriority
import com.example.ui.theme.PriorityHighColor
import com.example.ui.theme.PriorityLowColor
import com.example.ui.theme.PriorityMediumColor

@Composable
fun PriorityBadge(priority: TaskPriority, modifier: Modifier = Modifier) {
    val (bgColor, textColor) = when (priority) {
        TaskPriority.HIGH -> PriorityHighColor.copy(alpha = 0.15f) to PriorityHighColor
        TaskPriority.MEDIUM -> PriorityMediumColor.copy(alpha = 0.15f) to PriorityMediumColor
        TaskPriority.LOW -> PriorityLowColor.copy(alpha = 0.15f) to PriorityLowColor
    }

    Text(
        text = priority.name,
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
        color = textColor,
        modifier = modifier
            .background(bgColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}
