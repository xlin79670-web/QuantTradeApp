package com.quanttrade.app.ml

import com.quanttrade.app.data.Strategy
import com.quanttrade.app.data.StrategyPerformance
import kotlin.random.Random

class GeneticAlgorithmOptimizer {
    
    // 遗传算法参数
    private val populationSize = 50
    private val generations = 100
    private val mutationRate = 0.1
    private val crossoverRate = 0.7
    private val elitismCount = 2
    
    // 策略参数范围
    data class ParameterRange(
        val name: String,
        val min: Double,
        val max: Double,
        val isInt: Boolean = false
    )
    
    // 个体
    data class Individual(
        val parameters: Map<String, Double>,
        var fitness: Double = 0.0
    )
    
    // 优化结果
    data class OptimizationResult(
        val bestParameters: Map<String, Double>,
        val bestFitness: Double,
        val generations: Int,
        val convergenceHistory: List<Double>
    )
    
    // 优化策略参数
    fun optimizeStrategy(
        baseStrategy: Strategy,
        parameterRanges: List<ParameterRange>,
        fitnessFunction: (Map<String, Double>) -> Double
    ): OptimizationResult {
        // 初始化种群
        var population = initializePopulation(parameterRanges)
        val convergenceHistory = mutableListOf<Double>()
        
        // 迭代优化
        for (generation in 0 until generations) {
            // 评估适应度
            population.forEach { individual ->
                individual.fitness = fitnessFunction(individual.parameters)
            }
            
            // 按适应度排序
            population = population.sortedByDescending { it.fitness }
            
            // 记录收敛历史
            convergenceHistory.add(population.first().fitness)
            
            // 选择
            val selected = selection(population)
            
            // 交叉和变异
            val offspring = crossoverAndMutation(selected, parameterRanges)
            
            // 精英保留
            val elitism = population.take(elitismCount)
            
            // 形成新一代种群
            population = elitism + offspring.take(populationSize - elitismCount)
        }
        
        // 最终评估
        population.forEach { individual ->
            individual.fitness = fitnessFunction(individual.parameters)
        }
        
        val bestIndividual = population.maxByOrNull { it.fitness } ?: population.first()
        
        return OptimizationResult(
            bestParameters = bestIndividual.parameters,
            bestFitness = bestIndividual.fitness,
            generations = generations,
            convergenceHistory = convergenceHistory
        )
    }
    
    // 初始化种群
    private fun initializePopulation(parameterRanges: List<ParameterRange>): List<Individual> {
        return (1..populationSize).map { _ ->
            val parameters = parameterRanges.associate { range ->
                val value = Random.nextDouble(range.min, range.max)
                val clampedValue = value.coerceIn(range.min, range.max)
                range.name to if (range.isInt) clampedValue.toInt().toDouble() else clampedValue
            }
            Individual(parameters)
        }
    }
    
    // 选择操作（锦标赛选择）
    private fun selection(population: List<Individual>): List<Individual> {
        val selected = mutableListOf<Individual>()
        
        for (i in 0 until populationSize) {
            val tournamentSize = 3
            val tournament = population.shuffled().take(tournamentSize)
            val winner = tournament.maxByOrNull { it.fitness } ?: tournament.first()
            selected.add(winner.copy())
        }
        
        return selected
    }
    
    // 交叉和变异操作
    private fun crossoverAndMutation(
        population: List<Individual>,
        parameterRanges: List<ParameterRange>
    ): List<Individual> {
        val offspring = mutableListOf<Individual>()
        
        for (i in population.indices step 2) {
            val parent1 = population[i]
            val parent2 = if (i + 1 < population.size) population[i + 1] else population[0]
            
            // 交叉
            val (child1Params, child2Params) = if (Random.nextDouble() < crossoverRate) {
                crossover(parent1.parameters, parent2.parameters, parameterRanges)
            } else {
                Pair(parent1.parameters.toMutableMap(), parent2.parameters.toMutableMap())
            }
            
            // 变异
            val child1ParamsMutated = mutate(child1Params, parameterRanges)
            val child2ParamsMutated = mutate(child2Params, parameterRanges)
            
            offspring.add(Individual(child1ParamsMutated))
            if (offspring.size < populationSize) {
                offspring.add(Individual(child2ParamsMutated))
            }
        }
        
        return offspring
    }
    
