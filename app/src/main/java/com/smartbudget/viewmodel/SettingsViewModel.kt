package com.smartbudget.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbudget.data.Category
import com.smartbudget.data.CategoryRepository
import com.smartbudget.data.Expense
import com.smartbudget.data.ExpenseRepository
import com.smartbudget.data.PreferencesManager
import com.smartbudget.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val expenseRepository: ExpenseRepository,
    val preferencesManager: PreferencesManager
) : ViewModel() {

    val activeCategories: StateFlow<List<Category>> = categoryRepository.getActiveCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addCategory(name: String, icon: String, color: String) {
        viewModelScope.launch {
            categoryRepository.insertCategory(
                Category(name = name, icon = icon, color = color)
            )
        }
    }

    fun exportCSV(context: Context, month: Int, year: Int, onComplete: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val (start, end) = DateUtils.getMonthStartEndTimestamps(year, month)
                val expenses = expenseRepository.getExpensesBetweenDates(start, end).first()
                if (expenses.isEmpty()) {
                    onComplete("Aucune donnée pour ce mois.")
                    return@launch
                }
                val csvContent = buildString {
                    append("amount,categoryId,dateTimestamp,note\n")
                    expenses.forEach {
                        append("${it.amount},${it.categoryId},${it.date},${it.note?.replace(",", " ") ?: ""}\n")
                    }
                }
                
                val file = File(context.cacheDir, "export_budget_${month + 1}_$year.csv")
                file.writeText(csvContent)
                
                val uri = FileProvider.getUriForFile(
                    context,
                    context.applicationContext.packageName + ".provider",
                    file
                )
                
                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/csv"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(sendIntent, "Exporter CSV"))
                onComplete("Export lancé.")
            } catch(e: Exception) {
                onComplete("Erreur d'exportation: ${e.message}")
            }
        }
    }

    fun importCSV(context: Context, uri: Uri, onComplete: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val reader = inputStream?.bufferedReader()
                val expenses = mutableListOf<Expense>()
                
                reader?.useLines { lines ->
                    lines.drop(1).forEach { line -> 
                        val parts = line.split(",")
                        if(parts.size >= 3) {
                            val amt = parts[0].toDoubleOrNull()
                            val catId = parts[1].toLongOrNull()
                            val dt = parts[2].toLongOrNull()
                            if (amt != null && catId != null && dt != null) {
                                expenses.add(Expense(amount = amt, categoryId = catId, date = dt, note = parts.getOrNull(3)))
                            }
                        }
                    }
                }
                
                if (expenses.isNotEmpty()) {
                    expenseRepository.insertExpenses(expenses)
                    onComplete("${expenses.size} dépenses importées.")
                } else {
                    onComplete("Fichier CSV invalide.")
                }
            } catch(e: Exception) {
                onComplete("Erreur d'importation.")
            }
        }
    }
}
