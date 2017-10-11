package com.me.esztertoth.vetclinicapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Vet implements Serializable {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("speciality")
    private List<PetType> speciality;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<PetType> getSpeciality() {
        return speciality;
    }

    public void setSpeciality(List<PetType> speciality) {
        this.speciality = speciality;
    }

}
