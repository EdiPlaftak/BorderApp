package com.example.aplikacija.Entiteti;

import java.io.Serializable;
import java.time.LocalDate;

public non-sealed abstract class Osoba implements Godine, Serializable {

    private Integer Id;
    private String ime;
    private String prezime;
    private String OIB;
    private Podrijetlo podrijetlo;
    private LocalDate datumRodjenja;

    public Osoba(Integer id, String ime, String prezime, String OIB, Podrijetlo podrijetlo, LocalDate datumRodjenja) {
        Id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.OIB = OIB;
        this.podrijetlo = podrijetlo;
        this.datumRodjenja = datumRodjenja;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getOIB() {
        return OIB;
    }

    public void setOIB(String OIB) {
        this.OIB = OIB;
    }

    public Podrijetlo getPodrijetlo() {
        return podrijetlo;
    }

    public void setPodrijetlo(Podrijetlo podrijetlo) {
        this.podrijetlo = podrijetlo;
    }

    public LocalDate getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(LocalDate datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }
}
