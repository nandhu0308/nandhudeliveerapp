package com.example.raj.deliveryyy;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.raj.deliveryyy.Location.GPSTracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by venkat on 6/15/2017.
 */

public class UnDeliveredActivity extends AppCompatActivity{

    Button undelivered_save_button;
    EditText undelivered_save_edittext;
    int status;
    int MyDeviceAPI;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    LocationManager manager;
    LocationManager locationManager;
    private String bestProvider;
    InternetConnectionChecker connectionChecker;
    GoogleApiAvailability googleApiAvailability;
    boolean deliverd= false;
    double latitude,longitude;
//    EditText  mobilenumberedit, receiver_name;
    boolean Edp_Flage=false;
    TextView receivedlocationlat;
    int import_Flage,importFlag;
    String employeeCode,liveId,awbNo,statusCode,remarks,statusTime,statusDate,Svc_code, eempCode,phoneNo,revdBy;
    String eemp_code;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.undelivered_activity);
        if (MyDeviceAPI <=23){
            insertDummyContactWrapper();
        }
        connectionChecker = new InternetConnectionChecker();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("UnDeleviery Information");
        undelivered_save_button=(Button)findViewById(R.id.undelivered_save_button);

//        mobilenumberedit = (EditText) findViewById(R.id.mobilenumberedit);
//        receiver_name = (EditText) findViewById(R.id.receiverName);
        undelivered_save_edittext=(EditText)findViewById(R.id.remarksedit) ;

        statusCode=getIntent().getStringExtra("StatusCodeundelivery");
        Log.e("StatusCode",statusCode);

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        googleApiAvailability = GoogleApiAvailability.getInstance();
        status = googleApiAvailability.isGooglePlayServicesAvailable(UnDeliveredActivity.this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        bestProvider = locationManager.getBestProvider(criteria, false);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            Log.e("Permission", "check");
            if (status == ConnectionResult.SUCCESS){
                if (MyDeviceAPI < 23) {
                    location_tracker();
                } else {
                    if (checkWriteExternalPermission()) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, (LocationListener) this);
                    } else {
                        insertDummyContactWrapper();
                    }
                    location_tracker();
                }
            } else {
                int requestCodes = 10;
                Dialog dialog = googleApiAvailability.getErrorDialog(this, status, requestCodes);
                dialog.show();
            }
        }

        undelivered_save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remarks=undelivered_save_edittext.getText().toString();
