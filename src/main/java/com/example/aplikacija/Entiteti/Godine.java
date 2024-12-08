package com.example.aplikacija.Entiteti;

import java.time.LocalDate;
import java.time.Period;

public sealed interface Godine permits Osoba{

    default Integer odrediGodine(LocalDate datumRodjenja){
        LocalDate now = LocalDate.now();

        Integer godine = Period.between(datumRodjenja, now).getYears();

        return godine;
    };
}
