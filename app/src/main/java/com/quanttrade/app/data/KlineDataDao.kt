package com.quanttrade.app.data

import androidx.room.*

@Dao
interface KlineDataDao {
    
    @Query("SELECT * FROM kline_data WHERE symbol = :symbol AND interval = :interval ORDER BY openTime DESC LIMIT :limit")
    suspend fun getKlineData(symbol: String, interval: String, limit: Int): List<KlineData>
    
    @Query("SELECT * FROM kline_data WHERE symbol = :symbol AND interval = :interval AND openTime >= :startTime AND openTime <= :endTime ORDER BY openTime ASC")
    suspend fun getKlineDataByTimeRange(symbol: String, interval: String, startTime: Long, endTime: Long): List<KlineData>
    
    @Query("SELECT * FROM kline_data WHERE symbol = :symbol AND interval = :interval ORDER BY openTime DESC LIMIT 1")
    suspend fun getLatestKlineData(symbol: String, interval: String): KlineData?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKlineData(klineData: KlineData)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKlineDataList(klineDataList: List<KlineData>)
    
    @Update
    suspend fun updateKlineData(klineData: KlineData)
    
    @Delete
    suspend fun deleteKlineData(klineData: KlineData)
    
    @Query("DELETE FROM kline_data WHERE symbol = :symbol AND interval = :interval")
    suspend fun deleteKlineDataBySymbolAndInterval(symbol: String, interval: String)
    
    @Query("DELETE FROM kline_data")
    suspend fun deleteAllKlineData()
}