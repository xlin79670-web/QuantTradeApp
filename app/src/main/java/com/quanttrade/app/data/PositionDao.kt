package com.quanttrade.app.data

import androidx.room.*

@Dao
interface PositionDao {
    
    @Query("SELECT * FROM positions")
    suspend fun getAllPositions(): List<Position>
    
    @Query("SELECT * FROM positions WHERE symbol = :symbol")
    suspend fun getPositionBySymbol(symbol: String): Position?
    
    @Query("SELECT * FROM positions WHERE side = :side")
    suspend fun getPositionsBySide(side: PositionSide): List<Position>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosition(position: Position)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPositions(positions: List<Position>)
    
    @Update
    suspend fun updatePosition(position: Position)
    
    @Delete
    suspend fun deletePosition(position: Position)
    
    @Query("DELETE FROM positions")
    suspend fun deleteAllPositions()
    
    @Query("SELECT SUM(unrealizedProfit) FROM positions")
    suspend fun getTotalUnrealizedProfit(): Double?
}