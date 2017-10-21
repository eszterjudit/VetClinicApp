package com.me.esztertoth.vetclinicapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Vet extends User implements Serializable {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("speciality")
    private List<PetType> speciality;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<PetType> getSpeciality() {
        return speciality;
    }

    public void setSpeciality(List<PetType> speciality) {
        this.speciality = speciality;
    }

}
