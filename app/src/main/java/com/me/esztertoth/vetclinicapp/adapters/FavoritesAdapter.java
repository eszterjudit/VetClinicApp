package com.me.esztertoth.vetclinicapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.model.Clinic;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesViewHolder> {

    private List<Clinic> favorites;
    private RecyclerViewClickListener favoritesClickListener;

    public FavoritesAdapter(List<Clinic> favorites, RecyclerViewClickListener favoritesClickListener) {
        this.favorites = favorites;
        this.favoritesClickListener = favoritesClickListener;
    }

    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_list_item, parent, false);
        FavoritesViewHolder favoritesViewHolder = new FavoritesViewHolder(v, favoritesClickListener);
        return favoritesViewHolder;
    }

    @Override
    public void onBindViewHolder(FavoritesViewHolder holder, int position) {
        holder.setName(favorites.get(position).getName());
        holder.setAddress(favorites.get(position).getAddress().toString());
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }
}
