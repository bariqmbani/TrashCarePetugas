package com.unpad.trashcarepetugas;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;
import com.unpad.trashcarepetugas.adapters.WargaRecyclerAdapter;
import com.unpad.trashcarepetugas.models.LokasiPetugas;
import com.unpad.trashcarepetugas.models.LokasiWarga;
import com.unpad.trashcarepetugas.util.ViewWeightAnimationWrapper;

import java.util.ArrayList;

import static com.unpad.trashcarepetugas.util.Constants.MAPVIEW_BUNDLE_KEY;


public class MapActivity extends AppCompatActivity
        implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnInfoWindowClickListener {

    private static final String TAG = "MapActivity";
    private static final int MAP_LAYOUT_STATE_CONTRACTED = 0;
    private static final int MAP_LAYOUT_STATE_EXPANDED = 1;

    //widgets
    private RecyclerView mWargaListRecyclerView;
    private MapView mMapView;
    private RelativeLayout mMapContainer;


    TextView nama, alamat;
    ImageButton fullscreen;
    String id;


    //vars
    private ArrayList<LokasiWarga> wargas = new ArrayList<>();
    private WargaRecyclerAdapter mWargaRecyclerAdapter;
    
    private FirebaseFirestore db;
    private int mMapLayoutState = 0;

    //    private LokasiWarga mLokasiWarga;
    private FusedLocationProviderClient mFusedLocationClient;

    private GoogleMap mGoogleMap;
    private LatLngBounds mMapBoundary;
    private LokasiPetugas mPosisiPetugas;
    private GeoApiContext mGeoApiContext = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        db = FirebaseFirestore.getInstance();
        id = getIntent().getExtras().getString("ID");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mMapContainer = findViewById(R.id.map_container);
        mWargaListRecyclerView = findViewById(R.id.warga_list_recycler_view);
        mMapView = findViewById(R.id.warga_list_map);

        nama = findViewById(R.id.nama);
        alamat = findViewById(R.id.alamat);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mWargaListRecyclerView.setLayoutManager(layoutManager);

        fullscreen = findViewById(R.id.btn_full_screen_map);
        fullscreen.setOnClickListener(this);

        initWargaListRecyclerView();
        initGoogleMap(savedInstanceState);

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

        if (mGeoApiContext == null) {
            mGeoApiContext = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.google_maps_api_key))
                    .build();
        }
    }

    private void calculateDirections(Marker marker){
        Log.d(TAG, "calculateDirections: calculating directions.");

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                marker.getPosition().latitude,
                marker.getPosition().longitude
        );
        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);

        directions.alternatives(true);
        directions.origin(
                new com.google.maps.model.LatLng(
                        mPosisiPetugas.getGeo_point().getLatitude(),
                        mPosisiPetugas.getGeo_point().getLongitude()
                )
        );
        Log.d(TAG, "calculateDirections: destination: " + destination.toString());
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                Log.d(TAG, "calculateDirections: routes: " + result.routes[0].toString());
                Log.d(TAG, "calculateDirections: duration: " + result.routes[0].legs[0].duration);
                Log.d(TAG, "calculateDirections: distance: " + result.routes[0].legs[0].distance);
                Log.d(TAG, "calculateDirections: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "calculateDirections: Failed to get directions: " + e.getMessage() );
                Toast.makeText(MapActivity.this,"Google API melebihi limit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initWargaListRecyclerView() {
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

    private void setCameraView() {

        db.collection("Lokasi Petugas")
                .document(((UserClient)(getApplicationContext())).getPetugas().getId_petugas())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    mPosisiPetugas = documentSnapshot.toObject(LokasiPetugas.class);
                    Log.d(TAG,"ambil data success\n" + mPosisiPetugas.getGeo_point());
                    Log.d(TAG, mPosisiPetugas.getGeo_point().getLatitude() + "\n" + mPosisiPetugas.getGeo_point().getLatitude());
                    // Set a boundary to start
                    double bottomBoundary = mPosisiPetugas.getGeo_point().getLatitude() - .0015;
                    double leftBoundary = mPosisiPetugas.getGeo_point().getLongitude() - .0015;
                    double topBoundary = mPosisiPetugas.getGeo_point().getLatitude() + .0015;
                    double rightBoundary = mPosisiPetugas.getGeo_point().getLongitude() + .0015;

                    mMapBoundary = new LatLngBounds(
                            new LatLng(bottomBoundary, leftBoundary),
                            new LatLng(topBoundary, rightBoundary)
                    );

                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mMapBoundary, 0));
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
    public void onMapReady(final GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // Maps Style
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        db.collection("Lokasi Warga").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    wargas = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        LokasiWarga lokasiWarga = document.toObject(LokasiWarga.class);
                        if (lokasiWarga.getWarga().isRequest() != false) {
                            wargas.add(lokasiWarga);

                            //this is default marker
                            map.addMarker(new MarkerOptions()
                                    .position(new LatLng(lokasiWarga.getGeo_point().getLatitude(), lokasiWarga.getGeo_point().getLongitude()))
                                    .title(lokasiWarga.getWarga().getNama())
                                    .snippet(lokasiWarga.getWarga().getAlamat()));
                            Log.d(TAG,"nama: " + lokasiWarga.getWarga().getNama());
                            Log.d(TAG, "latitude: " + lokasiWarga.getGeo_point().getLatitude() + "\nlongitude: " + lokasiWarga.getGeo_point().getLongitude());

                        }
                    }
                }
            }
        });
        mGoogleMap = map;
        map.setMyLocationEnabled(true);
        setCameraView();
        map.setOnInfoWindowClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_full_screen_map:{

                if(mMapLayoutState == MAP_LAYOUT_STATE_CONTRACTED){
                    mMapLayoutState = MAP_LAYOUT_STATE_EXPANDED;
                    expandMapAnimation();
                }
                else if(mMapLayoutState == MAP_LAYOUT_STATE_EXPANDED){
                    mMapLayoutState = MAP_LAYOUT_STATE_CONTRACTED;
                    contractMapAnimation();
                }
                break;
            }

        }
    }

    private void expandMapAnimation(){
        ViewWeightAnimationWrapper mapAnimationWrapper = new ViewWeightAnimationWrapper(mMapContainer);
        ObjectAnimator mapAnimation = ObjectAnimator.ofFloat(mapAnimationWrapper,
                "weight",
                50,
                100);
        mapAnimation.setDuration(800);

        ViewWeightAnimationWrapper recyclerAnimationWrapper = new ViewWeightAnimationWrapper(mWargaListRecyclerView);
        ObjectAnimator recyclerAnimation = ObjectAnimator.ofFloat(recyclerAnimationWrapper,
                "weight",
                50,
                0);
        recyclerAnimation.setDuration(800);

        recyclerAnimation.start();
        mapAnimation.start();
    }

    private void contractMapAnimation(){
        ViewWeightAnimationWrapper mapAnimationWrapper = new ViewWeightAnimationWrapper(mMapContainer);
        ObjectAnimator mapAnimation = ObjectAnimator.ofFloat(mapAnimationWrapper,
                "weight",
                100,
                50);
        mapAnimation.setDuration(800);

        ViewWeightAnimationWrapper recyclerAnimationWrapper = new ViewWeightAnimationWrapper(mWargaListRecyclerView);
        ObjectAnimator recyclerAnimation = ObjectAnimator.ofFloat(recyclerAnimationWrapper,
                "weight",
                0,
                50);
        recyclerAnimation.setDuration(800);

        recyclerAnimation.start();
        mapAnimation.start();
    }


    @Override
    public void onInfoWindowClick(final Marker marker) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
        builder.setMessage("Anda ingin mengangkut sampah di " + marker.getSnippet() + "?")
                .setCancelable(true)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
//                        calculateDirections(marker);
                        CollectionReference lokasiRef = db.collection("Lokasi Warga");
                        lokasiRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    wargas = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        LokasiWarga lokasiWarga = document.toObject(LokasiWarga.class);
                                        LatLng position = new LatLng(lokasiWarga.getGeo_point().getLatitude(),
                                                lokasiWarga.getGeo_point().getLongitude());
                                        Log.d(TAG, document.getId() + lokasiWarga.getWarga().getNama() + position + "\n");
                                        wargas.add(lokasiWarga);
                                        if (position.equals(marker.getPosition())) {
                                            String idWarga = lokasiWarga.getWarga().getId_warga();
                                            Log.d(TAG, "id warga: " + idWarga);
                                            final DocumentReference wargaRef = db.collection("warga").document(idWarga);
                                            wargaRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if (documentSnapshot.exists()) {
                                                        wargaRef.update("request", false);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        });
                        marker.remove();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
