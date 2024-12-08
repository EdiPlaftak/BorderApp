package com.example.aplikacija.Niti;

import com.example.aplikacija.Entiteti.Sluzbenik;
import com.example.aplikacija.PocetnaController;

import java.time.LocalDate;
import java.time.Period;

public class NajbliziRodjendanNit implements Runnable{

    private static Sluzbenik trenutniSluzbenik;

    public NajbliziRodjendanNit() {}


    @Override public void run(){izracunajRodjendan();}

    public synchronized void izracunajRodjendan(){

        LocalDate now = LocalDate.now();

        Integer mjeseciDoRodjendana = 0;

        for(Sluzbenik sluzbenik : PocetnaController.sluzbeniciGranice){

            Integer godine = sluzbenik.odrediGodine(sluzbenik.getDatumRodjenja());

            LocalDate datumSljedecegRodjendana = sluzbenik.getDatumRodjenja().plusYears(godine + 1);

            mjeseciDoRodjendana = Period.between(now, datumSljedecegRodjendana).getMonths();

            trenutniSluzbenik = sluzbenik;

            break;
        }

        PocetnaController.sluzbeniciGranice.remove(trenutniSluzbenik);

        PocetnaController.mapaBuducihSlavljenika.put(trenutniSluzbenik.getIme() + " " + trenutniSluzbenik.getPrezime(), mjeseciDoRodjendana);
    }

}
