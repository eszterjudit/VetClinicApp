package com.me.esztertoth.vetclinicapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.adapters.SettingsClickListener;
import com.me.esztertoth.vetclinicapp.adapters.SettingsListAdapter;
import com.me.esztertoth.vetclinicapp.dialog.MapPerimeterPickerDialog;
import com.me.esztertoth.vetclinicapp.model.SettingsItem;
import com.me.esztertoth.vetclinicapp.utils.FavoriteUtils;
import com.me.esztertoth.vetclinicapp.utils.VetClinicPreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsFragment extends Fragment {

    @BindView(R.id.settings_list_recyclerview)
    RecyclerView settingsListRecyclerView;

    private static int KILOMETERS = 1000;

    private List<SettingsItem> settingsItemList;
    private SettingsListAdapter settingsListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        ButterKnife.bind(this, view);

        settingsItemList = new ArrayList<>();

        initSettingsItems();

        SettingsClickListener settingsClickListener = (view1, position) -> {
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
        MapPerimeterPickerDialog mapPerimeterPickerDialog = new MapPerimeterPickerDialog();
        FragmentManager fm = getFragmentManager();
        mapPerimeterPickerDialog.show(fm, "");
    }

    private void openDeleteAllFavsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.delete_all_favs_dialog_title))
                .setMessage(getString(R.string.delete_all_favs_dialog_description))
                .setPositiveButton(R.string.delete_all_favs_dialog_positive_button, (dialog, which) -> {
                    FavoriteUtils.deleteAllFavorites(getContext());
                })
                .setNegativeButton(R.string.delete_all_favs_dialog_negative_button, (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void initSettingsItems() {
        SettingsItem mapPerimeter = new SettingsItem();
        mapPerimeter.setIcon(getActivity().getDrawable(R.drawable.ic_map));
        mapPerimeter.setTitle(getString(R.string.map_perimeter_settings_item_title));
        mapPerimeter.setDescription(getString(R.string.map_perimeter_settings_item_description_1) + VetClinicPreferences.getPerimeter(getContext()) / KILOMETERS + getString(R.string.map_perimeter_settings_item_description_2));

        settingsItemList.add(mapPerimeter);

        SettingsItem deleteAllFavs = new SettingsItem();
        deleteAllFavs.setIcon(getActivity().getDrawable(R.drawable.ic_delete));
        deleteAllFavs.setTitle(getActivity().getString(R.string.delete_all_favs_settings_item_title));
        deleteAllFavs.setDescription(getActivity().getString(R.string.delete_all_favs_settings_item_description));

        settingsItemList.add(deleteAllFavs);
    }

}
