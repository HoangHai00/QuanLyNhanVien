package com.example.qunlnhnvin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {




    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // truy vấn không trả kết quả : CREATE, INSERT, UPDATA, DELETE,...
    public void QueryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        // getWritableDatabase() ghi và đọc dữ liệu
        database.execSQL(sql);
    }

    // truy vấn có trả kết quả: SELECT || cursor là con trỏ để đọc dữ liệu
    public Cursor GetData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        //getReadableDatabase đọc dữ liệu và không ghi
        return database.rawQuery(sql,null);


    }
    // nhập dữ liệu nhân viên khi chưa có dữ liệu

    public void INSET_NHANVIEN( String ten, Integer sdt, String email, String diaChi, byte[] hinh){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO NhanVien VALUES(null,?,?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, ten);
        statement.bindLong(2, sdt);
        statement.bindString(3,email);
        statement.bindString(4, diaChi);
        statement.bindBlob(5,hinh);

        statement.executeInsert();
    }





    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }
}