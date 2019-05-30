package com.unpad.trashcarepetugas.models;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class LokasiPetugas {
    private GeoPoint geo_point;
    private @ServerTimestamp Date timestamp;
    private Petugas petugas;

    public LokasiPetugas(GeoPoint geo_point, Date timestamp, Petugas petugas) {
        this.geo_point = geo_point;
        this.timestamp = timestamp;
        this.petugas = petugas;
    }

    public LokasiPetugas() {
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

    public Petugas getPetugas() {
        return petugas;
    }

    public void setPetugas(Petugas petugas) {
        this.petugas = petugas;
    }

    @Override
    public String toString() {
        return "LokasiPetugas{" +
                "geo_point=" + geo_point +
                ", timestamp=" + timestamp +
                ", petugas=" + petugas +
                '}';
    }
}

