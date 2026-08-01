package com.quanttrade.app.ml

import com.quanttrade.app.data.*
import java.text.SimpleDateFormat
import java.util.*

class BacktestEngine {
    
    // 回测结果
    data class BacktestResult(
        val totalReturn: Double,
        val annualizedReturn: Double,
        val sharpeRatio: Double,
        val maxDrawdown: Double,
        val winRate: Double,
        val profitFactor: Double,
        val totalTrades: Int,
        val winningTrades: Int,
        val losingTrades: Int,
        val averageWin: Double,
        val averageLoss: Double,
        val equityCurve: List<Double>,
        val drawdownCurve: List<Double>,
        val trades: List<BacktestTrade>
    )
    
    // 回测交易记录
    data class BacktestTrade(
        val symbol: String,
        val side: OrderSide,
        val entryTime: Long,
        val exitTime: Long,
        val entryPrice: Double,
        val exitPrice: Double,
        val quantity: Double,
        val profit: Double,
        val profitPercentage: Double,
        val holdingPeriod: Long
    )
    
    // 执行回测
    fun runBacktest(
        strategy: Strategy,
        historicalData: List<KlineData>,
        initialCapital: Double = 10000.0,
        commissionRate: Double = 0.001,
        slippage: Double = 0.0005
    ): BacktestResult {
        val strategyEngine = StrategyEngine()
        strategyEngine.startStrategy(strategy)
        
        var capital = initialCapital
        var position = 0.0
        var entryPrice = 0.0
        var entryTime = 0L
        
        val trades = mutableListOf<BacktestTrade>()
        val equityCurve = mutableListOf(initialCapital)
        val drawdownCurve = mutableListOf(0.0)
        var peakEquity = initialCapital
        
        // 遍历历史数据
        for (i in 20 until historicalData.size) {
            val currentKline = historicalData[i]
            val previousKlines = historicalData.subList(0, i)
            
            // 生成交易信号
            val signal = strategyEngine.generateSignal(previousKlines, currentKline.close)
            
            when (signal?.signalType) {
                SignalType.BUY -> {
                    // 如果没有持仓，则开多仓
                    if (position == 0.0) {
                        val adjustedPrice = currentKline.close * (1 + slippage)
                        val commission = capital * commissionRate
                        val investableCapital = capital - commission
                        position = investableCapital / adjustedPrice
                        entryPrice = adjustedPrice
                        entryTime = currentKline.openTime
                        capital = 0.0
                    }
                }
                SignalType.SELL -> {
                    // 如果有持仓，则平仓
                    if (position > 0.0) {
                        val adjustedPrice = currentKline.close * (1 - slippage)
                        val exitValue = position * adjustedPrice
                        val commission = exitValue * commissionRate
                        capital = exitValue - commission
                        
                        val profit = capital - (position * entryPrice)
                        val profitPercentage = (adjustedPrice - entryPrice) / entryPrice * 100
                        val holdingPeriod = currentKline.openTime - entryTime
                        
                        trades.add(
                            BacktestTrade(
                                symbol = currentKline.symbol,
                                side = OrderSide.BUY,
                                entryTime = entryTime,
                                exitTime = currentKline.openTime,
                                entryPrice = entryPrice,
                                exitPrice = adjustedPrice,
                                quantity = position,
                                profit = profit,
                                profitPercentage = profitPercentage,
                                holdingPeriod = holdingPeriod
                            )
                        )
                        
                        position = 0.0
                        entryPrice = 0.0
                    }
                }
                else -> { /* HOLD */ }
            }
            
            // 计算当前权益
            val currentEquity = if (position > 0.0) {
                position * currentKline.close
            } else {
                capital
            }
            
            equityCurve.add(currentEquity)
            
            // 计算回撤
            if (currentEquity > peakEquity) {
                peakEquity = currentEquity
            }
            val drawdown = (peakEquity - currentEquity) / peakEquity * 100
            drawdownCurve.add(drawdown)
        }
        
        // 如果最后还有持仓，则平仓
        if (position > 0.0) {
            val lastKline = historicalData.last()
            val adjustedPrice = lastKline.close * (1 - slippage)
            val exitValue = position * adjustedPrice
            val commission = exitValue * commissionRate
            capital = exitValue - commission
            
            val profit = capital - (position * entryPrice)
            val profitPercentage = (adjustedPrice - entryPrice) / entryPrice * 100
            val holdingPeriod = lastKline.openTime - entryTime
            
            trades.add(
                BacktestTrade(
                    symbol = lastKline.symbol,
                    side = OrderSide.BUY,
                    entryTime = entryTime,
                    exitTime = lastKline.openTime,
                    entryPrice = entryPrice,
                    exitPrice = adjustedPrice,
                    quantity = position,
                    profit = profit,
                    profitPercentage = profitPercentage,
                    holdingPeriod = holdingPeriod
                )
            )
        }
        
        // 计算回测统计
        return calculateBacktestStats(equityCurve, drawdownCurve, trades, initialCapital)
    }
    
