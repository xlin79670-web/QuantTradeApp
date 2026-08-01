package com.quanttrade.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// 交易对数据
@Entity(tableName = "trading_pairs")
data class TradingPair(
    @PrimaryKey
    val symbol: String,
    val baseCurrency: String,
    val quoteCurrency: String,
    val minTradeAmount: Double,
    val maxTradeAmount: Double,
    val tickSize: Double,
    val isActive: Boolean = true
)

// K线数据
@Entity(tableName = "kline_data")
data class KlineData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val symbol: String,
    val interval: String,  // 1m, 5m, 15m, 1h, 4h, 1d
    val openTime: Long,
    val closeTime: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Double,
    val quoteVolume: Double,
    val trades: Int
)

// 订单数据
@Entity(tableName = "orders")
data class Order(
    @PrimaryKey
    val orderId: String,
    val symbol: String,
    val side: OrderSide,
    val type: OrderType,
    val quantity: Double,
    val price: Double?,
    val stopPrice: Double?,
    val status: OrderStatus,
    val createdAt: Long,
    val updatedAt: Long,
    val filledQuantity: Double = 0.0,
    val averagePrice: Double = 0.0,
    val commission: Double = 0.0
)

// 交易记录
@Entity(tableName = "trades")
data class Trade(
    @PrimaryKey
    val tradeId: String,
    val orderId: String,
    val symbol: String,
    val side: OrderSide,
    val quantity: Double,
    val price: Double,
    val commission: Double,
    val commissionAsset: String,
    val time: Long,
    val isBuyer: Boolean
)

// 持仓数据
@Entity(tableName = "positions")
data class Position(
    @PrimaryKey
    val symbol: String,
    val side: PositionSide,
    val quantity: Double,
    val entryPrice: Double,
    val markPrice: Double,
    val unrealizedProfit: Double,
    val leverage: Int,
    val marginType: MarginType,
    val isolatedMargin: Double = 0.0,
    val liquidationPrice: Double = 0.0
)

// 账户余额
@Entity(tableName = "balances")
data class Balance(
    @PrimaryKey
    val asset: String,
    val free: Double,
    val locked: Double,
    val total: Double
)

enum class OrderSide {
    BUY,
    SELL
}

enum class OrderType {
    MARKET,
    LIMIT,
    STOP_LOSS,
    STOP_LOSS_LIMIT,
    TAKE_PROFIT,
    TAKE_PROFIT_LIMIT,
    LIMIT_MAKER
}

enum class OrderStatus {
    NEW,
    PARTIALLY_FILLED,
    FILLED,
    CANCELED,
    PENDING_CANCEL,
    REJECTED,
    EXPIRED
}

enum class PositionSide {
    LONG,
    SHORT,
    BOTH
}

enum class MarginType {
    ISOLATED,
    CROSSED
}

// 策略信号
data class StrategySignal(
    val symbol: String,
    val signalType: SignalType,
    val strength: Double,  // 0.0 - 1.0
    val timestamp: Long,
    val strategyId: String,
    val parameters: Map<String, Any> = emptyMap()
)

enum class SignalType {
    BUY,
    SELL,
    HOLD
}

// 机器学习预测
data class MLPrediction(
    val symbol: String,
    val predictionType: PredictionType,
    val value: Double,
    val confidence: Double,  // 0.0 - 1.0
    val timestamp: Long,
    val modelVersion: String
)

enum class PredictionType {
    PRICE_DIRECTION,
    VOLATILITY,
    TREND_STRENGTH,
    SUPPORT_RESISTANCE
}