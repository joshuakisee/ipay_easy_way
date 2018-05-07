package com.libs.ipay.ipayLibrary;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;


public class CardChannel extends Fragment {

    private WebView myWebView;
    private TextView oid, total_amount;
    private ProgressDialog progDailog;

    String theUrl;

    public CardChannel() {
        // Required empty public constructor
    }


    public static CardChannel newInstance(String param1, String param2) {
        CardChannel fragment = new CardChannel();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_channel, container, false);

        myWebView       = (WebView) view.findViewById(R.id.web_view);
        oid             = (TextView) view.findViewById(R.id.oid);
        total_amount    = (TextView) view.findViewById(R.id.total_amount);

        //get url value passed from channels class (extra)
        Bundle bundle=getArguments();
        theUrl = bundle.getString("url");
        String oid_text = bundle.getString("oid_text");
        String amount_text = bundle.getString("amount_text");

        oid.setText("Order ID: "+oid_text);
        total_amount.setText("Total KES "+amount_text+".00");

        LoadPage();

        return view;
    }

    private void LoadPage()
    {

        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);

        progDailog = ProgressDialog.show(getActivity(), "Loading","Please wait...", true);
        progDailog.setCancelable(false);
        myWebView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progDailog.show();
                view.loadUrl(url);

                return true;
            }
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
//
//                   dialog();
//
//
//            }

            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // Handle the error
                dialog();
            }

            @android.annotation.TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());

            }

            @Override
            public void onPageFinished(WebView view, final String url) {

                view.loadUrl("javascript:document.getElementById(\"orderDetails\").setAttribute(\"style\",\"display:none;\");");

                progDailog.dismiss();
            }

        });

        myWebView.loadUrl(theUrl);

    }

     private void dialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);

        builder.setMessage("Network error! check network connection and try again.");

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                LoadPage();
            }
        });

        builder.setCancelable(false);
        builder.show();
    }


}
