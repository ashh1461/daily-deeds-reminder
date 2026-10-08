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
import com.dailydeeds.reminder.worship.KhumsInput
import com.dailydeeds.reminder.worship.KhumsYear
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Locale

private val GREGORIAN_MONTHS = listOf(
    "يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو", "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"
)

private fun money(value: BigDecimal): String =
    NumberFormat.getInstance(Locale.ENGLISH).apply { minimumFractionDigits = 2; maximumFractionDigits = 2 }.format(value)

/** Khums on the annual surplus (20 %), the date of the khums year, and an optional reminder. */
@Composable
fun KhumsScreen(tools: ToolsViewModel, worship: WorshipViewModel, onNavigateBack: () -> Unit) {
    val offset by tools.hijriOffset.collectAsState()
    val year by worship.khumsYear.collectAsState()
    val reminder by worship.khumsReminder.collectAsState()
    val today = remember { LocalDate.now() }

    var gains by rememberSaveable { mutableStateOf("") }
    var expenses by rememberSaveable { mutableStateOf("") }
    var deductions by rememberSaveable { mutableStateOf("") }
    var paid by rememberSaveable { mutableStateOf("") }

    fun amount(text: String): BigDecimal? = if (text.isBlank()) BigDecimal.ZERO else Khums.parseAmount(text)
    val parsed = listOf(gains, expenses, deductions, paid).map(::amount)
    val result = if (parsed.all { it != null }) {
        Khums.calculate(KhumsInput(parsed[0]!!, parsed[1]!!, parsed[2]!!, parsed[3]!!))
    } else null

    ToolScaffold("حاسبة الخمس", onNavigateBack) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            WorshipCard {
                WorshipHeading("حساب فاضل السنة")
                AmountField("إجمالي الأرباح والمكاسب خلال السنة", gains) { gains = it }
                AmountField("مصاريف المعيشة (المؤونة) خلال السنة", expenses) { expenses = it }
                AmountField("ديون وما يُستثنى منها", deductions) { deductions = it }
                AmountField("خمس دفعتَه سابقاً عن هذه السنة", paid) { paid = it }
                if (result == null) {
                    Text("أدخل أرقاماً صحيحة فقط.", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                } else {
                    Text("الفاضل بعد المؤونة: ${money(result.surplus)}", style = MaterialTheme.typography.bodyLarge)
                    Text("الخمس (20%): ${money(result.khums)}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("المتبقي عليك: ${money(result.remaining)}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                    WorshipNote("يُقسَّم نصفين: سهم الإمام (عج) ${money(result.shareOfImam)} وسهم السادة ${money(result.shareOfSadat)}.")
                }
            }

            WorshipCard {
                WorshipHeading("رأس سنتك الخمسية")
                val current = year
                if (current == null) {
                    WorshipNote("حدّد التاريخ الهجري الذي تبدأ به سنتك الخمسية (تاريخ أول ربح تحصّله) لتصلك تذكرة.")
                } else {
                    val next = Khums.nextAnniversary(today, current, offset)
                    Text("${current.day} ${ShiaCalendar.MONTH_NAMES[current.month - 1]}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    if (next != null) {
                        val days = ChronoUnit.DAYS.between(today, next)
                        Text(
                            "الموعد القادم: ${next.dayOfMonth} ${GREGORIAN_MONTHS[next.monthValue - 1]} ${next.year} " +
                                if (days == 0L) "(اليوم)" else "(بعد $days يوماً)",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    SwitchLine("ذكّرني قبل أسبوع وفي اليوم نفسه", reminder) { worship.setKhumsReminder(it) }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(onClick = {
                        val h = ShiaCalendar.toHijri(today, offset)
                        worship.setKhumsYear(KhumsYear(h.month, h.day))
                    }, modifier = Modifier.weight(1f)) { Text("اجعل اليوم رأس السنة") }
                    if (current != null) TextButton(onClick = { worship.setKhumsYear(null); worship.setKhumsReminder(false) }) { Text("إزالة") }
                }
                if (current != null) {
                    WorshipNote("شهر رأس السنة")
                    ChoiceRow((1..12).toList(), current.month, { ShiaCalendar.MONTH_NAMES[it - 1] }) {
                        worship.setKhumsYear(KhumsYear(it, current.day))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(onClick = { if (current.day > 1) worship.setKhumsYear(KhumsYear(current.month, current.day - 1)) }) { Text("−") }
                        Text("اليوم ${current.day}", style = MaterialTheme.typography.titleMedium)
                        OutlinedButton(onClick = { if (current.day < 30) worship.setKhumsYear(KhumsYear(current.month, current.day + 1)) }) { Text("+") }
                    }
                }
            }

            WorshipCard {
                WorshipHeading("معلومات")
                listOf(
                    "الخمس عشرون بالمئة مما يفضل عن مؤونة السنة من أرباح الكسب والتجارة وغيرها.",
                    "تبدأ السنة من أول ربح تحصّله، ويجوز اعتماد تاريخ ثابت كل عام يُسمّى رأس السنة الخمسية.",
                    "يُقسَّم الخمس نصفين: سهم الإمام (عج) وسهم السادة، ويُدفع إلى المرجع أو وكيله المأذون."
                ).forEach { Text("• $it", style = MaterialTheme.typography.bodyMedium) }
                WorshipNote(
                    "هذه الأداة تقديرية لحساب الفاضل بالقاعدة العامة وليست فتوى. ما يُعدّ من المؤونة وكيفية احتساب الديون " +
                        "والأموال المستثمرة تختلف بين المراجع؛ فراجع مكتب مرجعك أو وكيله قبل الدفع. لا يُدفع أي مال عبر التطبيق."
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
