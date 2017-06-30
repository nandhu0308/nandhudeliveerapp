package com.example.raj.deliveryyy.Successfull;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.raj.deliveryyy.CaptureSignature.CaptureSignature;
import com.example.raj.deliveryyy.Contants;
import com.example.raj.deliveryyy.Home_Activity;
import com.example.raj.deliveryyy.InternetConnectionChecker;
import com.example.raj.deliveryyy.Location.GPSTracker;
import com.example.raj.deliveryyy.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Sucessfull extends AppCompatActivity {
    Button b1, getcamera, save, clear;
    TextView receivedlocation;
    ArrayList<Relation_model> relation_deta_item = new ArrayList<Relation_model>();
    ArrayList<Getting_User_ID_List_Model> user_id_deta_item = new ArrayList<Getting_User_ID_List_Model>();
    Relationship_Adapter relationship_Adapter;
    Getting_User_ID_List_Adapter getting_user_id_list_adapter;
    GPSTracker gpsTracker;
    LocationManager manager;
    LocationManager locationManager;
    private String bestProvider;
    GoogleApiAvailability googleApiAvailability;
    int relationId;
    int id = 0;
    int status;
    boolean send_sueessfull = false;
    int MyDeviceAPI;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    ImageView signImage, scannimageView;
    private static final int CAMERA_REQUEST = 1888;
    Spinner spinner, getting_user_id_show;
    boolean deliverd = true;
    double latitude, longitude;
    InternetConnectionChecker connectionChecker;
    EditText gating_user_id_number, mobilenumberedit, receiver_name, remarksedit;
    boolean Edp_Flage=false;
    int import_Flage;


    String employeeCode, Svc_code, eempCode, liveId, awbNo, statusCode, userIdnumber, scanImg, signatureImg, remarks, idNo, phoneNo, revdBy, statusTime, statusDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucessfull);
        if (MyDeviceAPI <=23){
            insertDummyContactWrapper();
        }
        connectionChecker = new InternetConnectionChecker();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Deleviery Information");

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        googleApiAvailability = GoogleApiAvailability.getInstance();
        status = googleApiAvailability.isGooglePlayServicesAvailable(Sucessfull.this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        bestProvider = locationManager.getBestProvider(criteria, false);

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
                Dialog dialog = googleApiAvailability.getErrorDialog(this, status, requestCodes);
                dialog.show();
            }
        }

        SharedPreferences pref = getApplicationContext().getSharedPreferences("liveId", MODE_PRIVATE);
        liveId = pref.getString("liveIde", "details");
        awbNo = pref.getString("awbNo", "details");
        eempCode= pref.getString("eemp_code", "details");
        Svc_code = pref.getString("svc_code", "details");
        Edp_Flage = pref.getBoolean("Edp_Flag", false);
        import_Flage = pref.getInt("Import_Flag",0);
        Log.e("livenumber", liveId);
        Log.e("awbNonumber", awbNo);
        Log.e("orgSvc", Svc_code);
        Log.e("dstSvc", Svc_code);
        Log.e("eemp_code",eempCode);
        Log.e("Edp_Flage", String.valueOf(Edp_Flage));

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        dateFormatter.setLenient(false);
        Date today = new Date();
        statusDate = dateFormatter.format(today);
        Log.e("date formate", statusDate);

        Date d = new Date();
        SimpleDateFormat tb = new SimpleDateFormat("HHmm");
        statusTime = tb.format(d);
        Log.e("timeFormare formate", statusTime);

        statusCode = getIntent().getStringExtra("StatusCode");
        Log.e("StatusCode", statusCode);


        SharedPreferences pref2 = getApplicationContext().getSharedPreferences("employeecode", MODE_PRIVATE);
        employeeCode = pref2.getString("employeecodeee", "notifye");
        Log.e("employeeCode", employeeCode);
        SharedPreferences useridnumber = getApplicationContext().getSharedPreferences("userId", MODE_PRIVATE);
        userIdnumber = useridnumber.getString("userIdProof", "id");
        Log.e("userIdnumber", userIdnumber);

        gating_user_id_number = (EditText) findViewById(R.id.gating_user_id_number);
        mobilenumberedit = (EditText) findViewById(R.id.mobilenumberedit);
        receiver_name = (EditText) findViewById(R.id.receiver_name);
        remarksedit = (EditText) findViewById(R.id.remarksedit);

        save = (Button) findViewById(R.id.saveBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idNo = gating_user_id_number.getText().toString().trim();
                phoneNo = mobilenumberedit.getText().toString().trim();
                revdBy = receiver_name.getText().toString().trim();
                remarks = remarksedit.getText().toString().trim();
                if (connectionChecker.isNetworkAvailable(Sucessfull.this)) {
//                    if (idNo.isEmpty()) {
//                        Toast.makeText(Sucessfull.this, "enter Id number", Toast.LENGTH_LONG).show();
//                    }
                    if (phoneNo.isEmpty()) {
                        Toast.makeText(Sucessfull.this, "enter Phone Number", Toast.LENGTH_LONG).show();
                    } else if (revdBy.isEmpty()) {
                        Toast.makeText(Sucessfull.this, "Enter Received By Name", Toast.LENGTH_LONG).show();
                    }
//                    else if (remarks.isEmpty()) {
//                        Toast.makeText(Sucessfull.this, "Enter Remarks", Toast.LENGTH_LONG).show();
//                    }
                    else if (TextUtils.isEmpty(scanImg)) {
                        Toast.makeText(Sucessfull.this, "Put Image", Toast.LENGTH_LONG).show();
                    } else if (TextUtils.isEmpty(signatureImg)) {
                        Toast.makeText(Sucessfull.this, "Put Signature", Toast.LENGTH_LONG).show();
                    } else if ( !phoneNo.isEmpty() && !revdBy.isEmpty() &&  !TextUtils.isEmpty(signatureImg) && !TextUtils.isEmpty(scanImg)) {
                        send_all_deta();
                        Intent intent = new Intent(Sucessfull.this, Home_Activity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(Sucessfull.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        spinner = (Spinner) findViewById(R.id.relation_show);
        getting_user_id_show = (Spinner) findViewById(R.id.getting_user_id_show);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner.setSelection(i);
                relationId = i + 1;
                Log.e("spinnerpos", String.valueOf(relationId));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        getting_user_id_show.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getting_user_id_show.setSelection(i);
                id = i + 1;
                Log.e("getting_user_id_show", String.valueOf(id));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        clear = (Button) findViewById(R.id.clearBtn);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "cleared data", Toast.LENGTH_SHORT).show();
                spinner.setAdapter(relationship_Adapter);
                getting_user_id_show.setAdapter(getting_user_id_list_adapter);
                gating_user_id_number.setText("");
                mobilenumberedit.setText("");
                receiver_name.setText("");
                remarksedit.setText("");
                scannimageView.setVisibility(View.GONE);
                signImage.setVisibility(View.GONE);
            }
        });

        b1 = (Button) findViewById(R.id.signBtn);
        signImage = (ImageView) findViewById(R.id.signImage);
        b1.setOnClickListener(onButtonClick);

        if (connectionChecker.isNetworkAvailable(Sucessfull.this)) {
            relationvoly();
            gating_user_id_volley();
        } else {
            Toast.makeText(Sucessfull.this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }
        scannitem();
    }
    public void scannitem() {
        getcamera = (Button) findViewById(R.id.scannbutton);
        scannimageView = (ImageView) findViewById(R.id.scannImage);
        getcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
    }

    Button.OnClickListener onButtonClick = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Intent i = new Intent(Sucessfull.this, CaptureSignature.class);
            startActivityForResult(i, 0);
        }
    };

    public void location_tracker() {
        receivedlocation = (TextView) findViewById(R.id.receivedlocation);
        GPSTracker mGpsLocationTracker = new GPSTracker(this);
        if (mGpsLocationTracker.canGetLocation()) {
            latitude = mGpsLocationTracker.getLatitude();
            longitude = mGpsLocationTracker.getLongitude();
            receivedlocation.setText(
                    "Lat: " + latitude + "\nLong: " + longitude);
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, Do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == 2) {
            Bitmap b = BitmapFactory.decodeByteArray(
                    data.getByteArrayExtra("byteArray"), 0,
                    data.getByteArrayExtra("byteArray").length);
            signatureImg = new String(Base64.encodeToString(data.getByteArrayExtra("byteArray"), Base64.DEFAULT));
            Log.e("imagebase64", signatureImg);
            signImage.setImageBitmap(b);
            signImage.setVisibility(View.VISIBLE);
        }
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
                    Dialog dialog = googleApiAvailability.getErrorDialog(Sucessfull.this, status, requestCodes);
                    dialog.show();
                }
            }
        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bs);
            byte[] imageBytes = bs.toByteArray();
            scanImg = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            Log.e("imagebase64camera", scanImg);
            scannimageView.setImageBitmap(photo);
            scannimageView.setVisibility(View.VISIBLE);
        }
    }

    public void relationvoly() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("loading....");
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("employeecode", MODE_PRIVATE);
        StringRequest sr = new StringRequest(Request.Method.GET, Contants.getRelationUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("responce", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Relation_model model_relation;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        model_relation = new Relation_model();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        model_relation.setRelationId(jsonObject.getInt("relationId"));
                        model_relation.setRelationDescription(jsonObject.getString("relationDescription"));
                        relation_deta_item.add(model_relation);
                    }
                    relationship_Adapter = new Relationship_Adapter(Sucessfull.this, relation_deta_item);
                    spinner.setAdapter(relationship_Adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> heder = new HashMap<>();
                heder.put("Accept", "application/json");
                heder.put("Authorization", pref.getString("authToken", ""));
                return heder;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);
    }

    public void gating_user_id_volley() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("loading....");
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("employeecode", MODE_PRIVATE);
        StringRequest sr = new StringRequest(Request.Method.GET, Contants.getIdUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("responce", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Getting_User_ID_List_Model model_getting_user_id;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        model_getting_user_id = new Getting_User_ID_List_Model();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        model_getting_user_id.setId(jsonObject.getInt("id"));
                        model_getting_user_id.setIdDescription(jsonObject.getString("idDescription"));
                        user_id_deta_item.add(model_getting_user_id);
                    }
                    getting_user_id_list_adapter = new Getting_User_ID_List_Adapter(Sucessfull.this, user_id_deta_item);
                    getting_user_id_show.setAdapter(getting_user_id_list_adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> heder = new HashMap<>();
                heder.put("Accept", "application/json");
                heder.put("Authorization", pref.getString("authToken", ""));
                return heder;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);
    }

    public void send_all_deta() {
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("employeecode", MODE_PRIVATE);
        StringRequest sr = new StringRequest(Request.Method.POST, Contants.getSaveallDetailsUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("responce", response);
                try {
                    JSONObject object = new JSONObject(response);
                    String message = object.getString("message");
                    Toast.makeText(Sucessfull.this, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("send_all_contacts", error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(Sucessfull.this, "Seems your internet connection is slow,try after sometime. ",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(Sucessfull.this, " Authentication error occurred,try again later",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(Sucessfull.this, " Server error occurred,try again later",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(Sucessfull.this, " Network error occurred,try again later",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(Sucessfull.this, " An error occurred,try again later",
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
                params.put("scanImg", scanImg);
                params.put("longitude", String.valueOf(longitude));
                params.put("latitude", String.valueOf(latitude));
                params.put("remarks", remarks);
                params.put("idNo", idNo);
                params.put("id", id);
                params.put("phoneNo", phoneNo);
                params.put("revdBy", revdBy);
                params.put("statusTime", statusTime);
                params.put("statusDate", statusDate);
                params.put("orgSvc", Svc_code);
                params.put("dstSvc", Svc_code);
                params.put("eempCode", eempCode);
                params.put("importFlag", import_Flage);
                params.put("edpFlag", Edp_Flage);
                params.put("relationId", relationId);
                params.put("signatureImg", signatureImg);


                Log.e("delivered", String.valueOf(deliverd));
                Log.e("deliveryEmpCode", employeeCode);
                Log.e("liveId", liveId);
                Log.e("awbNo", awbNo);
                Log.e("statusCode", statusCode);
                Log.e("scanImg", scanImg);
                Log.e("longitude", String.valueOf(longitude));
                Log.e("latitude", String.valueOf(latitude));
                Log.e("remarks", remarks);
                Log.e("idNo", idNo);
                Log.e("statusTime", statusTime);
                Log.e("id", String.valueOf(id));
                Log.e("phoneNo", phoneNo);
                Log.e("revdBy", revdBy);
                Log.e("statusDate", statusDate);
                Log.e("relationId", String.valueOf(relationId));
                Log.e("signatureImg", signatureImg);
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
                heder.put("Authorization", pref.getString("authToken", ""));
                return heder;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);
    }

    private void insertDummyContactWrapper() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();
            final List<String> permissionsList = new ArrayList<String>();
            if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
                permissionsNeeded.add("GPS");
            if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
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
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        /*&& perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED*/) {
                    //location_tracker();
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }


                    /*locationManager.requestLocationUpdates(bestProvider, 0, 100, this);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 100, this);*/
                    Log.v("All permission granted", "Yes granted");
                } else {
                    /*Toast.makeText(SignUpActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();*/
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Sucessfull.this);
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
        new android.app.AlertDialog.Builder(Sucessfull.this)
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private boolean checkWriteExternalPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

}