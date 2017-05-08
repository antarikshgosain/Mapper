package com.example.anta3.mapper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WebViewDemo extends AppCompatActivity implements View.OnClickListener {

    String url ;
    WebView mWebView;
    ImageView imgBack ;
    TextView txtBack ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        url = getIntent().getExtras().getString("url");
        imgBack = (ImageView)findViewById(R.id.imgBack);
        txtBack = (TextView) findViewById(R.id.txtBack);

        imgBack.setOnClickListener(this);
        txtBack.setOnClickListener(this);

        Toast.makeText(getApplicationContext(),"Loading! Wait a moment...",Toast.LENGTH_SHORT).show();

        mWebView = (WebView)findViewById(R.id.web_view_1);
        mWebView.getSettings().setJavaScriptEnabled(true);
        Log.i("antalog",url);
        if (url==""||url==null){
            Toast.makeText(getApplicationContext(),"URL not found",Toast.LENGTH_SHORT).show();
            finish();
        }

        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(url);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtBack:{
                finish();
                break;
            }
            case R.id.imgBack:{
                finish();
                break;
            }
        }
    }
}
