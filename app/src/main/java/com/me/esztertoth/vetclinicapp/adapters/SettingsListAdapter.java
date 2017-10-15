package com.me.esztertoth.vetclinicapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.model.SettingsItem;

import java.util.List;

public class SettingsListAdapter extends RecyclerView.Adapter<SettingsViewHolder> {

    private List<SettingsItem> settingsItemList;
    private SettingsClickListener settingsClickListener;

    public SettingsListAdapter(List<SettingsItem> settingsItemList, SettingsClickListener settingsClickListener) {
        this.settingsItemList = settingsItemList;
        this.settingsClickListener = settingsClickListener;
    }

    @Override
    public SettingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_list_item, parent, false);
        SettingsViewHolder holder = new SettingsViewHolder(v, settingsClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(SettingsViewHolder holder, int position) {
        holder.setSettingsItemImage(settingsItemList.get(position).getIcon());
        holder.setSettingsItemTitle(settingsItemList.get(position).getTitle());
        holder.setSettingsItemDescription(settingsItemList.get(position).getDescription());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return settingsItemList.size();
    }

}
