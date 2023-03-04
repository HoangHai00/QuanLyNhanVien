package com.example.qunlnhnvin;

import static java.lang.Long.getLong;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NhanVienAdapter extends RecyclerView.Adapter<NhanVienAdapter.ViewHolder> {
 public ArrayList<NhanVien> nhanVienArrayList;
 public MainActivity context;

    public NhanVienAdapter(ArrayList<NhanVien> nhanVienArrayList, MainActivity context) {
        this.nhanVienArrayList = nhanVienArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    // add layout
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemview = layoutInflater.inflate(R.layout.dong_nhan_vien,parent,false);

        return new ViewHolder(itemview);
    }

    // sự kiện sửa xóa
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final  NhanVien nhanVien = nhanVienArrayList.get(position);
        // sắp xếp theo tên nhân viên
        Collections.sort(nhanVienArrayList, new Comparator<NhanVien>() {
            @Override
            public int compare(NhanVien o1, NhanVien o2) {
                return (o1.getTen().compareTo(o2.getTen()));
            }
        });
        holder.txtTen.setText(nhanVien.getTen() + "");
        holder.txtSdt.setText("0"+nhanVien.getSdt());
        holder.txtEmail.setText(nhanVien.getEmail() + "");
        holder.txtDiaChi.setText(nhanVien.getDiachi() + "");



//         chuyen kieu byte --> bitmap để lấy hình ảnh
        byte[] anh = nhanVien.getHinh();
        Bitmap bitmap = BitmapFactory.decodeByteArray(anh,0,anh.length);
        holder.imgAnhNhanVien.setImageBitmap(bitmap);

        // sự kiện sửa, xóa, xem chi tiết

        holder.btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               context.UplateNhanVien(nhanVien.getId(),nhanVien.getTen(),nhanVien.getSdt(),nhanVien.getEmail(),nhanVien.getDiachi(),nhanVien.getHinh());


            }
        });
        holder.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.DiaLogXoaNhanVien(nhanVien.getId(),nhanVien.getTen(),nhanVien.getSdt(),nhanVien.getEmail(),nhanVien.getDiachi(),nhanVien.getHinh());
            }
        });

        holder.btnChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.ShowNhanVien(nhanVien.getId(),nhanVien.getTen(),nhanVien.getSdt(),nhanVien.getEmail(),nhanVien.getDiachi(),nhanVien.getHinh());

            }
        });

    }

    @Override
    public int getItemCount() {
        return nhanVienArrayList.size();
    }

    // ánh xạ view
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTen,txtSdt,txtEmail,txtDiaChi;
        ImageView imgAnhNhanVien;
        Button btnSua, btnXoa,btnChiTiet;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTen = itemView.findViewById(R.id.textviewTen);
            txtSdt = itemView.findViewById(R.id.textViewSDT);
            txtEmail = itemView.findViewById(R.id.textViewEmail);
            txtDiaChi = itemView.findViewById(R.id.textViewDiaChi);
            imgAnhNhanVien = itemView.findViewById(R.id.imageViewAnhNhanVien);
            btnSua = itemView.findViewById(R.id.buttonSua);
            btnXoa = itemView.findViewById(R.id.buttonXoa);
            btnChiTiet = itemView.findViewById(R.id.btnXemChiTiet);



        }
    }


}
