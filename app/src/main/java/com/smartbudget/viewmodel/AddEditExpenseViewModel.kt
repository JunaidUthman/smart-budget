package com.smartbudget.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbudget.data.Category
import com.smartbudget.data.CategoryRepository
import com.smartbudget.data.Expense
import com.smartbudget.data.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    val activeCategories: StateFlow<List<Category>> = categoryRepository.getActiveCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveExpense(
        amount: Double,
        categoryId: Long,
        dateTimestamp: Long,
        note: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val expense = Expense(
                amount = amount,
                categoryId = categoryId,
                date = dateTimestamp,
                note = note
            )
            expenseRepository.insertExpense(expense)
            onSuccess()
        }
    }
}
