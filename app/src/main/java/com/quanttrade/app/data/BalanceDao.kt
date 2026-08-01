package com.quanttrade.app.data

import androidx.room.*

@Dao
interface BalanceDao {
    
    @Query("SELECT * FROM balances")
    suspend fun getAllBalances(): List<Balance>
    
    @Query("SELECT * FROM balances WHERE asset = :asset")
    suspend fun getBalanceByAsset(asset: String): Balance?
    
    @Query("SELECT * FROM balances WHERE free > 0")
    suspend fun getNonZeroBalances(): List<Balance>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBalance(balance: Balance)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBalances(balances: List<Balance>)
    
    @Update
    suspend fun updateBalance(balance: Balance)
    
    @Delete
    suspend fun deleteBalance(balance: Balance)
    
    @Query("DELETE FROM balances")
    suspend fun deleteAllBalances()
    
    @Query("SELECT SUM(total) FROM balances WHERE asset = 'USDT' OR asset = 'USD'")
    suspend fun getTotalUSDBalance(): Double?
}