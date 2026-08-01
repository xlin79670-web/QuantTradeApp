package com.quanttrade.app.data

import androidx.room.*

@Dao
interface TradeDao {
    
    @Query("SELECT * FROM trades ORDER BY time DESC")
    suspend fun getAllTrades(): List<Trade>
    
    @Query("SELECT * FROM trades WHERE tradeId = :tradeId")
    suspend fun getTradeById(tradeId: String): Trade?
    
    @Query("SELECT * FROM trades WHERE symbol = :symbol ORDER BY time DESC")
    suspend fun getTradesBySymbol(symbol: String): List<Trade>
    
    @Query("SELECT * FROM trades WHERE orderId = :orderId ORDER BY time DESC")
    suspend fun getTradesByOrderId(orderId: String): List<Trade>
    
    @Query("SELECT * FROM trades WHERE time >= :startTime AND time <= :endTime ORDER BY time DESC")
    suspend fun getTradesByTimeRange(startTime: Long, endTime: Long): List<Trade>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrade(trade: Trade)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrades(trades: List<Trade>)
    
    @Update
    suspend fun updateTrade(trade: Trade)
    
    @Delete
    suspend fun deleteTrade(trade: Trade)
    
    @Query("DELETE FROM trades")
    suspend fun deleteAllTrades()
    
    @Query("SELECT SUM(commission) FROM trades WHERE time >= :startTime AND time <= :endTime")
    suspend fun getTotalCommission(startTime: Long, endTime: Long): Double?
}