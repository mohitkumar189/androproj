/*
    @Auther MOHIT KUMAR
    Created on 22/09/2016
 * Copyright (c) Visheshagya Pvt Ltd.
 */
package in.visheshagya.visheshagya.paymentGateways;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import in.visheshagya.visheshagya.activities.AfterBookingStatus;
import in.visheshagya.visheshagya.activities.AfterInstantBookingStatus;

public class InstantPayuPayment extends Activity {
    final Activity activity = this;
    WebView webView;
    ProgressDialog progressDialog;
    String action1 = "";
    String base_url = "https://secure.payu.in";
    int error = 0;
    Map<String, String> params;
    Handler mHandler = new Handler();
    ProgressDialog pDialog;
    private ArrayList<String> post_val = new ArrayList<String>();
    private String post_Data = "";
    private String tag = "PayuPaymentGateway";
    // parameters for payu payment
    private String getExpertName, getFirstName, getNumber, getEmailAddress, productInfo = "appointment fees", service_provider = "payu_paisa", txnid = "", txnidd = "";
    private float getConstAmount;
    private String getConsultTime, getConsultDate, getConsType;
    private String merchant_key = "r42B13KZ"; // live
    private String salt = "9csXZBXmrD"; // live 7
    private String SUCCESS_URL = "https://www.payumoney.com/mobileapp/payumoney/success.php"; // success
    private String FAILED_URL = "https://www.payumoney.com/mobileapp/payumoney/failure.php"; // fail url
    private String hashString = "";
    private String hash, hashSequence;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) { // activity works as webview
        super.onCreate(savedInstanceState);
        Intent oIntent = getIntent();
        getFirstName = oIntent.getExtras().getString("firstName");
        getNumber = oIntent.getExtras().getString("phn");
        getEmailAddress = oIntent.getExtras().getString("emailId");
        getConstAmount = oIntent.getExtras().getFloat("consAmt");
        txnidd = oIntent.getExtras().getString("txnId");
        getConsultTime = oIntent.getExtras().getString("consTime");
        getConsultDate = oIntent.getExtras().getString("consDate");
        getConsType = oIntent.getExtras().getString("consType");
        getExpertName = oIntent.getExtras().getString("expertName");
        txnid = hashCal("SHA-256", txnidd).substring(0, 20); // convert txn id in sha
        System.out.println(getFirstName + " " + getNumber + " " + getEmailAddress + " " + getConstAmount + " " + txnidd + " " + merchant_key + " " + salt);
        progressDialog = new ProgressDialog(activity);
        initializeParams(); // initialize parameters

    }

    private void initializeParams() {
        // insert parameters in params
        params = new HashMap<String, String>();
        params.put("key", merchant_key);
        params.put("amount", String.valueOf(getConstAmount));
        params.put("firstname", getFirstName);
        params.put("email", getEmailAddress);
        params.put("phone", getNumber);
        params.put("txnid", txnid);
        params.put("productinfo", productInfo);
        params.put("surl", SUCCESS_URL);
        params.put("furl", FAILED_URL);
        params.put("service_provider", service_provider);
        params.put("lastname", "");
        params.put("address1", "");
        params.put("address2", "");
        params.put("city", "");
        params.put("state", "");
        params.put("country", "");
        params.put("zipcode", "");
        params.put("udf1", "");
        params.put("udf2", "");
        params.put("udf3", "");
        params.put("udf4", "");
        params.put("udf5", "");
        params.put("pg", "");

        String hashSequence = "key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|udf6|udf7|udf8|udf9|udf10";

        if (empty(params.get("hash")) && params.size() > 0) {
            if (empty(params.get("key"))
                    || empty(params.get("txnid"))
                    || empty(params.get("amount"))
                    || empty(params.get("firstname"))
                    || empty(params.get("email"))
                    || empty(params.get("phone"))
                    || empty(params.get("productinfo"))
                    || empty(params.get("surl"))
                    || empty(params.get("furl"))
                    || empty(params.get("service_provider"))
                    ) {
                error = 1; //set error=1, if params is missing
            } else {
                String[] hashVarSeq = hashSequence.split("\\|");
                for (String part : hashVarSeq) {
                    hashString = (empty(params.get(part))) ? hashString.concat("") : hashString.concat(params.get(part));
                    hashString = hashString.concat("|");
                }
                hashString = hashString.concat(salt);
                hash = hashCal("SHA-512", hashString);
                action1 = base_url.concat("/_payment");
            }
        } else if (!empty(params.get("hash"))) {
            hash = params.get("hash");
            action1 = base_url.concat("/_payment");
        }

        initializeWebView();// initialize and show web view
    }

    private void initializeWebView() {
        // initialize webview
        webView = new WebView(this);
        setContentView(webView);

        webView.setWebViewClient(new MyWebViewClient() {

            public void onPageFinished(WebView view, final String url) {
                progressDialog.dismiss();
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //make sure dialog is showing
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            }

        });

        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setCacheMode(2);
        webView.getSettings().setDomStorageEnabled(true);
        webView.clearHistory();
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setLoadWithOverviewMode(false);

        //webView.addJavascriptInterface(new PayUJavaScriptInterface(getApplicationContext()), "PayUMoney");
        webView.addJavascriptInterface(new PayUJavaScriptInterface(), "PayUMoney");
        Map<String, String> mapParams = new HashMap<String, String>();
        mapParams.put("key", merchant_key);
        mapParams.put("hash", InstantPayuPayment.this.hash);
        mapParams.put("txnid", (empty(InstantPayuPayment.this.params.get("txnid"))) ? "" : InstantPayuPayment.this.params.get("txnid"));
        Log.d(tag, "txnid: " + InstantPayuPayment.this.params.get("txnid"));
        mapParams.put("service_provider", "payu_paisa");

        mapParams.put("amount", (empty(InstantPayuPayment.this.params.get("amount"))) ? "" : InstantPayuPayment.this.params.get("amount"));
        mapParams.put("firstname", (empty(InstantPayuPayment.this.params.get("firstname"))) ? "" : InstantPayuPayment.this.params.get("firstname"));
        mapParams.put("email", (empty(InstantPayuPayment.this.params.get("email"))) ? "" : InstantPayuPayment.this.params.get("email"));
        mapParams.put("phone", (empty(InstantPayuPayment.this.params.get("phone"))) ? "" : InstantPayuPayment.this.params.get("phone"));

        mapParams.put("productinfo", (empty(InstantPayuPayment.this.params.get("productinfo"))) ? "" : InstantPayuPayment.this.params.get("productinfo"));
        mapParams.put("surl", (empty(InstantPayuPayment.this.params.get("surl"))) ? "" : InstantPayuPayment.this.params.get("surl"));
        mapParams.put("furl", (empty(InstantPayuPayment.this.params.get("furl"))) ? "" : InstantPayuPayment.this.params.get("furl"));
        mapParams.put("lastname", (empty(InstantPayuPayment.this.params.get("lastname"))) ? "" : InstantPayuPayment.this.params.get("lastname"));

        mapParams.put("address1", (empty(InstantPayuPayment.this.params.get("address1"))) ? "" : InstantPayuPayment.this.params.get("address1"));
        mapParams.put("address2", (empty(InstantPayuPayment.this.params.get("address2"))) ? "" : InstantPayuPayment.this.params.get("address2"));
        mapParams.put("city", (empty(InstantPayuPayment.this.params.get("city"))) ? "" : InstantPayuPayment.this.params.get("city"));
        mapParams.put("state", (empty(InstantPayuPayment.this.params.get("state"))) ? "" : InstantPayuPayment.this.params.get("state"));

        mapParams.put("country", (empty(InstantPayuPayment.this.params.get("country"))) ? "" : InstantPayuPayment.this.params.get("country"));
        mapParams.put("zipcode", (empty(InstantPayuPayment.this.params.get("zipcode"))) ? "" : InstantPayuPayment.this.params.get("zipcode"));
        mapParams.put("udf1", (empty(InstantPayuPayment.this.params.get("udf1"))) ? "" : InstantPayuPayment.this.params.get("udf1"));
        mapParams.put("udf2", (empty(InstantPayuPayment.this.params.get("udf2"))) ? "" : InstantPayuPayment.this.params.get("udf2"));

        mapParams.put("udf3", (empty(InstantPayuPayment.this.params.get("udf3"))) ? "" : InstantPayuPayment.this.params.get("udf3"));
        mapParams.put("udf4", (empty(InstantPayuPayment.this.params.get("udf4"))) ? "" : InstantPayuPayment.this.params.get("udf4"));
        mapParams.put("udf5", (empty(InstantPayuPayment.this.params.get("udf5"))) ? "" : InstantPayuPayment.this.params.get("udf5"));
        mapParams.put("pg", (empty(InstantPayuPayment.this.params.get("pg"))) ? "" : InstantPayuPayment.this.params.get("pg"));
        webview_ClientPost(webView, action1, mapParams.entrySet());
    }

    public void webview_ClientPost(WebView webView, String url, Collection<Map.Entry<String, String>> postData) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><head></head>");
        sb.append("<body onload='form1.submit()'>");
        sb.append(String.format("<form id='form1' action='%s' method='%s'>", url, "post"));
        for (Map.Entry<String, String> item : postData) {
            sb.append(String.format("<input name='%s' type='hidden' value='%s' />", item.getKey(), item.getValue()));
        }
        sb.append("</form></body></html>");
        Log.d(tag, "webview_ClientPost called");

        //setup and load the progress bar
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading. Please wait...");
        webView.loadData(sb.toString(), "text/html", "utf-8");
    }

    public void success(final long id, final String paymentId) {

        mHandler.post(new Runnable() {
            public void run() {
                mHandler = null;
                //  new PostRechargeData().execute();
                //  Toast.makeText(getApplicationContext(), "Successfully payment\n redirect from Success Function", Toast.LENGTH_LONG).show();
                System.out.println("id is " + id + " paymentId is " + paymentId);
            }
        });
    }

    public boolean empty(String s) {
        if (s == null || s.trim().equals(""))
            return true;
        else
            return false;
    }

    public String hashCal(String type, String str) {
        byte[] hashseq = str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();

            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1) hexString.append("0");
                hexString.append(hex);
            }

        } catch (NoSuchAlgorithmException nsae) {
        }
        return hexString.toString();
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.startsWith("http")) {
                //Toast.makeText(getApplicationContext(),url ,Toast.LENGTH_LONG).show();
                progressDialog.show();
                view.loadUrl(url);
                System.out.println("myresult " + url);
                //return true;
            } else {
                return false;
            }
            return true;
        }
    }

    private final class PayUJavaScriptInterface {

        PayUJavaScriptInterface() {
        }

        @JavascriptInterface
        public void success(final long id, final String paymentId) {
            mHandler.post(new Runnable() {
                public void run() {
                    mHandler = null;

                    //System.out.println("againid is " + id + " paymentId is " + paymentId);
                    // Toast.makeText(getApplicationContext(), "Successfully payment", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(InstantPayuPayment.this, AfterInstantBookingStatus.class);
                    intent.putExtra("userName", getFirstName);
                    intent.putExtra("result", "success");
                    intent.putExtra("amount", String.valueOf(getConstAmount));
                    intent.putExtra("appoId", txnidd);
                    intent.putExtra("id", id);
                    intent.putExtra("paymentId", paymentId);
                    intent.putExtra("expertName", getExpertName);
                    intent.putExtra("consType", getConsType);
                    intent.putExtra("consDate", getConsultDate);
                    intent.putExtra("consTime", getConsultTime);
                    //System.out.println("parameters sent " + getConsultTime + " " + getFirstName + " " + getConstAmount + " " + txnidd + " " + paymentId + " " + getExpertName + " " + getConsType + " " + getConsultDate);
                    startActivity(intent);

                }
            });
        }

        @JavascriptInterface
        public void failure(final String id, String error) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    //cancelPayment();
                    Toast.makeText(getApplicationContext(), "Cancel payment", Toast.LENGTH_LONG).show();
                }
            });
        }

        @JavascriptInterface
        public void failure() {
            failure("");
        }

        @JavascriptInterface
        public void failure(final String params) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    System.out.println("payment failed code and error is " + params);
                    Toast.makeText(getApplicationContext(), "Payment failed", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(InstantPayuPayment.this, AfterInstantBookingStatus.class);
                    intent.putExtra("test", getFirstName);
                    intent.putExtra("result", "failed");
                    intent.putExtra("error", params);
                    startActivity(intent);
                }
            });
        }

    }
}
