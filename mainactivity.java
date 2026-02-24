package com.samed.panel;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Ekranın kapanmasını veya uykuya geçmesini engelle
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // 2. Tam Ekran (Immersive Mode) - Hiçbir tuş veya bar görünmez
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        
        // 3. DONANIM HIZLANDIRMA (Ucuz TV'lerde yağ gibi akması için)
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setDatabaseEnabled(true);
        ws.setRenderPriority(WebSettings.RenderPriority.HIGH);
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        
        // Masaüstü tarayıcı gibi davranması için
        ws.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36");

        webView.setWebViewClient(new WebViewClient());

        // Senin boot linkin
        webView.loadUrl("https://samedonline.gt.tc/boot.php");
    }

    @Override
    public void onBackPressed() {
        // Geri tuşu devredışı - Kimse uygulamadan yanlışlıkla çıkamaz
    }
}
