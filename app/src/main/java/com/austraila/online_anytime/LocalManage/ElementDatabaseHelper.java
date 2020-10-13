package com.austraila.online_anytime.LocalManage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

public class ElementDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="Elementregister.db";
    public static final String ElEMENTTABLE_NAME = "element_forms";
    public static final String ECOL_1 = "EID";
    public static final String ECOL_2 = "element_id";
    public static final String ECOL_3 = "element_title";
    public static final String ECOL_4 = "element_guidelines";
    public static final String ECOL_5 = "element_type";
    public static final String ECOL_6 = "element_position";
    public static final String ECOL_7 = "element_page_number";
    public static final String ECOL_8 = "element_default_value";
    public static final String ECOL_9 = "element_constraint";
    public static final String ECOL_10 = "element_address_hideline2";
    public static final String ECOL_11 = "formid";
    public static final String ECOL_12 = "element_media_type";
    public static final String ECOL_13 = "element_media_image_src";
    public static final String ECOL_14 = "element_media_pdf_src";

    public ElementDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ElEMENTTABLE_NAME + " (EID INTEGER PRIMARY KEY AUTOINCREMENT,element_id TEXT,element_title TEXT,element_guidelines TEXT" +
                ",element_type TEXT,element_position TEXT,element_page_number TEXT,element_default_value TEXT" +
                ",element_constraint TEXT, element_address_hideline2 TEXT ,formid TEXT,element_media_type TEXT" +
                ",element_media_image_src TEXT,element_media_pdf_src TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +ElEMENTTABLE_NAME);
        onCreate(db);
    }
}
