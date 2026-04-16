package com.smartbudget.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smartbudget.data.Category
import com.smartbudget.data.Expense
import com.smartbudget.viewmodel.ExpenseListViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ExpensesScreen(viewModel: ExpenseListViewModel) {
    val expenses by viewModel.expenses.collectAsState()
    val totalAmount by viewModel.totalAmount.collectAsState()
    val categories by viewModel.activeCategories.collectAsState()
    val currentMonth by viewModel.currentMonth.collectAsState()
    val currentYear by viewModel.currentYear.collectAsState()
    val currencyCode by viewModel.preferencesManager.currency.collectAsState()

    val monthNames = listOf("Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre")

    var expandedCategories by remember { mutableStateOf(setOf<Long>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Mois courant + boutons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.previousMonth() }) {
                Icon(
                    Icons.Rounded.KeyboardArrowLeft, 
                    contentDescription = "Mois précédent",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = "${monthNames[currentMonth]} $currentYear",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            IconButton(onClick = { viewModel.nextMonth() }) {
                Icon(
                    Icons.Rounded.KeyboardArrowRight, 
                    contentDescription = "Mois suivant",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Total du mois
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Total des dépenses",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                )
                Spacer(modifier = Modifier.height(12.dp))
                val formatter = NumberFormat.getCurrencyInstance()
                try {
                    formatter.currency = Currency.getInstance(currencyCode)
                } catch(e: Exception) {
                    formatter.currency = Currency.getInstance("MAD")
                }
                Text(
                    text = formatter.format(totalAmount ?: 0.0),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (categories.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aucune catégorie. Créez-en une dans les Paramètres.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(categories, key = { it.id }) { category ->
                    val categoryExpenses = expenses.filter { it.categoryId == category.id }
                    val categoryTotal = categoryExpenses.sumOf { it.amount }
                    val isExpanded = expandedCategories.contains(category.id)

                    CategoryAccordionItem(
                        category = category,
                        categoryTotal = categoryTotal,
                        categoryExpenses = categoryExpenses,
                        isExpanded = isExpanded,
                        currencyCode = currencyCode,
                        onDeleteExpense = { viewModel.deleteExpense(it) },
                        onToggle = {
                            expandedCategories = if (isExpanded) {
                                expandedCategories - category.id
                            } else {
                                expandedCategories + category.id
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryAccordionItem(
    category: Category,
    categoryTotal: Double,
    categoryExpenses: List<Expense>,
    isExpanded: Boolean,
    currencyCode: String,
    onDeleteExpense: (Expense) -> Unit,
    onToggle: () -> Unit
) {
    val formatter = NumberFormat.getCurrencyInstance()
    try {
        formatter.currency = Currency.getInstance(currencyCode)
    } catch(e: Exception) {
        formatter.currency = Currency.getInstance("MAD")
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggle)
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(android.graphics.Color.parseColor(category.color)).copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = category.icon, style = MaterialTheme.typography.titleLarge)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${categoryExpenses.size} dépense(s)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = formatter.format(categoryTotal),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = if (isExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 12.dp)
                ) {
                    if (categoryExpenses.isEmpty()) {
                        Text(
                            text = "Aucune dépense pour ce mois.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    } else {
                        categoryExpenses.forEach { expense ->
                            ExpenseSubItem(expense = expense, currencyCode = currencyCode, onDelete = { onDeleteExpense(expense) })
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseSubItem(expense: Expense, currencyCode: String, onDelete: () -> Unit) {
    val formatter = NumberFormat.getCurrencyInstance()
    try {
        formatter.currency = Currency.getInstance(currencyCode)
    } catch(e: Exception) {
        formatter.currency = Currency.getInstance("MAD")
    }
    val dateFormat = SimpleDateFormat("dd MMM", Locale("fr", "FR"))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = dateFormat.format(Date(expense.date)),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            if (!expense.note.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = expense.note,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Text(
            text = "-${formatter.format(expense.amount)}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.width(8.dp))
        TextButton(onClick = onDelete, contentPadding = PaddingValues(0.dp)) {
            Text("Supprimer", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
        }
    }
}
