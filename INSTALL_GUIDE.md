# 量化交易Android应用 - 安装使用指南

## 方法一：使用Android Studio（推荐）

### 步骤1：安装Android Studio
1. 访问 https://developer.android.com/studio
2. 下载并安装Android Studio

### 步骤2：打开项目
1. 打开Android Studio
2. 选择 "Open an Existing Project"
3. 选择 `QuantTradeApp` 文件夹

### 步骤3：构建APK
1. 等待Gradle同步完成（首次可能需要几分钟）
2. 点击菜单栏 "Build" → "Build Bundle(s) / APK(s)" → "Build APK(s)"
3. 等待构建完成
4. 点击通知栏中的 "locate" 找到APK文件

### 步骤4：安装到手机
1. 将APK文件传输到手机
2. 在手机上打开APK文件安装
3. 或使用ADB: `adb install app-debug.apk`

---

## 方法二：使用命令行

### 前置条件
- Java JDK 17 或更高版本
- Android SDK（通过Android Studio安装）

### 构建步骤
```bash
# 1. 打开终端，进入项目目录
cd QuantTradeApp

# 2. 设置环境变量（如果需要）
export ANDROID_HOME=$HOME/Android/Sdk

# 3. 给构建脚本执行权限
chmod +x gradlew

# 4. 构建APK
./gradlew assembleDebug

# 5. 找到APK文件
ls -la app/build/outputs/apk/debug/
```

---

## 方法三：使用我提供的一键脚本

```bash
# 1. 进入项目目录
cd QuantTradeApp

# 2. 运行构建脚本
chmod +x BUILD_APK.sh
./BUILD_APK.sh
```

---

## 安装APK到手机

### 方法1：直接安装
1. 将 `app-debug.apk` 文件传输到手机
2. 在手机文件管理器中找到并点击APK文件
3. 按提示完成安装

### 方法2：使用ADB安装
```bash
# 确保手机已连接电脑并开启USB调试
adb install app/build/outputs/apk/debug/app-debug.apk
```

### 方法3：使用WiFi传输
1. 在手机上安装"文件传输"应用
2. 通过WiFi将APK文件传输到手机
3. 在手机上安装

---

## 首次使用配置

### 1. 获取API密钥

#### 币安API密钥
1. 登录 https://www.binance.com
2. 进入 "API管理" 页面
3. 创建新的API密钥
4. 保存 API Key 和 Secret Key

#### MT4/5 API密钥
1. 联系您的经纪商获取API访问权限
2. 获取：服务器地址、登录号、密码

### 2. 在应用中配置
1. 打开应用
2. 进入 "账户管理" 页面
3. 输入API密钥
4. 点击 "连接"

### 3. 开始交易
1. 进入 "策略配置" 选择交易策略
2. 设置策略参数
3. 进入 "交易仪表盘" 点击 "开始交易"

---

## 常见问题

### Q: 构建时出现 "SDK not found" 错误
A: 确保已安装Android SDK，并设置ANDROID_HOME环境变量

### Q: 安装时出现 "未知来源" 错误
A: 在手机设置中允许安装未知来源应用

### Q: 应用无法连接到服务器
A: 检查网络连接和API密钥是否正确

### Q: 如何更新应用
A: 重新构建APK并安装，会自动覆盖旧版本

---

## 技术支持

如遇到问题，请提供：
1. 错误信息截图
2. 手机型号和Android版本
3. 操作步骤描述