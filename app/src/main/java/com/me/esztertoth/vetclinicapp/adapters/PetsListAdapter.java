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

import org.joda.time.DateTime;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

public class PetsListAdapter extends RecyclerView.Adapter<PetViewHolder> {

    private List<Pet> petsList;
    private Context context;

    private PetListAction petListAction;

    private static final String FORMAT = "yyyy-MM-dd";

    public PetsListAdapter(Context context, List<Pet> petsList, PetListAction deletePetCallback) {
        this.context = context;
        this.petsList = petsList;
        this.petListAction = deletePetCallback;
    }

    @Override
    public PetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_list_item, parent, false);
        return new PetViewHolder(v, petListAction, context);
    }

    @Override
    public void onBindViewHolder(PetViewHolder holder, int position) {
        holder.setPetId(petsList.get(position).getId());
        holder.setPetIcon(getPetIconByType(petsList.get(position).getType()));
        holder.setName(petsList.get(position).getName());
        holder.setType(petsList.get(position).getType().getValue());
        holder.setAge(calculateBirthDate(petsList.get(position)));
        holder.setWeight(petsList.get(position).getWeight());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private int calculateBirthDate(Pet pet) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(FORMAT);
        DateTime dt = formatter.parseDateTime(pet.getDateOfBirth());
        DateTime today = DateTime.now();

        return Years.yearsBetween(dt, today).getYears();
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
