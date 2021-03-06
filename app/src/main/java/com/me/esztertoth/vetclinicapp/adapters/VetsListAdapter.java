package com.me.esztertoth.vetclinicapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.model.Vet;

import java.util.List;

public class VetsListAdapter extends RecyclerView.Adapter<VetViewHolder> {

    private List<Vet> vets;

    public VetsListAdapter(List<Vet> vets) {
        this.vets = vets;
    }

    @Override
    public VetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vet_list_item, parent, false);
        return new VetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(VetViewHolder holder, int position) {
        String name = vets.get(position).getFirstName() + " " + vets.get(position).getLastName();
        String phone = vets.get(position).getPhone();

        holder.setName(name);
        holder.setPhone(phone);
    }

    @Override
    public int getItemCount() {
        return vets.size();
    }

}
