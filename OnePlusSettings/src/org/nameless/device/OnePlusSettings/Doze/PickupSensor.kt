/*
 * Copyright (C) 2021-2022 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.nameless.device.OnePlusSettings.Doze

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.PowerManager
import android.os.SystemClock
import android.util.Log

import java.util.concurrent.Executors

import org.nameless.device.OnePlusSettings.Utils.DozeUtils

class PickupSensor(
    private val context: Context, sensorType: String, private val sensorValue: Float
) : SensorEventListener {

    // Handling nullable PowerManager
    private val powerManager = context.getSystemService(PowerManager::class.java)
    private val wakeLock = powerManager?.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG)
        ?: throw IllegalStateException("PowerManager is null")

    // Handling nullable SensorManager
    private val sensorManager = context.getSystemService(SensorManager::class.java)
    private val sensor = sensorManager?.let {
        DozeUtils.getSensor(it, sensorType)
    } ?: throw IllegalStateException("SensorManager is null")

    private val executorService = Executors.newSingleThreadExecutor()
    private var entryTimestamp = 0L

    override fun onSensorChanged(event: SensorEvent) {
        if (DEBUG) Log.d(TAG, "Got sensor event: ${event.values[0]}")
        val delta = SystemClock.elapsedRealtime() - entryTimestamp
        if (delta < MIN_PULSE_INTERVAL_MS) {
            return
        }
        entryTimestamp = SystemClock.elapsedRealtime()
        if (event.values[0] == sensorValue) {
            if (DozeUtils.isPickUpSetToWake(context)) {
                wakeLock.acquire(WAKELOCK_TIMEOUT_MS)
                // Handling nullable PowerManager
                powerManager?.wakeUpWithProximityCheck(
                    SystemClock.uptimeMillis(), PowerManager.WAKE_REASON_GESTURE, TAG
                ) ?: Log.e(TAG, "PowerManager is null")
            } else {
                DozeUtils.launchDozePulse(context)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    fun enable() {
        if (sensor != null) {
            Log.d(TAG, "Enabling")
            executorService.submit {
                entryTimestamp = SystemClock.elapsedRealtime()
                // Handling nullable SensorManager
                sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
                    ?: Log.e(TAG, "SensorManager is null")
            }
        }
    }

    fun disable() {
        if (sensor != null) {
            Log.d(TAG, "Disabling")
            executorService.submit {
                // Handling nullable SensorManager
                sensorManager?.unregisterListener(this, sensor)
                    ?: Log.e(TAG, "SensorManager is null")
            }
        }
    }

    companion object {
        private const val TAG = "PickupSensor"
        private const val DEBUG = false

        private const val MIN_PULSE_INTERVAL_MS = 2500L
        private const val WAKELOCK_TIMEOUT_MS = 300L
    }
}
