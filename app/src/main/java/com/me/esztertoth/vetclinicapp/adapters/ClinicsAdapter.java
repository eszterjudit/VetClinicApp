package com.me.esztertoth.vetclinicapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.model.Clinic;

import java.util.List;

public class ClinicsAdapter extends RecyclerView.Adapter<ClinicViewHolder> {

    private List<Clinic> clinincs;
    private RecyclerViewClickListener clinicsClickListener;

    public ClinicsAdapter(List<Clinic> clinics, RecyclerViewClickListener clinicsClickListener) {
        this.clinincs = clinics;
        this.clinicsClickListener = clinicsClickListener;
    }

    @Override
    public ClinicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.clinic_list_item, parent, false);
        ClinicViewHolder pvh = new ClinicViewHolder(v, clinicsClickListener);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ClinicViewHolder holder, int position) {
        holder.setName(clinincs.get(position).getName());
        holder.setAddress(clinincs.get(position).getAddress().toString());
    }

    @Override
    public int getItemCount() {
        return clinincs.size();
    }

}
