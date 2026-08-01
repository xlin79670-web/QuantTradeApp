package com.quanttrade.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        TradingPair::class,
        KlineData::class,
        Order::class,
        Trade::class,
        Position::class,
        Balance::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun tradingPairDao(): TradingPairDao
    abstract fun klineDataDao(): KlineDataDao
    abstract fun orderDao(): OrderDao
    abstract fun tradeDao(): TradeDao
    abstract fun positionDao(): PositionDao
    abstract fun balanceDao(): BalanceDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "quant_trade_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}