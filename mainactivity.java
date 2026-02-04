package com.samed.panel;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private Handler reloadHandler = new Handler();
    private Handler networkHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUserAgentString("Mozilla/5.0 (Linux; Android 10; Android TV) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.101 Safari/537.36");

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://samed5266.github.io/panel/");

        startReloadTimer();
        startNetworkCheck();
    }

    // 5 DAKİKADA BİR YENİLEME (Sadece bu kaldı)
    private void startReloadTimer() {
        reloadHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                webView.reload();
                reloadHandler.postDelayed(this, 300000); // 300.000 ms = 5 Dakika
            }
        }, 300000);
    }

    // İNTERNET KONTROLÜ (Sürekli yenileme yapan komutu buradan sildim)
    private void startNetworkCheck() {
        networkHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isNetworkAvailable()) {
                    // Sayfa zaten açıksa dokunma, sadece internet kontrolü yap.
                }
                networkHandler.postDelayed(this, 60000); // Kontrol sıklığı 1 dakikaya çıkarıldı
            }
        }, 60000);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}
