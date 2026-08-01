package com.quanttrade.app.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "交易仪表盘"
    }
    val text: LiveData<String> = _text
    
    // 交易数据
    private val _balance = MutableLiveData<Double>().apply {
        value = 10000.0
    }
    val balance: LiveData<Double> = _balance
    
    private val _equity = MutableLiveData<Double>().apply {
        value = 10000.0
    }
    val equity: LiveData<Double> = _equity
    
    private val _profit = MutableLiveData<Double>().apply {
        value = 0.0
    }
    val profit: LiveData<Double> = _profit
    
    private val _margin = MutableLiveData<Double>().apply {
        value = 0.0
    }
    val margin: LiveData<Double> = _margin
    
    private val _freeMargin = MutableLiveData<Double>().apply {
        value = 10000.0
    }
    val freeMargin: LiveData<Double> = _freeMargin
    
    // 更新交易数据
    fun updateTradingData(balance: Double, equity: Double, profit: Double, margin: Double, freeMargin: Double) {
        _balance.value = balance
        _equity.value = equity
        _profit.value = profit
        _margin.value = margin
        _freeMargin.value = freeMargin
    }
}