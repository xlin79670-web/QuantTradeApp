package com.quanttrade.app.data

import androidx.room.*

@Dao
interface OrderDao {
    
    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    suspend fun getAllOrders(): List<Order>
    
    @Query("SELECT * FROM orders WHERE orderId = :orderId")
    suspend fun getOrderById(orderId: String): Order?
    
    @Query("SELECT * FROM orders WHERE symbol = :symbol ORDER BY createdAt DESC")
    suspend fun getOrdersBySymbol(symbol: String): List<Order>
    
    @Query("SELECT * FROM orders WHERE status = :status ORDER BY createdAt DESC")
    suspend fun getOrdersByStatus(status: OrderStatus): List<Order>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrders(orders: List<Order>)
    
    @Update
    suspend fun updateOrder(order: Order)
    
    @Delete
    suspend fun deleteOrder(order: Order)
    
    @Query("DELETE FROM orders")
    suspend fun deleteAllOrders()
    
    @Query("UPDATE orders SET status = :status WHERE orderId = :orderId")
    suspend fun updateOrderStatus(orderId: String, status: OrderStatus)
}