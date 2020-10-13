package com.austraila.online_anytime.activitys;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import com.austraila.online_anytime.LocalManage.ElementValueDatabaeHelper;
import com.austraila.online_anytime.LocalManage.FormDatabaeHelper;
import com.austraila.online_anytime.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SuccessActivity extends AppCompatActivity {
    private SQLiteDatabase db,VDb;
    private SQLiteOpenHelper openHelper,ElementValueopenHeloer;
    RequestQueue queue;
    String Token, formid, Vid;
    static int recordId = 0;
    TextView textView;
    RelativeLayout loading;
    HashMap<String, String> formData = new HashMap<String, String>();
    HashMap<String, String> photoData = new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        getSupportActionBar().hide();

        FormActivity.sigleElementArray.clear();
        FormActivity.numberElementArray.clear();
        FormActivity.emailElementArray.clear();

        Intent intent = getIntent();
        formData = (HashMap<String, String>) FormActivity.element_data;
        Log.e("elementData", String.valueOf(formData));
        formid = intent.getStringExtra("FormId");

        photoData = (HashMap<String, String>) FormActivity.elementPhotos_send;

        for (Map.Entry<String, Bitmap> entry : FormActivity.elementSignature.entrySet()) {
            String key = entry.getKey();
            Bitmap value = entry.getValue();
            String image = "data:image/png;base64," + toBase64(value);
            formData.put(key, image);
        }

        openHelper = new DatabaseHelper(this);
        ElementValueopenHeloer = new ElementValueDatabaeHelper(this);
        db = openHelper.getWritableDatabase();
        VDb = ElementValueopenHeloer.getWritableDatabase();

        final Cursor cursor = db.rawQuery("SELECT *FROM " + DatabaseHelper.TABLE_NAME,  null);
        if(cursor != null){
            if (cursor.moveToFirst()){
                do{
                    Token = cursor.getString(cursor.getColumnIndex("token"));
                }while(cursor.moveToNext());
            }
            cursor.close();
        }

        loading = findViewById(R.id.loadingSucees);
        textView = findViewById(R.id.successText);
        TextView back = findViewById(R.id.back_main);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuccessActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        sendcheck();
    }

    private void sendcheck() {
        String url = Common.getInstance().getSaveUrl();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String result = jsonObject.getString("success");
                            if (result.equals("true")){
                                Vid = jsonObject.getString("id");
                                PsendData(Vid);
                                Log.e("Vid value", Vid );
                                FormActivity.elementSignature.clear();
                                FormActivity.element_data.clear();
                            } else {
                                loading.setVisibility(View.GONE);
                                textView.setText("false");
                            }
                        } catch (JSONException e) {
                            loading.setVisibility(View.GONE);
                            e.printStackTrace();
                            Toast.makeText(SuccessActivity.this, "Request faild", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SuccessActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("senddata Error:", String.valueOf(error));
                        loading.setVisibility(View.GONE);
                        Toast.makeText(SuccessActivity.this, getResources().getString(R.string.send_faild), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SuccessActivity.this, MainActivity.class);
                        startActivity(intent);
                        textView.setText(getResources().getString(R.string.send_faild));

                        recordId += 1;
                        for (Map.Entry<String, String> entry : formData.entrySet()) {
                            String key = entry.getKey();
                            String value = entry.getValue();
                            insertData(key, value, formid);
                        }

                        for (Map.Entry<String, String> entry : photoData.entrySet()) {
                            String key = entry.getKey();
                            String value = entry.getValue();
                            insertData(key, value, formid);
                        }
//                        Toast.makeText(SuccessActivity.this, getResources().getString(R.string.offline_text), Toast.LENGTH_LONG).show();

                        FormActivity.elementPhotos.clear();
                        FormActivity.elementPhotos_send.clear();
                        FormActivity.element_filePath.clear();
                        FormActivity.element_data.clear();
                        FormActivity.elementSignature.clear();
                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("token", Token);
                return headers;
            }

            @Override
            protected Map<String, String> getParams()
            {
                formData.put("formId", formid);
                formData.put("id", "0");
                Log.e("formdata:", String.valueOf(formData));
                Log.e("token:", Token);
                return formData;
            }
        };
        queue = Volley.newRequestQueue(SuccessActivity.this);
        queue.add(postRequest);
    }

    private void PsendData(final String id) {
        String url = Common.getInstance().getSaveUrl();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        Log.e("server request", response );
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String result = jsonObject.getString("success");
                            if (result.equals("true")){
                                loading.setVisibility(View.GONE);
                                FormActivity.elementPhotos_send.clear();
                                FormActivity.elementPhotos.clear();
                                FormActivity.element_filePath.clear();

                                textView.setText(getResources().getString(R.string.success));
                                Toast.makeText(SuccessActivity.this, getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SuccessActivity.this, MainActivity.class);
                                startActivity(intent);

                            } else {
                                loading.setVisibility(View.GONE);
                                Toast.makeText(SuccessActivity.this, "Oops, Request failed.", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
//                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            Log.e("send res okay",e.toString() );
                            Toast.makeText(SuccessActivity.this, "request faild", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        System.out.println(error);
                        Intent intent = new Intent(SuccessActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(SuccessActivity.this, getResources().getString(R.string.offline_text), Toast.LENGTH_LONG).show();
                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("token", Token);
                return headers;
            }

            @Override
            protected Map<String, String> getParams()
            {
                photoData.put("formId", formid);
                photoData.put("id", id);
                photoData.put("Final", "end");
                return photoData;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                2500000, 0, 1f
        ));
        queue = Volley.newRequestQueue(SuccessActivity.this);
        queue.add(postRequest);
    }

    public String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void insertData(String elementkye, String elementValue, String elementformid) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ElementValueDatabaeHelper.VCOL_2, elementkye);
        contentValues.put(ElementValueDatabaeHelper.VCOL_3, elementValue);
        contentValues.put(ElementValueDatabaeHelper.VCOL_4, elementformid);
        contentValues.put(ElementValueDatabaeHelper.VCOL_5, String.valueOf(recordId));
        Log.e(String.valueOf(recordId), String.valueOf(contentValues));
        VDb.insert(ElementValueDatabaeHelper.VTABLE_NAME,null,contentValues);
    }
}
