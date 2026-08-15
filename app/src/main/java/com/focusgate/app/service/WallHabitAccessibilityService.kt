package com.focusgate.app.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class WallHabitAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Will be implemented in Task 4
    }

    override fun onInterrupt() {
        // No-op
    }
}
