package com.example.aplikacija.Entiteti;

import java.io.Serializable;
import java.time.LocalDate;

public class Sluzbenik extends Osoba implements Serializable{

    private Sluzbenik(SluzbenikBuilder builder) {
        super(builder.id, builder.ime, builder.prezime, builder.OIB, builder.podrijetlo, builder.datumRodjenja);
    }

    public static class SluzbenikBuilder{

        private Integer id;
        private String ime;
        private String prezime;
        private String OIB;
        private Podrijetlo podrijetlo;
        private LocalDate datumRodjenja;

        public SluzbenikBuilder(Integer id, String OIB) {
            this.id = id;
            this.OIB = OIB;
        }

        public SluzbenikBuilder unesiIme(String ime){
            this.ime = ime;

            return this;
        }

        public SluzbenikBuilder unesiPrezime(String prezime){
            this.prezime = prezime;

            return this;
        }

        public SluzbenikBuilder unesiPodrijetlo(Podrijetlo podrijetlo){
            this.podrijetlo = podrijetlo;

            return this;
        }

        public SluzbenikBuilder unesiDatumRodjenja(LocalDate datumRodjenja){
            this.datumRodjenja = datumRodjenja;

            return this;
        }

        public Sluzbenik build(){
            return new Sluzbenik(this);
        }
    }
}
