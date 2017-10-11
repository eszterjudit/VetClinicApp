package com.me.esztertoth.vetclinicapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pet {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private PetType type;
    @SerializedName("weight")
    @Expose
    private double weight;
    @SerializedName("dateOfBirth")
    private BirthDate dateOfBirth;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public BirthDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(BirthDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
