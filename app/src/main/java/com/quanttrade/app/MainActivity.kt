package com.quanttrade.app

import android.os.Bundle
import android.widget.*
import android.view.Gravity
import android.view.View
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.app.AlertDialog
import android.content.SharedPreferences

class MainActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences
    private var isTrading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = getSharedPreferences("QuantTrade", MODE_PRIVATE)

        val scrollView = ScrollView(this)
        val mainLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(30, 30, 30, 30)
            setBackgroundColor(Color.parseColor("#F5F5F5"))
        }

        // 标题
        val title = TextView(this)
        title.text = "📈 量化交易助手"
        title.textSize = 26f
        title.setTextColor(Color.parseColor("#6200EE"))
        title.gravity = Gravity.CENTER
        title.setPadding(0, 20, 0, 5)
        mainLayout.addView(title)

        val subtitle = TextView(this)
        subtitle.text = "机器学习策略 · 遗传算法优化"
        subtitle.textSize = 14f
        subtitle.setTextColor(Color.GRAY)
        subtitle.gravity = Gravity.CENTER
        subtitle.setPadding(0, 0, 0, 30)
        mainLayout.addView(subtitle)

        // 账户连接卡片
        addCard(mainLayout, "🔗 账户连接", arrayOf(
            "MT4/5 账户 → 连接外汇交易平台",
            "币安账户 → 连接加密货币交易所"
        ))

        // 交易策略卡片
        addCard(mainLayout, "🤖 交易策略", arrayOf(
            "趋势跟踪 → 基于移动平均线",
            "均值回归 → 基于布林带",
            "机器学习 → TensorFlow Lite",
            "遗传算法 → 自动优化参数"
        ))

        // 风险控制卡片
        addCard(mainLayout, "🛡️ 风险控制", arrayOf(
            "止损设置 → 2.0%",
            "止盈设置 → 4.0%",
            "最大仓位 → 10%"
        ))

        // 交易状态卡片
        addCard(mainLayout, "📊 交易状态", arrayOf(
            "账户余额 → $10,000.00",
            "今日盈亏 → +$0.00",
            "持仓数量 → 0"
        ))

        // 按钮区域
        val buttonLayout = LinearLayout(this)
        buttonLayout.orientation = LinearLayout.HORIZONTAL
        buttonLayout.setPadding(0, 20, 0, 20)

        val startBtn = Button(this)
        startBtn.text = "▶ 开始交易"
        startBtn.setBackgroundColor(Color.parseColor("#4CAF50"))
        startBtn.setTextColor(Color.WHITE)
        val startParams = LinearLayout.LayoutParams(0, 120, 1f)
        startParams.marginEnd = 10
        startBtn.layoutParams = startParams
        startBtn.setOnClickListener { startTrading() }
        buttonLayout.addView(startBtn)

        val stopBtn = Button(this)
        stopBtn.text = "⏸ 停止交易"
        stopBtn.setBackgroundColor(Color.parseColor("#F44336"))
        stopBtn.setTextColor(Color.WHITE)
        stopBtn.layoutParams = LinearLayout.LayoutParams(0, 120, 1f)
        stopBtn.setOnClickListener { stopTrading() }
        buttonLayout.addView(stopBtn)

        mainLayout.addView(buttonLayout)

        // 最近交易卡片
        addCard(mainLayout, "📋 最近交易", arrayOf(
            "BTC/USDT → +$45.20 买入",
            "ETH/USDT → -$12.30 卖出",
            "EUR/USD → +$92.60 买入",
            "GBP/USD → +$28.40 买入"
        ))

        // API设置按钮
        val settingsBtn = Button(this)
        settingsBtn.text = "⚙️ API设置"
        settingsBtn.setBackgroundColor(Color.parseColor("#6200EE"))
        settingsBtn.setTextColor(Color.WHITE)
        settingsBtn.setOnClickListener { showSettingsDialog() }
        mainLayout.addView(settingsBtn)

        // 底部说明
        val footer = TextView(this)
        footer.text = "\n⚠️ 风险提示：量化交易存在风险，请谨慎投资\n\n版本 1.0 · 机器学习策略"
        footer.textSize = 12f
        footer.setTextColor(Color.GRAY)
        footer.gravity = Gravity.CENTER
        footer.setPadding(0, 30, 0, 50)
        mainLayout.addView(footer)

        scrollView.addView(mainLayout)
        setContentView(scrollView)
    }

    private fun addCard(parent: LinearLayout, title: String, items: Array<String>) {
        val card = LinearLayout(this)
        card.orientation = LinearLayout.VERTICAL
        card.setBackgroundColor(Color.WHITE)
        card.setPadding(30, 25, 30, 25)
        val cardParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        cardParams.bottomMargin = 20
        card.layoutParams = cardParams

        val titleView = TextView(this)
        titleView.text = title
        titleView.textSize = 18f
        titleView.setTextColor(Color.parseColor("#333333"))
        titleView.setPadding(0, 0, 0, 15)
        card.addView(titleView)

        for (item in items) {
            val parts = item.split(" → ")
            val row = LinearLayout(this)
            row.orientation = LinearLayout.HORIZONTAL
            row.setPadding(0, 12, 0, 12)

            val label = TextView(this)
            label.text = parts[0]
            label.textSize = 15f
            label.setTextColor(Color.parseColor("#666666"))
            label.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            row.addView(label)

            val value = TextView(this)
            value.text = if (parts.size > 1) parts[1] else ""
            value.textSize = 15f
            value.setTextColor(Color.parseColor("#999999"))
            row.addView(value)

            card.addView(row)

            val divider = View(this)
            divider.setBackgroundColor(Color.parseColor("#EEEEEE"))
            divider.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1
            )
            card.addView(divider)
        }

        parent.addView(card)
    }

    private fun showSettingsDialog() {
        val dialogLayout = LinearLayout(this)
        dialogLayout.orientation = LinearLayout.VERTICAL
        dialogLayout.setPadding(50, 30, 50, 30)

        val mt4Label = TextView(this)
        mt4Label.text = "MT4/5 设置"
        mt4Label.textSize = 16f
        mt4Label.setTextColor(Color.BLACK)
        dialogLayout.addView(mt4Label)

        val serverInput = EditText(this)
        serverInput.hint = "服务器地址"
        serverInput.setText(prefs.getString("mt4_server", ""))
        dialogLayout.addView(serverInput)

        val loginInput = EditText(this)
        loginInput.hint = "登录号"
        loginInput.setText(prefs.getString("mt4_login", ""))
        dialogLayout.addView(loginInput)

        val binanceLabel = TextView(this)
        binanceLabel.text = "\n币安设置"
        binanceLabel.textSize = 16f
        binanceLabel.setTextColor(Color.BLACK)
        dialogLayout.addView(binanceLabel)

        val apiKeyInput = EditText(this)
        apiKeyInput.hint = "API Key"
        apiKeyInput.setText(prefs.getString("binance_api_key", ""))
        dialogLayout.addView(apiKeyInput)

        val apiSecretInput = EditText(this)
        apiSecretInput.hint = "API Secret"
        apiSecretInput.setText(prefs.getString("binance_api_secret", ""))
        dialogLayout.addView(apiSecretInput)

        AlertDialog.Builder(this)
            .setTitle("⚙️ API设置")
            .setView(dialogLayout)
            .setPositiveButton("保存") { _, _ ->
                prefs.edit()
                    .putString("mt4_server", serverInput.text.toString())
                    .putString("mt4_login", loginInput.text.toString())
                    .putString("binance_api_key", apiKeyInput.text.toString())
                    .putString("binance_api_secret", apiSecretInput.text.toString())
                    .apply()
                Toast.makeText(this, "✅ 设置已保存", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun startTrading() {
        isTrading = true
        Toast.makeText(this, "🚀 交易已启动", Toast.LENGTH_SHORT).show()
    }

    private fun stopTrading() {
        isTrading = false
        Toast.makeText(this, "⏸ 交易已停止", Toast.LENGTH_SHORT).show()
    }
}