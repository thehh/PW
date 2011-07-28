package cl.hh.android.PriceWatch;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.app.Activity;
import android.graphics.Bitmap;


public class MyWebView extends Activity{
	WebView mWebView;
    String theURL;
    final Activity activity = this;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.webview);
        
        //recibir URL
        Bundle barcodeIntent = getIntent().getExtras();
        theURL = barcodeIntent.getString("theURL");
         
        //WebView
        mWebView = (WebView) findViewById(R.id.webview);
               
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setPluginsEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.freeMemory();
        
        mWebView.loadUrl(theURL);
        
        
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                activity.setTitle("Cargando...");
                activity.setProgress(progress * 100);
                if(progress == 100)
                    activity.setTitle(R.string.app_name);
            }
        });
        
        mWebView.setWebViewClient(new MyWebViewClient());
        
    }
    
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {


        }

        @Override
        public void onPageFinished(WebView view, String url) {
          // hide the progress bar
        }
        
    }
    
   public boolean onKeyDown(int keyCode, KeyEvent event) {
        
    	if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
