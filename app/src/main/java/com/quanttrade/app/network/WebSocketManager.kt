package com.quanttrade.app.network

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.*
import java.util.concurrent.TimeUnit

class WebSocketManager {
    
    private var webSocket: WebSocket? = null
    private val client = OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.MILLISECONDS)
        .build()
    
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()
    
    private val _priceUpdates = MutableStateFlow<Map<String, Double>>(emptyMap())
    val priceUpdates: StateFlow<Map<String, Double>> = _priceUpdates.asStateFlow()
    
    private val _tradeUpdates = MutableStateFlow<List<TradeUpdate>>(emptyList())
    val tradeUpdates: StateFlow<List<TradeUpdate>> = _tradeUpdates.asStateFlow()
    
    data class TradeUpdate(
        val symbol: String,
        val price: Double,
        val quantity: Double,
        val time: Long,
        val isBuyerMaker: Boolean
    )
    
    // 连接到币安WebSocket
    fun connectToBinanceWebSocket(symbols: List<String>) {
        val symbolStreams = symbols.joinToString("/") { "${it.lowercase()}@trade" }
        val url = "wss://stream.binance.com:9443/ws/$symbolStreams"
        
        val request = Request.Builder()
            .url(url)
            .build()
        
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                _isConnected.value = true
                println("WebSocket连接成功")
            }
            
            override fun onMessage(webSocket: WebSocket, text: String) {
                // 解析交易数据
                try {
                    val json = org.json.JSONObject(text)
                    val symbol = json.getString("s")
                    val price = json.getDouble("p")
                    val quantity = json.getDouble("q")
                    val time = json.getLong("T")
                    val isBuyerMaker = json.getBoolean("m")
                    
                    // 更新价格
                    val currentPrices = _priceUpdates.value.toMutableMap()
                    currentPrices[symbol] = price
                    _priceUpdates.value = currentPrices
                    
                    // 添加交易更新
                    val currentTrades = _tradeUpdates.value.toMutableList()
                    currentTrades.add(0, TradeUpdate(symbol, price, quantity, time, isBuyerMaker))
                    if (currentTrades.size > 100) {
                        currentTrades.removeLast()
                    }
                    _tradeUpdates.value = currentTrades
                    
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            
            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
                _isConnected.value = false
            }
            
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                _isConnected.value = false
                println("WebSocket连接关闭")
            }
            
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                _isConnected.value = false
                println("WebSocket连接失败: ${t.message}")
            }
        })
    }
    
    // 连接到币安深度WebSocket
    fun connectToBinanceDepthWebSocket(symbol: String) {
        val url = "wss://stream.binance.com:9443/ws/${symbol.lowercase()}@depth"
        
        val request = Request.Builder()
            .url(url)
            .build()
        
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                _isConnected.value = true
            }
            
            override fun onMessage(webSocket: WebSocket, text: String) {
                // 解析深度数据
                try {
                    val json = org.json.JSONObject(text)
                    val bids = json.getJSONArray("b")
                    val asks = json.getJSONArray("a")
                    
                    // TODO: 处理深度数据
                    
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            
            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
                _isConnected.value = false
            }
            
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                _isConnected.value = false
            }
        })
    }
    
    // 断开连接
    fun disconnect() {
        webSocket?.close(1000, "客户端主动断开")
        webSocket = null
        _isConnected.value = false
    }
    
    // 发送消息
    fun sendMessage(message: String): Boolean {
        return webSocket?.send(message) ?: false
    }
    
    // 获取当前价格
    fun getCurrentPrice(symbol: String): Double? {
        return _priceUpdates.value[symbol]
    }
    
    // 获取所有价格
    fun getAllPrices(): Map<String, Double> {
        return _priceUpdates.value
    }
}