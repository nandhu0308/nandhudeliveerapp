package com.example.raj.deliveryyy.report;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.raj.deliveryyy.Contants;
import com.example.raj.deliveryyy.InternetConnectionChecker;
import com.example.raj.deliveryyy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Report extends AppCompatActivity {
    CustomAdepter adepter;
    Spinner_Report_Adapter spinner_report_adapter;
    Spinner_Report_Adapter_new spinner_report_adapter_new;
    Undelevered_Report_Adapter undelevered_report_adapter;
    ListView listView;
    private ArrayList<Report_Model> deta_item;
    private ArrayList<Undeliverd_Model> undelivery_deta_item;
    private ArrayList<String> spinner_list=new ArrayList<String>();
    ArrayList<Spinner_Model> spinner_deta_item = new ArrayList<Spinner_Model>();
    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 999;
    static final int DATE_DIALOG_ID_END_DATE = 99;
    LinearLayout startdate,enddate;
    EditText startdatetext,enddatetext;
    Button button_show_report;
    String EmployCode,start__date,end__date;
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog todatePickerDialog;
    Spinner report_spinner;
    InternetConnectionChecker connectionChecker;
    int spinner_position=0;
    ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_report);
        connectionChecker = new InternetConnectionChecker();
        report_spinner=(Spinner)findViewById(R.id.report_spinner);
        spinner_list.add("Delivered");
        spinner_list.add("UnDelivered");
        spinner_report_adapter_new=new Spinner_Report_Adapter_new(Report.this,spinner_list);
        report_spinner.setAdapter(spinner_report_adapter_new);
        getSupportActionBar().show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Report");
        button_show_report=(Button)findViewById(R.id.button_show_report);
        startdatetext = (EditText) findViewById(R.id.start_date);
        enddatetext = (EditText) findViewById(R.id.enddatetext);
        startdatetext.setInputType(InputType.TYPE_NULL);
        enddatetext.setInputType(InputType.TYPE_NULL);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        setDateTimeField();
        dateFormatter.setLenient(false);
        Date today = new Date();
        start__date = dateFormatter.format(today);
        startdatetext.setText(start__date);

        undelivery_deta_item = new ArrayList<Undeliverd_Model>();
        deta_item = new ArrayList<Report_Model>();

        DateFormat dateFormatterend = new SimpleDateFormat("yyyy-MM-dd");
        setDateFieldto();
        dateFormatterend.setLenient(false);
        Date todayend = new Date();
        end__date = dateFormatterend.format(todayend);
        enddatetext.setText(start__date);
        Log.e("date formate",end__date);
        edit_date_clivk();
       // spinnervolely();
        SharedPreferences sharedPreferences = getSharedPreferences("employeecode", Context.MODE_PRIVATE);
        EmployCode=sharedPreferences.getString("employeecodeee","notify");
        Log.e("EmployCode",EmployCode);
        button_show_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start__date=startdatetext.getText().toString();
                end__date=enddatetext.getText().toString();
                undelivery_deta_item.clear();
                deta_item.clear();
                Log.e("EmployCode",EmployCode);
                Log.e("startdate",start__date);
                Log.e("enddate",end__date);
                if (spinner_position==0){
                    //  undelevered_report_adapter.notifyDataSetChanged();
                    undelivery_deta_item.clear();
                    if (connectionChecker.isNetworkAvailable(Report.this)){
                        report_delevired_voly();
                    }else {
                        Toast.makeText(Report.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                    for (Undeliverd_Model model : undelivery_deta_item) {
                        undelivery_deta_item.add(model);
                    }
                } else {
                    //  adepter.notifyDataSetChanged();
                    deta_item.clear();
                    if (connectionChecker.isNetworkAvailable(Report.this)){
                        report_undelevired_voly();
                    }else {
                        Toast.makeText(Report.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }

                    for (Report_Model model : deta_item) {
                        deta_item.add(model);
                    }
                }
            }
        });
        listView = (ListView) findViewById(R.id.listview);

        report_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                report_spinner.setSelection(i);
                spinner_position=i;
                undelivery_deta_item.clear();
                deta_item.clear();
                if (adepter != null)
                    adepter.notifyDataSetChanged();
                if (undelevered_report_adapter != null)
                    undelevered_report_adapter.notifyDataSetChanged();
                Log.e("spinnerpos", String.valueOf(i));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void edit_date_clivk(){
        startdatetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromDatePickerDialog.show();
                setDateTimeField();
            }
        });
        enddatetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateFieldto();
                todatePickerDialog.show();
            }
        });
    }
    private void setDateTimeField(){
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                startdatetext.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
    private void setDateFieldto(){
        Calendar newCalendar = Calendar.getInstance();
        todatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                enddatetext.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    public void spinnervolely() {
        progressDialog = new ProgressDialog(Report.this);
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        progressDialog.setMessage(getString(R.string.login_spinner_message));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("employeecode", MODE_PRIVATE);
        StringRequest sr = new StringRequest(Request.Method.GET, Contants.getStatusUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("responce",response);
                progressDialog.setCancelable(true);
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Spinner_Model spinner_model;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        spinner_model = new Spinner_Model();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        spinner_model.setStatusDesc(jsonObject.getString("statusDesc"));
                        spinner_deta_item.add(spinner_model);
                    }
                    spinner_report_adapter=new Spinner_Report_Adapter(Report.this,spinner_deta_item);
                    report_spinner.setAdapter(spinner_report_adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                VolleyLog.d("ERROR",error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> heder=new HashMap<>();
                heder.put("Accept", "application/json");
                heder.put("Authorization", pref.getString("authToken",""));
                return heder;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);
    }
    public void report_delevired_voly() {
        progressDialog = new ProgressDialog(Report.this);
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        progressDialog.setMessage(getString(R.string.login_spinner_message));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("employeecode", MODE_PRIVATE);
        final StringRequest sr = new StringRequest(Request.Method.GET, Contants.getDeliveredReportUrl()+EmployCode+"/"+start__date+"/"+end__date, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("responce",response);
                progressDialog.setCancelable(true);
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Report_Model model;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        model = new Report_Model();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        model.setAwbNo(jsonObject.getString("awbNo"));
                        model.setDeliveryEmpCode(jsonObject.getString("deliveryEmpCode"));
                        model.setPhoneNo(jsonObject.getString("phoneNo"));
                        model.setRevdBy(jsonObject.getString("revdBy"));
                        model.setStatusDate(jsonObject.getString("statusDate"));
                        deta_item.add(model);
                    }
                    undelivery_deta_item.clear();
                    //  undelivery_deta_item.addAll(deta_item);

                    adepter=new CustomAdepter(Report.this,deta_item);
                    listView.setAdapter(adepter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                VolleyLog.d("ERROR",error.getMessage());
                NetworkResponse networkResponse = error.networkResponse;
                if (error instanceof ServerError && networkResponse != null) {
                    try {
                        if (networkResponse.statusCode==404){
                            String res = new String(networkResponse.data,
                                    HttpHeaderParser.parseCharset(networkResponse.headers, "utf-8"));
                            JSONObject response = new JSONObject(res);
                            Toast.makeText(Report.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> heder=new HashMap<>();
                heder.put("Accept", "application/json");
                heder.put("Authorization", pref.getString("authToken",""));
                return heder;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);
    }
    public void report_undelevired_voly() {
        progressDialog = new ProgressDialog(Report.this);
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        progressDialog.setMessage(getString(R.string.login_spinner_message));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("employeecode", MODE_PRIVATE);
        final StringRequest sr = new StringRequest(Request.Method.GET, Contants.getUndeliveredReportUrl()+EmployCode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("responce",response);
                progressDialog.setCancelable(true);
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Undeliverd_Model undelivery_model;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        undelivery_model = new Undeliverd_Model();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        undelivery_model.setAwbNo(jsonObject.getString("awbNo"));
                        undelivery_model.setLiveId(jsonObject.getString("liveId"));
                        undelivery_model.setStatusTime(jsonObject.getString("statusTime"));
                        undelivery_model.setStatusDate(jsonObject.getString("statusDate"));
                        undelivery_model.setStatusCode(jsonObject.getString("statusCode"));
                        undelivery_deta_item.add(undelivery_model);
                    }
                    deta_item.clear();

                    undelevered_report_adapter=new Undelevered_Report_Adapter(Report.this,undelivery_deta_item);
                    listView.setAdapter(undelevered_report_adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR",error.getMessage());
                progressDialog.dismiss();
                NetworkResponse networkResponse = error.networkResponse;
                if (error instanceof ServerError && networkResponse != null) {
                    try {
                        if (networkResponse.statusCode==404){
                            String res = new String(networkResponse.data,
                                    HttpHeaderParser.parseCharset(networkResponse.headers, "utf-8"));
                            JSONObject response = new JSONObject(res);
                            Toast.makeText(Report.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (UnsupportedEncodingException e) {
                    } catch (JSONException e) {
                    }
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> heder=new HashMap<>();
                heder.put("Accept", "application/json");
                heder.put("Authorization", pref.getString("authToken",""));
                return heder;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}





