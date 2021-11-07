//package com.odom.simplewatchface.config
//
//import android.app.Activity
//import android.graphics.Color
//import android.os.Bundle
//import android.support.wearable.view.CircledImageView
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.view.WindowInsets
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import androidx.wear.widget.BoxInsetLayout
//import androidx.wear.widget.WearableRecyclerView
//import com.google.android.gms.wearable.DataMap
//import com.odom.simplewatchface.R
//import com.odom.simplewatchface.util.DigitalWatchFaceUtil
//import java.util.concurrent.Callable
//
//class DigitalWatchFaceWearableConfigActivity : Activity() {
//
//    private var mColorSelectionRecyclerView: WearableRecyclerView? = null
//    private var mDigitalColorRecyclerViewAdapter: DigitalColorRecyclerViewAdapter? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_digital_config)
//        val content = findViewById<View>(R.id.content) as BoxInsetLayout
//
//        // BoxInsetLayout adds padding by default on round devices. Add some on square devices.
//        content.setOnApplyWindowInsetsListener { v, insets ->
//            if (!insets.isRound) {
//                v.setPaddingRelative(
//                    resources.getDimensionPixelSize(R.dimen.content_padding_start),
//                    v.paddingTop,
//                    v.paddingEnd,
//                    v.paddingBottom
//                )
//            }
//            v.onApplyWindowInsets(insets)
//        }
//        mColorSelectionRecyclerView = findViewById(R.id.color_picker_recycler_view)
//
//        // Aligns the first and last items on the list vertically centered on the screen.
//        mColorSelectionRecyclerView!!.setEdgeItemsCenteringEnabled(true)
//        mColorSelectionRecyclerView!!.setLayoutManager(LinearLayoutManager(this))
//        val colors = resources.getStringArray(R.array.color_array)
//        mDigitalColorRecyclerViewAdapter = DigitalColorRecyclerViewAdapter(colors)
//        mColorSelectionRecyclerView!!.setAdapter(mDigitalColorRecyclerViewAdapter)
//    }
//
//    fun updateConfigDataItem(backgroundColor: Int) {
//        val configKeysToOverwrite = DataMap()
//        configKeysToOverwrite.putInt(
//            DigitalWatchFaceUtil.KEY_BACKGROUND_COLOR,
//            backgroundColor
//        )
//        DigitalWatchFaceUtil.overwriteKeysInConfigDataMap(
//            applicationContext,
//            configKeysToOverwrite,
//            Callable<Void?> {
//                Log.d("===TAG", "callback successful for datalayer write")
//                finish()
//                null
//            })
//    }
//
//    inner class DigitalColorRecyclerViewAdapter(private val mColors: Array<String?>) :
//        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//            Log.d("===TAG", "onCreateViewHolder(): viewType: $viewType")
//            return DigitalColorViewHolder(
//                LayoutInflater.from(parent.context)
//                    .inflate(R.layout.color_config_list_item, parent, false)
//            )
//        }
//
//        override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
//            Log.d("===TAG", "Element $position set.")
//            val colorName = mColors[position]
//            val color = Color.parseColor(colorName)
//            val colorViewHolder = viewHolder as DigitalColorViewHolder
//            colorViewHolder.setColor(color)
//        }
//
//        override fun getItemCount(): Int {
//            return mColors.size
//        }
//
//        /**
//         * Displays color options for an item on the watch face.
//         */
//        inner class DigitalColorViewHolder(view: View) : RecyclerView.ViewHolder(view),
//            View.OnClickListener {
//            private val mColorCircleImageView: CircledImageView
//            fun setColor(color: Int) {
//                mColorCircleImageView.setCircleColor(color)
//            }
//
//            override fun onClick(view: View) {
//                val position = adapterPosition
//                val colorName = mColors[position]
//                val color = Color.parseColor(colorName)
//                Log.d("===TAG", "Color: $color onClick() position: $position")
//                updateConfigDataItem(color)
//            }
//
//            init {
//                mColorCircleImageView = view.findViewById<View>(R.id.color) as CircledImageView
//                view.setOnClickListener(this)
//            }
//        }
//    }
//}