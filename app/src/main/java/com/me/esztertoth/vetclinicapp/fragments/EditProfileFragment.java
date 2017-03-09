package com.me.esztertoth.vetclinicapp.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.me.esztertoth.vetclinicapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfileFragment extends Fragment {

    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this, view);
        fab = ButterKnife.findById(getActivity(), R.id.fab);
        hideFloatingActionButton();
        return view;
    }

    @OnClick(R.id.cancel_edit_button)
    public void cancelEdit() {
        showFloatingActionButton();
        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }

    @OnClick(R.id.save_profile_button)
    public void saveProfile() {
        showFloatingActionButton();
        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }

    private void hideFloatingActionButton() {
        fab.setVisibility(View.GONE);
    }

    private void showFloatingActionButton() {
        fab.setVisibility(View.VISIBLE);
    }

}
