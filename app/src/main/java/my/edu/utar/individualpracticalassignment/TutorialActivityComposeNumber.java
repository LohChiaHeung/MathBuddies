package my.edu.utar.individualpracticalassignment;

import android.annotation.SuppressLint;
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

        webView = findViewById(R.id.webView);
        fullScreenContainer = findViewById(R.id.fullscreen_container);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new TutorialActivityComposeNumber.FullscreenChromeClient());

        webView.loadUrl("https://www.youtube.com/embed/G05AgnEGmgw");

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
