package com.samed.panel;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private Handler reloadHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ekranın kapanmasını engelle
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Tam ekran modu
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        // Modern bir tarayıcı gibi görünmesini sağla
        ws.setUserAgentString("Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Mobile Safari/537.36");

        webView.setWebViewClient(new WebViewClient());

        // SENİN GÜNCEL LİNKİN
        webView.loadUrl("https://samedonline.gt.tc/boot.php");

        // 5 DAKİKADA BİR YENİLEME BAŞLAT
        startAutoRefresh();
    }

    private void startAutoRefresh() {
        reloadHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                webView.reload();
                reloadHandler.postDelayed(this, 300000); // 300.000 ms = 5 Dakika
            }
        }, 300000);
    }
}
