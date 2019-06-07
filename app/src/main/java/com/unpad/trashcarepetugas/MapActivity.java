package com.unpad.trashcarepetugas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.unpad.trashcarepetugas.adapters.WargaRecyclerAdapter;
import com.unpad.trashcarepetugas.models.LokasiPetugas;
import com.unpad.trashcarepetugas.models.LokasiWarga;

import java.util.ArrayList;

import static com.unpad.trashcarepetugas.util.Constants.MAPVIEW_BUNDLE_KEY;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";

    //widgets
    private RecyclerView mWargaListRecyclerView;
    private MapView mMapView;
//    private LokasiWarga mLokasiWarga;

    TextView nama, alamat;
    String id;


    //vars
    private ArrayList<LokasiWarga> mWargaList = new ArrayList<>();
    private ArrayList<LokasiWarga> mLokasiWarga = new ArrayList<>();
    private WargaRecyclerAdapter mWargaRecyclerAdapter;
    private FirebaseFirestore db;


    //    private LokasiWarga mLokasiWarga;
    private FusedLocationProviderClient mFusedLocationClient;

    private GoogleMap mGoogleMap;
    private LatLngBounds mMapBoundary;
    private LokasiPetugas mPosisiPetugas;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        db = FirebaseFirestore.getInstance();
        id = getIntent().getExtras().getString("ID");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mWargaListRecyclerView = findViewById(R.id.warga_list_recycler_view);
        mMapView = findViewById(R.id.warga_list_map);

        nama = findViewById(R.id.nama);
        alamat = findViewById(R.id.alamat);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mWargaListRecyclerView.setLayoutManager(layoutManager);

        initWargaListRecyclerView();
        initGoogleMap(savedInstanceState);
//        setPosisiPetugas();


    }


    private void setCameraView() {

        db.collection("Lokasi Petugas").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    mPosisiPetugas = documentSnapshot.toObject(LokasiPetugas.class);
                    Log.d(TAG,"ambil data success\n" + mPosisiPetugas.getGeo_point());
                    Log.d(TAG, mPosisiPetugas.getGeo_point().getLatitude() + "\n" + mPosisiPetugas.getGeo_point().getLatitude());
                    // Set a boundary to start
                    double bottomBoundary = mPosisiPetugas.getGeo_point().getLatitude() - .002;
                    double leftBoundary = mPosisiPetugas.getGeo_point().getLongitude() - .002;
                    double topBoundary = mPosisiPetugas.getGeo_point().getLatitude() + .002;
                    double rightBoundary = mPosisiPetugas.getGeo_point().getLongitude() + .002;

                    mMapBoundary = new LatLngBounds(
                            new LatLng(bottomBoundary, leftBoundary),
                            new LatLng(topBoundary, rightBoundary)
                    );

                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mMapBoundary, 0));
                }
            }
        });


    }

    private void setPosisiPetugas() {
    }



    private void initGoogleMap(Bundle savedInstanceState) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }

    private void initWargaListRecyclerView() {
         db.collection("Lokasi Warga").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    mWargaList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Log.d(TAG, document.getId() + " => " + document.getData());
                        LokasiWarga lokasiWarga = document.toObject(LokasiWarga.class);
                        mWargaList.add(lokasiWarga);
                        getLokasiWarga(lokasiWarga);
                        Log.d(TAG,"nama: " + lokasiWarga.getWarga().getNama());
                        Log.d(TAG, "latitude: " + lokasiWarga.getGeo_point().getLatitude() + "\nlongitude: " + lokasiWarga.getGeo_point().getLongitude());
                    }
                    mWargaRecyclerAdapter = new WargaRecyclerAdapter(mWargaList);
                    mWargaListRecyclerView.setAdapter(mWargaRecyclerAdapter);
                    mWargaRecyclerAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
//                    Toast.makeText(this, "gagal", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getLokasiWarga(LokasiWarga lokasiWarga) {
        DocumentReference lokasiRef = db.collection("Lokasi Warga").document();
        lokasiRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().toObject(LokasiWarga.class) != null) {
                        mLokasiWarga.add(task.getResult().toObject(LokasiWarga.class));
                    }
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        setCameraView();
        mGoogleMap = map;
        map.setMyLocationEnabled(true);
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}



















