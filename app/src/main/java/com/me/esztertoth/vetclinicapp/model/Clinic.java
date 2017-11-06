package com.me.esztertoth.vetclinicapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class Clinic implements Serializable {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("clinicName")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("openingHour")
    private String openingHour;
    @SerializedName("closingHour")
    private String closingHour;
    @SerializedName("vets")
    private List<Vet> vetList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpeningHour() {
        return openingHour;
    }

    public void setOpeningHour(String openingHour) {
        this.openingHour = openingHour;
    }

    public String getClosingHour() {
        return closingHour;
    }

    public void setClosingHour(String closingHour) {
        this.closingHour = closingHour;
    }

    public List<Vet> getVetList() {
        return vetList;
    }

    public void setVetList(List<Vet> vetList) {
        this.vetList = vetList;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Clinic))return false;
        Clinic clinic = (Clinic)obj;
        return clinic.getId() == ((Clinic) obj).getId();
    }
}