    // 计算回测统计
    private fun calculateBacktestStats(
        equityCurve: List<Double>,
        drawdownCurve: List<Double>,
        trades: List<BacktestTrade>,
        initialCapital: Double
    ): BacktestResult {
        val finalEquity = equityCurve.last()
        val totalReturn = (finalEquity - initialCapital) / initialCapital * 100
        
        // 计算年化收益率（假设365天）
        val totalDays = equityCurve.size
        val annualizedReturn = if (totalDays > 0) {
            Math.pow(finalEquity / initialCapital, 365.0 / totalDays) - 1
        } else {
            0.0
        }
        
        // 计算夏普比率
        val returns = equityCurve.zipWithNext().map { (prev, curr) ->
            (curr - prev) / prev
        }
        val averageReturn = if (returns.isNotEmpty()) returns.average() else 0.0
        val variance = if (returns.isNotEmpty()) {
            returns.map { (it - averageReturn) * (it - averageReturn) }.average()
        } else {
            0.0
        }
        val standardDeviation = Math.sqrt(variance)
        val sharpeRatio = if (standardDeviation > 0) {
            (averageReturn - 0.02) / standardDeviation // 假设无风险利率为2%
        } else {
            0.0
        }
        
        // 计算最大回撤
        val maxDrawdown = if (drawdownCurve.isNotEmpty()) drawdownCurve.max() else 0.0
        
        // 计算胜率
        val winningTrades = trades.filter { it.profit > 0 }
        val losingTrades = trades.filter { it.profit <= 0 }
        val winRate = if (trades.isNotEmpty()) {
            winningTrades.size.toDouble() / trades.size * 100
        } else {
            0.0
        }
        
        // 计算盈亏比
        val totalProfit = winningTrades.sumOf { it.profit }
        val totalLoss = Math.abs(losingTrades.sumOf { it.profit })
        val profitFactor = if (totalLoss > 0) totalProfit / totalLoss else Double.MAX_VALUE
        
        // 计算平均盈亏
        val averageWin = if (winningTrades.isNotEmpty()) {
            winningTrades.map { it.profitPercentage }.average()
        } else {
            0.0
        }
        
        val averageLoss = if (losingTrades.isNotEmpty()) {
            losingTrades.map { it.profitPercentage }.average()
        } else {
            0.0
        }
        
        return BacktestResult(
            totalReturn = totalReturn,
            annualizedReturn = annualizedReturn * 100,
            sharpeRatio = sharpeRatio,
            maxDrawdown = maxDrawdown,
            winRate = winRate,
            profitFactor = profitFactor,
            totalTrades = trades.size,
            winningTrades = winningTrades.size,
            losingTrades = losingTrades.size,
            averageWin = averageWin,
            averageLoss = averageLoss,
            equityCurve = equityCurve,
            drawdownCurve = drawdownCurve,
            trades = trades
        )
    }
    
    // 生成模拟历史数据
    fun generateMockHistoricalData(
        symbol: String,
        days: Int = 365,
        startPrice: Double = 100.0,
        volatility: Double = 0.02
    ): List<KlineData> {
        val klines = mutableListOf<KlineData>()
        var currentPrice = startPrice
        val random = Random()
        
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -days)
        
        for (day in 0 until days) {
            // 生成日内K线（假设每天24根1小时K线）
            for (hour in 0 until 24) {
                val openTime = calendar.timeInMillis
                
                // 生成价格变动
                val priceChange = currentPrice * volatility * (random.nextGaussian())
                val open = currentPrice
                val close = currentPrice + priceChange
                val high = maxOf(open, close) * (1 + random.nextDouble() * volatility / 2)
                val low = minOf(open, close) * (1 - random.nextDouble() * volatility / 2)
                val volume = random.nextDouble() * 1000 + 100
                
                klines.add(
                    KlineData(
                        symbol = symbol,
                        interval = "1h",
                        openTime = openTime,
                        closeTime = openTime + 3600000,
                        open = open,
                        high = high,
                        low = low,
                        close = close,
                        volume = volume,
                        quoteVolume = volume * close,
                        trades = random.nextInt(100, 1000)
                    )
                )
                
                currentPrice = close
                calendar.add(Calendar.HOUR_OF_DAY, 1)
            }
        }
        
        return klines
    }
}