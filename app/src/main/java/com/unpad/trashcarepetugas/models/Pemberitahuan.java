package com.unpad.trashcarepetugas.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Pemberitahuan {
    private String pengirim;
    private String penerima;
    @ServerTimestamp Date waktu;

    public Pemberitahuan(String pengirim, String penerima, Date waktu) {
        this.pengirim = pengirim;
        this.penerima = penerima;
        this.waktu = waktu;
    }

    public String getPengirim() {
        return pengirim;
    }

    public void setPengirim(String pengirim) {
        this.pengirim = pengirim;
    }

    public String getPenerima() {
        return penerima;
    }

    public void setPenerima(String penerima) {
        this.penerima = penerima;
    }

    public Date getWaktu() {
        return waktu;
    }

    public void setWaktu(Date waktu) {
        this.waktu = waktu;
    }
}
