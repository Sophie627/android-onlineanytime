package com.austraila.online_anytime.activitys.cameraActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.austraila.online_anytime.R;
import com.austraila.online_anytime.activitys.FormActivity;

public class CameraActivity extends AppCompatActivity {
    private Uri imageUri;
    public static final int Image_Capture_Code = 1;
    String formid, formDes, formtitle, scroll, page, checkpage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        formid = getIntent().getStringExtra("id");
        formDes = getIntent().getStringExtra("des");
        formtitle = getIntent().getStringExtra("title");
        scroll = getIntent().getStringExtra("scroll");
        page = getIntent().getStringExtra("page");
        checkpage = getIntent().getStringExtra("checkpage");

        ContentValues values = new ContentValues();
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cInt.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cInt,Image_Capture_Code);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Capture_Code) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(CameraActivity.this, FormActivity.class);
                intent.putExtra("url", imageUri.toString());
                intent.putExtra("id", formid);
                intent.putExtra("des", formDes);
                intent.putExtra("title", formtitle);
                intent.putExtra("title", formtitle);
                intent.putExtra("scroll", scroll);
                intent.putExtra("page", page);
                intent.putExtra("checkpage", checkpage);
                startActivity(intent);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(CameraActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }
}
