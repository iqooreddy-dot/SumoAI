package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TaskCategory

@Composable
fun CategoryBadge(category: TaskCategory, modifier: Modifier = Modifier) {
    val (bgColor, textColor) = when (category) {
        TaskCategory.WORK -> Color(0xFFE0E7FF) to Color(0xFF3730A3)
        TaskCategory.PERSONAL -> Color(0xFFFCE7F3) to Color(0xFF9D174D)
        TaskCategory.HEALTH -> Color(0xFFD1FAE5) to Color(0xFF065F46)
        TaskCategory.FINANCE -> Color(0xFFFEF3C7) to Color(0xFF92400E)
        TaskCategory.STUDY -> Color(0xFFEDE9FE) to Color(0xFF5B21B6)
        TaskCategory.GENERAL -> Color(0xFFF1F5F9) to Color(0xFF334155)
    }

    Text(
        text = category.name.lowercase().replaceFirstChar { it.uppercase() },
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
        color = textColor,
        modifier = modifier
            .background(bgColor, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    )
}
