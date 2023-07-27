package com.example.connet_internet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class NguoiDungDAO {
    Database database;
    ArrayList<NguoiDung>listND=new ArrayList<>();
    public  NguoiDungDAO(Context c){
        database = new Database(c);
    }
    public ArrayList<NguoiDung> getAllNguoiDung(){
        ArrayList<NguoiDung> ds= new ArrayList<NguoiDung>();
        SQLiteDatabase db= database.getReadableDatabase();
        Cursor c=db.rawQuery("select * from nguoidung",null);
        if (c.getCount()>0){
            c.moveToFirst();
            do {
                ds.add(new NguoiDung(c.getString(0),c.getString(1)));
            }while (c.moveToNext());
        }
        return ds;
    }
    public  void addNguoiDung(NguoiDung nd){
        SQLiteDatabase db= database.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tendangnhap",nd.getTendangnhap());
        values.put("matkhau",nd.getMatkhau());
        db.insert("nguoidung",null,values);
    }
    public  void  deleteNguoiDung(String tendangnhap){
        SQLiteDatabase db = database.getWritableDatabase();
        db.delete("nguoidung","tendangnhap=?",new String[]{tendangnhap});
    }
    public  void  updateNguoiDung(NguoiDung nd ){
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tendangnhap",nd.getTendangnhap());
        values.put("matkhau",nd.getMatkhau());
        db.update("nguoidung",values ,"tendangnhap=?",new String[]{nd.getTendangnhap()});
    }

    public void updateDatabase(){
        SQLiteDatabase db=database.getWritableDatabase();
        database.onUpgrade(db,1,1);
    }
}
