package com.odom.app

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.wearable.DataItem
import com.google.android.gms.wearable.DataMap
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.Wearable

class DigitalWatchFaceCompanionConfigActivity : AppCompatActivity(), OnCompleteListener<DataItem> {

    // TODO: use the shared constants (needs covering all the samples with Gradle build model)
    private val KEY_BACKGROUND_COLOR = "BACKGROUND_COLOR"
    private val KEY_HOURS_COLOR = "HOURS_COLOR"
    private val KEY_MINUTES_COLOR = "MINUTES_COLOR"
    private val KEY_SECONDS_COLOR = "SECONDS_COLOR"
    private val PATH_WITH_FEATURE = "/watch_face_config/Digital"

    private val mPeerId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // 설정 완료
    override fun onComplete(dataItemTask: Task<DataItem>) {

        if (dataItemTask.isSuccessful && dataItemTask.result != null) {
            val configDataItem: DataItem = dataItemTask.result!!
            val dataMapItem = DataMapItem.fromDataItem(configDataItem)
            val config = dataMapItem.dataMap
            setUpAllPickers(config)

        } else {
            // If DataItem with the current config can't be retrieved, select the default items on
            // each picker.
            setUpAllPickers(null)
        }

    }

    /**
     * Sets up selected items for all pickers according to given `config` and sets up their
     * item selection listeners.
     *
     * @param config the `DigitalWatchFaceService` config [DataMap]. If null, the
     * default items are selected.
     */
    private fun setUpAllPickers(config: DataMap?) {
        setUpColorPickerSelection(R.id.background,KEY_BACKGROUND_COLOR, config, R.string.color_black)
        setUpColorPickerSelection(R.id.hours, KEY_HOURS_COLOR, config, R.string.color_white)
        setUpColorPickerSelection(R.id.minutes, KEY_MINUTES_COLOR, config, R.string.color_white)
        setUpColorPickerSelection(R.id.seconds, KEY_SECONDS_COLOR, config, R.string.color_gray)

        setUpColorPickerListener(R.id.background, KEY_BACKGROUND_COLOR)
        setUpColorPickerListener(R.id.hours, KEY_HOURS_COLOR)
        setUpColorPickerListener(R.id.minutes, KEY_MINUTES_COLOR)
        setUpColorPickerListener(R.id.seconds, KEY_SECONDS_COLOR)
    }

    private fun setUpColorPickerSelection(spinnerId: Int, configKey: String, config: DataMap?, defaultColorNameResId: Int) {
        val defaultColorName = getString(defaultColorNameResId)
        val defaultColor = Color.parseColor(defaultColorName)
        val color: Int = config?.getInt(configKey, defaultColor) ?: defaultColor
        val spinner = findViewById<View>(spinnerId) as Spinner
        val colorNames = resources.getStringArray(R.array.color_array)
        for (i in colorNames.indices) {
            if (Color.parseColor(colorNames[i]) == color) {
                spinner.setSelection(i)
                break
            }
        }
    }

    private fun setUpColorPickerListener(spinnerId: Int, configKey: String) {
        val spinner = findViewById<View>(spinnerId) as Spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, pos: Int, id: Long) {
                val colorName = adapterView.getItemAtPosition(pos) as String
                sendConfigUpdateMessageToWatch(configKey, Color.parseColor(colorName))
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    private fun sendConfigUpdateMessageToWatch(configKey: String, color: Int) {
        if (mPeerId != null) {
            val config = DataMap()
            config.putInt(configKey, color)
            val rawData = config.toByteArray()
            val messageClient = Wearable.getMessageClient(
                applicationContext
            )
            messageClient.sendMessage(mPeerId, PATH_WITH_FEATURE, rawData)

            Log.d("===TAG", "Sent watch face config message: " + configKey + " -> " + Integer.toHexString(color))
        }
    }

}