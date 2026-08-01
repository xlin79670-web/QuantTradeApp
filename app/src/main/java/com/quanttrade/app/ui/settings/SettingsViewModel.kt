package com.quanttrade.app.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "设置"
    }
    val text: LiveData<String> = _text
    
    // 应用设置
    private val _settings = MutableLiveData<AppSettings>().apply {
        value = AppSettings()
    }
    val settings: LiveData<AppSettings> = _settings
    
    // 更新设置
    fun updateSettings(newSettings: AppSettings) {
        _settings.value = newSettings
    }
    
    // 更新单个设置项
    fun updateSetting(key: String, value: Any) {
        val currentSettings = _settings.value ?: AppSettings()
        val updatedSettings = when (key) {
            "darkMode" -> currentSettings.copy(darkMode = value as Boolean)
            "notifications" -> currentSettings.copy(notificationsEnabled = value as Boolean)
            "autoConnect" -> currentSettings.copy(autoConnect = value as Boolean)
            "riskLevel" -> currentSettings.copy(riskLevel = value as RiskLevel)
            "maxPositionSize" -> currentSettings.copy(maxPositionSize = value as Double)
            "maxDailyLoss" -> currentSettings.copy(maxDailyLoss = value as Double)
            else -> currentSettings
        }
        _settings.value = updatedSettings
    }
}

data class AppSettings(
    val darkMode: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val autoConnect: Boolean = false,
    val riskLevel: RiskLevel = RiskLevel.MEDIUM,
    val maxPositionSize: Double = 0.1,  // 最大仓位大小（账户百分比）
    val maxDailyLoss: Double = 5.0,     // 最大日亏损百分比
    val language: String = "zh",
    val currency: String = "USD"
)

enum class RiskLevel {
    LOW,
    MEDIUM,
    HIGH,
    VERY_HIGH
}