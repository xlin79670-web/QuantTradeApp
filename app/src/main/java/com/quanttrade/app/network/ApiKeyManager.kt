package com.quanttrade.app.network

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class ApiKeyManager(context: Context) {
    
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    
    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "api_key_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    
    // 币安API密钥
    fun saveBinanceApiKey(apiKey: String, apiSecret: String) {
        sharedPreferences.edit()
            .putString("binance_api_key", apiKey)
            .putString("binance_api_secret", apiSecret)
            .apply()
    }
    
    fun getBinanceApiKey(): String? {
        return sharedPreferences.getString("binance_api_key", null)
    }
    
    fun getBinanceApiSecret(): String? {
        return sharedPreferences.getString("binance_api_secret", null)
    }
    
    // MT4/5 API密钥
    fun saveMT4Credentials(server: String, login: String, password: String) {
        sharedPreferences.edit()
            .putString("mt4_server", server)
            .putString("mt4_login", login)
            .putString("mt4_password", password)
            .apply()
    }
    
    fun getMT4Server(): String? {
        return sharedPreferences.getString("mt4_server", null)
    }
    
    fun getMT4Login(): String? {
        return sharedPreferences.getString("mt4_login", null)
    }
    
    fun getMT4Password(): String? {
        return sharedPreferences.getString("mt4_password", null)
    }
    
    // 清除所有API密钥
    fun clearAllApiKeys() {
        sharedPreferences.edit().clear().apply()
    }
    
    // 检查是否有有效的API密钥
    fun hasValidBinanceApiKeys(): Boolean {
        val apiKey = getBinanceApiKey()
        val apiSecret = getBinanceApiSecret()
        return !apiKey.isNullOrBlank() && !apiSecret.isNullOrBlank()
    }
    
    fun hasValidMT4Credentials(): Boolean {
        val server = getMT4Server()
        val login = getMT4Login()
        val password = getMT4Password()
        return !server.isNullOrBlank() && !login.isNullOrBlank() && !password.isNullOrBlank()
    }
}