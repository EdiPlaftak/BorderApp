package com.example.aplikacija;

import com.example.aplikacija.BazaPodataka.BazaPodataka;
import com.example.aplikacija.Entiteti.Podrijetlo;
import com.example.aplikacija.Entiteti.Promjena;
import com.example.aplikacija.Entiteti.Turist;
import com.example.aplikacija.Iznimke.NemoguciUnosOsobe;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class UlazakTuristaController {

    @FXML
    private TextField imeTuristaTextField;

    @FXML
    private TextField prezimeTuristaTextField;

    @FXML
    private TextField OIBTuristaTextField;

    @FXML
    private TextField drzavaPodrijetlaTextField;

    @FXML
    private TextField mjestoPodrijetlaTextField;

    @FXML
    private DatePicker datumRodjenjaDatePicker;

    @FXML
    private TextField lokacijaPosjetaTextField;


    private static List<Turist> turisti;


    public void initialize(){
        turisti = BazaPodataka.dohvatiTuriste();
    }

    public void ulazakTurista(){

        String ime = imeTuristaTextField.getText();

        String prezime = prezimeTuristaTextField.getText();

        String OIB = OIBTuristaTextField.getText();

        String drzavaPodrijetla = drzavaPodrijetlaTextField.getText();

        String mjestoPodrijetla = mjestoPodrijetlaTextField.getText();

        LocalDate datumRodjenja = datumRodjenjaDatePicker.getValue();

        String lokacijaPosjeta = lokacijaPosjetaTextField.getText();

        if(ime.isBlank() == false && prezime.isBlank() == false && OIB.isBlank() == false && drzavaPodrijetla.isBlank() == false && mjestoPodrijetla.isBlank() == false && datumRodjenja != null && lokacijaPosjeta.isBlank() == false){

            Podrijetlo podrijetlo = new Podrijetlo(drzavaPodrijetla, mjestoPodrijetla);

            Turist turist = new Turist(null, ime, prezime, OIB, podrijetlo, datumRodjenja, lokacijaPosjeta);

            Integer broj = 0;

            for(Turist turistP : turisti){
                if(turistP.getIme().equals(turist.getIme()) && turistP.getPrezime().equals(turist.getPrezime())){
                    broj = 1;
                }
            }

            if(broj == 1){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ulazak turista");
                alert.setHeaderText("Neuspješno obavljen ulazak turista!");
                alert.setContentText("Unosite turista još prisutnog u zemlji.");

                alert.showAndWait();
            }
            else {
                Integer u = 0;

                try{
                    u = dodajTurista(turist);
                }
                catch(NemoguciUnosOsobe ex){
                    String message = "Nemogući unos turista s već postojećim OIB-om u bazi podataka!";

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ulazak turista");
                    alert.setHeaderText("Neuspješno obavljen ulazak turista!");
                    alert.setContentText(message);

                    alert.showAndWait();

                    Aplikacija.getLogger().error(message, ex);
                }

                if(u == 1){
                    Promjena promjena = new Promjena(turist, "-", turist.getIme() + " " + turist.getPrezime(), PrijavaController.getPristupniPodatakKorisnika().getKorisnickaRola(), LocalDateTime.now());

                    Aplikacija.getListaPromjena().add(promjena);
                }
            }

        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pogreška");
            alert.setHeaderText("Nedovoljan broj unešenih podataka!");
            alert.setContentText("Pokušajte ponovno unijeti sve potrebne podatke.");

            alert.showAndWait();

            Aplikacija.getLogger().error("Neispravan unos podataka za ulazak turista!");
        }
    }

    private Integer dodajTurista(Turist noviTurist) throws NemoguciUnosOsobe {

        Integer u = 0;

        Integer i = 0;

        for(Turist turist : turisti){
            if(turist.getOIB().equals(noviTurist.getOIB())){
                i = 1;
            }
        }

        if(i == 0){

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Ulazak turista");
            alert.setHeaderText("Jeste li sigurni da želite izvršiti ulazak turista?");
            alert.setContentText("Odaberite svoj odgovor.");

            ButtonType da = new ButtonType("Da");
            ButtonType ne = new ButtonType("Ne");

            alert.getButtonTypes().setAll(da, ne);

            Optional<ButtonType> rez = alert.showAndWait();

            if(rez.get() == da){

                u = 1;

                BazaPodataka.spremiNovogTurista(noviTurist);

                Alert alertU = new Alert(Alert.AlertType.INFORMATION);
                alertU.setTitle("Ulazak turista");
                alertU.setHeaderText("Uspješno obavljen ulazak turista!");
                alertU.setContentText("Turist je dodan u bazu podataka.");

                alertU.showAndWait();

                Aplikacija.getLogger().info("Uspješno unešen turist u bazu podataka!");
            }

        }
        else{
            throw new NemoguciUnosOsobe();
        }

        return u;
    }

}
