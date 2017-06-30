package com.example.raj.deliveryyy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.raj.deliveryyy.status.StatusDesc;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by venkat on 6/14/2017.
 */

public class AwbDetails extends AppCompatActivity {

    String Username;
    String employeeCode;
    String awbNoList;
    String awbDetails;
    String awbNumber;
    String awbNum;
    InternetConnectionChecker connectionChecker;
    private Button status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.awb_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("AWB Details");
        connectionChecker = new InternetConnectionChecker();
        Intent intent = getIntent();
        awbNumber = intent.getStringExtra("awbNumber");

        if (connectionChecker.isNetworkAvailable(AwbDetails.this)){
            loadRecyclerStatus();
        } else {
            Toast.makeText(AwbDetails.this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }
        status=(Button)findViewById(R.id.goToStatus);

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(AwbDetails.this,StatusDesc.class);
                startActivity(intent1);
            }
        });
    }
    private void loadRecyclerStatus(){
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("employeecode", MODE_PRIVATE);

        StringRequest stringRequest=new StringRequest(Request.Method.GET, Contants.getAwbDetailsUrl()+awbNumber, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Responce Awb details",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    SharedPreferences sharedPreferences=getSharedPreferences("liveId", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1=sharedPreferences.edit();
                    editor1.putString("liveIde",jsonObject.getString("liveId"));
                    editor1.putString("awbNo",jsonObject.getString("awbNo"));
                    editor1.putString("svc_code",jsonObject.getString("svcCode"));
                    editor1.putString("eemp_code",jsonObject.getString("eempCode"));
                    editor1.putBoolean("Edp_Flag",jsonObject.getBoolean("edpFlag"));
                    editor1.putInt("Import_Flag",jsonObject.getInt("importFlag"));
                    editor1.apply();

                    TextView liveId,awbNo,cityCode,svcCode,scanDtTime,statusCode,empCode,eempCode,deliveryPrintNo,printOrder;
                    liveId=(TextView)findViewById(R.id.details_textView1);
                    liveId.setText(jsonObject.getString(Contants.KEY_AWB_LIVEID));
                    awbNo=(TextView)findViewById(R.id.details_textView2);
                    awbNo.setText(jsonObject.getString(Contants.KEY_AWB_AWBNO));
                    cityCode=(TextView)findViewById(R.id.details_textView3);
                    cityCode.setText(jsonObject.getString(Contants.KEY_AWB_CITYCODE));
                    svcCode=(TextView)findViewById(R.id.details_textView4);
                    svcCode.setText(jsonObject.getString(Contants.KEY_AWB_SVCCODE));
                    scanDtTime=(TextView)findViewById(R.id.details_textView5);
                    scanDtTime.setText(jsonObject.getString(Contants.KEY_AWB_SCANDTTIME));
                    statusCode=(TextView)findViewById(R.id.details_textView6);
                    statusCode.setText(jsonObject.getString(Contants.KEY_AWB_STATUSCODE));
                    empCode=(TextView)findViewById(R.id.details_textView7);
                    empCode.setText(jsonObject.getString(Contants.KEY_AWB_EMPCODE));
                    eempCode=(TextView)findViewById(R.id.details_textView8);
                    eempCode.setText(jsonObject.getString(Contants.KEY_AWB_EEMPCODE));
                    deliveryPrintNo=(TextView)findViewById(R.id.details_textView9);
                    deliveryPrintNo.setText(jsonObject.getString(Contants.KEY_AWB_DELIVERYPRINTNO));
                    printOrder=(TextView)findViewById(R.id.details_textView10);
                    printOrder.setText(jsonObject.getString(Contants.KEY_AWB_PRINTORDER));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(AwbDetails.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Authorization", pref.getString("authToken",""));
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}

