package com.quanttrade.app.data

import androidx.room.*

@Dao
interface TradingPairDao {
    
    @Query("SELECT * FROM trading_pairs")
    suspend fun getAllTradingPairs(): List<TradingPair>
    
    @Query("SELECT * FROM trading_pairs WHERE symbol = :symbol")
    suspend fun getTradingPairBySymbol(symbol: String): TradingPair?
    
    @Query("SELECT * FROM trading_pairs WHERE isActive = 1")
    suspend fun getActiveTradingPairs(): List<TradingPair>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTradingPair(tradingPair: TradingPair)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTradingPairs(tradingPairs: List<TradingPair>)
    
    @Update
    suspend fun updateTradingPair(tradingPair: TradingPair)
    
    @Delete
    suspend fun deleteTradingPair(tradingPair: TradingPair)
    
    @Query("DELETE FROM trading_pairs")
    suspend fun deleteAllTradingPairs()
}