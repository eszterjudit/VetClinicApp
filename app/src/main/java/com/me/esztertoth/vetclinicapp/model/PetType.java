package com.me.esztertoth.vetclinicapp.model;

public enum PetType {
    CAT("Cat"), DOG("Dog"), REPTILE("Reptile"), BIRD("Bird"), RODENT("Rodent");

    private String value;

    PetType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
