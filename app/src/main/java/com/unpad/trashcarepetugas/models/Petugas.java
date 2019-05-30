package com.unpad.trashcarepetugas.models;

public class Petugas {
    private String nama;
    private String alamat;
    private int usia;
    private String password;
    private int nilai;
    private String id_petugas;

    public Petugas() {
    }

    public Petugas(String nama, String alamat, int usia, String password, int nilai, String id_petugas) {
        this.nama = nama;
        this.alamat = alamat;
        this.usia = usia;
        this.password = password;
        this.nilai = nilai;
        this.id_petugas = id_petugas;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public int getUsia() {
        return usia;
    }

    public void setUsia(int usia) {
        this.usia = usia;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getNilai() {
        return nilai;
    }

    public void setNilai(int nilai) {
        this.nilai = nilai;
    }

    public String getId_petugas() {
        return id_petugas;
    }

    public void setId_petugas(String id_petugas) {
        this.id_petugas = id_petugas;
    }
}