//                phoneNo = mobilenumberedit.getText().toString().trim();
//                revdBy = receiver_name.getText().toString().trim();

                if (connectionChecker.isNetworkAvailable(UnDeliveredActivity.this)) {
//                   if (phoneNo.isEmpty()) {
//                        Toast.makeText(UnDeliveredActivity.this, "enter Phone Number", Toast.LENGTH_SHORT).show();
//                    } else if (revdBy.isEmpty()) {
//                        Toast.makeText(UnDeliveredActivity.this, "enter Reciver Name", Toast.LENGTH_SHORT).show();
//                    }
                    if (remarks.isEmpty()) {
                        Toast.makeText(UnDeliveredActivity.this, "enter Remarks", Toast.LENGTH_LONG).show();
                    }   else if (!remarks.isEmpty()) {
                        send_all_deta_undelivered();

                    }
                } else {
                    Toast.makeText(UnDeliveredActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
        SharedPreferences pref = getApplicationContext().getSharedPreferences("liveId", MODE_PRIVATE);
        liveId=pref.getString("liveIde","details");
        awbNo=pref.getString("awbNo","details");
        eempCode = pref.getString("eemp_code", "details");
        Svc_code = pref.getString("svc_code", "details");
        Edp_Flage = pref.getBoolean("Edp_Flag", false);
        importFlag = pref.getInt("Import_Flag",0);
        Log.e("livenumber", liveId);
        Log.e("awbNonumber", awbNo);
        Log.e("orgSvc", Svc_code);
        Log.e("dstSvc", Svc_code);
        Log.e("eemp_code", eempCode);
        Log.e("Edp_Flage", String.valueOf(Edp_Flage));
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        dateFormatter.setLenient(false);
        Date today = new Date();
        statusDate = dateFormatter.format(today);
        Log.e("date formate",statusDate);

        Date d= new Date();
        SimpleDateFormat tb =new SimpleDateFormat("HHmm");
        statusTime= tb.format(d);
        Log.e("timeFormare formate",statusTime);


        SharedPreferences pref2 = getApplicationContext().getSharedPreferences("employeecode", MODE_PRIVATE);
        employeeCode=pref2.getString("employeecodeee","notifye");
        Log.e("employeeCode",employeeCode);
    }


    public void location_tracker() {
        receivedlocationlat=(TextView)findViewById(R.id.receivedlocationlat);
        GPSTracker mGpsLocationTracker = new GPSTracker(this);
        if (mGpsLocationTracker.canGetLocation()) {
            latitude = mGpsLocationTracker.getLatitude();
            longitude = mGpsLocationTracker.getLongitude();
            receivedlocationlat.setText(
                    "Lat: " + latitude + "\nLong: " + longitude);
        }
    }

    private void buildAlertMessageNoGps(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, Do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void send_all_deta_undelivered() {
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("employeecode", MODE_PRIVATE);
        StringRequest sr = new StringRequest(Request.Method.POST, Contants.getSaveallDetailsUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("responce",response);
                try {
                    JSONObject object = new JSONObject(response);
                    String message = object.getString("message");
                    Toast.makeText(UnDeliveredActivity.this, message, Toast.LENGTH_SHORT).show();
                    System.out.println("Home_Activity.onResponseeeee" + response);
                    Intent intent = new Intent(UnDeliveredActivity.this, Home_Activity.class);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("send_all_contacts", error.getMessage());
                NetworkResponse networkResponse = error.networkResponse;
                if (error instanceof ServerError && networkResponse != null) {
                    try {
                            String res = new String(networkResponse.data,
                                    HttpHeaderParser.parseCharset(networkResponse.headers, "utf-8"));
                            JSONObject response = new JSONObject(res);
                            Toast.makeText(UnDeliveredActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();

                    } catch (JSONException ex) {
                        ex.printStackTrace();

                    }
                }
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(UnDeliveredActivity.this, "Seems your internet connection is slow, please try in sometime. ",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(UnDeliveredActivity.this, " Authentication error occurred, please try again later",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(UnDeliveredActivity.this, " Server error occurred, please try again later",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(UnDeliveredActivity.this, " Network error occurred, please try again later",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(UnDeliveredActivity.this, " An error occurred, please try again later",
                            Toast.LENGTH_LONG).show();
                }
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("delivered", deliverd);
                params.put("deliveryEmpCode", employeeCode);
                params.put("liveId", liveId);
                params.put("awbNo", awbNo);
                params.put("statusCode", statusCode);
                params.put("remarks", remarks);
                params.put("statusTime", statusTime);
                params.put("statusDate", statusDate);
                params.put("longitude", String.valueOf(longitude));
                params.put("latitude", String.valueOf(latitude));
                params.put("orgSvc", Svc_code);
                params.put("dstSvc", Svc_code);
                params.put("eempCode", eempCode);
                params.put("importFlag", import_Flage);
                params.put("edpFlag", Edp_Flage);
//                params.put("phoneNo", phoneNo);
//                params.put("revdBy", revdBy);


                Log.e("delivered", String.valueOf(deliverd));
                Log.e("deliveryEmpCode", employeeCode);
                Log.e("liveId", liveId);
                Log.e("awbNo", awbNo);
                Log.e("statusCode", statusCode);
                Log.e("longitude", String.valueOf(longitude));
                Log.e("latitude", String.valueOf(latitude));
                Log.e("remarks", remarks);
                Log.e("statusTime", statusTime);
//                Log.e("phoneNo", phoneNo);
//                Log.e("revdBy", revdBy);
                Log.e("statusDate", statusDate);
                Log.e("orgSvc", Svc_code);
                Log.e("dstSvc", Svc_code);
                Log.e("eemp_code", eempCode);
                Log.e("Edp_Flage", String.valueOf(Edp_Flage));


                JSONObject mapJson = new JSONObject(params);
                return mapJson.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> heder = new HashMap<String, String>();
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

    private void insertDummyContactWrapper(){
        if (Build.VERSION.SDK_INT >= 23){
            List<String> permissionsNeeded = new ArrayList<String>();
            final List<String> permissionsList = new ArrayList<String>();
            if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_FINE_LOCATION))
                permissionsNeeded.add("GPS");
            if (!addPermission(permissionsList, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
                permissionsNeeded.add("Write External");
            if (permissionsList.size() > 0) {
                if (permissionsNeeded.size() > 0) {
                    String message = "You need to grant access to " + permissionsNeeded.get(0);
                    for (int i = 1; i < permissionsNeeded.size(); i++)
                        message = message + ", " + permissionsNeeded.get(i);
                    showMessageOKCancel(message,
                            new DialogInterface.OnClickListener() {
                                @TargetApi(Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            });
                    return;
                }
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    //location_tracker();
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    Log.v("All permission granted", "Yes granted");
                } else {

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UnDeliveredActivity.this);
                    builder.setTitle("User Permission Required").setMessage("We need permission to continue this app").setCancelable(false)
                            .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    insertDummyContactWrapper();
                                }
                            }).setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(1);
                            finish();
                        }
                    });
                    final android.app.AlertDialog alert = builder.create();
                    alert.show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.app.AlertDialog.Builder(UnDeliveredActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }
        return true;
    }
    private boolean checkWriteExternalPermission(){
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == 1) {
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            } else {
                if (status == ConnectionResult.SUCCESS) {
                    if (MyDeviceAPI < 23) {
                        location_tracker();
                    } else {
                        if (checkWriteExternalPermission()) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, (LocationListener) this);
                        } else {
                            insertDummyContactWrapper();
                        }
                        location_tracker();
                    }
                } else {
                    int requestCodes = 10;
                    Dialog dialog = googleApiAvailability.getErrorDialog(UnDeliveredActivity.this, status, requestCodes);
                    dialog.show();
                }
            }
        }

    }

}
