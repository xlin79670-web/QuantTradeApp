#!/usr/bin/env python3
"""
量化交易机器学习模型训练脚本
用于生成预训练的交易策略模型
"""

import numpy as np
import pandas as pd
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, classification_report
import joblib
import os

class TradingModelTrainer:
    def __init__(self):
        self.model = None
        self.scaler = None
        
    def generate_mock_data(self, num_samples=1000):
        """
        生成模拟交易数据用于训练
        """
        np.random.seed(42)
        
        # 生成价格数据
        prices = [100.0]
        for _ in range(num_samples - 1):
            change = np.random.normal(0, 0.02)  # 2%波动率
            new_price = prices[-1] * (1 + change)
            prices.append(new_price)
        
        prices = np.array(prices)
        
        # 计算技术指标
        data = pd.DataFrame()
        data['price'] = prices
        data['returns'] = data['price'].pct_change()
        
        # 移动平均线
        data['sma_10'] = data['price'].rolling(window=10).mean()
        data['sma_20'] = data['price'].rolling(window=20).mean()
        data['sma_50'] = data['price'].rolling(window=50).mean()
        
        # RSI
        delta = data['price'].diff()
        gain = (delta.where(delta > 0, 0)).rolling(window=14).mean()
        loss = (-delta.where(delta < 0, 0)).rolling(window=14).mean()
        rs = gain / loss
        data['rsi'] = 100 - (100 / (1 + rs))
        
        # 布林带
        data['bb_middle'] = data['price'].rolling(window=20).mean()
        bb_std = data['price'].rolling(window=20).std()
        data['bb_upper'] = data['bb_middle'] + 2 * bb_std
        data['bb_lower'] = data['bb_middle'] - 2 * bb_std
        
        # MACD
        exp1 = data['price'].ewm(span=12, adjust=False).mean()
        exp2 = data['price'].ewm(span=26, adjust=False).mean()
        data['macd'] = exp1 - exp2
        data['macd_signal'] = data['macd'].ewm(span=9, adjust=False).mean()
        data['macd_histogram'] = data['macd'] - data['macd_signal']
        
        # 成交量指标（模拟）
        data['volume'] = np.random.randint(1000, 10000, num_samples)
        data['volume_sma'] = data['volume'].rolling(window=20).mean()
        
        # 生成标签（交易信号）
        # 1: 买入, 0: 持有, -1: 卖出
        data['signal'] = 0
        
        # 简单的交易策略作为标签
        for i in range(50, num_samples):
            current_price = data['price'].iloc[i]
            sma_10 = data['sma_10'].iloc[i]
            sma_20 = data['sma_20'].iloc[i]
            rsi = data['rsi'].iloc[i]
            
            # 买入条件：价格在均线上方，RSI超卖后回升
            if (current_price > sma_10 > sma_20 and 
                rsi < 30 and 
                data['rsi'].iloc[i-1] < rsi):
                data.loc[data.index[i], 'signal'] = 1
            
            # 卖出条件：价格在均线下方，RSI超买后回落
            elif (current_price < sma_10 < sma_20 and 
                  rsi > 70 and 
                  data['rsi'].iloc[i-1] > rsi):
                data.loc[data.index[i], 'signal'] = -1
        
        # 删除NaN值
        data = data.dropna()
        
        return data
    
    def prepare_features(self, data):
        """
        准备特征数据
        """
        features = [
            'returns', 'sma_10', 'sma_20', 'sma_50', 'rsi',
            'bb_upper', 'bb_lower', 'macd', 'macd_signal', 'macd_histogram',
            'volume', 'volume_sma'
        ]
        
        X = data[features].values
        y = data['signal'].values
        
        return X, y
    
    def train_model(self, X, y):
        """
        训练机器学习模型
        """
        # 分割训练集和测试集
        X_train, X_test, y_train, y_test = train_test_split(
            X, y, test_size=0.2, random_state=42
        )
        
        # 训练随机森林模型
        self.model = RandomForestClassifier(
            n_estimators=100,
            max_depth=10,
            random_state=42,
            n_jobs=-1
        )
        
        self.model.fit(X_train, y_train)
        
        # 评估模型
        y_pred = self.model.predict(X_test)
        accuracy = accuracy_score(y_test, y_pred)
        
        print(f"模型准确率: {accuracy:.4f}")
        print("\n分类报告:")
        print(classification_report(y_test, y_pred))
        
        return accuracy
    
    def save_model(self, model_path):
        """
        保存模型到文件
        """
        if self.model is not None:
            joblib.dump(self.model, model_path)
            print(f"模型已保存到: {model_path}")
        else:
            print("没有可保存的模型")
    
    def load_model(self, model_path):
        """
        从文件加载模型
        """
        if os.path.exists(model_path):
            self.model = joblib.load(model_path)
            print(f"模型已从 {model_path} 加载")
            return True
        else:
            print(f"模型文件不存在: {model_path}")
            return False
    
    def predict(self, features):
        """
        使用模型进行预测
        """
        if self.model is None:
            raise ValueError("模型未训练或未加载")
        
        prediction = self.model.predict(features.reshape(1, -1))
        probability = self.model.predict_proba(features.reshape(1, -1))
        
        return prediction[0], probability[0]

def main():
    # 创建训练器
    trainer = TradingModelTrainer()
    
    # 生成模拟数据
    print("生成模拟交易数据...")
    data = trainer.generate_mock_data(num_samples=2000)
    
    # 准备特征
    print("准备特征数据...")
    X, y = trainer.prepare_features(data)
    
    # 训练模型
    print("训练机器学习模型...")
    accuracy = trainer.train_model(X, y)
    
    # 保存模型
    model_dir = os.path.dirname(os.path.abspath(__file__))
    model_path = os.path.join(model_dir, "trading_model.joblib")
    trainer.save_model(model_path)
    
    # 测试预测
    print("\n测试预测:")
    test_features = X[-1]  # 使用最后一个样本测试
    prediction, probability = trainer.predict(test_features)
    
    signal_names = {1: "买入", 0: "持有", -1: "卖出"}
    print(f"预测信号: {signal_names.get(prediction, '未知')}")
    print(f"预测概率: {probability}")
    
    print(f"\n模型训练完成，准确率: {accuracy:.2%}")
    print(f"模型文件: {model_path}")

if __name__ == "__main__":
    main()