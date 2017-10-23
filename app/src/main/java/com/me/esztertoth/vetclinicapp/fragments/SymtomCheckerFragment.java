package com.me.esztertoth.vetclinicapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.me.esztertoth.vetclinicapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SymtomCheckerFragment extends Fragment {

    @BindView(R.id.question) TextView question;
    @BindView(R.id.answer) TextView answer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_symptom_checker, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

}
