package in.visheshagya.visheshagya.webViews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import in.visheshagya.visheshagya.resourses.APIUrls;

public class WebViewActivity extends AppCompatActivity {

    android.webkit.WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get the intents
        Intent intent = getIntent();
        String action = intent.getStringExtra("action");

        webview = new android.webkit.WebView(this); // initialize window as webview
        setContentView(webview);

        // set url for webview
        switch (action) {
            case "tmc":
                loadWebPage(APIUrls.VISHESH_TERMS);
                break;
            case "pr":
                loadWebPage(APIUrls.VISHESH_PRIVACY);
                break;
            default:
                break;
        }
    }

    public void loadWebPage(String url) {
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webview.loadUrl(url);
    }
}
