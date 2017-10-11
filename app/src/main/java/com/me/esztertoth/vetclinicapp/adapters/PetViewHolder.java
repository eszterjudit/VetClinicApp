package com.me.esztertoth.vetclinicapp.adapters;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.esztertoth.vetclinicapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PetViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.cardView) CardView cardView;
    @BindView(R.id.pet_icon) ImageView petIconView;
    @BindView(R.id.name) TextView nameView;
    @BindView(R.id.age) TextView ageView;
    @BindView(R.id.type) TextView typeView;
    @BindView(R.id.weight) TextView weightView;

    public PetViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setPetIcon(Drawable icon) {
        petIconView.setImageDrawable(icon);
    }

    public void setName(String name) {
        nameView.setText(name);
    }

    public void setAge(int age) {
        ageView.setText(age + " years old");
    }

    public void setWeight(double weight) {
        weightView.setText(weight + " kg");
    }

    public void setType(String type) {
        typeView.setText(type);
    }

}
