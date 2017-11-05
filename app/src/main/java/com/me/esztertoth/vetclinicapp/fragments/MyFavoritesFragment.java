package com.me.esztertoth.vetclinicapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.me.esztertoth.vetclinicapp.App;
import com.me.esztertoth.vetclinicapp.ClinicDetailsActivity;
import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.adapters.FavoritesAdapter;
import com.me.esztertoth.vetclinicapp.adapters.RecyclerViewClickListener;
import com.me.esztertoth.vetclinicapp.model.Clinic;
import com.me.esztertoth.vetclinicapp.utils.VetClinicPreferences;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyFavoritesFragment extends Fragment {

    @BindView(R.id.favorites_list_recyclerview) RecyclerView favoritesRecyclerView;
    @BindView(R.id.no_favorites_message) TextView noFavoritesMessage;

    @Inject VetClinicPreferences prefs;

    private List<Clinic> favorites;
    private FavoritesAdapter favoritesAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        satisfyDependencies();
    }

    private void satisfyDependencies() {
        ((App) getActivity().getApplication()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_favorites, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("My favorite clinics");

        favorites = getFavorites();

        RecyclerViewClickListener favoritesClickListener = (view1, position) -> {
            openClinicDetailsFragment(position);
        };

        favoritesAdapter = new FavoritesAdapter(favorites, favoritesClickListener);
        favoritesRecyclerView.setAdapter(favoritesAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        favoritesRecyclerView.setLayoutManager(llm);

        return view;
    }

    private void showView(View view, boolean show) {
        if(show) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        favorites.clear();
        favorites.addAll(getFavorites());
        favoritesAdapter.notifyDataSetChanged();
        if(favorites.isEmpty()) {
            showView(noFavoritesMessage, true);
            showView(favoritesRecyclerView, false);
        } else {
            showView(noFavoritesMessage, false);
            showView(favoritesRecyclerView, true);
        }
    }

    private void openClinicDetailsFragment(int position) {
        Intent i = new Intent(getActivity(), ClinicDetailsActivity.class);
        i.putExtra("clinic", favorites.get(position));
        startActivity(i);
    }

    public List<Clinic> getFavorites() {
        return prefs.getFavoriteClinics();
    }
}
