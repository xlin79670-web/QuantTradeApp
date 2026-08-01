package com.quanttrade.app;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.view.Window;
import android.view.WindowManager;
import android.graphics.Color;

public class MainActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        
        // 创建WebView
        webView = new WebView(this);
        setContentView(webView);
        
        // 配置WebView
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(false);
        
        webView.setWebViewClient(new WebViewClient());
        webView.setBackgroundColor(Color.WHITE);
        
        // 加载本地HTML
        String html = generateTradingHTML();
        webView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "UTF-8", null);
    }
    
    private String generateTradingHTML() {
        return "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>量化交易助手</title>\n" +
            "    <style>\n" +
            "        * { margin: 0; padding: 0; box-sizing: border-box; }\n" +
            "        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: #f5f5f5; }\n" +
            "        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 20px; text-align: center; }\n" +
            "        .header h1 { font-size: 24px; margin-bottom: 5px; }\n" +
            "        .header p { font-size: 14px; opacity: 0.9; }\n" +
            "        .container { padding: 15px; }\n" +
            "        .card { background: white; border-radius: 12px; padding: 20px; margin-bottom: 15px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }\n" +
            "        .card h2 { font-size: 18px; color: #333; margin-bottom: 15px; }\n" +
            "        .stat-row { display: flex; justify-content: space-between; padding: 10px 0; border-bottom: 1px solid #eee; }\n" +
            "        .stat-row:last-child { border-bottom: none; }\n" +
            "        .stat-label { color: #666; }\n" +
            "        .stat-value { font-weight: bold; color: #333; }\n" +
            "        .stat-value.positive { color: #4CAF50; }\n" +
            "        .stat-value.negative { color: #f44336; }\n" +
            "        .btn { width: 100%; padding: 15px; border: none; border-radius: 8px; font-size: 16px; font-weight: bold; cursor: pointer; margin-top: 10px; }\n" +
            "        .btn-primary { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; }\n" +
            "        .btn-danger { background: #f44336; color: white; }\n" +
            "        .feature-list { list-style: none; }\n" +
            "        .feature-list li { padding: 12px 0; border-bottom: 1px solid #eee; display: flex; align-items: center; }\n" +
            "        .feature-list li:last-child { border-bottom: none; }\n" +
            "        .feature-icon { width: 40px; height: 40px; border-radius: 10px; display: flex; align-items: center; justify-content: center; margin-right: 15px; font-size: 20px; }\n" +
            "        .icon-blue { background: #E3F2FD; }\n" +
            "        .icon-green { background: #E8F5E9; }\n" +
            "        .icon-purple { background: #F3E5F5; }\n" +
            "        .icon-orange { background: #FFF3E0; }\n" +
            "        .footer { text-align: center; padding: 20px; color: #999; font-size: 12px; }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"header\">\n" +
            "        <h1>📈 量化交易助手</h1>\n" +
            "        <p>智能学习进化优化策略 v1.0</p>\n" +
            "    </div>\n" +
            "    \n" +
            "    <div class=\"container\">\n" +
            "        <div class=\"card\">\n" +
            "            <h2>💰 账户概览</h2>\n" +
            "            <div class=\"stat-row\">\n" +
            "                <span class=\"stat-label\">总资产</span>\n" +
            "                <span class=\"stat-value\">$10,000.00</span>\n" +
            "            </div>\n" +
            "            <div class=\"stat-row\">\n" +
            "                <span class=\"stat-label\">今日盈亏</span>\n" +
            "                <span class=\"stat-value positive\">+$125.50</span>\n" +
            "            </div>\n" +
            "            <div class=\"stat-row\">\n" +
            "                <span class=\"stat-label\">持仓数量</span>\n" +
            "                <span class=\"stat-value\">3</span>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "        \n" +
            "        <div class=\"card\">\n" +
            "            <h2>🤖 策略状态</h2>\n" +
            "            <div class=\"stat-row\">\n" +
            "                <span class=\"stat-label\">当前策略</span>\n" +
            "                <span class=\"stat-value\">机器学习策略</span>\n" +
            "            </div>\n" +
            "            <div class=\"stat-row\">\n" +
            "                <span class=\"stat-label\">策略状态</span>\n" +
            "                <span class=\"stat-value positive\">运行中</span>\n" +
            "            </div>\n" +
            "            <div class=\"stat-row\">\n" +
            "                <span class=\"stat-label\">胜率</span>\n" +
            "                <span class=\"stat-value\">68.5%</span>\n" +
            "            </div>\n" +
            "            <button class=\"btn btn-primary\">⏸ 暂停策略</button>\n" +
            "        </div>\n" +
            "        \n" +
            "        <div class=\"card\">\n" +
            "            <h2>✨ 功能特性</h2>\n" +
            "            <ul class=\"feature-list\">\n" +
            "                <li>\n" +
            "                    <div class=\"feature-icon icon-blue\">🔗</div>\n" +
            "                    <div>\n" +
            "                        <strong>多平台支持</strong><br>\n" +
            "                        <small style=\"color:#666\">MT4/5外汇 + 币安加密货币</small>\n" +
            "                    </div>\n" +
            "                </li>\n" +
            "                <li>\n" +
            "                    <div class=\"feature-icon icon-green\">🧠</div>\n" +
            "                    <div>\n" +
            "                        <strong>智能学习</strong><br>\n" +
            "                        <small style=\"color:#666\">TensorFlow Lite机器学习</small>\n" +
            "                    </div>\n" +
            "                </li>\n" +
            "                <li>\n" +
            "                    <div class=\"feature-icon icon-purple\">🧬</div>\n" +
            "                    <div>\n" +
            "                        <strong>遗传算法优化</strong><br>\n" +
            "                        <small style=\"color:#666\">自动优化策略参数</small>\n" +
            "                    </div>\n" +
            "                </li>\n" +
            "                <li>\n" +
            "                    <div class=\"feature-icon icon-orange\">🛡️</div>\n" +
            "                    <div>\n" +
            "                        <strong>风险控制</strong><br>\n" +
            "                        <small style=\"color:#666\">智能止损和仓位管理</small>\n" +
            "                    </div>\n" +
            "                </li>\n" +
            "            </ul>\n" +
            "        </div>\n" +
            "        \n" +
            "        <div class=\"card\">\n" +
            "            <h2>📊 最近交易</h2>\n" +
            "            <div class=\"stat-row\">\n" +
            "                <span class=\"stat-label\">BTC/USDT</span>\n" +
            "                <span class=\"stat-value positive\">+$45.20</span>\n" +
            "            </div>\n" +
            "            <div class=\"stat-row\">\n" +
            "                <span class=\"stat-label\">ETH/USDT</span>\n" +
            "                <span class=\"stat-value negative\">-$12.30</span>\n" +
            "            </div>\n" +
            "            <div class=\"stat-row\">\n" +
            "                <span class=\"stat-label\">EUR/USD</span>\n" +
            "                <span class=\"stat-value positive\">+$92.60</span>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "    \n" +
            "    <div class=\"footer\">\n" +
            "        <p>量化交易助手 v1.0</p>\n" +
            "        <p>⚠️ 风险提示：量化交易存在风险，请谨慎投资</p>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";
    }
    
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}