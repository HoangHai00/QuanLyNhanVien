package com.example.qunlnhnvin;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainActivity extends AppCompatActivity {

   public static Database database;
    RecyclerView rcNhanVien;
    NhanVienAdapter nhanVienAdapter;
    ArrayList<NhanVien> nhanVienArrayList = new ArrayList<>();
    ImageView imgAnhMau;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addLayout();

        // tạo new database trống
        database = new Database(this, "QuanlyNhanVien.sqlite", null,1);

        // tạo bảng chứa dữ liệu
        database.QueryData("CREATE TABLE IF NOT EXISTS NhanVien(Id INTEGER PRIMARY KEY AUTOINCREMENT, TenNhanVien VACHAR(200), SoDienThoai INTEGER, Email TEXT, DiaChi VACHAR(200), HinhAnh BLOB)");


        GetDataNhanVien();

    }

    // sửa nhân viên
    public void UplateNhanVien(int id, String ten, int sdt, String email, String diachi, byte[] hinh){
        Intent intent = new Intent(MainActivity.this,SuaNhanVien.class);

        // tạo bundle truyền dữ liệu
        Bundle bundle =  new Bundle();
        bundle.putInt("id",id);
        bundle.putString("ten",ten);
        bundle.putInt("sdt",sdt);
        bundle.putString("email",email);
        bundle.putString("diaChi",diachi);
        bundle.putByteArray("hinhAnh",hinh);
        intent.putExtra("duLieu",bundle);

        startActivity(intent);

    }
    // xóa nhân viênD
    public void DiaLogXoaNhanVien(int id, String ten, int sodienthoai, String email, String diaChi, byte[] hinh){
        AlertDialog.Builder dialogXoa = new AlertDialog.Builder(this);
        dialogXoa.setMessage("Bạn có muốn xóa Nhân Viên: "+ten+" không?");
        dialogXoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.QueryData("DELETE FROM NhanVien WHERE Id = '"+ id+"'");
                GetDataNhanVien();
                Toast.makeText(MainActivity.this, "Đã xóa" + ten, Toast.LENGTH_SHORT).show();

            }
        });
        dialogXoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogXoa.show();
    }

    // chi tiết nhân viên
    public void ShowNhanVien(int id, String ten, int sodienthoai, String email, String diaChi, byte[] hinh){

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_xemnhanvien);
        dialog.setCanceledOnTouchOutside(false);


        ImageView imgDiaLogAnh = dialog.findViewById(R.id.imageViewDiaLog);
        TextView txtTen = dialog.findViewById(R.id.textViewDialogTen);
        TextView txtSdt = dialog.findViewById(R.id.textViewDiaLogSDT);
        TextView txtEmail = dialog.findViewById(R.id.textViewDiaLogEmail);
        TextView txtDiaChi = dialog.findViewById(R.id.textViewDiaLogDiaChi);
        Button btnXong = dialog.findViewById(R.id.buttonXong);


        //         chuyen kieu byte --> bitmap để lấy hình ảnh
        Bitmap bitmap = BitmapFactory.decodeByteArray(hinh,0,hinh.length);
        imgDiaLogAnh.setImageBitmap(bitmap);

        txtTen.setText("Họ và tên: "+ten);
        txtSdt.setText("Số điện thoại :0"+sodienthoai);
        txtEmail.setText("Email: "+email);
        txtDiaChi.setText("Địa chỉ: "+diaChi);

        btnXong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();

    }




    // dữ liệu của nhân viên
    public void GetDataNhanVien(){
        // trả dữ liệu ra
        Cursor dataNhanVien = database.GetData("SELECT * FROM NhanVien");
        nhanVienArrayList.clear();
        while (dataNhanVien.moveToNext()){
            int id = dataNhanVien.getInt(0);
            String tenNhanVien = dataNhanVien.getString(1);
            int soDienThoai = dataNhanVien.getInt(2);
            String email = dataNhanVien.getString(3);
            String diaChi = dataNhanVien.getString(4);
            byte[] hinh = dataNhanVien.getBlob(5);
            nhanVienArrayList.add(new NhanVien(id,tenNhanVien,soDienThoai,email,diaChi,hinh));
        }

        nhanVienAdapter.notifyDataSetChanged();
    }

    // add menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // thiết lập menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_menu_Them:addNhanVien();
                break;
            case R.id.main_menu_Thung_Rac:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // thêm nhân viên
    public  void addNhanVien(){
        Intent intent = new Intent(MainActivity.this,ThemNhanVien.class);
        startActivity(intent);


    }
    // add lay out
    public void addLayout(){
        rcNhanVien = findViewById(R.id.recyclerviewMain);
        rcNhanVien.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.custom_divider);
        itemDecoration.setDrawable(drawable);
        rcNhanVien.addItemDecoration(itemDecoration);

        nhanVienAdapter =new NhanVienAdapter(nhanVienArrayList, this);
        rcNhanVien.setAdapter(nhanVienAdapter);
    }
}