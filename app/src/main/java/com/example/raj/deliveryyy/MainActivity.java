package com.example.raj.deliveryyy;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private EditText editUserName, editPassword;
    String memployeecode;
    InternetConnectionChecker connectionChecker;
    String musername;
    public static final String SHARED_PREFERENCE_CHECKLOGIN = "shared_preference_checklogin";

    private Button loginButton;
    ProgressDialog progressDialog;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        generatehash();
        SharedPreferences preferences = getSharedPreferences("employeecode", Context.MODE_PRIVATE);
        String checkAuth = preferences.getString("authToken", "");
        if (!TextUtils.isEmpty(checkAuth)) {
            Intent intent = new Intent(MainActivity.this, Home_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        connectionChecker = new InternetConnectionChecker();
        editUserName = (EditText) findViewById(R.id.useredit);
        editPassword = (EditText) findViewById(R.id.passwordedit);
        loginButton = (Button) findViewById(R.id.loginBtn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = editUserName.getText().toString().trim();
                String password = editPassword.getText().toString().trim();
                if (username.isEmpty()) {
                    Toast.makeText(MainActivity.this, "enter username", Toast.LENGTH_LONG).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "enter password", Toast.LENGTH_LONG).show();

                } else if (!username.isEmpty() && !password.isEmpty()) {
                    if (connectionChecker.isNetworkAvailable(MainActivity.this)) {
                        checkLogin(username, password);
                    } else {
                        Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    public void generatehash(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }
    public void checkLogin(final String username, final String password) {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.cancel();
                dialog.dismiss();
            }
        });
        progressDialog.setMessage(getString(R.string.login_spinner_message));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, Contants.getLoginUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.setCancelable(true);
                        progressDialog.dismiss();
                        try {
                            Log.e("responsessss", response);
                            JSONObject jObject = new JSONObject(response);
                            memployeecode = jObject.getString("employeeCode");
                            SharedPreferences sharedPreferences = getSharedPreferences("employeecode", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor1 = sharedPreferences.edit();
                            editor1.putString("employeecodeee", memployeecode);
                            editor1.putString("authToken", jObject.getString("userAuthToken"));
                            editor1.apply();
                            musername = jObject.getString("username");
                            SharedPreferences sharedPreferencesusername = getSharedPreferences("musername", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferencesusername.edit();
                            editor.putString("musernameeee", musername);
                            editor.apply();

                            SharedPreferences sharedPrefchecklogin = getSharedPreferences(SHARED_PREFERENCE_CHECKLOGIN, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editorFb1 = sharedPrefchecklogin.edit();
                            editorFb1.putBoolean("Login", true);
                            editorFb1.apply();

                            if (jObject != null && jObject.length() > 0) {
                                Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this, Home_Activity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, "check login credentials", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Log.e("Json response", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                NetworkResponse networkResponse = error.networkResponse;
                if (error instanceof ServerError && networkResponse != null) {
                    try {
                        String res = new String(networkResponse.data,
                                HttpHeaderParser.parseCharset(networkResponse.headers, "utf-8"));
                        Toast.makeText(MainActivity.this, res, Toast.LENGTH_LONG).show();

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                Map<String, String> jsonRequestBodyMap = new HashMap<>();
                jsonRequestBodyMap.put("username", username);
                jsonRequestBodyMap.put("password", password);
                JSONObject mapJson = new JSONObject(jsonRequestBodyMap);
                return mapJson.toString().getBytes();
            }

        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }
}

