package com.me.esztertoth.vetclinicapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.me.esztertoth.vetclinicapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VetViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.name) TextView nameTextView;
    @BindView(R.id.specialities) TextView specialitiesTextView;
    @BindView(R.id.phone) TextView phoneTextView;

    public VetViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setName(String name) {
        nameTextView.setText(name);
    }

    public void setSpecialities(String specialities) {
        specialitiesTextView.setText(specialities);
    }

    public void setPhone(String phone) {
        phoneTextView.setText(phone);
    }

}