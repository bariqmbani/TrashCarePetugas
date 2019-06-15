package com.unpad.trashcarepetugas.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Artikel implements Parcelable {
    public static final String Art = "ART";
    private String judul;
    private String jenis;
    private String sumber;
    private String isi;
    private String img;


    public Artikel() {
    }


    public String getJudul() { return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getSumber() { return sumber;
    }

    public void setSumber(String sumber) {
        this.sumber = sumber;
    }

    public String getIsi() { return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getImg() { return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    protected Artikel(Parcel in) {
        judul = in.readString();
        jenis = in.readString();
        sumber = in.readString();
        isi = in.readString();
        img = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(judul);
        dest.writeString(jenis);
        dest.writeString(sumber);
        dest.writeString(isi);
        dest.writeString(img);
    }

    public static final Parcelable.Creator<Artikel> CREATOR = new Parcelable.Creator<Artikel>() {
        public Artikel createFromParcel(Parcel in) {
            return new Artikel(in);
        }

        public Artikel[] newArray(int size) {
            return new Artikel[size];
        }
    };
}
