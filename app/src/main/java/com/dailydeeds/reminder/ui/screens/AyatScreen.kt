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
import androidx.compose.ui.res.stringResource
import com.dailydeeds.reminder.R
import com.dailydeeds.reminder.ui.gregorianMonthName
import com.dailydeeds.reminder.ui.labelRes
import com.dailydeeds.reminder.ui.theme.RtlContent

private val CLOCK = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)

/**
 * Which events call for the prayer of signs, and how it is performed. A summary: the marja's book decides. This is
 * fiqh guidance, kept in its original Arabic in every UI language (v1.9.0) and shown right to left.
 */
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

    ToolScaffold(stringResource(R.string.ayat_title), onNavigateBack) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val tabLabels = listOf(stringResource(R.string.ayat_tab_how), stringResource(R.string.ayat_tab_upcoming))
            ChoiceRow(listOf(0, 1), tab, { tabLabels[it] }) { tab = it }

            if (tab == 0) {
                RtlContent {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        GUIDE.forEach { (title, lines) ->
                            WorshipCard {
                                WorshipHeading(title)
                                lines.forEach { Text("• $it", style = MaterialTheme.typography.bodyMedium) }
                            }
                        }
                    }
                }
                WorshipNote(stringResource(R.string.ayat_summary_note))
            } else {
                WorshipCard {
                    SwitchLine(stringResource(R.string.ayat_reminder), reminder) { worship.setEclipseReminder(it) }
                    WorshipNote(
                        stringResource(R.string.ayat_reminder_note, place.name)
                    )
                }
                if (upcoming.isEmpty()) {
                    WorshipNote(stringResource(R.string.ayat_table_ended, Eclipses.LAST_YEAR.toString()))
                }
                upcoming.forEach { EclipseCard(it, place.zone, place) }
                WorshipNote(
                    stringResource(R.string.ayat_source, Eclipses.LAST_YEAR.toString())
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
        val kind = stringResource(if (e.kind == EclipseKind.LUNAR) R.string.eclipse_lunar else R.string.eclipse_solar)
        Text(stringResource(R.string.eclipse_title, kind, stringResource(e.type.labelRes)), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(
            stringResource(
                R.string.eclipse_when, "${local.dayOfMonth} ${gregorianMonthName(local.monthValue)} ${local.year}",
                local.format(CLOCK), place.name
            ),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(stringResource(R.string.eclipse_regions, e.regionArabic), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (e.kind == EclipseKind.LUNAR && e.umbralMinutes > 0) {
            Text(stringResource(R.string.eclipse_umbral, (e.umbralMinutes / 60).toString(), (e.umbralMinutes % 60).toString()), style = MaterialTheme.typography.bodySmall)
        }
        Text(
            stringResource(visibility.labelRes),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = if (visibility == Visibility.VISIBLE) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (!e.needsPrayer) {
            WorshipNote(stringResource(R.string.eclipse_penumbral_note))
        } else {
            WorshipNote(stringResource(if (e.kind == EclipseKind.LUNAR) R.string.eclipse_prayer_lunar else R.string.eclipse_prayer_solar))
        }
    }
}
