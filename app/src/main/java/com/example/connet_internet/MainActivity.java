package com.example.connet_internet;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connet_internet.NguoiDung;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Database database;
    EditText edtUsername, edtPass;
    TextView tv;
    NguoiDungDAO nguoiDungDAO = new NguoiDungDAO(this);
    UpData upData =new UpData();
    Button btn, btnDelete;
    ArrayList<NguoiDung> list = new ArrayList<>();
    NguoiDung nguoiDung;
    boolean isDataLoaded = false;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btnLogin);
        tv = findViewById(R.id.tv);
        btnDelete= findViewById(R.id.btnDelete);
        edtPass = findViewById(R.id.edtPass);
        edtUsername = findViewById(R.id.edtUsername);
        database = new Database(this);
        getDataFromFirebase();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        // Lấy dữ liệu từ Firebase khi mở ứng dụng
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Xóa toàn bộ dữ liệu trên Firebase Realtime Database
                        databaseReference.removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        getDataFromFirebase();
                                        Toast.makeText(MainActivity.this, "Xóa dữ liệu thành công", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Xóa thất bại, thông báo lỗi
                                        Toast.makeText(MainActivity.this, "Xóa dữ liệu thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
            }
        });
        // Khi nhấn nút, gửi dữ liệu lên Firebase và cập nhật lại TextView
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u = edtUsername.getText().toString();
                String p = edtPass.getText().toString();
                nguoiDungDAO.addNguoiDung(new NguoiDung(u, p));
                upData.putdata(MainActivity.this);
                getDataFromFirebase();
                 // Refresh the data after adding a new user
            }
        });

//        databaseReference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
//                getDataFromFirebase();
//            }
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
//                // Không cần xử lý
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//            // Không cần xử lý
//        }
//
//        @Override
//        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
//            // Không cần xử lý
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError databaseError) {
//            // Không cần xử lý
//        }
//    });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getDataFromFirebase();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
                }


    // Hàm để lấy dữ liệu từ Firebase thông qua UpData
    private void getDataFromFirebase() {
        upData.getData(MainActivity.this,new UpData.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(ArrayList<NguoiDung> listND) {
                // Dữ liệu đã được lấy thành công, cập nhật dữ liệu và hiển thị lên TextView
                updateTextView(listND);
            }

            @Override
            public void onDataLoadFailed(String errorMessage) {
                // Xử lý lỗi nếu có
                Toast.makeText(MainActivity.this, "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });


    }
    // Hàm cập nhật TextView
    private void updateTextView(ArrayList<NguoiDung> listND) {
        if (listND != null && !listND.isEmpty()) {
            StringBuilder userInfo = new StringBuilder();
            for (NguoiDung x : listND) {
                userInfo.append(x.tendangnhap).append("\n").append(x.matkhau).append("\n\n");
            }
            tv.setText(userInfo.toString());
        } else {
            tv.setText("Danh sách người dùng trống hoặc không thể lấy dữ liệu");
        }
    }
}
