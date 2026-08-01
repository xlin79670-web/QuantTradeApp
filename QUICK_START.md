# 🚀 快速开始 - 生成可安装APK

## 方法一：使用Android Studio（最简单，推荐）

### 第1步：下载Android Studio
访问 https://developer.android.com/studio 下载安装

### 第2步：打开项目
1. 打开Android Studio
2. 选择 `Open` → 选择 `QuantTradeApp` 文件夹
3. 等待Gradle同步完成

### 第3步：构建APK
1. 菜单栏：`Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`
2. 等待构建完成
3. 点击通知栏的 `locate` 找到APK

### 第4步：安装到手机
将 `app-debug.apk` 传到手机直接安装

---

## 方法二：命令行构建

### 前置条件
```bash
# macOS
brew install openjdk@17

# Ubuntu/Debian
sudo apt install openjdk-17-jdk

# 下载Android SDK命令行工具
# https://developer.android.com/studio#command-line-tools-only
```

### 构建命令
```bash
cd QuantTradeApp
chmod +x gradlew
./gradlew assembleDebug

# APK位置
ls app/build/outputs/apk/debug/app-debug.apk
```

---

## 方法三：使用在线构建服务

### GitHub Actions
1. 将项目上传到GitHub
2. 添加 `.github/workflows/build.yml`:
```yaml
name: Build APK
on: push
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '17'
      - run: chmod +x gradlew && ./gradlew assembleDebug
      - uses: actions/upload-artifact@v3
        with:
          name: apk
          path: app/build/outputs/apk/debug/app-debug.apk
```
3. 推送代码，自动构建，从Actions下载APK

---

## 📱 应用功能

这个量化交易应用包含：

### 核心功能
- ✅ MT4/5外汇平台自动化交易
- ✅ 币安加密货币自动化交易
- ✅ 机器学习策略（TensorFlow Lite）
- ✅ 遗传算法参数优化
- ✅ 实时WebSocket价格推送
- ✅ 智能止损止盈
- ✅ 风险控制管理

### 界面
- 📊 交易仪表盘
- 👤 账户管理
- 🤖 策略配置
- ⚙️ 设置中心

---

## ⚠️ 风险提示

量化交易存在风险，请：
1. 先用模拟账户测试
2. 设置严格止损
3. 不要投入超过承受能力的资金

---

## 📞 技术支持

如遇到构建问题，请提供：
1. 操作系统版本
2. Java版本 (`java -version`)
3. 错误信息截图