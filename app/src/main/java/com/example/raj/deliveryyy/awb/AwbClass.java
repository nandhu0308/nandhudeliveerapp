package com.example.raj.deliveryyy.awb;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
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
import com.example.raj.deliveryyy.Contants;
import com.example.raj.deliveryyy.InternetConnectionChecker;
import com.example.raj.deliveryyy.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AwbClass extends AppCompatActivity {
    private RecyclerView recyclerView;

    private ArrayList<String> items = new ArrayList<String>();
    String Username;
    String employeeCode;
    InternetConnectionChecker connectionChecker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awb_class);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("AWB Number");

        SharedPreferences pref = getApplicationContext().getSharedPreferences("employeecode", MODE_PRIVATE);
        employeeCode = pref.getString("employeecodeee", "notifye");

        Log.e("employeeCode", employeeCode);

        recyclerView = (RecyclerView) findViewById(R.id.awbrecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        connectionChecker = new InternetConnectionChecker();
        if (connectionChecker.isNetworkAvailable(AwbClass.this)) {
            loadRecyclerStatus();
        } else {
            Toast.makeText(AwbClass.this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    private void loadRecyclerStatus() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("employeecode", MODE_PRIVATE);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Contants.getAwbUrl() + employeeCode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("AWB responce", response);

                progressDialog.dismiss();
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    Gson gson = new Gson();
                    Type type = new TypeToken<AwbNo>() {
                    }.getType();
                    AwbNo awbNoList = gson.fromJson(response.toString(), type);

                    AwbAdapter adapter = new AwbAdapter(awbNoList.getAwbNoList(), AwbClass.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(AwbClass.this));
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {

                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        if (response.statusCode == 404) {
                            try {
                                JSONObject errorJson = new JSONObject(res);
                                if (!errorJson.isNull("message")) ;
                            } catch (JSONException e) {
                            }
                        } else {
                        }
                    } catch (UnsupportedEncodingException e1) {
                        Log.e("ERROR", "", e1);
                    }
                } else {
                }

                progressDialog.dismiss();
                Toast.makeText(AwbClass.this, error.toString(), Toast.LENGTH_LONG).show();
            }


        }) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                Map<String, String> jsonRequestBodyMap = new HashMap<>();
                jsonRequestBodyMap.put("username", "username");
                jsonRequestBodyMap.put("password", "password");
                jsonRequestBodyMap.put("employeeCode", employeeCode);

                JSONObject mapJson = new JSONObject();
                return mapJson.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Authorization", pref.getString("authToken", ""));

                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
