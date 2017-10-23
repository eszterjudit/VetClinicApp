package com.me.esztertoth.vetclinicapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.me.esztertoth.vetclinicapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClinicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.name) TextView nameView;
    @BindView(R.id.address) TextView addressView;

    private RecyclerViewClickListener clinicClickListener;

    public ClinicViewHolder(View itemView, RecyclerViewClickListener clinicClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        this.clinicClickListener = clinicClickListener;
    }

    public void setName(String name) {
        nameView.setText(name);
    }

    public void setAddress(String address) {
        addressView.setText(address);
    }

    @Override
    public void onClick(View view) {
        clinicClickListener.onClick(view, getAdapterPosition());
    }

}
