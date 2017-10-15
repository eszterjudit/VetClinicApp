package com.me.esztertoth.vetclinicapp.adapters;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.esztertoth.vetclinicapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    @BindView(R.id.settings_item_image)
    ImageView settingsItemImage;
    @BindView(R.id.settings_item_title)
    TextView settingsItemTitle;
    @BindView(R.id.settings_item_desc)
    TextView settingsItemDescription;

    private SettingsClickListener settingsClickListener;

    public SettingsViewHolder(View itemView, SettingsClickListener settingsClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        this.settingsClickListener = settingsClickListener;
    }

    public void setSettingsItemImage(Drawable icon) {
        settingsItemImage.setImageDrawable(icon);
    }

    public void setSettingsItemTitle(String title) {
        settingsItemTitle.setText(title);
    }

    public void setSettingsItemDescription(String description) {
        settingsItemDescription.setText(description);
    }

    @Override
    public void onClick(View view) {
        settingsClickListener.onClick(view, getAdapterPosition());
    }
}
