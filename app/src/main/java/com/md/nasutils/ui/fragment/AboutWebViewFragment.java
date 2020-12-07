/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.md.nasutils.R;
import com.md.nasutils.ui.webkit.WebViewInterface;

import androidx.fragment.app.Fragment;

/**
 * WebView for displaying the About page.
 * 
 * @author michaeldoyle
 *
 */
public class AboutWebViewFragment extends Fragment {

    public static final String INTERFACE_NAME = "Android";

    private static final String ABOUT_URL = "file:///android_asset/about.html";
    
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        
        WebView webView = (WebView) view.findViewById(R.id.about_web_view);
        webView.loadUrl(ABOUT_URL);
        webView.reload();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebViewInterface(getActivity()), INTERFACE_NAME);
        
        return view;
    }
}
