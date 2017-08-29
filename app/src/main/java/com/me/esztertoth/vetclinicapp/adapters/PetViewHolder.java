package com.me.esztertoth.vetclinicapp.adapters;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.esztertoth.vetclinicapp.R;

public class PetViewHolder extends RecyclerView.ViewHolder {

    private CardView cardView;
    private ImageView petIconView;
    private TextView nameView;
    private TextView ageView;
    private TextView typeView;

    public PetViewHolder(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.cardView);
        petIconView = (ImageView) itemView.findViewById(R.id.pet_icon);
        nameView = (TextView) itemView.findViewById(R.id.name);
        ageView = (TextView) itemView.findViewById(R.id.age);
        typeView = (TextView) itemView.findViewById(R.id.type);
    }

    public void setPetIcon(Drawable icon) {
        petIconView.setImageDrawable(icon);
    }

    public void setName(String name) {
        nameView.setText(name);
    }

    public void setAge(String age) {
        ageView.setText(age + " years old");
    }

    public void setType(String type) {
        typeView.setText(type);
    }

}
