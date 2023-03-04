package com.example.qunlnhnvin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
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
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SuaNhanVien extends AppCompatActivity {

    public MainActivity context2;
    EditText edtSTen, edtSSdt, edtSEmail, edtSDiaChi;
    Button btnSChupAnh, btnSChonAnh, btnSXacNhan, btnSHuy;
    ImageView imgSHinhAnh;
    final   int REQUEST_CODE_SCAMERA = 234;
    final   int REQUEST_CODE_SFILE = 567;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_nhan_vien);
        Anhxa();

        // nhận dữ liệu từ bundle
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("duLieu");
        int id = bundle.getInt("id");
        String ten = bundle.getString("ten");
        int sdt = bundle.getInt("sdt");
        String email = bundle.getString("email");
        String diaChi = bundle.getString("diaChi");
        byte[] anh = bundle.getByteArray("hinhAnh");

        // truyền dữ liệu vào activity
        edtSTen.setText(ten);
        edtSEmail.setText(email);
        edtSSdt.setText(sdt+"");
        edtSDiaChi.setText(diaChi);

        // chuyển ảnh từ byte --> bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(anh,0,anh.length);
        imgSHinhAnh.setImageBitmap(bitmap);


        // Chụp ảnh
        btnSChupAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hỏi xem có cho phép sử dụng camera không. Nhớ xin quyền ở file Androidmanifest
                ActivityCompat.requestPermissions(SuaNhanVien.this,
                        new  String[]{Manifest.permission.CAMERA}, REQUEST_CODE_SCAMERA);
            }
        });



        // chọn ảnh từ thư viện
        btnSChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hỏi xem có cho phép sử dụng thư viện không. Nhớ xin quyền ở file Androidmanifest
                ActivityCompat.requestPermissions(SuaNhanVien.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_SFILE);
            }
        });



        // hủy activity
        btnSHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // uplate dữ liệu
        btnSXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtSTen.length() != 0 && edtSSdt.length() != 0 && edtSEmail.length() != 0 && edtSDiaChi.length() != 0){

                    //         chuyển data imageview --> byte []
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) imgSHinhAnh.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArray);
                    byte[] hinhAnhS = byteArray.toByteArray();


                    String tenS = edtSTen.getText().toString().trim();
                    int sdtS = Integer.valueOf(edtSSdt.getText().toString().trim());
                    String emailS = edtSEmail.getText().toString().trim();
                    String diaChiS = edtSDiaChi.getText().toString().trim();


                    // update Nhân Viên
                    SQLiteDatabase db = MainActivity.database.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("TenNhanVien",tenS);
                    contentValues.put("SoDienThoai",sdtS);
                    contentValues.put("Email",emailS);
                    contentValues.put("DiaChi",diaChiS);
                    contentValues.put("HinhAnh",hinhAnhS);

                    // NhanVien(Id INTEGER PRIMARY KEY AUTOINCREMENT, TenNhanVien VACHAR(200), SoDienThoai INTEGER, Email TEXT, DiaChi VACHAR(200), HinhAnh BLOB)");

                    db.update("NhanVien",contentValues,"Id = ?",new String[]{id +""});
                    db.close();
                    Toast.makeText(SuaNhanVien.this, "Đã Uplate thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SuaNhanVien.this, MainActivity.class));

                }
                else {
                    Toast.makeText(SuaNhanVien.this, "Vui lòng cung cấp đủ thông tin!", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    // xử lý sụ kiện hỏi có cho phép truy cập camera và thư viện không
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE_SCAMERA:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent();
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,REQUEST_CODE_SCAMERA);
                }
                else {
                    Toast.makeText(this, "Bạn không cho phép mở camera", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_SFILE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE_SFILE);
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
        if(requestCode == REQUEST_CODE_SCAMERA && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgSHinhAnh.setImageBitmap(bitmap);
        }
        // trả từ thư viện ảnh
        if(requestCode == REQUEST_CODE_SFILE && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgSHinhAnh.setImageBitmap(bitmap);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



    public void Anhxa(){
        edtSTen = findViewById(R.id.edtSuaTen);
        edtSSdt = findViewById(R.id.edtSuaSDT);
        edtSEmail = findViewById(R.id.edtSuaEmail);
        edtSDiaChi = findViewById(R.id.edtSuaDiaChi);
        btnSChupAnh = findViewById(R.id.buttonSuaChupAnh);
        btnSChonAnh = findViewById(R.id.buttonSuaChonAnh);
        btnSXacNhan = findViewById(R.id.btnSuaXacNhan);
        btnSHuy = findViewById(R.id.btnSuaHuy);
        imgSHinhAnh = findViewById(R.id.imageViewSuaAnhNhanVien);

    }
}