    // 交叉操作（单点交叉）
    private fun crossover(
        params1: Map<String, Double>,
        params2: Map<String, Double>,
        parameterRanges: List<ParameterRange>
    ): Pair<MutableMap<String, Double>, MutableMap<String, Double>> {
        val child1 = mutableMapOf<String, Double>()
        val child2 = mutableMapOf<String, Double>()
        
        val paramNames = parameterRanges.map { it.name }
        val crossoverPoint = Random.nextInt(1, paramNames.size)
        
        for (i in paramNames.indices) {
            val paramName = paramNames[i]
            if (i < crossoverPoint) {
                child1[paramName] = params1[paramName] ?: 0.0
                child2[paramName] = params2[paramName] ?: 0.0
            } else {
                child1[paramName] = params2[paramName] ?: 0.0
                child2[paramName] = params1[paramName] ?: 0.0
            }
        }
        
        return Pair(child1, child2)
    }
    
    // 变异操作
    private fun mutate(
        params: MutableMap<String, Double>,
        parameterRanges: List<ParameterRange>
    ): MutableMap<String, Double> {
        val mutatedParams = params.toMutableMap()
        
        for (range in parameterRanges) {
            if (Random.nextDouble() < mutationRate) {
                val currentValue = mutatedParams[range.name] ?: range.min
                val mutation = Random.nextDouble(-0.1, 0.1) * (range.max - range.min)
                val newValue = (currentValue + mutation).coerceIn(range.min, range.max)
                mutatedParams[range.name] = if (range.isInt) newValue.toInt().toDouble() else newValue
            }
        }
        
        return mutatedParams
    }
    
    // 适应度函数（夏普比率）
    fun calculateFitness(
        returns: List<Double>,
        riskFreeRate: Double = 0.02
    ): Double {
        if (returns.isEmpty()) return 0.0
        
        val averageReturn = returns.average()
        val variance = returns.map { (it - averageReturn) * (it - averageReturn) }.average()
        val standardDeviation = Math.sqrt(variance)
        
        if (standardDeviation == 0.0) return 0.0
        
        val sharpeRatio = (averageReturn - riskFreeRate) / standardDeviation
        return sharpeRatio
    }
    
    // 适应度函数（考虑最大回撤）
    fun calculateFitnessWithDrawdown(
        returns: List<Double>,
        riskFreeRate: Double = 0.02,
        maxDrawdownPenalty: Double = 0.5
    ): Double {
        if (returns.isEmpty()) return 0.0
        
        val averageReturn = returns.average()
        val variance = returns.map { (it - averageReturn) * (it - averageReturn) }.average()
        val standardDeviation = Math.sqrt(variance)
        
        if (standardDeviation == 0.0) return 0.0
        
        val sharpeRatio = (averageReturn - riskFreeRate) / standardDeviation
        
        // 计算最大回撤
        var peak = 1.0
        var maxDrawdown = 0.0
        var cumulative = 1.0
        
        for (returnVal in returns) {
            cumulative *= (1 + returnVal)
            if (cumulative > peak) {
                peak = cumulative
            }
            val drawdown = (peak - cumulative) / peak
            if (drawdown > maxDrawdown) {
                maxDrawdown = drawdown
            }
        }
        
        // 调整适应度（回撤越大，惩罚越重）
        val drawdownPenalty = maxDrawdown * maxDrawdownPenalty
        return sharpeRatio - drawdownPenalty
    }
    
    // 生成默认参数范围
    fun getDefaultParameterRanges(): List<ParameterRange> {
        return listOf(
            ParameterRange("fast_period", 5.0, 50.0, true),
            ParameterRange("slow_period", 20.0, 200.0, true),
            ParameterRange("stop_loss", 1.0, 10.0),
            ParameterRange("take_profit", 2.0, 20.0),
            ParameterRange("position_size", 0.01, 0.1),
            ParameterRange("rsi_period", 10.0, 30.0, true),
            ParameterRange("rsi_overbought", 60.0, 90.0),
            ParameterRange("rsi_oversold", 10.0, 40.0)
        )
    }
}