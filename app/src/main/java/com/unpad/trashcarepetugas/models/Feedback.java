package com.unpad.trashcarepetugas.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Feedback {
    private @ServerTimestamp Date waktu;
    private String pengirim;
    private float rating;
    private String feedback;

    public Feedback(Date waktu, String pengirim, float rating, String feedback) {
        this.waktu = waktu;
        this.pengirim = pengirim;
        this.rating = rating;
        this.feedback = feedback;
    }

    public Date getWaktu() {
        return waktu;
    }

    public void setWaktu(Date waktu) {
        this.waktu = waktu;
    }

    public String getPengirim() {
        return pengirim;
    }

    public void setPengirim(String pengirim) {
        this.pengirim = pengirim;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}

