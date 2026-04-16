package com.smartbudget.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

data class CategoryTotal(
    val categoryId: Long,
    val totalAmount: Double,
    val categoryName: String,
    val categoryColor: String
)

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getExpensesBetweenDates(startDate: Long, endDate: Long): Flow<List<Expense>>

    @Query("SELECT SUM(amount) FROM expenses WHERE date >= :startDate AND date <= :endDate")
    fun getTotalAmountBetweenDates(startDate: Long, endDate: Long): Flow<Double?>

    @Query("""
        SELECT e.categoryId, SUM(e.amount) as totalAmount, c.name as categoryName, c.color as categoryColor 
        FROM expenses e 
        INNER JOIN categories c ON e.categoryId = c.id 
        WHERE e.date >= :startDate AND e.date <= :endDate 
        GROUP BY e.categoryId 
        ORDER BY totalAmount DESC
    """)
    fun getCategoryTotalsBetweenDates(startDate: Long, endDate: Long): Flow<List<CategoryTotal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenses(expenses: List<Expense>)

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)
}
