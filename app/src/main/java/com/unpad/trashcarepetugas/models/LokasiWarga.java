package com.unpad.trashcarepetugas.models;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class LokasiWarga {

    private GeoPoint geo_point;
    private @ServerTimestamp Date timestamp;
    private Warga warga;

    public LokasiWarga(GeoPoint geo_point, Date timestamp, Warga warga) {
        this.geo_point = geo_point;
        this.timestamp = timestamp;
        this.warga = warga;
    }
    public LokasiWarga() {
    }

    public GeoPoint getGeo_point() {
        return geo_point;
    }

    public void setGeo_point(GeoPoint geo_point) {
        this.geo_point = geo_point;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Warga getWarga() {
        return warga;
    }

    public void setWarga(Warga warga) {
        this.warga = warga;
    }

    @Override
    public String toString() {
        return "LokasiWarga{" +
                "geo_point=" + geo_point +
                ", timestamp=" + timestamp +
                ", warga=" + warga +
                '}';
    }
}
