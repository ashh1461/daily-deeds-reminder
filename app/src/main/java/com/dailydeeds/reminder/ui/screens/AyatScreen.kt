package com.dailydeeds.reminder.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.viewmodel.ToolsViewModel
import com.dailydeeds.reminder.viewmodel.WorshipViewModel
import com.dailydeeds.reminder.worship.Eclipse
import com.dailydeeds.reminder.worship.EclipseKind
import com.dailydeeds.reminder.worship.Eclipses
import com.dailydeeds.reminder.worship.Visibility
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val GREGORIAN_MONTHS = listOf(
    "يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو", "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"
)
private val CLOCK = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)

/** Which events call for the prayer of signs, and how it is performed. A summary: the marja's book decides. */
private val GUIDE: List<Pair<String, List<String>>> = listOf(
    "متى تجب؟" to listOf(
        "عند كسوف الشمس وخسوف القمر ولو كان جزئياً، وعند الزلزلة.",
        "وكذلك عند الرعد والبرق والريح السوداء أو الحمراء ونحوها من المخاوف السماوية إذا خاف منها أكثر الناس.",
        "تجب على من حدثت الآية في بلده وظهرت له، لا على أهل البلدان الأخرى."
    ),
    "وقتها" to listOf(
        "الكسوف والخسوف: من بدء الظاهرة إلى بدء الانجلاء على المشهور، ويوسّع بعض المراجع الوقت إلى تمام الانجلاء.",
        "الزلزلة والرعد ونحوها: تُؤدّى فور حدوثها."
    ),
    "كيفيتها" to listOf(
        "ركعتان، في كل ركعة خمسة ركوعات، بلا أذان ولا إقامة.",
        "النية ثم تكبيرة الإحرام.",
        "الطريقة الأولى: في كل ركعة تقرأ الفاتحة وسورة كاملة ثم تركع وترفع رأسك، وتكرر ذلك خمس مرات، ثم تسجد سجدتين.",
        "الطريقة الثانية: تقرأ الفاتحة مرة واحدة في الركعة وتقسّم سورة إلى خمسة أجزاء؛ تقرأ جزءاً ثم تركع، وتقوم فتقرأ الجزء التالي دون فاتحة، وهكذا حتى تُتمّ السورة في الركوع الخامس. ويجوز المزج بين الطريقتين.",
        "تقوم للركعة الثانية فتفعل مثل الأولى، ثم تتشهد وتسلّم."
    ),
    "المستحبات" to listOf(
        "القنوت قبل الركوع الثاني والرابع والسادس والثامن والعاشر.",
        "التكبير عند الهوي إلى كل ركوع وعند الرفع منه، ويقول بعد رفع الرأس من الركوع الخامس والعاشر: «سمع الله لمن حمده».",
        "إطالة الصلاة بقدر مدة الآية، وأداؤها جماعة، وقول «الصلاة» ثلاثاً بدل الأذان في الجماعة."
    )
)

/** Salat al-Ayat: how to pray it, and the next eclipses with their visibility from the chosen city. */
@Composable
fun AyatScreen(tools: ToolsViewModel, worship: WorshipViewModel, onNavigateBack: () -> Unit) {
    val place by tools.place.collectAsState()
    val reminder by worship.eclipseReminder.collectAsState()
    var tab by rememberSaveable { mutableIntStateOf(0) }
    val upcoming = remember { Eclipses.upcoming(Instant.now(), 12) }

    ToolScaffold("صلاة الآيات", onNavigateBack) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ChoiceRow(listOf(0, 1), tab, { if (it == 0) "كيف تُصلّى" else "الخسوف والكسوف القادم" }) { tab = it }

            if (tab == 0) {
                GUIDE.forEach { (title, lines) ->
                    WorshipCard {
                        WorshipHeading(title)
                        lines.forEach { Text("• $it", style = MaterialTheme.typography.bodyMedium) }
                    }
                }
                WorshipNote("هذه خلاصة مأخوذة من الرسائل العملية المتداولة، وقد تختلف التفاصيل بين المراجع؛ فراجع رسالة مرجع تقليدك.")
            } else {
                WorshipCard {
                    SwitchLine("تنبيه صباح يوم الخسوف المرئي في مدينتي", reminder) { worship.setEclipseReminder(it) }
                    WorshipNote(
                        "المدينة: ${place.name}. يصلك تنبيه الساعة 8 صباحاً إذا كان خسوف قمري مرئي من مدينتك " +
                            "خلال الـ 24 ساعة التالية (يُحسب ارتفاع القمر في مدينتك). الكسوف الشمسي غير مشمول بالتنبيه."
                    )
                }
                if (upcoming.isEmpty()) {
                    WorshipNote("انتهى نطاق الجدول المدمج (حتى سنة ${Eclipses.LAST_YEAR}).")
                }
                upcoming.forEach { EclipseCard(it, place.zone, place) }
                WorshipNote(
                    "المصدر: كتالوج ناسا لخسوف وكسوف الأرض والقمر (F. Espenak, NASA/GSFC)، 2021–${Eclipses.LAST_YEAR}. " +
                        "رؤية الكسوف الشمسي تتطلب حساباً محلياً غير مدمج، فتحقق من المناطق المذكورة أو من مصدر موثوق."
                )
            }
        }
    }
}

@Composable
private fun EclipseCard(e: Eclipse, zone: ZoneId, place: com.dailydeeds.reminder.model.Place) {
    val local = e.greatest.atZone(zone)
    val visibility = Eclipses.visibility(e, place)
    WorshipCard {
        val kind = if (e.kind == EclipseKind.LUNAR) "خسوف القمر" else "كسوف الشمس"
        Text("$kind ${e.type.labelArabic}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(
            "${local.dayOfMonth} ${GREGORIAN_MONTHS[local.monthValue - 1]} ${local.year} — ذروة الحدث ${local.format(CLOCK)} بتوقيت ${place.name}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text("المناطق: ${e.regionArabic}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (e.kind == EclipseKind.LUNAR && e.umbralMinutes > 0) {
            Text("مدة الطور الظلّي: ${e.umbralMinutes / 60} ساعة و${e.umbralMinutes % 60} دقيقة", style = MaterialTheme.typography.bodySmall)
        }
        Text(
            visibility.labelArabic,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = if (visibility == Visibility.VISIBLE) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (!e.needsPrayer) {
            WorshipNote("الخسوف شبه الظلّي لا يُرى بالعين غالباً ولا تُعدّ له صلاة الآيات عادةً.")
        } else {
            WorshipNote("تجب صلاة الآيات على من يرى ${if (e.kind == EclipseKind.LUNAR) "الخسوف" else "الكسوف"} في بلده.")
        }
    }
}
