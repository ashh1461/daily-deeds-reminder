package com.dailydeeds.reminder.worship

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import androidx.core.content.res.ResourcesCompat
import com.dailydeeds.reminder.R
import java.io.OutputStream
import java.time.format.TextStyle
import java.util.Locale

/** One-page PDF of a monthly timetable (Imsak and Iftar), drawn with the app's Tajawal font. */
object TimetablePdf {
    private const val PAGE_WIDTH = 595
    private const val PAGE_HEIGHT = 842
    private const val MARGIN = 28f
    private val HEADERS = listOf("اليوم", "التاريخ", "الإمساك", "الفجر", "الشروق", "الظهر", "المغرب", "العشاء")

    fun write(context: Context, rows: List<TimetableRow>, title: String, subtitle: String, out: OutputStream) {
        val regular = ResourcesCompat.getFont(context, R.font.tajawal_regular) ?: Typeface.DEFAULT
        val bold = ResourcesCompat.getFont(context, R.font.tajawal_bold) ?: Typeface.DEFAULT_BOLD
        val titlePaint = paint(bold, 20f, Paint.Align.CENTER)
        val subPaint = paint(regular, 11f, Paint.Align.CENTER, Color.DKGRAY)
        val headPaint = paint(bold, 10.5f, Paint.Align.CENTER, Color.WHITE)
        val cellPaint = paint(regular, 10.5f, Paint.Align.CENTER)
        val boldCell = paint(bold, 10.5f, Paint.Align.CENTER)
        val fillHead = Paint().apply { color = Color.rgb(6, 63, 52) }
        val fillQadr = Paint().apply { color = Color.rgb(246, 232, 190) }
        val fillBand = Paint().apply { color = Color.rgb(240, 246, 243) }

        val doc = PdfDocument()
        try {
            val page = doc.startPage(PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create())
            val c = page.canvas
            c.drawText(title, PAGE_WIDTH / 2f, 56f, titlePaint)
            c.drawText(subtitle, PAGE_WIDTH / 2f, 76f, subPaint)

            val tableWidth = PAGE_WIDTH - 2 * MARGIN
            val colWidth = tableWidth / HEADERS.size
            val rowHeight = 21f
            var y = 100f
            // Right-to-left: the first column sits at the right edge.
            fun centerX(col: Int) = PAGE_WIDTH - MARGIN - colWidth * (col + 0.5f)

            c.drawRect(MARGIN, y, PAGE_WIDTH - MARGIN, y + rowHeight, fillHead)
            HEADERS.forEachIndexed { i, h -> c.drawText(h, centerX(i), y + 14.5f, headPaint) }
            y += rowHeight

            rows.take(31).forEachIndexed { index, row ->
                if (row.qadrNight != null) c.drawRect(MARGIN, y, PAGE_WIDTH - MARGIN, y + rowHeight, fillQadr)
                else if (index % 2 == 1) c.drawRect(MARGIN, y, PAGE_WIDTH - MARGIN, y + rowHeight, fillBand)
                val t = row.times
                val weekday = row.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("ar"))
                val cells = listOf(
                    "${row.hijri.day} $weekday",
                    "${row.date.dayOfMonth}/${row.date.monthValue}",
                    clock(t.imsak), clock(t.fajr), clock(t.sunrise), clock(t.dhuhr), clock(t.maghrib), clock(t.isha)
                )
                cells.forEachIndexed { i, text -> c.drawText(text, centerX(i), y + 14.5f, if (i == 0 || i == 2 || i == 6) boldCell else cellPaint) }
                y += rowHeight
            }

            y += 18f
            val note = paint(regular, 9.5f, Paint.Align.CENTER, Color.DKGRAY)
            if (rows.any { it.qadrNight != null }) {
                c.drawText("الأسطر الملوّنة: اليوم الذي تبدأ بمغربه ليلة من ليالي القدر (19 و21 و23).", PAGE_WIDTH / 2f, y, note)
                y += 14f
            }
            c.drawText("المواعيد تقريبية بحسب الحساب الفلكي؛ احتط في الإمساك وأفطر عند تحقق المغرب. تطبيق الأعمال اليومية.", PAGE_WIDTH / 2f, y, note)
            doc.finishPage(page)
            doc.writeTo(out)
        } finally {
            doc.close()
        }
    }

    private fun clock(t: java.time.LocalTime?): String = t?.let { "%02d:%02d".format(Locale.ENGLISH, it.hour, it.minute) } ?: "--:--"

    private fun paint(face: Typeface, size: Float, align: Paint.Align, color: Int = Color.BLACK) = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        typeface = face
        textSize = size
        textAlign = align
        this.color = color
    }
}
