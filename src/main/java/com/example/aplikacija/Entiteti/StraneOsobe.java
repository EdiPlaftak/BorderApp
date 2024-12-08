package com.example.aplikacija.Entiteti;

import java.util.ArrayList;
import java.util.List;

public class StraneOsobe<T extends Osoba>{

    private List<T> straneOsobe;

    public StraneOsobe() {
        straneOsobe = new ArrayList<>();
    }

    public void dodajStranuOsobu(T osoba){straneOsobe.add(osoba);}

    public void ukloniStranuOsobu(T osoba){straneOsobe.remove(osoba);}

    public List<T> getStraneOsobe() {
        return straneOsobe;
    }

}
