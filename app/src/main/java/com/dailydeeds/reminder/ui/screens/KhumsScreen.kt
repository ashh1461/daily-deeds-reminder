package com.dailydeeds.reminder.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.calendar.ShiaCalendar
import com.dailydeeds.reminder.viewmodel.ToolsViewModel
import com.dailydeeds.reminder.viewmodel.WorshipViewModel
import com.dailydeeds.reminder.worship.Khums
import com.dailydeeds.reminder.worship.KhumsDraft
import com.dailydeeds.reminder.worship.KhumsInput
import com.dailydeeds.reminder.worship.KhumsYear
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Locale
import androidx.compose.ui.res.stringResource
import com.dailydeeds.reminder.R
import com.dailydeeds.reminder.ui.gregorianMonthName
import com.dailydeeds.reminder.ui.daysAwayText

private fun money(value: BigDecimal): String =
    NumberFormat.getInstance(Locale.ENGLISH).apply { minimumFractionDigits = 2; maximumFractionDigits = 2 }.format(value)

/** Khums on the annual surplus (20 %), the date of the khums year, and an optional reminder. */
@Composable
fun KhumsScreen(tools: ToolsViewModel, worship: WorshipViewModel, onNavigateBack: () -> Unit) {
    val offset by tools.hijriOffset.collectAsState()
    val year by worship.khumsYear.collectAsState()
    val reminder by worship.khumsReminder.collectAsState()
    val today = remember { LocalDate.now() }

    // The figures are kept as a draft, so leaving the screen (or the app) does not lose them.
    val saved = remember { worship.khumsDraft.value }
    var gains by rememberSaveable { mutableStateOf(saved.gains) }
    var expenses by rememberSaveable { mutableStateOf(saved.expenses) }
    var deductions by rememberSaveable { mutableStateOf(saved.deductions) }
    var paid by rememberSaveable { mutableStateOf(saved.paid) }
    fun saveDraft() = worship.setKhumsDraft(KhumsDraft(gains, expenses, deductions, paid))

    fun amount(text: String): BigDecimal? = if (text.isBlank()) BigDecimal.ZERO else Khums.parseAmount(text)
    val parsed = listOf(gains, expenses, deductions, paid).map(::amount)
    val result = if (parsed.all { it != null }) {
        Khums.calculate(KhumsInput(parsed[0]!!, parsed[1]!!, parsed[2]!!, parsed[3]!!))
    } else null

    ToolScaffold(stringResource(R.string.khums_title), onNavigateBack) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            WorshipCard {
                WorshipHeading(stringResource(R.string.khums_heading_surplus))
                AmountField(stringResource(R.string.khums_gains), gains) { gains = it; saveDraft() }
                AmountField(stringResource(R.string.khums_expenses), expenses) { expenses = it; saveDraft() }
                AmountField(stringResource(R.string.khums_deductions), deductions) { deductions = it; saveDraft() }
                AmountField(stringResource(R.string.khums_paid), paid) { paid = it; saveDraft() }
                if (listOf(gains, expenses, deductions, paid).any { it.isNotBlank() }) {
                    TextButton(onClick = { gains = ""; expenses = ""; deductions = ""; paid = ""; saveDraft() }) {
                        Text(stringResource(R.string.khums_clear))
                    }
                }
                if (result == null) {
                    Text(stringResource(R.string.khums_invalid), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                } else {
                    Text(stringResource(R.string.khums_surplus, money(result.surplus)), style = MaterialTheme.typography.bodyLarge)
                    Text(stringResource(R.string.khums_amount, money(result.khums)), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(stringResource(R.string.khums_remaining, money(result.remaining)), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                    WorshipNote(stringResource(R.string.khums_split, money(result.shareOfImam), money(result.shareOfSadat)))
                }
            }

            WorshipCard {
                WorshipHeading(stringResource(R.string.khums_heading_year))
                val current = year
                if (current == null) {
                    WorshipNote(stringResource(R.string.khums_year_hint))
                } else {
                    val next = Khums.nextAnniversary(today, current, offset)
                    Text("${current.day} ${ShiaCalendar.MONTH_NAMES[current.month - 1]}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    if (next != null) {
                        val days = ChronoUnit.DAYS.between(today, next)
                        Text(
                            stringResource(
                                R.string.khums_next,
                                "${next.dayOfMonth} ${gregorianMonthName(next.monthValue)} ${next.year}", daysAwayText(days)
                            ),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    SwitchLine(stringResource(R.string.khums_remind), reminder) { worship.setKhumsReminder(it) }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(onClick = {
                        val h = ShiaCalendar.toHijri(today, offset)
                        worship.setKhumsYear(KhumsYear(h.month, h.day))
                    }, modifier = Modifier.weight(1f)) { Text(stringResource(R.string.khums_set_today)) }
                    if (current != null) TextButton(onClick = { worship.setKhumsYear(null); worship.setKhumsReminder(false) }) { Text(stringResource(R.string.common_remove)) }
                }
                if (current != null) {
                    WorshipNote(stringResource(R.string.khums_year_month))
                    ChoiceRow((1..12).toList(), current.month, { ShiaCalendar.MONTH_NAMES[it - 1] }) {
                        worship.setKhumsYear(KhumsYear(it, current.day))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(onClick = { if (current.day > 1) worship.setKhumsYear(KhumsYear(current.month, current.day - 1)) }) { Text("−") }
                        Text(stringResource(R.string.khums_day, current.day.toString()), style = MaterialTheme.typography.titleMedium)
                        OutlinedButton(onClick = { if (current.day < 30) worship.setKhumsYear(KhumsYear(current.month, current.day + 1)) }) { Text("+") }
                    }
                }
            }

            WorshipCard {
                WorshipHeading(stringResource(R.string.khums_heading_info))
                listOf(
                    stringResource(R.string.khums_info_1),
                    stringResource(R.string.khums_info_2),
                    stringResource(R.string.khums_info_3)
                ).forEach { Text("• $it", style = MaterialTheme.typography.bodyMedium) }
                WorshipNote(
                    stringResource(R.string.khums_disclaimer)
                )
            }
        }
    }
}

@Composable
private fun AmountField(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value, onValueChange = { onChange(it.take(26)) },
        label = { Text(label) }, singleLine = true, modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
    )
}
