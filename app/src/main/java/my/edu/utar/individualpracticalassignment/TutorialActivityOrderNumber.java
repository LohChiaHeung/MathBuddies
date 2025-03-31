package my.edu.utar.individualpracticalassignment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;

public class TutorialActivityOrderNumber extends BaseActivity {

    private WebView webView;
    private FrameLayout fullScreenContainer;
    private View customView;
    private WebChromeClient.CustomViewCallback customViewCallback;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_ordernumbers);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#693c28"));
        }

        webView = findViewById(R.id.webView);
        fullScreenContainer = findViewById(R.id.fullscreen_container); // You need to add this in XML

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new TutorialActivityOrderNumber.FullscreenChromeClient());

        webView.loadUrl("https://www.youtube.com/embed/E34PAOGYRNk");

        setupBackButton(R.id.btnBack);
    }

    private class FullscreenChromeClient extends WebChromeClient {
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            customView = view;
            customViewCallback = callback;
            fullScreenContainer.setVisibility(View.VISIBLE);
            fullScreenContainer.addView(view);
            webView.setVisibility(View.GONE);
        }

        @Override
        public void onHideCustomView() {
            fullScreenContainer.setVisibility(View.GONE);
            fullScreenContainer.removeView(customView);
            customView = null;
            webView.setVisibility(View.VISIBLE);
            customViewCallback.onCustomViewHidden();
        }
    }
}
