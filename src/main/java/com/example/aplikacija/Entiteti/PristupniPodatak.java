package com.example.aplikacija.Entiteti;

import java.io.Serializable;

public class PristupniPodatak<T, Z> implements Serializable {

    private Integer idSluzbenika;
    private T korisnickoIme;
    private Z lozinka;
    private String korisnickaRola;

    public PristupniPodatak(Integer idSluzbenika, T korisnickoIme, Z lozinka, String korisnickaRola) {
        this.idSluzbenika = idSluzbenika;
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
        this.korisnickaRola = korisnickaRola;
    }

    public Integer getIdSluzbenika() {
        return idSluzbenika;
    }

    public void setIdSluzbenika(Integer idSluzbenika) {
        this.idSluzbenika = idSluzbenika;
    }

    public T getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(T korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public Z getLozinka() {return lozinka;}

    public void setLozinka(Z lozinka) {this.lozinka = lozinka;}

    public String getKorisnickaRola() {
        return korisnickaRola;
    }

    public void setKorisnickaRola(String korisnickaRola) {
        this.korisnickaRola = korisnickaRola;
    }
}
