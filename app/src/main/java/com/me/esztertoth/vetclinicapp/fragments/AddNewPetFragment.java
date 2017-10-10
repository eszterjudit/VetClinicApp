package com.me.esztertoth.vetclinicapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.model.Pet;
import com.me.esztertoth.vetclinicapp.model.PetType;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddNewPetFragment extends Fragment {

    @BindView(R.id.pet_name_edit_text)
    EditText petNameEditText;
    @BindView(R.id.pet_age_edit_text)
    EditText petAgeEditText;
    @BindView(R.id.type_spinner)
    Spinner typeSpinnerView;

    private Pet newPet;
    private String name;
    private PetType type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_pet, container, false);
        ButterKnife.bind(this, view);

        typeSpinnerView.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, PetType.values()));

        return view;
    }

    @OnClick(R.id.save_pet_button)
    public void savePetAndClose() {
        name = petNameEditText.getText().toString();
        type = (PetType) typeSpinnerView.getItemAtPosition(typeSpinnerView.getSelectedItemPosition());

    }

}
