package com.me.esztertoth.vetclinicapp.model;

public enum PetType {
    ALL, CAT, DOG, REPTILE, BIRD, RODENT;

    @Override
    public String toString() {
        switch (this) {
            case ALL:
                return "All pet types";
            case CAT:
                return "Cat";
            case DOG:
                return "Dog";
            case REPTILE:
                return "Reptile";
            case BIRD:
                return "Bird";
            case RODENT:
                return "Rodent";
            default:
                throw new IllegalArgumentException();
        }
    }
}
