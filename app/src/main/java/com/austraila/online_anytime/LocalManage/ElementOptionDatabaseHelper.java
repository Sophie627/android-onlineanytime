package com.austraila.online_anytime.LocalManage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ElementOptionDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="FormOptionRegister.db";
    public static final String OPTIONTABLE_NAME = "ap_element_options";
    public static final String OCOL_1 = "OID";
    public static final String OCOL_2 = "OFormId";
    public static final String OCOL_3 = "OElementId";
    public static final String OCOL_4 = "OOptionId";
    public static final String OCOL_5 = "OPosition";
    public static final String OCOL_6 = "OOption";
    public static final String OCOL_7 = "Ooption_is_default";

    public ElementOptionDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + OPTIONTABLE_NAME + " (OID INTEGER PRIMARY KEY AUTOINCREMENT,OFormId TEXT,OElementId TEXT" +
                ",OOptionId TEXT,OPosition TEXT,OOption TEXT,Ooption_is_default TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +OPTIONTABLE_NAME);
        onCreate(db);
    }
}
