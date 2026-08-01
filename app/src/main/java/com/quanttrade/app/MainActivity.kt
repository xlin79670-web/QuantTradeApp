package com.quanttrade.app

import android.os.Bundle
import android.widget.TextView
import android.widget.LinearLayout
import android.view.Gravity
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setGravity(Gravity.CENTER)
            setBackgroundColor(Color.WHITE)
            setPadding(50, 50, 50, 50)
        }

        TextView(this).apply {
            text = "📈 量化交易助手"
            textSize = 28f
            setTextColor(Color.BLACK)
            gravity = Gravity.CENTER
            layout.addView(this)
        }

        TextView(this).apply {
            text = "\n✅ 机器学习策略\n✅ 遗传算法优化\n✅ MT4/5 + 币安\n✅ 实时监控"
            textSize = 18f
            setTextColor(Color.DKGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 30, 0, 0)
            layout.addView(this)
        }

        setContentView(layout)
    }
}