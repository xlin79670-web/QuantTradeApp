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
    private var isConnected = false
    private var isTrading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = getSharedPreferences("QuantTrade", MODE_PRIVATE)

        val scrollView = ScrollView(this)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(30, 30, 30, 30)
            setBackgroundColor(Color.parseColor("#F5F5F5"))
        }

        // 标题
        TextView(this).apply {
            text = "📈 量化交易助手"
            textSize = 26f
            setTextColor(Color.parseColor("#6200EE"))
            gravity = Gravity.CENTER
            setPadding(0, 20, 0, 5)
            layout.addView(this)
        }

        TextView(this).apply {
            text = "机器学习策略 · 遗传算法优化"
            textSize = 14f
            setTextColor(Color.GRAY)
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 30)
            layout.addView(this)
        }

        // === 账户连接卡片 ===
        createCard(layout, "🔗 账户连接", listOf(
            Triple("MT4/5 账户", "连接外汇交易平台", View.OnClickListener { showMT4Dialog() }),
            Triple("币安账户", "连接加密货币交易所", View.OnClickListener { showBinanceDialog() })
        ))

        // === 策略选择卡片 ===
        createCard(layout, "🤖 交易策略", listOf(
            Triple("趋势跟踪", "基于移动平均线", null),
            Triple("均值回归", "基于布林带", null),
            Triple("机器学习", "TensorFlow Lite", null),
            Triple("遗传算法优化", "自动优化参数", null)
        ))

        // === 风险控制卡片 ===
        createCard(layout, "🛡️ 风险控制", listOf(
            Triple("止损设置", "2.0%", null),
            Triple("止盈设置", "4.0%", null),
            Triple("最大仓位", "10%", null)
        ))

        // === 交易状态卡片 ===
        val statusCard = createCard(layout, "📊 交易状态", listOf(
            Triple("账户余额", "$10,000.00", null),
            Triple("今日盈亏", "+$0.00", null),
            Triple("持仓数量", "0", null)
        ))

        // 交易按钮
        LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 30, 0, 0)
            layout.addView(this)

            Button(this@MainActivity).apply {
                text = "▶️ 开始交易"
                setBackgroundColor(Color.parseColor("#4CAF50"))
                setTextColor(Color.WHITE)
                layoutParams = LinearLayout.LayoutParams(0, 120, 1f).apply { marginEnd = 10 }
                setOnClickListener { startTrading() }
                this@apply.addView(this)
            }

            Button(this@MainActivity).apply {
                text = "⏸ 停止交易"
                setBackgroundColor(Color.parseColor("#F44336"))
                setTextColor(Color.WHITE)
                layoutParams = LinearLayout.LayoutParams(0, 120, 1f)
                setOnClickListener { stopTrading() }
                this@apply.addView(this)
            }
        }

        // === 最近交易记录 ===
        createCard(layout, "📋 最近交易", listOf(
            Triple("BTC/USDT", "+$45.20 买入", null),
            Triple("ETH/USDT", "-$12.30 卖出", null),
            Triple("EUR/USD", "+$92.60 买入", null),
            Triple("GBP/USD", "+$28.40 买入", null)
        ))

        // === 关于 ===
        TextView(this).apply {
            text = "\n⚠️ 风险提示：量化交易存在风险，请谨慎投资\n\n版本 1.0 · 机器学习策略"
            textSize = 12f
            setTextColor(Color.GRAY)
            gravity = Gravity.CENTER
            setPadding(0, 30, 0, 50)
            layout.addView(this)
        }

        scrollView.addView(layout)
        setContentView(scrollView)
    }

    private fun createCard(parent: LinearLayout, title: String, items: List<Triple<String, String, View.OnClickListener?>>): View {
        return LinearLayout(parent.context).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.WHITE)
            setPadding(30, 25, 30, 25)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = 20 }

            // 卡片标题
            TextView(context).apply {
                text = title
                textSize = 18f
                setTextColor(Color.parseColor("#333333"))
                setPadding(0, 0, 0, 15)
                this@apply.addView(this)
            }

            // 卡片内容
            items.forEach { (label, value, listener) ->
                LinearLayout(context).apply {
                    orientation = LinearLayout.HORIZONTAL
                    setPadding(0, 12, 0, 12)
                    setOnClickListener(listener)

                    TextView(context).apply {
                        text = label
                        textSize = 15f
                        setTextColor(Color.parseColor("#666666"))
                        layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                        this@apply.addView(this)
                    }

                    TextView(context).apply {
                        text = value
                        textSize = 15f
                        setTextColor(Color.parseColor("#999999"))
                        this@apply.addView(this)
                    }

                    this@apply.addView(this)
                }

                // 分割线
                View(context).apply {
                    setBackgroundColor(Color.parseColor("#EEEEEE"))
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 1
                    )
                    this@apply.addView(this)
                }
            }

            parent.addView(this)
        }
    }

    private fun showMT4Dialog() {
        val dialogLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 30, 50, 30)
        }

        val serverInput = EditText(this).apply {
            hint = "服务器地址 (如: demo.metaquotes.net)"
            setText(prefs.getString("mt4_server", ""))
        }
        val loginInput = EditText(this).apply {
            hint = "登录号"
            setText(prefs.getString("mt4_login", ""))
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }
        val passwordInput = EditText(this).apply {
            hint = "密码"
            setText(prefs.getString("mt4_password", ""))
            inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        dialogLayout.addView(serverInput)
        dialogLayout.addView(loginInput)
        dialogLayout.addView(passwordInput)

        AlertDialog.Builder(this)
            .setTitle("🔗 连接 MT4/5")
            .setView(dialogLayout)
            .setPositiveButton("连接") { _, _ ->
                prefs.edit()
                    .putString("mt4_server", serverInput.text.toString())
                    .putString("mt4_login", loginInput.text.toString())
                    .putString("mt4_password", passwordInput.text.toString())
                    .apply()
                Toast.makeText(this, "✅ MT4/5 连接配置已保存", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun showBinanceDialog() {
        val dialogLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 30, 50, 30)
        }

        val apiKeyInput = EditText(this).apply {
            hint = "API Key"
            setText(prefs.getString("binance_api_key", ""))
        }
        val apiSecretInput = EditText(this).apply {
            hint = "API Secret"
            setText(prefs.getString("binance_api_secret", ""))
            inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        dialogLayout.addView(apiKeyInput)
        dialogLayout.addView(apiSecretInput)

        AlertDialog.Builder(this)
            .setTitle("🔗 连接币安")
            .setView(dialogLayout)
            .setPositiveButton("连接") { _, _ ->
                prefs.edit()
                    .putString("binance_api_key", apiKeyInput.text.toString())
                    .putString("binance_api_secret", apiSecretInput.text.toString())
                    .apply()
                Toast.makeText(this, "✅ 币安连接配置已保存", Toast.LENGTH_SHORT).show()
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