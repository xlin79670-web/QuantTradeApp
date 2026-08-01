package com.quanttrade.app.ui.strategy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StrategyViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "策略配置"
    }
    val text: LiveData<String> = _text
    
    // 策略列表
    private val _strategies = MutableLiveData<List<Strategy>>().apply {
        value = emptyList()
    }
    val strategies: LiveData<List<Strategy>> = _strategies
    
    // 当前选中的策略
    private val _selectedStrategy = MutableLiveData<Strategy?>()
    val selectedStrategy: LiveData<Strategy?> = _selectedStrategy
    
    // 添加策略
    fun addStrategy(strategy: Strategy) {
        val currentList = _strategies.value?.toMutableList() ?: mutableListOf()
        currentList.add(strategy)
        _strategies.value = currentList
    }
    
    // 删除策略
    fun removeStrategy(strategyId: String) {
        val currentList = _strategies.value?.toMutableList() ?: mutableListOf()
        currentList.removeAll { it.id == strategyId }
        _strategies.value = currentList
        
        // 如果删除的是当前选中的策略，则清空选择
        if (_selectedStrategy.value?.id == strategyId) {
            _selectedStrategy.value = null
        }
    }
    
    // 选择策略
    fun selectStrategy(strategyId: String) {
        val strategy = _strategies.value?.find { it.id == strategyId }
        _selectedStrategy.value = strategy
    }
    
    // 更新策略参数
    fun updateStrategyParameters(strategyId: String, parameters: Map<String, Any>) {
        val currentList = _strategies.value?.toMutableList() ?: mutableListOf()
        val index = currentList.indexOfFirst { it.id == strategyId }
        if (index != -1) {
            currentList[index] = currentList[index].copy(parameters = parameters)
            _strategies.value = currentList
            
            // 如果更新的是当前选中的策略，则更新选择
            if (_selectedStrategy.value?.id == strategyId) {
                _selectedStrategy.value = currentList[index]
            }
        }
    }
}

data class Strategy(
    val id: String,
    val name: String,
    val type: StrategyType,
    val parameters: Map<String, Any> = emptyMap(),
    val isActive: Boolean = false,
    val performance: StrategyPerformance = StrategyPerformance()
)

enum class StrategyType {
    TREND_FOLLOWING,
    MEAN_REVERSION,
    ARBITRAGE,
    MACHINE_LEARNING,
    REINFORCEMENT_LEARNING
}

data class StrategyPerformance(
    val totalReturn: Double = 0.0,
    val sharpeRatio: Double = 0.0,
    val maxDrawdown: Double = 0.0,
    val winRate: Double = 0.0,
    val profitFactor: Double = 0.0
)