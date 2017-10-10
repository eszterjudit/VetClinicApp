package com.me.esztertoth.vetclinicapp.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.model.Pet;
import com.me.esztertoth.vetclinicapp.model.PetType;

import java.util.List;

public class PetsListAdapter extends RecyclerView.Adapter<PetViewHolder> {

    private List<Pet> petsList;
    private Context context;

    public PetsListAdapter(Context context, List<Pet> petsList) {
        this.context = context;
        this.petsList = petsList;
    }

    public void addData(Pet pet) {
        petsList.add(pet);
        notifyDataSetChanged();
    }

    @Override
    public PetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_list_item, parent, false);
        PetViewHolder pvh = new PetViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PetViewHolder holder, int position) {
        holder.setPetIcon(getPetIconByType(petsList.get(position).getType()));
        holder.setName(petsList.get(position).getName());
        holder.setType(petsList.get(position).getType().toString());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return petsList.size();
    }

    private Drawable getPetIconByType(PetType type) {
        Drawable icon = null;
        switch (type) {
            case CAT:
                icon = context.getDrawable(R.drawable.ic_cat);
                break;
            case DOG:
                icon = context.getDrawable(R.drawable.ic_dog);
                break;
            case REPTILE:
                icon = context.getDrawable(R.drawable.ic_reptile);
                break;
            case BIRD:
                icon = context.getDrawable(R.drawable.ic_bird);
                break;
            case RODENT:
                icon = context.getDrawable(R.drawable.ic_rodent);
                break;
        }

        return icon;
    }
}
