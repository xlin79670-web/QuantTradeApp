# 🚀 通过GitHub自动构建APK

## 一键操作步骤

### 第1步：创建GitHub账号
如果还没有，访问 https://github.com 注册

### 第2步：创建新仓库
1. 登录GitHub
2. 点击右上角 `+` → `New repository`
3. 仓库名：`QuantTradeApp`
4. 选择 `Public`
5. 点击 `Create repository`

### 第3步：上传项目文件
在你的电脑上执行：

```bash
# 克隆仓库
git clone https://github.com/你的用户名/QuantTradeApp.git

# 把项目文件复制到仓库目录
cp -r /workspace/QuantTradeApp/* QuantTradeApp/

# 进入仓库目录
cd QuantTradeApp

# 添加所有文件
git add .

# 提交
git commit -m "Initial commit"

# 推送
git push origin main
```

### 第4步：等待自动构建
1. 推送后，GitHub会自动开始构建
2. 点击仓库页面的 `Actions` 标签
3. 等待绿色勾号出现（约3-5分钟）

### 第5步：下载APK
1. 在 `Actions` 页面点击最新的构建
2. 在 `Artifacts` 部分找到 `debug-apk`
3. 点击下载
4. 解压得到 `app-debug.apk`

### 第6步：安装到手机
1. 把 `app-debug.apk` 传到手机
2. 在手机上点击安装
3. 如果提示"未知来源"，在设置中允许

---

## 📱 APK功能

| 功能 | 说明 |
|------|------|
| 🔗 多平台交易 | MT4/5 + 币安 |
| 🧠 机器学习策略 | TensorFlow Lite |
| 🧬 遗传算法优化 | 自动调参 |
| 🛡️ 风险控制 | 智能止损止盈 |
| 📊 实时监控 | WebSocket推送 |
| 🔐 安全存储 | API密钥加密 |

---

## ⚠️ 风险提示

量化交易存在风险，请：
1. 先用模拟账户测试
2. 设置严格止损
3. 不要投入超过承受能力的资金

---

## 🔄 更新代码后自动重新构建

每次你修改代码并推送到GitHub，APK会自动重新构建。

```bash
# 修改代码后
git add .
git commit -m "Update feature"
git push origin main
```

---

## ❓ 常见问题

**Q: 构建失败怎么办？**
A: 点击失败的构建查看日志，通常是依赖下载问题，重新运行即可。

**Q: 如何查看构建状态？**
A: 在仓库页面点击 `Actions` 标签查看。

**Q: APK无法安装？**
A: 确保手机允许安装未知来源应用。