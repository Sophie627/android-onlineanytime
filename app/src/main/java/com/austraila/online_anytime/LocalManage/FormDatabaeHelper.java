package com.austraila.online_anytime.LocalManage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class FormDatabaeHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="Formregister.db";
    public static final String FORMTABLE_NAME = "ap_forms";
    public static final String FCOL_1 = "FID";
    public static final String FCOL_2 = "Ftitle";
    public static final String FCOL_3 = "Ftitle_id";
    public static final String FCOL_4 = "Fchecksum";
    public static final String FCOL_5 = "form_description";

    public FormDatabaeHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + FORMTABLE_NAME + " (FID INTEGER PRIMARY KEY AUTOINCREMENT,Ftitle TEXT,Ftitle_id TEXT" +
                ",Fchecksum TEXT,form_description TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +FORMTABLE_NAME);
        onCreate(db);
    }
}
