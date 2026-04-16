package com.smartbudget.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbudget.data.CategoryTotal
import com.smartbudget.data.ExpenseRepository
import com.smartbudget.data.PreferencesManager
import com.smartbudget.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    val preferencesManager: PreferencesManager
) : ViewModel() {

    private val calendar = Calendar.getInstance()
    private val _currentYear = MutableStateFlow(calendar.get(Calendar.YEAR))
    private val _currentMonth = MutableStateFlow(calendar.get(Calendar.MONTH)) // 0-based

    val currentYear: StateFlow<Int> = _currentYear
    val currentMonth: StateFlow<Int> = _currentMonth

    @OptIn(ExperimentalCoroutinesApi::class)
    val categoryTotals: StateFlow<List<CategoryTotal>> = _currentYear.flatMapLatest { year ->
        _currentMonth.flatMapLatest { month ->
            val (start, end) = DateUtils.getMonthStartEndTimestamps(year, month)
            expenseRepository.getCategoryTotalsBetweenDates(start, end)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val totalAmount: StateFlow<Double?> = _currentYear.flatMapLatest { year ->
        _currentMonth.flatMapLatest { month ->
            val (start, end) = DateUtils.getMonthStartEndTimestamps(year, month)
            expenseRepository.getTotalAmountBetweenDates(start, end)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    fun previousMonth() {
        if (_currentMonth.value == 0) {
            _currentMonth.value = 11
            _currentYear.value -= 1
        } else {
            _currentMonth.value -= 1
        }
    }

    fun nextMonth() {
        if (_currentMonth.value == 11) {
            _currentMonth.value = 0
            _currentYear.value += 1
        } else {
            _currentMonth.value += 1
        }
    }
}
