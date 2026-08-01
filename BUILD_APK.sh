#!/bin/bash
# ============================================
# 量化交易Android应用 - 一键构建脚本
# ============================================
# 使用方法:
# 1. 确保已安装 Java JDK 17+ 和 Android SDK
# 2. chmod +x BUILD_APK.sh
# 3. ./BUILD_APK.sh
# ============================================

set -e

echo "=========================================="
echo "  量化交易Android应用 - 开始构建"
echo "=========================================="

# 检查Java
if ! command -v java &> /dev/null; then
    echo "❌ 未找到Java，请安装Java JDK 17+"
    echo "   Ubuntu/Debian: sudo apt install openjdk-17-jdk"
    echo "   macOS: brew install openjdk@17"
    exit 1
fi

echo "✅ Java版本: $(java -version 2>&1 | head -1)"

# 检查ANDROID_HOME
if [ -z "$ANDROID_HOME" ]; then
    # 尝试常见路径
    if [ -d "$HOME/Android/Sdk" ]; then
        export ANDROID_HOME="$HOME/Android/Sdk"
    elif [ -d "$HOME/Library/Android/sdk" ]; then
        export ANDROID_HOME="$HOME/Library/Android/sdk"
    elif [ -d "/opt/android-sdk" ]; then
        export ANDROID_HOME="/opt/android-sdk"
    else
        echo "❌ 未设置ANDROID_HOME环境变量"
        echo "   请安装Android Studio或设置ANDROID_HOME"
        echo "   export ANDROID_HOME=/path/to/android-sdk"
        exit 1
    fi
fi

echo "✅ ANDROID_HOME: $ANDROID_HOME"

# 构建项目
echo ""
echo "🔨 开始构建APK..."

cd "$(dirname "$0")"

# 给gradlew执行权限
chmod +x gradlew

# 构建调试版本APK
./gradlew assembleDebug

# 查找生成的APK
APK_PATH=$(find app/build/outputs -name "*.apk" -type f 2>/dev/null | head -1)

if [ -n "$APK_PATH" ]; then
    echo ""
    echo "=========================================="
    echo "  ✅ 构建成功!"
    echo "=========================================="
    echo ""
    echo "📱 APK文件位置: $APK_PATH"
    echo ""
    echo "安装到设备:"
    echo "  adb install $APK_PATH"
    echo ""
    echo "或直接复制到手机安装"
    echo "=========================================="
else
    echo "❌ 构建失败，未找到APK文件"
    exit 1
fi