package com.smartbudget.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao
) {
    fun getAllExpenses(): Flow<List<Expense>> {
        return expenseDao.getAllExpenses()
    }

    fun getExpensesBetweenDates(startDate: Long, endDate: Long): Flow<List<Expense>> {
        return expenseDao.getExpensesBetweenDates(startDate, endDate)
    }

    fun getTotalAmountBetweenDates(startDate: Long, endDate: Long): Flow<Double?> {
        return expenseDao.getTotalAmountBetweenDates(startDate, endDate)
    }

    fun getCategoryTotalsBetweenDates(startDate: Long, endDate: Long): Flow<List<CategoryTotal>> {
        return expenseDao.getCategoryTotalsBetweenDates(startDate, endDate)
    }

    suspend fun insertExpense(expense: Expense): Long {
        return expenseDao.insertExpense(expense)
    }

    suspend fun insertExpenses(expenses: List<Expense>) {
        expenseDao.insertExpenses(expenses)
    }

    suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpense(expense)
    }

    suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense)
    }
}
