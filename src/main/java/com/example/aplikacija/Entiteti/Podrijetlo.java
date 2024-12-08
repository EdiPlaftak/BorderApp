package com.example.aplikacija.Entiteti;

import java.io.Serializable;

public record Podrijetlo(String drzava, String mjesto) implements Serializable{

    public Podrijetlo(String drzava, String mjesto) {
        this.drzava = drzava;
        this.mjesto = mjesto;
    }

    @Override
    public String drzava() {
        return drzava;
    }

    @Override
    public String mjesto() {
        return mjesto;
    }
}
