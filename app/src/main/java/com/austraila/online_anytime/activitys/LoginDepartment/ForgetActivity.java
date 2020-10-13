package com.austraila.online_anytime.activitys.LoginDepartment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.austraila.online_anytime.R;
import com.austraila.online_anytime.activitys.FormActivity;

public class ForgetActivity extends AppCompatActivity {
    Button Forgetbtn;
    Intent intent;
    EditText forgetEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        getSupportActionBar().hide();

        forgetEmail = findViewById(R.id.forget_email);

        Forgetbtn = findViewById(R.id.forgot_btn);
        Forgetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(forgetEmail.getText().toString().trim().isEmpty()){
                    Toast.makeText(ForgetActivity.this, "Enter your Email to send", Toast.LENGTH_LONG).show();
                    return;
                }

//                AlertDialog alertDialog = new AlertDialog.Builder(ForgetActivity.this).create();
//                alertDialog.setTitle("Notice");
//                alertDialog.setMessage("It was sent successfully.");
//                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//
//                            }
//                        });
//                alertDialog.show();
            }
        });
    }

    public void loginPageClick (View view) {
        intent = new Intent(ForgetActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
