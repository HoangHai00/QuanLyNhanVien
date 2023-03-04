package com.example.qunlnhnvin;

public class NhanVien {
    private int Id;
    private String Ten;
    private int Sdt;
    private String Email;
    private  String Diachi;
    private byte[] Hinh;

    public NhanVien(int id, String ten, int sdt, String email, String diachi, byte[] hinh) {
        Id = id;
        Ten = ten;
        Sdt = sdt;
        Email = email;
        Diachi = diachi;
        Hinh = hinh;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public int getSdt() {
        return Sdt;
    }

    public void setSdt(int sdt) {
        Sdt = sdt;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDiachi() {
        return Diachi;
    }

    public void setDiachi(String diachi) {
        Diachi = diachi;
    }

    public byte[] getHinh() {
        return Hinh;
    }

    public void setHinh(byte[] hinh) {
        Hinh = hinh;
    }
}
