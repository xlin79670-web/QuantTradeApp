package com.quanttrade.app.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    
    private const val BINANCE_BASE_URL = "https://api.binance.com"
    private const val BINANCE_TESTNET_URL = "https://testnet.binance.vision"
    private const val MT4_BASE_URL = "http://localhost:8080"  // 本地MT4/5桥接服务器
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val binanceClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    private val mt4Client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()
    
    val binanceApi: BinanceApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BINANCE_BASE_URL)
            .client(binanceClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BinanceApiService::class.java)
    }
    
    val binanceTestnetApi: BinanceApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BINANCE_TESTNET_URL)
            .client(binanceClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BinanceApiService::class.java)
    }
    
    val mt4Api: MT4ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(MT4_BASE_URL)
            .client(mt4Client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MT4ApiService::class.java)
    }
}