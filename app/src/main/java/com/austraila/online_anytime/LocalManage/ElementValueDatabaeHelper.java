package com.austraila.online_anytime.LocalManage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ElementValueDatabaeHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="ValueData.db";
    public static final String VTABLE_NAME="ElementValue";
    public static final String VCOL_1="ID";
    public static final String VCOL_2="ElementId";
    public static final String VCOL_3="ElementValue";
    public static final String VCOL_4="ElementFormId";
    public static final String VCOL_5="ElementType";

    public ElementValueDatabaeHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + VTABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,ElementId TEXT,ElementValue TEXT" +
                ",ElementFormId TEXT,ElementType TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +VTABLE_NAME);
        onCreate(db);
    }
}
