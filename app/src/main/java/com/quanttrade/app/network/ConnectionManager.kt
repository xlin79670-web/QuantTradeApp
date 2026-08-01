package com.quanttrade.app.network

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ConnectionManager {
    
    enum class ConnectionState {
        DISCONNECTED,
        CONNECTING,
        CONNECTED,
        ERROR
    }
    
    private val _binanceConnectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val binanceConnectionState: StateFlow<ConnectionState> = _binanceConnectionState.asStateFlow()
    
    private val _mt4ConnectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val mt4ConnectionState: StateFlow<ConnectionState> = _mt4ConnectionState.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    // 连接到币安
    suspend fun connectToBinance(apiKey: String, apiSecret: String): Boolean {
        _binanceConnectionState.value = ConnectionState.CONNECTING
        
        return try {
            // 这里应该实现实际的连接测试
            // 比如尝试获取账户信息
            val response = ApiClient.binanceApi.getAccountInfo()
            
            if (response.isSuccessful) {
                _binanceConnectionState.value = ConnectionState.CONNECTED
                _errorMessage.value = null
                true
            } else {
                _binanceConnectionState.value = ConnectionState.ERROR
                _errorMessage.value = "币安连接失败: ${response.message()}"
                false
            }
        } catch (e: Exception) {
            _binanceConnectionState.value = ConnectionState.ERROR
            _errorMessage.value = "币安连接错误: ${e.message}"
            false
        }
    }
    
    // 断开币安连接
    fun disconnectFromBinance() {
        _binanceConnectionState.value = ConnectionState.DISCONNECTED
        _errorMessage.value = null
    }
    
    // 连接到MT4/5
    suspend fun connectToMT4(server: String, login: String, password: String): Boolean {
        _mt4ConnectionState.value = ConnectionState.CONNECTING
        
        return try {
            val response = ApiClient.mt4Api.connect(
                MT4ConnectRequest(
                    server = server,
                    login = login,
                    password = password
                )
            )
            
            if (response.isSuccessful && response.body()?.success == true) {
                _mt4ConnectionState.value = ConnectionState.CONNECTED
                _errorMessage.value = null
                true
            } else {
                _mt4ConnectionState.value = ConnectionState.ERROR
                _errorMessage.value = "MT4连接失败: ${response.body()?.message ?: response.message()}"
                false
            }
        } catch (e: Exception) {
            _mt4ConnectionState.value = ConnectionState.ERROR
            _errorMessage.value = "MT4连接错误: ${e.message}"
            false
        }
    }
    
    // 断开MT4连接
    fun disconnectFromMT4() {
        _mt4ConnectionState.value = ConnectionState.DISCONNECTED
        _errorMessage.value = null
    }
    
    // 检查连接状态
    fun isBinanceConnected(): Boolean {
        return _binanceConnectionState.value == ConnectionState.CONNECTED
    }
    
    fun isMT4Connected(): Boolean {
        return _mt4ConnectionState.value == ConnectionState.CONNECTED
    }
    
    fun isAnyConnected(): Boolean {
        return isBinanceConnected() || isMT4Connected()
    }
    
    // 获取连接状态文本
    fun getBinanceConnectionStatusText(): String {
        return when (_binanceConnectionState.value) {
            ConnectionState.DISCONNECTED -> "未连接"
            ConnectionState.CONNECTING -> "连接中..."
            ConnectionState.CONNECTED -> "已连接"
            ConnectionState.ERROR -> "连接错误"
        }
    }
    
    fun getMT4ConnectionStatusText(): String {
        return when (_mt4ConnectionState.value) {
            ConnectionState.DISCONNECTED -> "未连接"
            ConnectionState.CONNECTING -> "连接中..."
            ConnectionState.CONNECTED -> "已连接"
            ConnectionState.ERROR -> "连接错误"
        }
    }
}