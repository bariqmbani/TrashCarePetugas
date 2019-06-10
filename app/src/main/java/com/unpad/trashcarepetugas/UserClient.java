package com.unpad.trashcarepetugas;

import android.app.Application;

import com.unpad.trashcarepetugas.models.Petugas;


public class UserClient extends Application {

    private Petugas petugas;

    public Petugas getPetugas() {
        return petugas;
    }

    public void setPetugas(Petugas petugas) {
        this.petugas = petugas;
    }
}
