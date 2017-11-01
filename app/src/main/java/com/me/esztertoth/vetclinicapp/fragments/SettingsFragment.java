package com.me.esztertoth.vetclinicapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.me.esztertoth.vetclinicapp.App;
import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.adapters.RecyclerViewClickListener;
import com.me.esztertoth.vetclinicapp.adapters.SettingsListAdapter;
import com.me.esztertoth.vetclinicapp.dialog.MapPerimeterPickerDialog;
import com.me.esztertoth.vetclinicapp.model.SettingsItem;
import com.me.esztertoth.vetclinicapp.utils.FavoriteUtils;
import com.me.esztertoth.vetclinicapp.utils.VetClinicPreferences;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsFragment extends Fragment implements PerimeterChangedCallback {

    @BindView(R.id.settings_list_recyclerview) RecyclerView settingsListRecyclerView;

    @Inject VetClinicPreferences prefs;
    @Inject FavoriteUtils favoriteUtils;

    private static int KILOMETERS = 1000;

    private List<SettingsItem> settingsItemList;
    private SettingsListAdapter settingsListAdapter;

    SettingsItem mapPerimeter = new SettingsItem();

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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        ButterKnife.bind(this, view);

        settingsItemList = new ArrayList<>();

        initSettingsItems();

        RecyclerViewClickListener settingsClickListener = (view1, position) -> {
            openSettingsDialogForPosition(position);
        };

        settingsListAdapter = new SettingsListAdapter(settingsItemList, settingsClickListener);
        settingsListRecyclerView.setAdapter(settingsListAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        settingsListRecyclerView.setLayoutManager(llm);

        return view;
    }

    private void openSettingsDialogForPosition(int position) {
        switch (position) {
            case 0:
                openMapPerimeterDialog();
                break;
            case 1:
                openDeleteAllFavsDialog();
                break;
            default:
                break;
        }
    }

    private void openMapPerimeterDialog() {
        MapPerimeterPickerDialog mapPerimeterPickerDialog = new MapPerimeterPickerDialog(this);
        FragmentManager fm = getFragmentManager();
        mapPerimeterPickerDialog.show(fm, "");
    }

    private void openDeleteAllFavsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.delete_all_favs_dialog_title))
                .setMessage(getString(R.string.delete_all_favs_dialog_description))
                .setPositiveButton(R.string.delete_all_favs_dialog_positive_button, (dialog, which) -> favoriteUtils.deleteAllFavorites())
                .setNegativeButton(R.string.delete_all_favs_dialog_negative_button, (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void initSettingsItems() {

        mapPerimeter.setIcon(getActivity().getDrawable(R.drawable.ic_map));
        mapPerimeter.setTitle(getString(R.string.map_perimeter_settings_item_title));
        mapPerimeter.setDescription(getString(R.string.map_perimeter_settings_item_description_1) + prefs.getPerimeter() / KILOMETERS + getString(R.string.map_perimeter_settings_item_description_2));

        settingsItemList.add(mapPerimeter);

        if(!prefs.getIsVet()) {
            SettingsItem deleteAllFavs = new SettingsItem();

            deleteAllFavs.setIcon(getActivity().getDrawable(R.drawable.ic_delete));
            deleteAllFavs.setTitle(getActivity().getString(R.string.delete_all_favs_settings_item_title));
            deleteAllFavs.setDescription(getActivity().getString(R.string.delete_all_favs_settings_item_description));

            settingsItemList.add(deleteAllFavs);
        }
    }

    @Override
    public void perimeterChanged() {
        mapPerimeter.setDescription(getString(R.string.map_perimeter_settings_item_description_1) + prefs.getPerimeter() / KILOMETERS + getString(R.string.map_perimeter_settings_item_description_2));
        settingsListAdapter.notifyDataSetChanged();
    }
}
