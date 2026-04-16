package com.smartbudget.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monthly_budgets")
data class MonthlyBudget(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val month: String, // format YYYY-MM
    val categoryId: Long,
    val limitAmount: Double
)
