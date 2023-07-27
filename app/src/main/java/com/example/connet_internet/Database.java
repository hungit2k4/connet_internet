package com.example.connet_internet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context) {
        super(context,"myDatabase",null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlnd= " create table nguoidung(\n" +
                "    tendangnhap text primary key,\n" +
                "    matkhau text\n" +
                "    )";
        db.execSQL(sqlnd);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists nguoidung");
        onCreate(db);
    }
}
