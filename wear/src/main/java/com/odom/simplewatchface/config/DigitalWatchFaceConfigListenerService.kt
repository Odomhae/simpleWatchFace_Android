package com.odom.simplewatchface.config

import android.util.Log
import com.google.android.gms.wearable.DataMap
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import com.odom.simplewatchface.util.DigitalWatchFaceUtil

class DigitalWatchFaceConfigListenerService : WearableListenerService() {
    private val TAG = "DigitalListenerService"

    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "onMessageReceived: $messageEvent")
        }
        if (messageEvent.path != DigitalWatchFaceUtil.PATH_WITH_FEATURE) {
            return
        }

        val rawData = messageEvent.data
        // It's allowed that the message carries only some of the keys used in the config DataItem
        // and skips the ones that we don't want to change.
        val configKeysToOverwrite = DataMap.fromByteArray(rawData)
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "Received watch face config message: $configKeysToOverwrite")
        }
        DigitalWatchFaceUtil.overwriteKeysInConfigDataMap(
            applicationContext,
            configKeysToOverwrite,
            null
        )
    }
}