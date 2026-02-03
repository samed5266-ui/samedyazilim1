package com.samed.panel;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    WebView webView;
    Handler reloadHandler = new Handler();
    Handler networkHandler = new Handler();
    Handler dailyResetHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ekran kapanmasın
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Tam ekran
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);

        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setDatabaseEnabled(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setMediaPlaybackRequiresUserGesture(false);
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        ws.setUserAgentString(
                "Mozilla/5.0 (Linux; Android TV) AppleWebKit/537.36 Chrome/120 Safari/537.36"
        );

        webView.setWebViewClient(new WebViewClient());

        // Konum izni: 1 kere sorar
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(
                    String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });

        // Panel URL
        webView.loadUrl("https://samedonline.gt.tc/boot.php");

        // 5 dakikada bir reload (donma önleme)
        reloadHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                webView.reload();
                reloadHandler.postDelayed(this, 300000);
            }
        }, 300000);

        // İnternet gidip gelme kontrolü
        networkHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isOnline()) {
                    webView.reload();
                }
                networkHandler.postDelayed(this, 15000);
            }
        }, 15000);

        // Günde 1 kez gece reset (04:00)
        scheduleDailyReset(4);
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private void scheduleDailyReset(int hour) {
        Calendar now = Calendar.getInstance();
        Calendar resetTime = Calendar.getInstance();
        resetTime.set(Calendar.HOUR_OF_DAY, hour);
        resetTime.set(Calendar.MINUTE, 0);
        resetTime.set(Calendar.SECOND, 0);
        if (resetTime.before(now)) {
            resetTime.add(Calendar.DAY_OF_MONTH, 1);
        }
        long delay = resetTime.getTimeInMillis() - now.getTimeInMillis();

        dailyResetHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                webView.reload();
                scheduleDailyReset(hour); // tekrar
            }
        }, delay);
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        webView.resumeTimers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
        webView.pauseTimers();
    }

    @Override
    public void onBackPressed() {
        // geri tuşu kapalı (kiosk modu)
    }
}
