package com.quanttrade.app.network

import com.quanttrade.app.data.*
import retrofit2.Response
import retrofit2.http.*

// 币安API接口
interface BinanceApiService {
    
    // 获取交易对信息
    @GET("/api/v3/exchangeInfo")
    suspend fun getExchangeInfo(): Response<ExchangeInfo>
    
    // 获取K线数据
    @GET("/api/v3/klines")
    suspend fun getKlines(
        @Query("symbol") symbol: String,
        @Query("interval") interval: String,
        @Query("limit") limit: Int = 500
    ): Response<List<List<Any>>>
    
    // 获取当前价格
    @GET("/api/v3/ticker/price")
    suspend fun getPrice(@Query("symbol") symbol: String): Response<PriceTicker>
    
    // 获取账户信息
    @GET("/api/v3/account")
    suspend fun getAccountInfo(): Response<AccountInfo>
    
    // 下单
    @POST("/api/v3/order")
    suspend fun placeOrder(
        @Query("symbol") symbol: String,
        @Query("side") side: String,
        @Query("type") type: String,
        @Query("quantity") quantity: Double,
        @Query("price") price: Double? = null,
        @Query("stopPrice") stopPrice: Double? = null,
        @Query("timeInForce") timeInForce: String? = null
    ): Response<OrderResponse>
    
    // 查询订单
    @GET("/api/v3/order")
    suspend fun getOrder(
        @Query("symbol") symbol: String,
        @Query("orderId") orderId: Long
    ): Response<OrderStatus>
    
    // 取消订单
    @DELETE("/api/v3/order")
    suspend fun cancelOrder(
        @Query("symbol") symbol: String,
        @Query("orderId") orderId: Long
    ): Response<OrderStatus>
    
    // 获取当前挂单
    @GET("/api/v3/openOrders")
    suspend fun getOpenOrders(@Query("symbol") symbol: String? = null): Response<List<OrderStatus>>
    
    // 获取历史成交
    @GET("/api/v3/myTrades")
    suspend fun getMyTrades(
        @Query("symbol") symbol: String,
        @Query("limit") limit: Int = 500
    ): Response<List<TradeHistory>>
}

// MT4/5 API接口 (模拟)
interface MT4ApiService {
    
    // 连接到MT4/5服务器
    @POST("/api/connect")
    suspend fun connect(
        @Body request: MT4ConnectRequest
    ): Response<MT4ConnectResponse>
    
    // 获取账户信息
    @GET("/api/account")
    suspend fun getAccountInfo(): Response<MT4AccountInfo>
    
    // 获取持仓
    @GET("/api/positions")
    suspend fun getPositions(): Response<List<MT4Position>>
    
    // 下单
    @POST("/api/order")
    suspend fun placeOrder(
        @Body request: MT4OrderRequest
    ): Response<MT4OrderResponse>
    
    // 获取K线数据
    @GET("/api/klines")
    suspend fun getKlines(
        @Query("symbol") symbol: String,
        @Query("timeframe") timeframe: String,
        @Query("count") count: Int = 100
    ): Response<List<MT4Kline>>
    
    // 获取当前价格
    @GET("/api/price")
    suspend fun getPrice(@Query("symbol") symbol: String): Response<MT4Price>
}

// 数据类
data class ExchangeInfo(
    val symbols: List<SymbolInfo>
)

data class SymbolInfo(
    val symbol: String,
    val baseAsset: String,
    val quoteAsset: String,
    val filters: List<SymbolFilter>
)

data class SymbolFilter(
    val filterType: String,
    val minQty: Double? = null,
    val maxQty: Double? = null,
    val stepSize: Double? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val tickSize: Double? = null
)

data class PriceTicker(
    val symbol: String,
    val price: Double
)

data class AccountInfo(
    val makerCommission: Int,
    val takerCommission: Int,
    val canTrade: Boolean,
    val canWithdraw: Boolean,
    val canDeposit: Boolean,
    val balances: List<Balance>
)

data class OrderResponse(
    val symbol: String,
    val orderId: Long,
    val clientOrderId: String,
    val transactTime: Long,
    val price: Double,
    val origQty: Double,
    val executedQty: Double,
    val cummulativeQuoteQty: Double,
    val status: String,
    val type: String,
    val side: String
)

data class OrderStatus(
    val symbol: String,
    val orderId: Long,
    val clientOrderId: String,
    val price: Double,
    val origQty: Double,
    val executedQty: Double,
    val cummulativeQuoteQty: Double,
    val status: String,
    val type: String,
    val side: String,
    val stopPrice: Double = 0.0,
    val time: Long,
    val updateTime: Long
)

data class TradeHistory(
    val symbol: String,
    val id: Long,
    val orderId: Long,
    val price: Double,
    val qty: Double,
    val commission: Double,
    val commissionAsset: String,
    val time: Long,
    val isBuyer: Boolean,
    val isMaker: Boolean
)

// MT4/5数据类
data class MT4ConnectRequest(
    val server: String,
    val login: String,
    val password: String
)

data class MT4ConnectResponse(
    val success: Boolean,
    val message: String,
    val sessionId: String? = null
)

data class MT4AccountInfo(
    val login: String,
    val name: String,
    val server: String,
    val balance: Double,
    val equity: Double,
    val margin: Double,
    val freeMargin: Double,
    val marginLevel: Double,
    val profit: Double
)

data class MT4Position(
    val ticket: Long,
    val symbol: String,
    val type: Int,  // 0=BUY, 1=SELL
    val volume: Double,
    val openPrice: Double,
    val currentPrice: Double,
    val profit: Double,
    val swap: Double,
    val openTime: Long
)

data class MT4OrderRequest(
    val symbol: String,
    val type: Int,  // 0=BUY, 1=SELL
    val volume: Double,
    val price: Double? = null,
    val stopLoss: Double? = null,
    val takeProfit: Double? = null,
    val comment: String? = null
)

data class MT4OrderResponse(
    val success: Boolean,
    val message: String,
    val ticket: Long? = null
)

data class MT4Kline(
    val time: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Double
)

data class MT4Price(
    val symbol: String,
    val bid: Double,
    val ask: Double,
    val time: Long
)