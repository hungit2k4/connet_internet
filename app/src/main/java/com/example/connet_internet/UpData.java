package com.example.connet_internet;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpData {
    private ArrayList<NguoiDung> listND;
     private ArrayList<NguoiDung> listNDfromDatabase;
    private Boolean check=false;
    //private OnDataLoadedListener onDataLoadedListener;


    public void putdata(Context c) {
        NguoiDungDAO nguoiDungDAO = new NguoiDungDAO(c);
        listND = new ArrayList<>();
        listND = nguoiDungDAO.getAllNguoiDung();
        // Tham chiếu đến FirebaseDatabase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        // Gửi đối tượng Map lên Firebase Realtime Database
        databaseReference.child("data").setValue(listND, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    // Gửi dữ liệu thành công, thêm lắng nghe sự kiện thay đổi dữ liệu trên Firebase
                 //   addDataChangeListener();
                } else {
                    // Xử lý lỗi nếu có khi gửi dữ liệu
                    Log.e(TAG, "Lỗi khi gửi dữ liệu lên Firebase: " + error.getMessage());
                }
            }
        });
    }

    //    public interface OnDataLoadedListener {
//        void onDataLoaded(ArrayList<NguoiDung> listND);
//
//        void onCancelled(DatabaseError databaseError);
//    }
    // Các hàm và lắng nghe sự kiện thay đổi dữ liệu khác (đã được thêm ở trên)
    public interface OnDataLoadedListener {
        void onDataLoaded(ArrayList<NguoiDung> listND);
        void onDataLoadFailed(String errorMessage);
    }


        // Các hàm và biến khác...

        // Hàm lấy dữ liệu từ Firebase
        public void getData(Context c,final OnDataLoadedListener onDataLoadedListener) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("data");

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Dữ liệu đã thay đổi, xử lý tại đây
                    // dataSnapshot chứa dữ liệu mới từ "data"
                    listND = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Lấy giá trị của từng phần tử con dưới dạng Map<String, String>
                        Map<String, String> userData = (Map<String, String>) snapshot.getValue();

                        // Kiểm tra nếu userData không rỗng và có cả hai thuộc tính tendangnhap và matkhau
                        if (userData != null && userData.containsKey("tendangnhap") && userData.containsKey("matkhau")) {
                            String tendangnhap = userData.get("tendangnhap");
                            String matkhau = userData.get("matkhau");

                            // Tạo đối tượng NguoiDung từ thông tin lấy được và thêm vào danh sách listND
                            listND.add(new NguoiDung(tendangnhap, matkhau));
                        }

                    }
                    NguoiDungDAO nguoiDungDAO= new NguoiDungDAO(c);
                    listNDfromDatabase=nguoiDungDAO.getAllNguoiDung();
                    for (NguoiDung x:listND){
                        if (check){
                            nguoiDungDAO.updateDatabase();
                            for (NguoiDung z:listND)
                            {
                                nguoiDungDAO.addNguoiDung(z);
                            }
                        }
                        for (NguoiDung y:listNDfromDatabase){
                            if (!x.tendangnhap.equals(y.tendangnhap)||x.tendangnhap.equals(y.tendangnhap)&&!x.matkhau.equals(y.matkhau)){
                                check= true;
                                break;
                            }
                        }
                    }

                // Gọi onDataLoadedListener để truyền danh sách người dùng về Activity hoặc Fragment
                    onDataLoadedListener.onDataLoaded(listND);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý lỗi nếu có
                    onDataLoadedListener.onDataLoadFailed(databaseError.getMessage());
                }
            });
        }

    // Hàm để cập nhật biến listND

        // Lặp qua các phần tử con trong DataSnapshot


        // Sau khi đã lấy được danh sách listND, bạn có thể làm gì đó với dữ liệu này
        // Ví dụ: cập nhật TextView
    }



    // Các hàm và lắng nghe sự kiện thay đổi dữ liệu khác (đã được thêm ở trên)

