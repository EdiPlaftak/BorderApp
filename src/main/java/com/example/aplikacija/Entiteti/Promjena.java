package com.example.aplikacija.Entiteti;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Promjena implements Serializable {

    private Osoba osoba;

    private String staraVrijednost;

    private String novaVrijednost;

    private String korisnickaRola;

    private LocalDateTime datumIVrijeme;


    public Promjena(Osoba osoba, String staraVrijednost, String novaVrijednost, String korisnickaRola, LocalDateTime datumIVrijeme) {
        this.osoba = osoba;
        this.staraVrijednost = staraVrijednost;
        this.novaVrijednost = novaVrijednost;
        this.korisnickaRola = korisnickaRola;
        this.datumIVrijeme = datumIVrijeme;
    }

    public Osoba getOsoba() {
        return osoba;
    }

    public void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }

    public String getStaraVrijednost() {
        return staraVrijednost;
    }

    public void setStaraVrijednost(String staraVrijednost) {
        this.staraVrijednost = staraVrijednost;
    }

    public String getNovaVrijednost() {
        return novaVrijednost;
    }

    public void setNovaVrijednost(String novaVrijednost) {
        this.novaVrijednost = novaVrijednost;
    }

    public String getKorisnickaRola() {
        return korisnickaRola;
    }

    public void setKorisnickaRola(String korisnickaRola) {
        this.korisnickaRola = korisnickaRola;
    }

    public LocalDateTime getDatumIVrijeme() {
        return datumIVrijeme;
    }

    public void setDatumIVrijeme(LocalDateTime datumIVrijeme) {
        this.datumIVrijeme = datumIVrijeme;
    }
}
