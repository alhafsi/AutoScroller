package com.example.autoscroller

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Handler
import android.os.Looper
import android.util.Log

class MyScrollService : AccessibilityService() {
    private val handler = Handler(Looper.getMainLooper())
    private var directionDown = true
    private var currentCount = 0
    private var maxCount = 5
    private var intervalMs: Long = 5000
    private var totalDurationSec: Long = 60
    private var startTime: Long = 0
    private var running = false

    private val TAG = "MyScrollService"

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.i(TAG, "Service connected")
    }

    override fun onAccessibilityEvent(event: android.view.accessibility.AccessibilityEvent?) {}

    override fun onInterrupt() {}

    override fun onStartCommand(intent: android.content.Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val action = it.getStringExtra("action") ?: "start"
            if (action == "stop") {
                stopAuto()
                return START_NOT_STICKY
            }

            maxCount = it.getIntExtra("count", 5)
            val intervalSec = it.getDoubleExtra("interval_sec", 5.0)
            intervalMs = (intervalSec * 1000).toLong()
            totalDurationSec = it.getLongExtra("duration_sec", 60)

            startAuto()
        }
        return START_STICKY
    }

    private val scrollRunnable = object : Runnable {
        override fun run() {
            if (!running) return

            val elapsed = (System.currentTimeMillis() - startTime) / 1000
            if (elapsed >= totalDurationSec) {
                stopAuto()
                return
            }

            performScroll(directionDown)
            currentCount++

            if (currentCount >= maxCount) {
                directionDown = !directionDown
                currentCount = 0
            }

            handler.postDelayed(this, intervalMs)
        }
    }

    private fun startAuto() {
        if (running) return
        running = true
        startTime = System.currentTimeMillis()
        currentCount = 0
        directionDown = true
        handler.post(scrollRunnable)
    }

    private fun stopAuto() {
        running = false
        handler.removeCallbacksAndMessages(null)
    }

    private fun performScroll(down: Boolean) {
        try {
            val path = Path()
            if (down) {
                path.moveTo(540f, 1600f)
                path.lineTo(540f, 400f)
            } else {
                path.moveTo(540f, 400f)
                path.lineTo(540f, 1600f)
            }

            val gesture = GestureDescription.Builder()
                .addStroke(GestureDescription.StrokeDescription(path, 0, 400))
                .build()

            dispatchGesture(gesture, null, null)
        } catch (e: Exception) {
            Log.e(TAG, "performScroll error: ${e.message}")
        }
    }

    override fun onDestroy() {
        stopAuto()
        super.onDestroy()
    }
}
