package com.dailydeeds.reminder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dailydeeds.reminder.model.Deed

@Composable
internal fun ReaderTextCard(
    deed: Deed,
    activeContent: String,
    readerFontSize: Float
) {
    // Ornate Islamic Frame Card
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(22.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Ornament
            Text(
                text = deed.headerOrnament ?: "۞ بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ ۞",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.secondary
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Decorative Divider
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.35f))
                )
                Text(
                    text = " ✤ ✦ ✤ ",
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.35f))
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Sacred Text Presentation
            Text(
                text = activeContent,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = readerFontSize.sp,
                    lineHeight = (readerFontSize * 1.9f).sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Closing Divider
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(2.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f))
            )
        }
    }
}
