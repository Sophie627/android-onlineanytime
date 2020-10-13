package com.austraila.online_anytime.activitys.LoginDepartment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.austraila.online_anytime.LocalManage.FormDatabaeHelper;
import com.austraila.online_anytime.R;
import com.austraila.online_anytime.activitys.EachItemActivity;
import com.austraila.online_anytime.activitys.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.PasswordAuthentication;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity{
    private SQLiteDatabase db,Db,EDb,ODb;
    private SQLiteOpenHelper openHelper,FormopenHelper,ElementopenHelper,ElementOptionopenHelper;
    EditText email, pass;
    Button loginBtn;
    String Email, Pass, userfullname, result, user_id, token, useremail, userpass;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        openHelper = new DatabaseHelper(this);
        FormopenHelper = new FormDatabaeHelper(this);
        ElementopenHelper = new ElementDatabaseHelper(this);
        ElementOptionopenHelper = new ElementOptionDatabaseHelper(this);
        db = openHelper.getWritableDatabase();
        Db = FormopenHelper.getWritableDatabase();
        EDb = ElementopenHelper.getWritableDatabase();
        ODb = ElementOptionopenHelper.getWritableDatabase();

        final Cursor cursor = db.rawQuery("SELECT *FROM " + DatabaseHelper.TABLE_NAME,  null);
        if(cursor != null){
            if (cursor.moveToFirst()){
                do{
                    useremail = cursor.getString(cursor.getColumnIndex("Gmail"));
                    userpass = cursor.getString(cursor.getColumnIndex("Password"));
                    System.out.println(useremail);
                    System.out.println(userpass);
                }while(cursor.moveToNext());
            }
            cursor.close();
        }
        System.out.println(cursor);

        email = findViewById(R.id.login_email);
        pass = findViewById(R.id.login_pass);

        loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email = email.getText().toString().trim();
                Pass = pass.getText().toString().trim();
                if (Email.isEmpty() || Pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.entry_info_login), Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    ApiLogin();
                }
            }
        });
    }

    private void locallogin(){
        if(Email.equals(useremail) && Pass.equals(userpass)){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }else {
            Toast.makeText(LoginActivity.this, "It is currently offline or wrong information.", Toast.LENGTH_SHORT).show();
        }
    }

    private void ApiLogin() {
        String url = Common.getInstance().getBaseURL();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            result = jsonObject.getString("success");
                            userfullname = jsonObject.getString("user_fullname");
                            user_id = jsonObject.getString("user_id");
                            token = jsonObject.getString("token");
                            if (result.equals("true")){
                                insertData(Email, Pass, userfullname, token);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                Db.execSQL("delete from "+ FormDatabaeHelper.FORMTABLE_NAME);
                                EDb.execSQL("delete from "+ ElementDatabaseHelper.ElEMENTTABLE_NAME);
                                ODb.execSQL("delete from "+ ElementOptionDatabaseHelper.OPTIONTABLE_NAME);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.retry_info_login), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                        locallogin();
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.offline_text), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", Email);
                params.put("password", Pass);
                return params;
            }
        };
        queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(postRequest);
    }

    public void foregetClick(View view) {
        Intent intent = new Intent(LoginActivity.this, EachItemActivity.class);
        startActivity(intent);
    }
    
    public void insertData(String fGmail,String fPassword, String fusername, String token){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_2,fGmail);
        contentValues.put(DatabaseHelper.COL_3,fPassword);
        contentValues.put(DatabaseHelper.COL_4, fusername);
        contentValues.put(DatabaseHelper.COL_5, token);
        db.insert(DatabaseHelper.TABLE_NAME,null,contentValues);
    }
}
