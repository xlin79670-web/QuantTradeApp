# 量化交易Android应用

这是一个功能完整的量化交易Android应用，支持MT4/5和币安平台的自动化交易，并集成了机器学习策略优化功能。

## 功能特性

### 1. 多平台支持
- **MT4/5外汇平台**: 支持MetaTrader 4/5平台的自动化交易
- **币安加密货币**: 支持币安交易所的现货和合约交易

### 2. 智能交易策略
- **趋势跟踪**: 基于移动平均线的趋势跟踪策略
- **均值回归**: 基于布林带的均值回归策略
- **机器学习**: 基于TensorFlow Lite的机器学习策略
- **强化学习**: 基于Q-learning的强化学习策略

### 3. 策略优化
- **遗传算法**: 使用遗传算法自动优化策略参数
- **回测系统**: 完整的回测引擎，验证策略有效性
- **性能评估**: 夏普比率、最大回撤、胜率等指标

### 4. 风险控制
- **止损止盈**: 智能止损止盈管理
- **仓位管理**: 动态仓位大小调整
- **风险等级**: 多级风险控制设置

### 5. 实时监控
- **实时价格**: WebSocket实时价格推送
- **交易监控**: 实时交易状态监控
- **性能仪表盘**: 策略表现可视化

## 项目结构

```
QuantTradeApp/
├── app/src/main/
│   ├── java/com/quanttrade/app/
│   │   ├── MainActivity.kt              # 主活动
│   │   ├── QuantTradeApplication.kt     # 应用类
│   │   ├── data/                        # 数据层
│   │   │   ├── AppDatabase.kt          # Room数据库
│   │   │   ├── TradingData.kt          # 数据模型
│   │   │   ├── TradingRepository.kt    # 数据仓库
│   │   │   └── *Dao.kt                 # 数据访问对象
│   │   ├── ml/                          # 机器学习层
│   │   │   ├── StrategyEngine.kt       # 策略引擎
│   │   │   ├── GeneticAlgorithmOptimizer.kt  # 遗传算法优化器
│   │   │   └── BacktestEngine.kt       # 回测引擎
│   │   ├── network/                     # 网络层
│   │   │   ├── ApiService.kt           # API接口
│   │   │   ├── ApiClient.kt            # API客户端
│   │   │   ├── ApiKeyManager.kt        # API密钥管理
│   │   │   ├── ConnectionManager.kt    # 连接管理
│   │   │   └── WebSocketManager.kt     # WebSocket管理
│   │   └── ui/                          # 界面层
│   │       ├── dashboard/               # 仪表盘
│   │       ├── accounts/                # 账户管理
│   │       ├── strategy/                # 策略配置
│   │       └── settings/                # 设置
│   ├── res/                             # 资源文件
│   └── AndroidManifest.xml              # 清单文件
├── build.gradle                         # 项目构建文件
├── app/build.gradle                     # 应用构建文件
└── README.md                            # 项目说明
```

## 安装和运行

### 1. 环境要求
- Android Studio Arctic Fox 或更高版本
- JDK 11 或更高版本
- Android SDK 34

### 2. 构建步骤
```bash
# 克隆项目
git clone <repository-url>

# 进入项目目录
cd QuantTradeApp

# 构建调试版本
./gradlew assembleDebug

# 安装到设备
./gradlew installDebug
```

### 3. 配置API密钥
1. 打开应用
2. 进入"账户管理"页面
3. 输入MT4/5或币安的API密钥
4. 点击"连接"按钮

## 使用指南

### 1. 连接交易平台
1. 在"账户管理"页面选择交易平台
2. 输入相应的服务器地址和登录信息
3. 点击"连接"按钮

### 2. 配置交易策略
1. 进入"策略配置"页面
2. 选择交易策略类型
3. 设置策略参数
4. 保存配置

### 3. 开始交易
1. 进入"交易仪表盘"
2. 点击"开始交易"按钮
3. 监控交易状态和表现

### 4. 策略优化
1. 进入"策略配置"页面
2. 点击"训练模型"按钮
3. 等待遗传算法优化完成
4. 查看优化结果

## 技术栈

### Android开发
- Kotlin + Jetpack Compose
- Room数据库
- Retrofit网络库
- WebSocket实时通信
- MVVM架构模式

### 机器学习
- TensorFlow Lite (移动端推理)
- 遗传算法 (参数优化)
- 强化学习 (策略学习)

### 交易API
- 币安 REST API + WebSocket
- MT4/5 专用API

## 风险提示

⚠️ **重要风险提示**:

1. **资金风险**: 量化交易存在亏损风险，请使用闲置资金进行交易
2. **技术风险**: API连接可能中断，程序可能存在bug
3. **市场风险**: 市场波动可能导致策略失效
4. **学习风险**: 机器学习模型可能过拟合或失效

**建议**:
- 先用模拟账户测试
- 设置严格的止损
- 定期检查策略表现
- 不要投入超过承受能力的资金

## 开发计划

### 已完成
- ✅ 基础UI框架
- ✅ API连接模块
- ✅ 数据层实现
- ✅ 基本策略引擎

### 进行中
- 🔄 机器学习模型集成
- 🔄 遗传算法优化
- 🔄 回测系统完善

### 计划中
- 📋 强化学习策略
- 📋 高级可视化
- 📋 多策略组合
- 📋 云端同步

## 贡献指南

欢迎贡献代码、报告问题或提出建议！

1. Fork 项目
2. 创建功能分支
3. 提交更改
4. 推送到分支
5. 创建 Pull Request

## 许可证

MIT License - 详见 LICENSE 文件

## 联系方式

- 项目地址: [GitHub Repository]
- 问题反馈: [Issues]
- 邮箱: [your-email@example.com]

---

**免责声明**: 本软件仅供学习和研究使用，不构成任何投资建议。使用本软件进行交易的风险由用户自行承担。