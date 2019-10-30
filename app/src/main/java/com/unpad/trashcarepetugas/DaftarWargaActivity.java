package com.unpad.trashcarepetugas;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.unpad.trashcarepetugas.adapters.WargaRecyclerAdapter;
import com.unpad.trashcarepetugas.models.LokasiWarga;

import java.util.ArrayList;

public class DaftarWargaActivity extends AppCompatActivity {

    private final static String TAG = "DaftarWargaActivity";

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView mWargaListRecyclerView;
    private ArrayList<LokasiWarga> wargas = new ArrayList<>();
    private WargaRecyclerAdapter mWargaRecyclerAdapter;


    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_warga);

        db = FirebaseFirestore.getInstance();

        mWargaListRecyclerView = findViewById(R.id.warga_list_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mWargaListRecyclerView.setLayoutManager(layoutManager);

        swipeRefresh = findViewById(R.id.refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                clearWargaList();
                initWargaList();
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                    }
                }, 500);
            }
        });
        initWargaList();
        Toolbar toolbar = findViewById(R.id.toolbarDaftarWarga);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void clearWargaList() {
        wargas.clear();
        mWargaRecyclerAdapter.notifyDataSetChanged();
    }

    private void initWargaList() {
        db.collection("Lokasi Warga").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    wargas = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        LokasiWarga lokasiWarga = document.toObject(LokasiWarga.class);
                        if (lokasiWarga.getWarga().isRequest() != false) {
                            wargas.add(lokasiWarga);
                        }
                    }
                    mWargaRecyclerAdapter = new WargaRecyclerAdapter(wargas);
                    mWargaListRecyclerView.setAdapter(mWargaRecyclerAdapter);
                    mWargaRecyclerAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
//                    Toast.makeText(this, "gagal", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
