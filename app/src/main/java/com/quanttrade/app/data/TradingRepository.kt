package com.quanttrade.app.data

import android.content.Context
import com.quanttrade.app.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TradingRepository(context: Context) {
    
    private val database = AppDatabase.getDatabase(context)
    private val tradingPairDao = database.tradingPairDao()
    private val klineDataDao = database.klineDataDao()
    private val orderDao = database.orderDao()
    private val tradeDao = database.tradeDao()
    private val positionDao = database.positionDao()
    private val balanceDao = database.balanceDao()
    
    private val binanceApi = ApiClient.binanceApi
    private val mt4Api = ApiClient.mt4Api
    
    // 交易对操作
    suspend fun getAllTradingPairs(): List<TradingPair> = withContext(Dispatchers.IO) {
        tradingPairDao.getAllTradingPairs()
    }
    
    suspend fun getActiveTradingPairs(): List<TradingPair> = withContext(Dispatchers.IO) {
        tradingPairDao.getActiveTradingPairs()
    }
    
    suspend fun syncTradingPairsFromBinance() = withContext(Dispatchers.IO) {
        try {
            val response = binanceApi.getExchangeInfo()
            if (response.isSuccessful) {
                val exchangeInfo = response.body()
                val tradingPairs = exchangeInfo?.symbols?.map { symbolInfo ->
                    TradingPair(
                        symbol = symbolInfo.symbol,
                        baseCurrency = symbolInfo.baseAsset,
                        quoteCurrency = symbolInfo.quoteAsset,
                        minTradeAmount = symbolInfo.filters.find { it.filterType == "LOT_SIZE" }?.minQty ?: 0.001,
                        maxTradeAmount = symbolInfo.filters.find { it.filterType == "LOT_SIZE" }?.maxQty ?: 1000000.0,
                        tickSize = symbolInfo.filters.find { it.filterType == "PRICE_FILTER" }?.tickSize ?: 0.01,
                        isActive = true
                    )
                } ?: emptyList()
                
                tradingPairDao.insertTradingPairs(tradingPairs)
            }
        } catch (e: Exception) {
            // 处理网络错误
            e.printStackTrace()
        }
    }
    
    // K线数据操作
    suspend fun getKlineData(symbol: String, interval: String, limit: Int = 500): List<KlineData> = withContext(Dispatchers.IO) {
        klineDataDao.getKlineData(symbol, interval, limit)
    }
    
    suspend fun syncKlineDataFromBinance(symbol: String, interval: String, limit: Int = 500) = withContext(Dispatchers.IO) {
        try {
            val response = binanceApi.getKlines(symbol, interval, limit)
            if (response.isSuccessful) {
                val klineDataList = response.body()?.map { klineArray ->
                    KlineData(
                        symbol = symbol,
                        interval = interval,
                        openTime = (klineArray[0] as Number).toLong(),
                        closeTime = (klineArray[6] as Number).toLong(),
                        open = (klineArray[1] as String).toDouble(),
                        high = (klineArray[2] as String).toDouble(),
                        low = (klineArray[3] as String).toDouble(),
                        close = (klineArray[4] as String).toDouble(),
                        volume = (klineArray[5] as String).toDouble(),
                        quoteVolume = (klineArray[7] as String).toDouble(),
                        trades = (klineArray[8] as Number).toInt()
                    )
                } ?: emptyList()
                
                klineDataDao.insertKlineDataList(klineDataList)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    // 订单操作
    suspend fun getAllOrders(): List<Order> = withContext(Dispatchers.IO) {
        orderDao.getAllOrders()
    }
    
    suspend fun placeOrder(order: Order): Boolean = withContext(Dispatchers.IO) {
        try {
            // 先保存到本地数据库
            orderDao.insertOrder(order)
            
            // 然后发送到交易所
            val response = binanceApi.placeOrder(
                symbol = order.symbol,
                side = order.side.name,
                type = order.type.name,
                quantity = order.quantity,
                price = order.price,
                stopPrice = order.stopPrice
            )
            
            if (response.isSuccessful) {
                val orderResponse = response.body()
                // 更新订单状态
                orderDao.updateOrderStatus(order.orderId, OrderStatus.NEW)
                true
            } else {
                // 如果发送失败，更新状态为拒绝
                orderDao.updateOrderStatus(order.orderId, OrderStatus.REJECTED)
                false
            }
        } catch (e: Exception) {
            orderDao.updateOrderStatus(order.orderId, OrderStatus.REJECTED)
            e.printStackTrace()
            false
        }
    }
    
    suspend fun cancelOrder(orderId: String, symbol: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = binanceApi.cancelOrder(symbol, orderId.toLong())
            if (response.isSuccessful) {
                orderDao.updateOrderStatus(orderId, OrderStatus.CANCELED)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    // 交易记录操作
    suspend fun getAllTrades(): List<Trade> = withContext(Dispatchers.IO) {
        tradeDao.getAllTrades()
    }
    
    suspend fun syncTradesFromBinance(symbol: String) = withContext(Dispatchers.IO) {
        try {
            val response = binanceApi.getMyTrades(symbol)
            if (response.isSuccessful) {
                val trades = response.body()?.map { tradeHistory ->
                    Trade(
                        tradeId = tradeHistory.id.toString(),
                        orderId = tradeHistory.orderId.toString(),
                        symbol = tradeHistory.symbol,
                        side = if (tradeHistory.isBuyer) OrderSide.BUY else OrderSide.SELL,
                        quantity = tradeHistory.qty,
                        price = tradeHistory.price,
                        commission = tradeHistory.commission,
                        commissionAsset = tradeHistory.commissionAsset,
                        time = tradeHistory.time,
                        isBuyer = tradeHistory.isBuyer
                    )
                } ?: emptyList()
                
                tradeDao.insertTrades(trades)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    // 持仓操作
    suspend fun getAllPositions(): List<Position> = withContext(Dispatchers.IO) {
        positionDao.getAllPositions()
    }
    
    suspend fun updatePosition(position: Position) = withContext(Dispatchers.IO) {
        positionDao.updatePosition(position)
    }
    
    // 余额操作
    suspend fun getAllBalances(): List<Balance> = withContext(Dispatchers.IO) {
        balanceDao.getAllBalances()
    }
    
    suspend fun syncBalancesFromBinance() = withContext(Dispatchers.IO) {
        try {
            val response = binanceApi.getAccountInfo()
            if (response.isSuccessful) {
                val accountInfo = response.body()
                val balances = accountInfo?.balances?.map { balance ->
                    Balance(
                        asset = balance.asset,
                        free = balance.free,
                        locked = balance.locked,
                        total = balance.free + balance.locked
                    )
                } ?: emptyList()
                
                balanceDao.insertBalances(balances)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    // 获取当前价格
    suspend fun getCurrentPrice(symbol: String): Double? = withContext(Dispatchers.IO) {
        try {
            val response = binanceApi.getPrice(symbol)
            if (response.isSuccessful) {
                response.body()?.price
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}