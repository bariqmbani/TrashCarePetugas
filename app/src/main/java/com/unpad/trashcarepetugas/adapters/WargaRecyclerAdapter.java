package com.unpad.trashcarepetugas.adapters;


import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unpad.trashcarepetugas.R;
import com.unpad.trashcarepetugas.models.LokasiWarga;

import java.util.ArrayList;

//import com.unpad.trashcarepetugas.models.Warga;

public class WargaRecyclerAdapter extends RecyclerView.Adapter<WargaRecyclerAdapter.ViewHolder>{

    private ArrayList<LokasiWarga> mWarga = new ArrayList<>();


    public WargaRecyclerAdapter(ArrayList<LokasiWarga> warga) {
        this.mWarga= warga;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_warga_list_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ((ViewHolder)holder).nama.setText(mWarga.get(position).getWarga().getNama());
        ((ViewHolder)holder).alamat.setText(mWarga.get(position).getWarga().getAlamat());

    }

    @Override
    public int getItemCount() {
        return mWarga.size();
//        return (mWarga != null) ? mWarga.size() : 0;

    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView nama, alamat;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.nama);
            alamat = itemView.findViewById(R.id.alamat);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}
















