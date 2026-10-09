package com.dailydeeds.reminder.model

/** F: preface and title pages, S: commentary on a Quran passage, I: an index or table-of-contents block. */
enum class MizanKind { FRONT, SECTION, INDEX }

/** Where one entry of the complete Tafsir al-Mizan sits in the bundled file (see data/MizanData). */
data class MizanHeader(
    val id: Int,
    val kind: MizanKind,
    val surah: Int,
    val from: Int,
    val to: Int,
    val title: String,
    val offset: Long,
    val length: Int
)

/** tag is P (paragraph), H1..H4 (heading), or G (page marker, text "volume:page"). */
data class MizanParagraph(val tag: String, val text: String) {
    val isPage: Boolean get() = tag == "G"
    val headingLevel: Int get() = if (tag.length == 2 && tag[0] == 'H') tag[1] - '0' else 0
}

data class MizanEntry(val header: MizanHeader, val paragraphs: List<MizanParagraph>)

data class MizanHit(val entryId: Int, val paragraphIndex: Int, val snippet: String)
