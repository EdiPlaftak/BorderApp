package com.example.aplikacija.Entiteti;

import java.io.Serializable;
import java.time.LocalDate;

public class Turist extends Osoba implements Serializable{

    private String lokacijaPosjeta;

    public Turist(Integer id, String ime, String prezime, String OIB, Podrijetlo podrijetlo, LocalDate datumRodjenja, String lokacijaPosjeta) {
        super(id, ime, prezime, OIB, podrijetlo, datumRodjenja);
        this.lokacijaPosjeta = lokacijaPosjeta;
    }

    public String getLokacijaPosjeta() {
        return lokacijaPosjeta;
    }

    public void setLokacijaPosjeta(String lokacijaPosjeta) {
        this.lokacijaPosjeta = lokacijaPosjeta;
    }
}
