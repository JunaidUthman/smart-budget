package com.smartbudget.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories",
    indices = [Index(value = ["name"], unique = true)]
)
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val icon: String, // String representation of an emoji or icon name
    val color: String, // Hex color string, e.g., "#10B981"
    val isActive: Boolean = true
)
