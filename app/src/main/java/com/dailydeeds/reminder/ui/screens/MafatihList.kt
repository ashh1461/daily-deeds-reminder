package com.dailydeeds.reminder.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.model.MafatihItem
import com.dailydeeds.reminder.ui.components.cardBorder

/** A row of a grouped list: either a chapter header or a section. */
sealed interface ListRow {
    val key: String

    data class Header(val text: String) : ListRow {
        override val key get() = "h:$text"
    }

    data class Entry(val item: MafatihItem) : ListRow {
        override val key get() = "e:${item.id}"
    }
}

/**
 * Inserts a header whenever the chapter changes. Lists that belong to a single chapter (for example the
 * 15 Munajat) get no header at all, so short lists are not cluttered.
 */
fun buildRows(items: List<MafatihItem>): List<ListRow> {
    if (items.map { it.group }.distinct().size < 2) return items.map { ListRow.Entry(it) }
    val rows = ArrayList<ListRow>(items.size + 16)
    var last: String? = null
    for (item in items) {
        if (item.group != last) {
            rows += ListRow.Header(item.group)
            last = item.group
        }
        rows += ListRow.Entry(item)
    }
    return rows
}

fun LazyListScope.mafatihRows(rows: List<ListRow>, onClick: (String) -> Unit) {
    items(rows, key = { it.key }) { row ->
        when (row) {
            is ListRow.Header -> Text(
                row.text,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 2.dp, start = 4.dp)
            )
            is ListRow.Entry -> ItemRow(row.item.title) { onClick(row.item.id) }
        }
    }
}

@Composable
fun ItemRow(title: String, subtitle: String? = null, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        border = cardBorder(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                if (subtitle != null) {
                    Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Icon(Icons.Default.ChevronLeft, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun SearchField(value: String, placeholder: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(placeholder) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "بحث") },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onChange("") }) { Icon(Icons.Default.Clear, contentDescription = "مسح") }
            }
        },
        shape = MaterialTheme.shapes.medium,
        singleLine = true
    )
}
