package com.focusgate.app

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

class OnboardingActivity : AppCompatActivity() {

    private lateinit var container: LinearLayout
    private lateinit var btnNext: Button
    private lateinit var btnSkip: Button
    private lateinit var stepIndicator: TextView

    private var currentStep = 0
    private val totalSteps = 5

    // Permission request codes
    private val REQUEST_OVERLAY_PERMISSION = 1001
    private val REQUEST_ACCESSIBILITY_PERMISSION = 1002
    private val REQUEST_USAGE_STATS_PERMISSION = 1003
    private val REQUEST_BATTERY_OPTIMIZATION = 1004

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        container = findViewById(R.id.onboardingContainer)
        btnNext = findViewById(R.id.btnNext)
        btnSkip = findViewById(R.id.btnSkip)
        stepIndicator = findViewById(R.id.stepIndicator)

        btnNext.setOnClickListener { nextStep() }
        btnSkip.setOnClickListener { finishOnboarding() }

        showStep(currentStep)
    }

    private fun showStep(step: Int) {
        // Inflate the appropriate step layout into the container
        container.removeAllViews()

        val inflater = layoutInflater
        val view = when (step) {
            0 -> inflater.inflate(R.layout.onboarding_step_welcome, container, false)
            1 -> inflater.inflate(R.layout.onboarding_step_permissions, container, false)
            2 -> inflater.inflate(R.layout.onboarding_step_accessibility, container, false)
            3 -> inflater.inflate(R.layout.onboarding_step_usage_overlay, container, false)
            4 -> inflater.inflate(R.layout.onboarding_step_finish, container, false)
            else -> inflater.inflate(R.layout.onboarding_step_welcome, container, false)
        }

        container.addView(view)

        // Update bottom buttons and indicator
        btnSkip.visibility = if (step == totalSteps - 1) View.GONE else View.VISIBLE
        btnNext.text = if (step == totalSteps - 1) "Done" else "Next"

        stepIndicator.text = "${step + 1} / $totalSteps"

        // Set up permission request actions for specific steps
        if (step == 1) {
            // Permission step: we have buttons to request each permission
            val btnAccessibility = view.findViewById<Button>(R.id.btnRequestAccessibility)
            val btnUsage = view.findViewById<Button>(R.id.btnRequestUsageStats)
            val btnOverlay = view.findViewById<Button>(R.id.btnRequestOverlay)
            val btnBattery = view.findViewById<Button>(R.id.btnRequestBatteryOptimization)

            btnAccessibility.setOnClickListener { requestAccessibilityPermission() }
            btnUsage.setOnClickListener { requestUsageStatsPermission() }
            btnOverlay.setOnClickListener { requestOverlayPermission() }
            btnBattery.setOnClickListener { requestBatteryOptimization() }
        }

        // If we are on the final step, the "Done" button finishes onboarding.
    }

    private fun nextStep() {
        if (currentStep == totalSteps - 1) {
            finishOnboarding()
        } else {
            currentStep++
            showStep(currentStep)
        }
    }

    private fun finishOnboarding() {
        // Mark onboarding as completed
        getSharedPreferences("onboarding", MODE_PRIVATE)
            .edit()
            .putBoolean("completed", true)
            .apply()
        // Start MainActivity
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    // Permission request methods
    private fun requestAccessibilityPermission() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
    }

    private fun requestUsageStatsPermission() {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        startActivity(intent)
    }

    private fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName"))
            startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION)
        }
    }

    private fun requestBatteryOptimization() {
        val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
        startActivity(intent)
    }

    // Optionally override onActivityResult to check if permissions were granted,
    // but we'll rely on the user to come back and proceed.

    override fun onResume() {
        super.onResume()
        // If we are on the permission step, we can update the UI to show granted status
        // For simplicity, we'll just check and show a toast or update text.
        // We'll implement a simple check in the permission step's view.
        val view = container.getChildAt(0)
        if (view != null && currentStep == 1) {
            // Update status indicators if needed
            updatePermissionStatus(view)
        }
    }

    private fun updatePermissionStatus(view: View) {
        // We'll just set text on some TextViews to show granted/not granted.
        // This is optional; we'll keep it simple.
    }
}
