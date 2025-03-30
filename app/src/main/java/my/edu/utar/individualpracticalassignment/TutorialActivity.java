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

public class TutorialActivity extends BaseActivity {

    private WebView webView;
    private FrameLayout fullScreenContainer;
    private View customView;
    private WebChromeClient.CustomViewCallback customViewCallback;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        webView = findViewById(R.id.webView);
        fullScreenContainer = findViewById(R.id.fullscreen_container); // You need to add this in XML

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new FullscreenChromeClient());

        webView.loadUrl("https://www.youtube.com/embed/M6Efzu2slaI");

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
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
