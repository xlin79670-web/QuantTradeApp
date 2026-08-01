#!/bin/bash
# ============================================
# 一键上传到GitHub并自动构建APK
# ============================================
# 使用方法:
# 1. 先在GitHub创建仓库
# 2. chmod +x upload_to_github.sh
# 3. ./upload_to_github.sh 你的GitHub用户名
# ============================================

set -e

echo "=========================================="
echo "  量化交易应用 - 上传到GitHub"
echo "=========================================="

# 检查参数
if [ -z "$1" ]; then
    echo "❌ 请提供GitHub用户名"
    echo "用法: ./upload_to_github.sh 你的GitHub用户名"
    exit 1
fi

GITHUB_USER="$1"
REPO_NAME="QuantTradeApp"
REPO_URL="https://github.com/${GITHUB_USER}/${REPO_NAME}.git"

echo ""
echo "📌 GitHub用户: $GITHUB_USER"
echo "📌 仓库名称: $REPO_NAME"
echo ""

# 检查git
if ! command -v git &> /dev/null; then
    echo "❌ 未安装Git，请先安装:"
    echo "   Ubuntu/Debian: sudo apt install git"
    echo "   macOS: brew install git"
    exit 1
fi

echo "✅ Git版本: $(git --version)"

# 初始化git仓库
echo ""
echo "🔧 初始化Git仓库..."
cd "$(dirname "$0")"
git init
git branch -M main

# 配置git用户（如果未配置）
if [ -z "$(git config user.name)" ]; then
    read -p "请输入你的名字: " GIT_NAME
    read -p "请输入你的邮箱: " GIT_EMAIL
    git config user.name "$GIT_NAME"
    git config user.email "$GIT_EMAIL"
fi

# 添加文件
echo ""
echo "📁 添加文件..."
git add .

# 提交
echo ""
echo "💾 提交代码..."
git commit -m "feat: 量化交易Android应用 - 机器学习策略优化"

# 添加远程仓库
echo ""
echo "🔗 连接GitHub仓库..."
git remote remove origin 2>/dev/null || true
git remote add origin "$REPO_URL"

# 推送
echo ""
echo "🚀 推送到GitHub..."
echo ""
echo "⚠️  可能需要输入GitHub用户名和密码/Token"
echo "   如果使用Token，请确保已开启仓库写入权限"
echo ""
git push -u origin main

echo ""
echo "=========================================="
echo "  ✅ 上传成功!"
echo "=========================================="
echo ""
echo "📌 下一步:"
echo "   1. 访问 https://github.com/${GITHUB_USER}/${REPO_NAME}"
echo "   2. 点击 'Actions' 标签"
echo "   3. 等待构建完成（约3-5分钟）"
echo "   4. 在 'Artifacts' 中下载APK"
echo ""
echo "📌 或者直接访问:"
echo "   https://github.com/${GITHUB_USER}/${REPO_NAME}/actions"
echo "=========================================="