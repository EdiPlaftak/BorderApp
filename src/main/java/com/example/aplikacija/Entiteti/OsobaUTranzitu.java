package com.example.aplikacija.Entiteti;

import java.io.Serializable;
import java.time.LocalDate;

public class OsobaUTranzitu extends Osoba implements Serializable{

    private String odredisnaDrzava;

    public OsobaUTranzitu(Integer id, String ime, String prezime, String OIB, Podrijetlo podrijetlo, LocalDate datumRodjenja, String odredisnaDrzava) {
        super(id, ime, prezime, OIB, podrijetlo, datumRodjenja);
        this.odredisnaDrzava = odredisnaDrzava;
    }

    public String getOdredisnaDrzava() {
        return odredisnaDrzava;
    }

    public void setOdredisnaDrzava(String odredisnaDrzava) {
        this.odredisnaDrzava = odredisnaDrzava;
    }
}
