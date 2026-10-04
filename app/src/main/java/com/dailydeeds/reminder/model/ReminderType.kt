package com.dailydeeds.reminder.model

import java.time.DayOfWeek

enum class ReminderType(
    val id: Int,
    val preferenceKey: String,
    val title: String,
    val message: String,
    val defaultHour: Int,
    val category: DeedCategory,
    val dayOfWeek: DayOfWeek? = null
) {
    MORNING(1, "morning", "الأوراد الصباحية", "حان وقت الأوراد الصباحية.", 7, DeedCategory.MORNING_EVENING),
    EVENING(2, "evening", "الأوراد المسائية", "حان وقت الأوراد المسائية.", 20, DeedCategory.MORNING_EVENING),
    NIGHT(3, "night", "دعاء الليل", "دعاء الليل: اللهم يا مغير الأحوال غير حالي إلى أحسن حال.", 21, DeedCategory.NIGHT),
    BEDTIME(4, "bedtime", "القراءة قبل النوم", "قبل النوم: اقرأ «ولكم فيها جمال حين تريحون وحين تسرحون» ثلاث مرات.", 22, DeedCategory.BEDTIME),
    THURSDAY(5, "thursday", "قراءات صباح الخميس", "عند الخروج لطلب الحاجة: آخر آل عمران، آية الكرسي، القدر، والفاتحة.", 7, DeedCategory.THURSDAY, DayOfWeek.THURSDAY);

    companion object {
        fun fromId(id: Int): ReminderType? = values().find { it.id == id }
    }
}

data class ReminderSettings(val enabled: Boolean, val hour: Int, val minute: Int)
