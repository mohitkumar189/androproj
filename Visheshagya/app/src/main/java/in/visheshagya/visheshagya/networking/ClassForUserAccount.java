/*
    @Auther MOHIT KUMAR
    Created on 15/09/2016
 * Copyright (c) Visheshagya Pvt Ltd.
 */
package in.visheshagya.visheshagya.networking;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import in.visheshagya.visheshagya.JSONDownloaderPackage.JSONDownloader;
import in.visheshagya.visheshagya.resourses.APIUrls;
import in.visheshagya.visheshagya.storagePackage.SharedPrefsClass;

public class ClassForUserAccount {
    private Context mctx;
    private boolean success = false;
    private String response = "";

    public ClassForUserAccount(Context ctx) {
        this.mctx = ctx;
    }

    public boolean loginClient(String userEmail, String userPassword, int clientType) {
        String loginUrl = APIUrls.VISHESHAGYA_URL + APIUrls.LOGIN_URL + "email="
                + userEmail + "&password=" + userPassword + "&role=" + clientType;
        System.out.println("url " + loginUrl);

        try {
            response = new Downloader().execute(loginUrl).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("Json to be processed " + response);

        try {
            JSONObject jsonObject = new JSONObject(response);
            int successRESULT = jsonObject.getInt("success");
            String message = jsonObject.getString("message");
            String mobileNumber = jsonObject.getString("contact");
            String userName = jsonObject.getString("name");
            if (successRESULT == 1) {
                //Toast.makeText(mctx, "success", Toast.LENGTH_SHORT).show();
                SharedPrefsClass sharedPrefsClass = new SharedPrefsClass(mctx);
                sharedPrefsClass.setLoginStatus(true);
                sharedPrefsClass.setClientId(message);
                sharedPrefsClass.setMobileNumber(mobileNumber);
                sharedPrefsClass.setEmailAddress(userEmail);
                sharedPrefsClass.setUserName(userName);
                success = true;
            } else {
                success = false;
            }
            // System.out.println("RESPONSE FROM SERVER is " + response.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            //  System.out.println("SOMETHING WENT WRONG " + e.toString());
        }
        return success;
    }

    public boolean signupClient(String name, String userEmail, String mobileNumber, int clientType) {
        String passwordRECEIVED;
        String registerUrl = APIUrls.VISHESHAGYA_URL + APIUrls.REGISTER_URL +
                "name=" + name + "&email=" + userEmail + "&contactNo=" + mobileNumber + "&role=" + clientType;
        System.out.println("url is: " + registerUrl);
        try {
            response = new Downloader().execute(registerUrl).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //System.out.println("Json to be processed " + response);

        try {
            JSONObject jsonObject = new JSONObject(response);
            int successRESULT = jsonObject.getInt("success");
            passwordRECEIVED = jsonObject.getString("message").toString().trim();
            //System.out.println("The message from server is " + successRESULT);
            if (successRESULT == 1) {
                // Toast.makeText(mctx, passwordRECEIVED, Toast.LENGTH_SHORT).show();
                success = true;
                success = false;
                // Toast.makeText(mctx, passwordRECEIVED, Toast.LENGTH_SHORT).show();
            }
            //System.out.println("RESPONSE FROM SERVER is " + response.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            //System.out.println("SOMETHING WENT WRONG " + e.toString());
        }
        return success;
    }

    public boolean fogotPassword(String userEmail, String mobileNumber, String clientType) {
        String registerUrl = APIUrls.VISHESHAGYA_URL + APIUrls.RESET_PASSWORD +
                "emailId=" + userEmail + "&contactNo=" + mobileNumber + "&role=" + clientType;

        try {
            response = new Downloader().execute(registerUrl).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("Json to be processed " + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            int successRESULT = jsonObject.getInt("success");
            //System.out.println("The message from server is " + successRESULT);
            if (successRESULT == 1) {
                Toast.makeText(mctx, "Password has sent on registered mobile", Toast.LENGTH_SHORT).show();
                success = true;
            } else {
                success = false;
                //Toast.makeText(mctx, "wrong details", Toast.LENGTH_SHORT).show();
            }
            // System.out.println("RESPONSE FROM SERVER is " + response.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            // System.out.println("SOMETHING WENT WRONG " + e.toString());
        }
        return success;
    }

    class Downloader extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(mctx);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            //pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            //System.out.println("URL for response is "+url);
            String response = new JSONDownloader().jsonData(url);
            return response;
        }

        @Override
        protected void onPostExecute(String receivedResponse) {
            super.onPostExecute(receivedResponse);
            if (pDialog.isShowing()) {
                System.out.println("output by async task " + receivedResponse);
                pDialog.dismiss();
            }
        }
    }
}
