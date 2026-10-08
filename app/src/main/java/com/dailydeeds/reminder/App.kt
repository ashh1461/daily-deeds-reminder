package com.dailydeeds.reminder

import android.app.Application
import com.dailydeeds.reminder.notification.NotificationHelper

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // Channels are created once, here, instead of on every notification.
        NotificationHelper.createChannels(this)
    }
}
