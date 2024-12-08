package com.example.aplikacija;

import com.example.aplikacija.BazaPodataka.BazaPodataka;
import com.example.aplikacija.Datoteke.Datoteke;
import com.example.aplikacija.Entiteti.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PrijavaController {

    @FXML
    private ChoiceBox<String> granicaChoiceBox;

    @FXML
    private TextField korisnickoImeTextField;

    @FXML
    private TextField lozinkaTextField;

    private List<Granica> granice;
    private List<Sluzbenik> sluzbenici;
    private List<Turist> turisti;
    private List<OsobaUTranzitu> osobeUTranzitu;
    private List<Osoba> listaStranihOsoba;
    private Set<PristupniPodatak<String, String>> pristupniPodaci;

    private static PristupniPodatak<String, String> pristupniPodatakKorisnika;


    private static Granica trenutnaGranica;


    public void initialize(){
        pristupniPodaci = new HashSet<>();

        sluzbenici = BazaPodataka.dohvatiSluzbenike();
        turisti = BazaPodataka.dohvatiTuriste();
        osobeUTranzitu = BazaPodataka.dohvatiOsobeUTranzitu();

        StraneOsobe<Osoba> straneOsobe = new StraneOsobe<>();

        for(Turist turist : turisti){
            straneOsobe.dodajStranuOsobu(turist);
        }

        for(OsobaUTranzitu osobaUTranzitu : osobeUTranzitu){
            straneOsobe.dodajStranuOsobu(osobaUTranzitu);
        }

        listaStranihOsoba = straneOsobe.getStraneOsobe();

        granice = BazaPodataka.dohvatiGranice(listaStranihOsoba);

        for(Granica granica : granice){
            List<Sluzbenik> sluzbeniciGranice = BazaPodataka.dohvatiGranice_Sluzbenik(sluzbenici, granica);

            granica.setSluzbenici(sluzbeniciGranice);
        }

        Datoteke.dohvatiPristupnePodatke(pristupniPodaci);

        for(Granica granica : granice){
            granicaChoiceBox.getItems().add(granica.getNaziv());
        }
    }

    public void prijava() throws IOException {

        String nazivGranice = "";

        if(granicaChoiceBox.getValue() != null){
            nazivGranice = granicaChoiceBox.getValue();
        }

        String korisnickoIme = korisnickoImeTextField.getText();

        String lozinka = lozinkaTextField.getText();

        if(nazivGranice.isBlank() == false && korisnickoIme.isBlank() == false && lozinka.isBlank() == false){

            for(Granica granica : granice){
                if(granica.getNaziv().equals(nazivGranice)){

                    trenutnaGranica = granica;

                    List<Sluzbenik> sluzbeniciGranice = granica.getSluzbenici();

                    Integer u = 0;

                    for(Sluzbenik sluzbenik : sluzbeniciGranice){

                        Integer idSluzbenika = sluzbenik.getId();

                        for(PristupniPodatak<String, String> pristupniPodatak : pristupniPodaci){
                            if(pristupniPodatak.getIdSluzbenika().equals(idSluzbenika)){

                                String hashiranaLozinka = Aplikacija.hashLozinke(lozinka);

                                if(pristupniPodatak.getKorisnickoIme().equals(korisnickoIme) && pristupniPodatak.getLozinka().equals(hashiranaLozinka)){

                                    u = 1;

                                    pristupniPodatakKorisnika = pristupniPodatak;

                                    showPocetnaScreen();
                                }
                            }
                        }
                    }

                    if(u == 0){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Pogreška");
                        alert.setHeaderText("Neispravni pristupni podaci!");
                        alert.setContentText("Pokušajte ponovno unijeti pristupne podatke.");

                        alert.showAndWait();

                        Aplikacija.getLogger().error("Neispravni pristupni podaci!");
                    }
                }
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pogreška");
            alert.setHeaderText("Neispravna prijava!");
            alert.setContentText("Unesite sve potrebne pristupne podatke.");

            alert.showAndWait();

            Aplikacija.getLogger().error("Neispravna prijava!");
        }

    }

    private static void showPocetnaScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("pocetna.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        Aplikacija.getMainStage().setTitle("Početna");
        Aplikacija.getMainStage().setScene(scene);
        Aplikacija.getMainStage().show();
    }


    public static PristupniPodatak<String, String> getPristupniPodatakKorisnika() {return pristupniPodatakKorisnika;}

    public static Granica getTrenutnaGranica() {return trenutnaGranica;}
}