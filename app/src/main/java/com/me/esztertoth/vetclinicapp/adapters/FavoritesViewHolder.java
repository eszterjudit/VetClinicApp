package com.me.esztertoth.vetclinicapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.me.esztertoth.vetclinicapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.name) TextView nameView;
    @BindView(R.id.address) TextView addressView;

    private RecyclerViewClickListener favoritesClickListener;

    public FavoritesViewHolder(View itemView, RecyclerViewClickListener favoritesClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        this.favoritesClickListener = favoritesClickListener;
    }

    public void setName(String name) {
        nameView.setText(name);
    }

    public void setAddress(String address) {
        addressView.setText(address);
    }

    @Override
    public void onClick(View view) {
        favoritesClickListener.onClick(view, getAdapterPosition());
    }

}
