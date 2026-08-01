# 📱 量化交易Android应用 - 完整指南

## 🎯 目标
通过GitHub自动构建，获得可安装的APK文件。

---

## 📋 完整步骤（只需10分钟）

### 第1步：注册GitHub账号（如果还没有）
1. 访问 https://github.com
2. 点击 `Sign up`
3. 按提示完成注册

---

### 第2步：在GitHub创建仓库
1. 登录GitHub
2. 点击右上角 `+` 按钮
3. 选择 `New repository`
4. 填写：
   - **Repository name**: `QuantTradeApp`
   - **Description**: `量化交易Android应用 - 机器学习策略`
   - **选择**: `Public`
5. 点击 `Create repository`

---

### 第3步：下载项目到你的电脑
```bash
# 在你的电脑上打开终端/命令行

# 进入你想要存放项目的目录
cd ~/Desktop  # 或者任何你喜欢的目录

# 克隆仓库
git clone https://github.com你的用户名/QuantTradeApp.git

# 进入项目目录
cd QuantTradeApp
```

---

### 第4步：复制项目文件
把 `/workspace/QuantTradeApp/` 中的所有文件复制到你刚才克隆的仓库目录。

或者使用命令：
```bash
# 如果你在Linux/Mac上
cp -r /workspace/QuantTradeApp/* .

# 如果你用的是Windows，手动复制所有文件到仓库文件夹
```

---

### 第5步：推送代码到GitHub
```bash
# 添加所有文件
git add .

# 提交
git commit -m "feat: 量化交易应用v1.0"

# 推送
git push origin main
```

---

### 第6步：等待自动构建
1. 访问你的仓库：`https://github.com/你的用户名/QuantTradeApp`
2. 点击 `Actions` 标签
3. 你会看到构建正在进行（黄色圆圈）
4. 等待变成绿色勾号（约3-5分钟）

---

### 第7步：下载APK
1. 在 `Actions` 页面，点击最新的构建
2. 页面底部找到 `Artifacts` 部分
3. 点击 `debug-apk` 下载
4. 解压下载的zip文件
5. 得到 `app-debug.apk`

---

### 第8步：安装到手机
1. 把 `app-debug.apk` 传到手机（微信/QQ/数据线都可以）
2. 在手机上点击APK文件
3. 如果提示"不允许安装"，去设置中允许安装未知来源应用
4. 安装完成！

---

## 📱 应用功能预览

| 功能 | 说明 |
|------|------|
| 📊 **交易仪表盘** | 实时显示账户余额、盈亏、持仓 |
| 👤 **账户管理** | 配置MT4/5和币安API密钥 |
| 🤖 **策略配置** | 选择交易策略、调整参数 |
| ⚙️ **设置中心** | 风险控制、通知设置 |

---

## ⚠️ 重要风险提示

**量化交易存在风险，请务必：**
1. ✅ 先用模拟账户测试
2. ✅ 设置严格的止损（建议2-5%）
3. ✅ 不要投入超过承受能力的资金
4. ✅ 定期检查策略表现

---

## 🔧 常见问题

### Q: git push时提示输入密码？
A: GitHub已经不支持密码登录，需要使用Token：
1. 访问 https://github.com/settings/tokens
2. 点击 `Generate new token`
3. 勾选 `repo` 权限
4. 复制Token，作为密码使用

### Q: Actions构建失败？
A: 点击失败的构建查看错误日志，常见原因：
- 依赖下载超时（重试即可）
- 代码语法错误（检查代码）

### Q: APK安装失败？
A: 确保手机开启了"允许安装未知来源应用"

### Q: 如何更新应用？
A: 修改代码后再次推送，APK会自动重新构建

---

## 📞 需要帮助？

如果遇到问题，请提供：
1. 操作系统（Windows/Mac/Linux）
2. 错误截图
3. 具体报错信息

---

## 🎉 恭喜！

完成以上步骤后，你就拥有了自己的量化交易Android应用！

**记住：投资有风险，交易需谨慎！**