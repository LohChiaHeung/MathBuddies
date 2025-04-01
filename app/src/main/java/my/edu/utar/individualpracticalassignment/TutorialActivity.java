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

public class TutorialActivity extends BaseActivity {

    private WebView webView;
    private FrameLayout fullScreenContainer; //Container for fullscreen video playback
    private View customView; //Holds the fullscreen video view
    private WebChromeClient.CustomViewCallback customViewCallback; //Callback to exit the fullscreen video playback

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        //Customize the status bar color for phone
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#693c28"));
        }

        // Initialize WebView and fullscreen container
        webView = findViewById(R.id.webView);
        fullScreenContainer = findViewById(R.id.fullscreen_container); // You need to add this in XML

        // Enable JavaScript and allow media playback without user gesture
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);

        // Set custom WebViewClient and WebChromeClient to handle full screen
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new FullscreenChromeClient());

        // Load YouTube video in embedded fullscreen-compatible mode
        webView.loadUrl("https://www.youtube.com/embed/M6Efzu2slaI");

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    // Custom the WebChromeClient to support fullscreen video playback
    private class FullscreenChromeClient extends WebChromeClient {
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            // Show video in fullscreen mode
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
