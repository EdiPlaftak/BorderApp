package com.example.aplikacija.Entiteti;

import java.util.List;

public class Granica {

    private Integer id;
    private String naziv;
    private List<Sluzbenik> sluzbenici;
    private List<Osoba> straneOsobe;

    public Granica(Integer id, String naziv, List<Sluzbenik> sluzbenici, List<Osoba> straneOsobe) {
        this.id = id;
        this.naziv = naziv;
        this.sluzbenici = sluzbenici;
        this.straneOsobe = straneOsobe;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public List<Sluzbenik> getSluzbenici() {
        return sluzbenici;
    }

    public void setSluzbenici(List<Sluzbenik> sluzbenici) {
        this.sluzbenici = sluzbenici;
    }

    public List<Osoba> getStraneOsobe() {
        return straneOsobe;
    }

    public void setStraneOsobe(List<Osoba> straneOsobe) {
        this.straneOsobe = straneOsobe;
    }
}
