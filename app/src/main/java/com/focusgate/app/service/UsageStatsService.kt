package com.focusgate.app.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class UsageStatsService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null
}
