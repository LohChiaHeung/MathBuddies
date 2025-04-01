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


public class TutorialActivityComposeNumber extends BaseActivity {
    private WebView webView;
    private FrameLayout fullScreenContainer;
    private View customView;
    private WebChromeClient.CustomViewCallback customViewCallback;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_composenumbers);

        //Set the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#693c28"));
        }

        //Initialize the UI elements
        webView = findViewById(R.id.webView);
        fullScreenContainer = findViewById(R.id.fullscreen_container);

        // Configure WebView settings for media playback
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);

        // Set WebView clients
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new TutorialActivityComposeNumber.FullscreenChromeClient());

        webView.loadUrl("https://www.youtube.com/embed/G05AgnEGmgw");

        setupBackButton(R.id.btnBack);
    }

    private class FullscreenChromeClient extends WebChromeClient {
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            // Display video in fullscreen mode
            customView = view;
            customViewCallback = callback;
            fullScreenContainer.setVisibility(View.VISIBLE);
            fullScreenContainer.addView(view);
            webView.setVisibility(View.GONE);
        }

        @Override
        public void onHideCustomView() {
            // Exit fullscreen mode
            fullScreenContainer.setVisibility(View.GONE);
            fullScreenContainer.removeView(customView);
            customView = null;
            webView.setVisibility(View.VISIBLE);
            customViewCallback.onCustomViewHidden();
        }
    }
}
