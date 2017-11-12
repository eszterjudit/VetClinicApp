package com.me.esztertoth.vetclinicapp.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.esztertoth.vetclinicapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PetViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.cardView) CardView cardView;
    @BindView(R.id.pet_icon) ImageView petIconView;
    @BindView(R.id.name) TextView nameView;
    @BindView(R.id.age) TextView ageView;
    @BindView(R.id.type) TextView typeView;
    @BindView(R.id.weight) TextView weightView;

    private long petId;
    private PetListAction petListAction;
    private Context context;

    public PetViewHolder(View itemView, PetListAction deletePetCallback, Context context) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.petListAction = deletePetCallback;
        this.context = context;
    }

    public void setPetId(long petId) { this.petId = petId; }

    public void setPetIcon(Drawable icon) {
        petIconView.setImageDrawable(icon);
    }

    public void setName(String name) {
        nameView.setText(name);
    }

    public void setAge(int age) {
        ageView.setText(String.format(context.getString(R.string.pet_years_old), age));
    }

    public void setWeight(double weight) {
        weightView.setText(String.format(context.getString(R.string.pet_kg), weight));
    }

    public void setType(String type) {
        typeView.setText(type);
    }

    @OnClick(R.id.delete_pet)
    void deletePet() {
        petListAction.deletePet(petId);
    }

    @OnClick(R.id.edit_pet)
    void editPet() {
        petListAction.editPet(petId);
    }

}
