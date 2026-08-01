package com.quanttrade.app

import android.app.Application
import com.quanttrade.app.data.AppDatabase
import com.quanttrade.app.data.TradingRepository
import com.quanttrade.app.ml.StrategyEngine
import com.quanttrade.app.ml.GeneticAlgorithmOptimizer
import com.quanttrade.app.ml.BacktestEngine
import com.quanttrade.app.network.ApiKeyManager
import com.quanttrade.app.network.ConnectionManager
import com.quanttrade.app.network.WebSocketManager

class QuantTradeApplication : Application() {
    
    lateinit var database: AppDatabase
        private set
    
    lateinit var tradingRepository: TradingRepository
        private set
    
    lateinit var strategyEngine: StrategyEngine
        private set
    
    lateinit var geneticAlgorithmOptimizer: GeneticAlgorithmOptimizer
        private set
    
    lateinit var backtestEngine: BacktestEngine
        private set
    
    lateinit var apiKeyManager: ApiKeyManager
        private set
    
    lateinit var connectionManager: ConnectionManager
        private set
    
    lateinit var webSocketManager: WebSocketManager
        private set
    
    override fun onCreate() {
        super.onCreate()
        
        // 初始化数据库
        database = AppDatabase.getDatabase(this)
        
        // 初始化Repository
        tradingRepository = TradingRepository(this)
        
        // 初始化机器学习引擎
        strategyEngine = StrategyEngine()
        geneticAlgorithmOptimizer = GeneticAlgorithmOptimizer()
        backtestEngine = BacktestEngine()
        
        // 初始化网络组件
        apiKeyManager = ApiKeyManager(this)
        connectionManager = ConnectionManager()
        webSocketManager = WebSocketManager()
        
        println("量化交易应用初始化完成")
    }
    
    override fun onTerminate() {
        super.onTerminate()
        
        // 清理资源
        webSocketManager.disconnect()
        connectionManager.disconnectFromBinance()
        connectionManager.disconnectFromMT4()
        
        println("量化交易应用已终止")
    }
}