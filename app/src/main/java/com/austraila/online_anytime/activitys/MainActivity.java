package com.austraila.online_anytime.activitys;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.austraila.online_anytime.Common.Common;
import com.austraila.online_anytime.LocalManage.DatabaseHelper;
import com.austraila.online_anytime.LocalManage.ElementDatabaseHelper;
import com.austraila.online_anytime.LocalManage.ElementOptionDatabaseHelper;
import com.austraila.online_anytime.LocalManage.ElementValueDatabaeHelper;
import com.austraila.online_anytime.LocalManage.FormDatabaeHelper;
import com.austraila.online_anytime.R;
import com.austraila.online_anytime.activitys.LoginDepartment.LoginActivity;
import com.austraila.online_anytime.adapter.CustomAdapter;
import com.austraila.online_anytime.model.Listmodel;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    TextView reloadBtn;
    private DrawerLayout drawer;
    private NavigationView navigation;
    private SQLiteDatabase db,Db,EDb,ODb,VDb;
    private SQLiteOpenHelper openHelper,FormopenHelper,ElementopenHelper,ElementOptionopenHelper,ElementValueHelper;
    RelativeLayout loading;
    ListView listView;
    ArrayList Listitem=new ArrayList<>();
    JSONArray ApiList = new JSONArray();
    JSONArray ElemnetList = new JSONArray();
    JSONArray ElemnetOptionList = new JSONArray();
    SearchView searchView;
    String useremail, result, checksum, token, userepass,formId,ElementValue, ElementId, ElementType;
    CustomAdapter myAdapter;
    RequestQueue queue;
    ArrayList<String> listFormId = new ArrayList<String>();
    ArrayList<String> listFormDes = new ArrayList<String>();
    ArrayList<String> listFormtitle = new ArrayList<String>();
    ArrayList<String> data = new ArrayList<String>();
    Map<String, Map<String, String>> elementdata = new HashMap<String, Map<String, String>>();
    List<String> value = new ArrayList<String>();
    ArrayList<String> groupkeyList = new ArrayList<String>();
    Boolean checksend = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //defile the element
        listView = findViewById(R.id.mainListView);
        searchView = findViewById(R.id.search_view);
        reloadBtn = findViewById(R.id.reload_btn);
        loading = findViewById(R.id.loadingLayout);
        loading.setVisibility(View.VISIBLE);

        ProgressBar progressBar = findViewById(R.id.loading_spinner);
        TextView progressTxt = findViewById(R.id.loading_txt);
        progressBar.setVisibility(View.GONE);
        progressTxt.setText("Please wait while App syncs.");
        //local database define
        openHelper = new DatabaseHelper(this);
        FormopenHelper = new FormDatabaeHelper(this);
        ElementopenHelper = new ElementDatabaseHelper(this);
        ElementOptionopenHelper = new ElementOptionDatabaseHelper(this);
        ElementValueHelper = new ElementValueDatabaeHelper(this);
        db = openHelper.getWritableDatabase();
        Db = FormopenHelper.getWritableDatabase();
        EDb = ElementopenHelper.getWritableDatabase();
        ODb = ElementOptionopenHelper.getWritableDatabase();
        VDb = ElementValueHelper.getWritableDatabase();

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Log.e("back", "back" );
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        };
        new MainActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        Cursor cursor = Db.rawQuery("SELECT *FROM " + FormDatabaeHelper.FORMTABLE_NAME,  null);
        if (cursor.moveToFirst()) // data?
            checksum = cursor.getString(cursor.getColumnIndex("Fchecksum"));
        cursor.close();

        Cursor fcursor = db.rawQuery("SELECT *FROM " + DatabaseHelper.TABLE_NAME,  null);
        if(fcursor != null){
            if (fcursor.moveToFirst()){
                do{
                    token = fcursor.getString(fcursor.getColumnIndex("token"));
                }while(fcursor.moveToNext());
            }
            fcursor.close();
        }

        final Cursor Vcursor = VDb.rawQuery("SELECT *FROM " + ElementValueDatabaeHelper.VTABLE_NAME, null);
        if(Vcursor != null){
            if (Vcursor.moveToFirst()){
                do{
                    formId = Vcursor.getString(Vcursor.getColumnIndex("ElementFormId"));
                    ElementId = Vcursor.getString(Vcursor.getColumnIndex("ElementId"));
                    ElementValue = Vcursor.getString(Vcursor.getColumnIndex("ElementValue"));
                    ElementType = Vcursor.getString(Vcursor.getColumnIndex("ElementType"));
                    String ekey = formId +"_"+ ElementType;
                    if(!elementdata.containsKey(ekey)){
                        Map<String, String> FData = new HashMap<String, String>();
                        FData.put("formId", formId);
                        elementdata.put(ekey, FData);
                    }
                    elementdata.get(ekey).put(ElementId, ElementValue);
                    value.add(formId +"_"+ ElementType);
                }while(Vcursor.moveToNext());
            }
            Vcursor.close();
        }

        for(int i = 0; i < value.size(); i++){
            String key = value.get(i);
            if(!groupkeyList.contains(key)){
                groupkeyList.add(key);
            }else{
                continue;
            }
        }

        if(groupkeyList != null){
            if(isNetworkAvailable()){
                Log.e("checksend", "send" );
                progressTxt.setText("sending data...");
                progressBar.setVisibility(View.VISIBLE);
                loading.setVisibility(View.VISIBLE);
                for(int i = 0; i < groupkeyList.size(); i++){
                    sendData(groupkeyList.get(i));
                }
                checksend = true;
                deleteLocal();
            }
        }

        reloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

        Cursor Ccursor = Db.rawQuery("SELECT *FROM " + FormDatabaeHelper.FORMTABLE_NAME,  null);
        if(Ccursor.getCount() == 0){
            init();
        }else {
            ListviewManagement();
        }

        sideMenu_mangement();
        listView.setTextFilterEnabled(true);
        setupSearchView();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void sendData(final String formid) {
        String url = Common.getInstance().getSaveUrl();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            result = jsonObject.getString("success");
                            if (result.equals("true")){
                                FormActivity.elementPhotos.clear();
                                FormActivity.elementSignature.clear();
                                FormActivity.element_data.clear();

                                checksend = true;

                            } else {
                                loading.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this, "Oops, Request failed.", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
//                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "request faild", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        System.out.println(error);
                        checksend = false;
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.offline_text), Toast.LENGTH_LONG).show();
                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("token", token);
                return headers;
            }

            @Override
            protected Map<String, String> getParams()
            {
                String[] separated = formid.split("_");
                elementdata.get(formid).put("formId", separated[0]);
                elementdata.get(formid).put("id", "0");
                elementdata.get(formid).put("Final", "end");
                Log.e("senddata local:", String.valueOf(elementdata.get(formid)));
                return elementdata.get(formid);
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                2500000, 0, 1f
        ));
        queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(postRequest);
    }

    private void deleteLocal(){
        if(checksend == true){
            loading.setVisibility(View.GONE);
            VDb.execSQL("delete from "+ ElementValueDatabaeHelper.VTABLE_NAME);
            checksend = false;
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(MainActivity.this, "Sorry, You can't go back to the form.", Toast.LENGTH_LONG).show();
    }

    private void reload() {
        loading.setVisibility(View.VISIBLE);
        final Cursor cursor = db.rawQuery("SELECT *FROM " + DatabaseHelper.TABLE_NAME,  null);
        if(cursor != null){
            if (cursor.moveToFirst()){
                do{
                    useremail = cursor.getString(cursor.getColumnIndex("Gmail"));
                    userepass = cursor.getString(cursor.getColumnIndex("Password"));
                }while(cursor.moveToNext());
            }
            cursor.close();
        }

        String url = Common.getInstance().getBaseURL();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            result = jsonObject.getString("success");
                            if (result.equals("true")){

                                Db.execSQL("delete from "+ FormDatabaeHelper.FORMTABLE_NAME);
                                EDb.execSQL("delete from "+ ElementDatabaseHelper.ElEMENTTABLE_NAME);
                                ODb.execSQL("delete from "+ ElementOptionDatabaseHelper.OPTIONTABLE_NAME);
                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                loading.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this, "Oops, can't login! please try to login again.", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        System.out.println(error);
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.offline_text), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", useremail);
                params.put("password", userepass);
                return params;
            }
        };
        queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(postRequest);
    }
    
    private void elementSave() throws JSONException {
        for (int i = 0; i < ApiList.length(); i ++){
            final String formId = ApiList.getJSONObject(i).getString("form_id");
            StringRequest postRequest = new StringRequest(Request.Method.GET, Common.getInstance().getFormelementUrl() + formId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                        result = jsonObject.getString("success");
                        if (result.equals("true")){
                            ElemnetList = jsonObject.getJSONArray("forms");

                            for(int j = 0; j < ElemnetList.length(); j++){
                                insertElementData(ElemnetList.getJSONObject(j).getString("element_id")
                                        ,ElemnetList.getJSONObject(j).getString("element_title")
                                        ,ElemnetList.getJSONObject(j).getString("element_guidelines")
                                        ,ElemnetList.getJSONObject(j).getString("element_type")
                                        ,ElemnetList.getJSONObject(j).getString("element_position")
                                        ,ElemnetList.getJSONObject(j).getString("element_page_number")
                                        ,ElemnetList.getJSONObject(j).getString("element_default_value")
                                        ,ElemnetList.getJSONObject(j).getString("element_constraint")
                                        ,ElemnetList.getJSONObject(j).getString("element_address_hideline2")
                                        ,formId
                                        ,ElemnetList.getJSONObject(j).getString("element_media_type")
                                        ,ElemnetList.getJSONObject(j).getString("element_media_image_src")
                                        ,ElemnetList.getJSONObject(j).getString("element_media_pdf_src"));
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Oops, Request failed..", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error);
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.offline_text), Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("token", token);
                    return headers;
                }
            };
            queue = Volley.newRequestQueue(MainActivity.this);
            queue.add(postRequest);
        }
    }

    private void elementOptionSave() {
        StringRequest postRequest = new StringRequest(Request.Method.GET, Common.getInstance().getElemnetOptionUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    result = jsonObject.getString("success");
                    if (result.equals("true")){
                        ElemnetOptionList = jsonObject.getJSONArray("forms");
                        for(int j = 0; j < ElemnetOptionList.length(); j++){
                            insertElementOptionData(ElemnetOptionList.getJSONObject(j).getString("form_id")
                                    ,ElemnetOptionList.getJSONObject(j).getString("element_id")
                                    ,ElemnetOptionList.getJSONObject(j).getString("option_id")
                                    ,ElemnetOptionList.getJSONObject(j).getString("position")
                                    ,ElemnetOptionList.getJSONObject(j).getString("option")
                                    ,ElemnetOptionList.getJSONObject(j).getString("option_is_default"));
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Oops, Request failed..", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                Toast.makeText(MainActivity.this, getResources().getString(R.string.offline_text), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("token", token);
                return headers;
            }
        };
        queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(postRequest);
    }
    
   private void init() {
        //Connect the Api
        String url = Common.getInstance().getMainItemUrl();
        StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    result = jsonObject.getString("success");
                    if (result.equals("true")){
                        ApiList = jsonObject.getJSONArray("forms");
                        String Apichecksum = jsonObject.getString("checksum");

                        if(checksum != Apichecksum){
                            Db.execSQL("delete from "+ FormDatabaeHelper.FORMTABLE_NAME);
                            EDb.execSQL("delete from "+ ElementDatabaseHelper.ElEMENTTABLE_NAME);
                            ODb.execSQL("delete from "+ ElementOptionDatabaseHelper.OPTIONTABLE_NAME);
                            for(int i = 0; i < ApiList.length(); i++){
                                insertData(ApiList.getJSONObject(i).getString("form_name")
                                        ,ApiList.getJSONObject(i).getString("form_id")
                                        , Apichecksum
                                        ,ApiList.getJSONObject(i).getString("form_description"));
                            }
                            elementSave();
                            elementOptionSave();
                            ListviewManagement();

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loading.setVisibility(View.GONE);
                                }
                            }, 500);
                        }else {
                            ListviewManagement();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loading.setVisibility(View.GONE);
                                }
                            }, 500);
                        }
                    } else {
                        ListviewManagement();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loading.setVisibility(View.GONE);
                            }
                        }, 500);
                        Toast.makeText(MainActivity.this, "Oops, can't login! please try to login again.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.GONE);
                ListviewManagement();
                System.out.println(error);
                Toast.makeText(MainActivity.this, getResources().getString(R.string.offline_text), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("token", token);
                return headers;
            }
        };
        queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(postRequest);
    }

    private void sideMenu_mangement() {
        final Cursor cursor = db.rawQuery("SELECT *FROM " + DatabaseHelper.TABLE_NAME,  null);
        if(cursor != null){
            if (cursor.moveToFirst()){
                do{
                    useremail = cursor.getString(cursor.getColumnIndex("Gmail"));
                }while(cursor.moveToNext());
            }
            cursor.close();
        }

        TextView sidemenu_main = findViewById(R.id.sidemenu_main);
        sidemenu_main.setText(useremail);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        findViewById(R.id.menu_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        navigation = findViewById(R.id.nav_view);
        navigation.setItemIconTintList(null);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sidebar_course:
                        Intent intent_main = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent_main);
                        return true;

                    case R.id.sidebar_setting:
                        Intent intent_setting = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent_setting);
                        return true;

                    case R.id.sidebar_logout:
                        Intent intent_logout = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent_logout);
                        return true;
                }
                return false;
            }
        });
    }

    private void ListviewManagement() {
        final Cursor fcursor = Db.rawQuery("SELECT *FROM " + FormDatabaeHelper.FORMTABLE_NAME,  null);

        if (fcursor.moveToFirst()){
            do{
                data.add(fcursor.getString(fcursor.getColumnIndex("Ftitle")));
                listFormId.add(fcursor.getString(fcursor.getColumnIndex("Ftitle_id")));
                listFormDes.add(fcursor.getString(fcursor.getColumnIndex("form_description")));
                listFormtitle.add(fcursor.getString(fcursor.getColumnIndex("Ftitle")));
            }while(fcursor.moveToNext());
        }
        fcursor.close();

        for(int i = 0; i < data.size(); i++){
            Listitem.add(new Listmodel(data.get(i)));
        }

        myAdapter=new CustomAdapter(MainActivity.this, Listitem);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id){
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                intent.putExtra("id", listFormId.get(position));
                intent.putExtra("des", listFormDes.get(position));
                intent.putExtra("title", listFormtitle.get(position));
                startActivity(intent);
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loading.setVisibility(View.GONE);
            }
        }, 500);
    }

    private void setupSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search Here");
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (TextUtils.isEmpty(newText)) {
            listView.clearTextFilter();
        } else {
            listView.setFilterText(newText);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    public void insertData(String ftitle,String ftitle_id, String checksum,String formDes){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FormDatabaeHelper.FCOL_2, ftitle);
        contentValues.put(FormDatabaeHelper.FCOL_3, ftitle_id);
        contentValues.put(FormDatabaeHelper.FCOL_4, checksum);
        contentValues.put(FormDatabaeHelper.FCOL_5, formDes);
        Db.insert(FormDatabaeHelper.FORMTABLE_NAME,null,contentValues);
    }

    public void insertElementData(String element_id,String element_title, String element_guidelines
            , String element_type, String element_position, String element_page_number, String element_default_value, String element_constraint
            , String element_address_hideline2, String formid, String element_media_type, String element_media_image_src
            , String element_media_pdf_src){
        ContentValues contentValues = new ContentValues();
        String Localurl = null;

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if(element_media_image_src == "null" || element_media_image_src.isEmpty()) {
            contentValues.put(ElementDatabaseHelper.ECOL_13, "null");
        }else {
            InputStream in = null;
            try
            {
                URL url = new URL(element_media_image_src);
                URLConnection urlConn = url.openConnection();
                HttpURLConnection httpConn = (HttpURLConnection) urlConn;
                httpConn.connect();

                in = httpConn.getInputStream();
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            Bitmap bmpimg = BitmapFactory.decodeStream(in);
            if (isExternalStorageWritable()) {
                Localurl = saveImageExternal(bmpimg);
                Log.e("UrlEx", Localurl );
            }else{
                Localurl = saveImageInternal(bmpimg);
                Log.e("UrlIn", Localurl );
            }
            contentValues.put(ElementDatabaseHelper.ECOL_13, Localurl);
        }

        contentValues.put(ElementDatabaseHelper.ECOL_2, element_id);
        contentValues.put(ElementDatabaseHelper.ECOL_3, element_title);
        contentValues.put(ElementDatabaseHelper.ECOL_4, element_guidelines);
        contentValues.put(ElementDatabaseHelper.ECOL_5, element_type);
        contentValues.put(ElementDatabaseHelper.ECOL_6, element_position);
        contentValues.put(ElementDatabaseHelper.ECOL_7, element_page_number);
        contentValues.put(ElementDatabaseHelper.ECOL_8, element_default_value);
        contentValues.put(ElementDatabaseHelper.ECOL_9, element_constraint);
        contentValues.put(ElementDatabaseHelper.ECOL_10, element_address_hideline2);
        contentValues.put(ElementDatabaseHelper.ECOL_11, formid);
        contentValues.put(ElementDatabaseHelper.ECOL_12, element_media_type);
        contentValues.put(ElementDatabaseHelper.ECOL_14, element_media_pdf_src);
        EDb.insert(ElementDatabaseHelper.ElEMENTTABLE_NAME,null,contentValues);
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


    private String saveImageExternal(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/save_images");
        myDir.mkdirs();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fname = "Shutta_"+ timeStamp +".jpg";

        File file = new File(myDir, fname);
        if (file.exists()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(file);
    }

    private String saveImageInternal(Bitmap finalBitmap) {

        String root = Environment.getDataDirectory().toString();
        File myDir = new File(root + "/save_images");
        myDir.mkdirs();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fname = "Shutta_"+ timeStamp +".jpg";

        File file = new File(myDir, fname);
        if (file.exists()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(file);
    }

    public void insertElementOptionData(String form_id, String element_id, String option_id, String position, String option, String option_is_default){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ElementOptionDatabaseHelper.OCOL_2, form_id);
        contentValues.put(ElementOptionDatabaseHelper.OCOL_3, element_id);
        contentValues.put(ElementOptionDatabaseHelper.OCOL_4, option_id);
        contentValues.put(ElementOptionDatabaseHelper.OCOL_5, position);
        contentValues.put(ElementOptionDatabaseHelper.OCOL_6, option);
        contentValues.put(ElementOptionDatabaseHelper.OCOL_7, option_is_default);
        ODb.insert(ElementOptionDatabaseHelper.OPTIONTABLE_NAME,null,contentValues);
    }

}
