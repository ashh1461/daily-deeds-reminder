package com.dailydeeds.reminder.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.ui.theme.EmeraldDeep
import com.dailydeeds.reminder.ui.theme.EmeraldPrimary
import com.dailydeeds.reminder.ui.theme.GoldBright
import com.dailydeeds.reminder.ui.theme.GoldMain
import com.dailydeeds.reminder.ui.theme.ReadingFamily

/** Eight-pointed star (two overlapping squares) drawn as an outline. */
private fun DrawScope.khatam(center: Offset, radius: Float, color: Color, strokeWidth: Float) {
    val stroke = Stroke(width = strokeWidth)
    val side = radius * 1.4142f
    val topLeft = Offset(center.x - side / 2f, center.y - side / 2f)
    drawRect(color = color, topLeft = topLeft, size = Size(side, side), style = stroke)
    rotate(degrees = 45f, pivot = center) {
        drawRect(color = color, topLeft = topLeft, size = Size(side, side), style = stroke)
    }
}

/** Repeating khatam lattice, drawn behind a hero or header. */
@Composable
fun IslamicPattern(modifier: Modifier = Modifier, color: Color = GoldBright.copy(alpha = 0.16f), cell: Dp = 44.dp) {
    Canvas(modifier = modifier) {
        val step = cell.toPx()
        val radius = step * 0.42f
        var y = 0f
        var row = 0
        while (y < size.height + step) {
            var x = if (row % 2 == 0) 0f else step / 2f
            while (x < size.width + step) {
                khatam(Offset(x, y), radius, color, 1.2f)
                x += step
            }
            y += step / 2f
            row++
        }
    }
}

/** Emerald banner with the khatam lattice, used for the Home hero and surah headers. */
@Composable
fun EmeraldBanner(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(26.dp))
            .background(Brush.linearGradient(listOf(EmeraldDeep, EmeraldPrimary)))
            .border(BorderStrokeGold, RoundedCornerShape(26.dp))
    ) {
        IslamicPattern(Modifier.matchParentSize())
        Box(Modifier.padding(20.dp)) { content() }
    }
}

private val BorderStrokeGold = BorderStroke(1.dp, GoldMain.copy(alpha = 0.55f))

/** Thin line – diamond – thin line separator. */
@Composable
fun OrnamentDivider(modifier: Modifier = Modifier, color: Color = MaterialTheme.colorScheme.secondary) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(Modifier.weight(1f).height(1.dp).background(color.copy(alpha = 0.45f)))
        Canvas(Modifier.padding(horizontal = 10.dp).size(14.dp)) {
            val c = Offset(size.width / 2f, size.height / 2f)
            val r = size.minDimension / 2f
            val path = Path().apply {
                moveTo(c.x, c.y - r); lineTo(c.x + r, c.y); lineTo(c.x, c.y + r); lineTo(c.x - r, c.y); close()
            }
            drawPath(path, color)
        }
        Box(Modifier.weight(1f).height(1.dp).background(color.copy(alpha = 0.45f)))
    }
}

/** Section heading in the reading typeface with an ornament underneath. */
@Composable
fun OrnateTitle(text: String, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium,
            fontFamily = ReadingFamily,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        OrnamentDivider(Modifier.padding(top = 6.dp, start = 40.dp, end = 40.dp))
    }
}

/** Border colour for cards: a hairline of antique gold in light mode, deep green in dark mode. */
@Composable
fun cardBorder(): BorderStroke = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
