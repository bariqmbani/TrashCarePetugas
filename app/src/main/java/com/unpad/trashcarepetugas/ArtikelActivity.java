package com.unpad.trashcarepetugas;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.unpad.trashcarepetugas.adapters.ArtikelAdapter;
import com.unpad.trashcarepetugas.models.Artikel;

import java.util.ArrayList;

public class ArtikelActivity extends AppCompatActivity {

    private static final String TAG = "ArtikelAct";

    public static  final  String COLLECTION_NAME_KEY = "artikel";
    public static  final  String JUDUL_KEY = "judul";
    public static  final  String JENIS_KEY = "jenis";
    public static  final  String IMG_KEY = "pict";

    TextView dispJudul, dispJenis;
    ImageView dispImg;

    RecyclerView list_artikel;
    private DatabaseReference databaseReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static ArrayList<Artikel> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artikel);

        list_artikel = findViewById(R.id.list_artikel);
        dispJudul = findViewById(R.id.tvJudul);
        dispJenis = findViewById(R.id.tvJenis);
        dispImg  = findViewById(R.id.ivArtikel);

        list_artikel.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        list_artikel.setLayoutManager(layoutManager);
        list_artikel.setItemAnimator(new DefaultItemAnimator());

        getListArtikel();

        Toolbar toolbar = findViewById(R.id.toolbarArtikel);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void getListArtikel(){
        db.collection("artikel").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    data = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        Artikel artikel = document.toObject(Artikel.class);
                        data.add(artikel);
                    }
                    adapter = new ArtikelAdapter(data);
                    list_artikel.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                    Toast.makeText(ArtikelActivity.this, "Please try again" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}