package com.example.autoscroller

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etScrollCount = findViewById<EditText>(R.id.etScrollCount)
        val etInterval = findViewById<EditText>(R.id.etInterval)
        val etDuration = findViewById<EditText>(R.id.etDuration)
        val btnStart = findViewById<Button>(R.id.btnStart)
        val btnStop = findViewById<Button>(R.id.btnStop)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)

        btnStart.setOnClickListener {
            // Check Accessibility enabled
            if (!isAccessibilityServiceEnabled()) {
                AlertDialog.Builder(this)
                    .setTitle("Permission required")
                    .setMessage("Please enable Auto Scroller in Accessibility settings to allow scrolling other apps.")
                    .setPositiveButton("Open settings") { _, _ ->
                        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
                return@setOnClickListener
            }

            val scrollCount = etScrollCount.text.toString().toIntOrNull() ?: 5
            val interval = (etInterval.text.toString().toDoubleOrNull() ?: 5.0)
            val duration = etDuration.text.toString().toIntOrNull() ?: 60

            val intent = Intent(this, MyScrollService::class.java)
            intent.putExtra("count", scrollCount)
            intent.putExtra("interval_sec", interval)
            intent.putExtra("duration_sec", duration)
            intent.putExtra("action", "start")
            startService(intent)

            tvStatus.text = "Status: Running"
        }

        btnStop.setOnClickListener {
            val intent = Intent(this, MyScrollService::class.java)
            intent.putExtra("action", "stop")
            startService(intent)
            tvStatus.text = "Status: Stopped"
        }
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val enabled = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES) ?: return false
        return enabled.contains(packageName)
    }
}
