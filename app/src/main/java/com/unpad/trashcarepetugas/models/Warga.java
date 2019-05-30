package com.unpad.trashcarepetugas.models;

public class Warga {
    private String alamat;
    private String nama;
    private String no_telp;
    private String password;
    private String id_warga;

    public Warga(String alamat, String nama, String no_telp, String password, String id_warga) {
        this.alamat = alamat;
        this.nama = nama;
        this.no_telp = no_telp;
        this.password = password;
        this.id_warga = id_warga;
    }

    public Warga() {
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNo_telp() {
        return no_telp;
    }

    public void setNo_telp(String no_telp) {
        this.no_telp = no_telp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId_warga() {
        return id_warga;
    }

    public void setId_warga(String id_warga) {
        this.id_warga = id_warga;
    }
}
