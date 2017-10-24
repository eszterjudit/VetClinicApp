package com.me.esztertoth.vetclinicapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
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


    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Vet))return false;
        Vet vet = (Vet)obj;
        return vet.getId() == ((Vet) obj).getId();
    }
}
