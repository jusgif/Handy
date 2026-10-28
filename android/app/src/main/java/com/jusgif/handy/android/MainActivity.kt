package com.jusgif.handy.android

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        render()
    }

    override fun onResume() {
        super.onResume()
        if (::status.isInitialized) updateStatus()
    }

    private fun render() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(28, 32, 28, 32)
        }

        val title = TextView(this).apply {
            text = "Handy Voice Typing"
            textSize = 26f
            gravity = Gravity.CENTER
        }
        val description = TextView(this).apply {
            text = "Private, on-device voice typing. No transcript or recording history is kept."
            textSize = 16f
            gravity = Gravity.CENTER
            setPadding(0, 16, 0, 24)
        }
        status = TextView(this).apply {
            textSize = 14f
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 20)
        }
        val overlayButton = Button(this).apply {
            text = "Grant floating button permission"
            setOnClickListener { requestOverlayPermission() }
        }
        val micButton = Button(this).apply {
            text = "Grant microphone and enable floating button"
            setOnClickListener { enableFloatingButton() }
        }
        val stopButton = Button(this).apply {
            text = "Hide floating button"
            setOnClickListener {
                stopService(Intent(this@MainActivity, FloatingMicService::class.java))
                updateStatus()
            }
        }

        root.addView(title, match())
        root.addView(description, match())
        root.addView(status, match())
        root.addView(overlayButton, match())
        root.addView(micButton, match())
        root.addView(stopButton, match())
        setContentView(root)
        updateStatus()
    }

    private fun requestOverlayPermission() {
        if (Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "Floating button permission is already enabled", Toast.LENGTH_SHORT).show()
            return
        }
        startActivity(
            Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
        )
    }

    private fun enableFloatingButton() {
        if (!Settings.canDrawOverlays(this)) {
            requestOverlayPermission()
            return
        }
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_AUDIO)
            return
        }
        startService(Intent(this, FloatingMicService::class.java))
        updateStatus()
        Toast.makeText(this, "Floating button enabled", Toast.LENGTH_SHORT).show()
    }

    @Deprecated("Use the Activity Result API in a follow-up UI pass")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_AUDIO &&
            grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED
        ) {
            enableFloatingButton()
        } else {
            Toast.makeText(this, "Microphone permission is required for dictation", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateStatus() {
        if (!::status.isInitialized) return
        val overlay = Settings.canDrawOverlays(this)
        val audio = checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        status.text = "Floating permission: " + (if (overlay) "enabled" else "not granted") + "\n" +
            "Microphone permission: " + (if (audio) "enabled" else "not granted") + "\n\n" +
            "Transcription engine integration is still in progress."
    }

    private fun match() = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )

    companion object {
        private const val REQUEST_AUDIO = 1001
    }
}
