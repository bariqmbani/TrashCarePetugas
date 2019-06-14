package com.unpad.trashcarepetugas.models;

public class Warga {
    private String alamat;
    private String nama;
    private String no_telp;
    private String password;
    private String id_warga;
    private boolean request;

    public Warga(String alamat, String nama, String no_telp, String password, String id_warga, boolean  request) {
        this.alamat = alamat;
        this.nama = nama;
        this.no_telp = no_telp;
        this.password = password;
        this.id_warga = id_warga;
        this.request = request;
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

    public boolean isRequest() {
        return request;
    }

    public void setRequest(boolean request) {
        this.request = request;
    }

    @Override
    public String toString() {
        return "Warga{" +
                "alamat='" + alamat + '\'' +
                ", nama='" + nama + '\'' +
                ", no_telp='" + no_telp + '\'' +
                ", password='" + password + '\'' +
                ", id_warga='" + id_warga + '\'' +
                ", request=" + request +
                '}';
    }
}
