package com.example.aplikacija;

import com.example.aplikacija.BazaPodataka.BazaPodataka;
import com.example.aplikacija.Entiteti.Sluzbenik;
import com.example.aplikacija.Niti.NajbliziRodjendanNit;
import javafx.scene.control.Alert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PocetnaController {

    public static Map<String, Integer> mapaBuducihSlavljenika;

    public static List<Sluzbenik> sluzbeniciGranice;


    public void initialize(){

        List<Sluzbenik> sluzbenici = BazaPodataka.dohvatiSluzbenike();

        sluzbeniciGranice = BazaPodataka.dohvatiGranice_Sluzbenik(sluzbenici, PrijavaController.getTrenutnaGranica());

        Integer n = sluzbeniciGranice.size();

        mapaBuducihSlavljenika = new HashMap<>();


        for(int i = 0; i < n; i++){
            NajbliziRodjendanNit nit = new NajbliziRodjendanNit();

            nit.run();
        }


        String poruka = "";

        for(String kljuc : mapaBuducihSlavljenika.keySet()){
            if(mapaBuducihSlavljenika.get(kljuc) == 1){
                poruka = poruka + kljuc + " | " + mapaBuducihSlavljenika.get(kljuc) + " mjesec do rođendana\n";
            }
            else {
                poruka = poruka + kljuc + " | " + mapaBuducihSlavljenika.get(kljuc) + " mjeseca do rođendana\n";
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Budući rođendani");
        alert.setHeaderText("Lista budućih slavljenika:");
        alert.setContentText(poruka);

        alert.showAndWait();

    }

}
