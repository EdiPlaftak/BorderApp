package com.example.aplikacija.Niti;

import com.example.aplikacija.Aplikacija;
import com.example.aplikacija.BazaPodataka.BazaPodataka;
import com.example.aplikacija.Entiteti.*;
import com.example.aplikacija.PrijavaController;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;

import java.util.List;

public class OsvjezavanjePodatakaNit implements Runnable{

    private String naziv;

    private TableView tableView;

    public OsvjezavanjePodatakaNit(String naziv, TableView tableView) {
        this.naziv = naziv;
        this.tableView = tableView;
    }

    @Override public void run(){

        if(naziv.isBlank() == true){
            Aplikacija.getLogger().error("Došlo je do pogreške u radu s niti!");
        }
        else if(naziv.equals("t")) {

            List<Turist> turisti = BazaPodataka.dohvatiTuriste();

            tableView.setItems(FXCollections.observableList(turisti));

        }
        else if(naziv.equals("out")){

            List<OsobaUTranzitu> osobeUTranzitu = BazaPodataka.dohvatiOsobeUTranzitu();

            tableView.setItems(FXCollections.observableList(osobeUTranzitu));

        }
        else if(naziv.equals("sl")){

            List<Sluzbenik> sluzbenici = BazaPodataka.dohvatiSluzbenike();

            List<Sluzbenik> sluzbeniciGranice = BazaPodataka.dohvatiGranice_Sluzbenik(sluzbenici, PrijavaController.getTrenutnaGranica());

            tableView.setItems(FXCollections.observableList(sluzbeniciGranice));

        }
        else {
            Aplikacija.getLogger().error("Došlo je do pogreške u radu s niti!");
        }
    }

}
