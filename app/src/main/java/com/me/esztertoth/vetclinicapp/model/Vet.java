package com.me.esztertoth.vetclinicapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class Vet extends User implements Serializable {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("speciality")
    private Set<PetType> speciality;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<PetType> getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Set<PetType> speciality) {
        this.speciality = speciality;
    }

}
