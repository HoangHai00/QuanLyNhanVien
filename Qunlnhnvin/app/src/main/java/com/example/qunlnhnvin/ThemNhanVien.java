package com.example.qunlnhnvin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ThemNhanVien extends AppCompatActivity {


    EditText edtTen, edtSdt, edtEmail, edtDiaChi;
    Button btnChupAnh, btnChonAnh, btnXacNhan, btnHuy;
    ImageView imgHinhAnh;
    final   int REQUEST_CODE_CAMERA = 123;
    final   int REQUEST_CODE_FILE = 456;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_nhan_vien);
        anhxa();


        // chụp ảnh
        btnChupAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hỏi xem có cho phép sử dụng camera không. Nhớ xin quyền ở file Androidmanifest
                ActivityCompat.requestPermissions(ThemNhanVien.this,
                        new  String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);

//                Intent intent = new Intent();
//                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent,REQUEST_CODE_CAMERA);
            }
        });
        // chọn ảnh
        btnChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hỏi xem có cho phép sử dụng thư viện không. Nhớ xin quyền ở file Androidmanifest
                ActivityCompat.requestPermissions(ThemNhanVien.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_FILE);
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent, REQUEST_CODE_FILE);

            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtTen.length() != 0 && edtSdt.length() != 0 && edtEmail.length() != 0 && edtDiaChi.length() != 0){
                    //         chuyển data imageview --> byte []
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) imgHinhAnh.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArray);
                    byte[] hinhAnh = byteArray.toByteArray();

                    MainActivity.database.INSET_NHANVIEN(
                            edtTen.getText().toString().trim(),
                            Integer.valueOf(edtSdt.getText().toString().trim()),
                            edtEmail.getText().toString().trim(),
                            edtDiaChi.getText().toString().trim(),
                            hinhAnh
                    );
                    Toast.makeText(ThemNhanVien.this, "Đã Thêm", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(ThemNhanVien.this, MainActivity.class));
                }
                else {
                    Toast.makeText(ThemNhanVien.this, "Vui lòng cung cấp đủ thông tin!", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    // xử lý sụ kiện hỏi có cho phép truy cập camera và thư viện không
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE_CAMERA:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent();
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,REQUEST_CODE_CAMERA);
                }
                else {
                    Toast.makeText(this, "Bạn không cho phép mở camera", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_FILE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE_FILE);
                }else {
                    Toast.makeText(this, "Bạn không cho phép truy cập thư viện", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // trả ảnh ra imageview
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       // trả từ chụp ảnh
        if(requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgHinhAnh.setImageBitmap(bitmap);
        }
        // trả từ thư viện ảnh
        if(requestCode == REQUEST_CODE_FILE && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgHinhAnh.setImageBitmap(bitmap);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void anhxa(){
        edtTen = findViewById(R.id.edtThemTen);
        edtSdt = findViewById(R.id.edtThemSDT);
        edtEmail = findViewById(R.id.edtThemEmail);
        edtDiaChi = findViewById(R.id.edtThemDiaChi);
        btnChonAnh = findViewById(R.id.buttonChonAnh);
        btnChupAnh = findViewById(R.id.buttonChupAnh);
        btnXacNhan = findViewById(R.id.btnThemXacNhan);
        btnHuy = findViewById(R.id.btnThemHuy);
        imgHinhAnh = findViewById(R.id.imageViewThemAnhNhanVien);

    }
}