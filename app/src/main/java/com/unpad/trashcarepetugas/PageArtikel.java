package com.unpad.trashcarepetugas;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.unpad.trashcarepetugas.models.Artikel;

import java.util.ArrayList;

public class PageArtikel extends AppCompatActivity {

    TextView tvJudul, tvSumber, tvIsi;
    ImageView ivArtikel;
    String judulArtikel;
    private ArrayList<Artikel> artikels = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_artikel);

        judulArtikel = getIntent().getExtras().getString("Judul Artikel");

        tvJudul = findViewById(R.id.tvJudulArt);
        tvSumber = findViewById(R.id.tvSumber);
        tvIsi = findViewById(R.id.tvIsiArt);
        ivArtikel = findViewById(R.id.ivArtikelImg);


        CollectionReference artikelRef = db.collection("artikel");
        artikelRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    artikels = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Artikel artikel = document.toObject(Artikel.class);
                        if (artikel.getJudul().equals(judulArtikel)) {
                            StringBuilder fieldJudul = new StringBuilder();
                            StringBuilder fieldSumber = new StringBuilder();
                            StringBuilder fieldIsi = new StringBuilder();
                            StringBuilder fieldImg = new StringBuilder();
                            fieldJudul.append(artikel.getJudul());
                            fieldSumber.append(artikel.getSumber());
                            fieldIsi.append(artikel.getIsi());
                            fieldImg.append(artikel.getImg());
                            tvJudul.setText(fieldJudul.toString());
                            tvSumber.setText(fieldSumber.toString());
                            tvIsi.setText(fieldIsi.toString());
                            Glide.with(context)
                        }
                    }
                }
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbarPageArtikel);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}