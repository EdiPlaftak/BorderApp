package com.example.aplikacija;

import com.example.aplikacija.BazaPodataka.BazaPodataka;
import com.example.aplikacija.Entiteti.OsobaUTranzitu;
import com.example.aplikacija.Entiteti.Podrijetlo;
import com.example.aplikacija.Entiteti.Promjena;
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

public class UlazakOsobeUTranzituController {

    @FXML
    private TextField imeOsobeUTranzituTextField;

    @FXML
    private TextField prezimeOsobeUTranzituTextField;

    @FXML
    private TextField OIBOsobeUTranzituTextField;

    @FXML
    private TextField drzavaPodrijetlaTextField;

    @FXML
    private TextField mjestoPodrijetlaTextField;

    @FXML
    private DatePicker datumRodjenjaDatePicker;

    @FXML
    private TextField odredisnaDrzavaTextField;


    private static List<OsobaUTranzitu> osobeUTranzitu;


    public void initialize(){osobeUTranzitu = BazaPodataka.dohvatiOsobeUTranzitu();}

    public void ulazak(){

        String ime = imeOsobeUTranzituTextField.getText();

        String prezime = prezimeOsobeUTranzituTextField.getText();

        String OIB = OIBOsobeUTranzituTextField.getText();

        String drzavaPodrijetla = drzavaPodrijetlaTextField.getText();

        String mjestoPodrijetla = mjestoPodrijetlaTextField.getText();

        LocalDate datumRodjenja = datumRodjenjaDatePicker.getValue();

        String odredisnaDrzava = odredisnaDrzavaTextField.getText();

        if(ime.isBlank() == false && prezime.isBlank() == false && OIB.isBlank() == false && drzavaPodrijetla.isBlank() == false && mjestoPodrijetla.isBlank() == false && datumRodjenja != null && odredisnaDrzava.isBlank() == false){

            Podrijetlo podrijetlo = new Podrijetlo(drzavaPodrijetla, mjestoPodrijetla);

            OsobaUTranzitu osobaUTranzitu = new OsobaUTranzitu(null, ime, prezime, OIB, podrijetlo, datumRodjenja, odredisnaDrzava);

            Integer u = 0;

            try{
                u = dodajOsobuUTranzitu(osobaUTranzitu);
            }
            catch(NemoguciUnosOsobe ex){
                String message = "Nemogući unos osobe u tranzitu s već postojećim OIB-om u bazi podataka!";

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ulazak osobe u tranzitu");
                alert.setHeaderText("Neuspješno obavljen ulazak osobe u tranzitu!");
                alert.setContentText(message);

                alert.showAndWait();

                Aplikacija.getLogger().error(message, ex);
            }

            if(u == 1){
                Promjena promjena = new Promjena(osobaUTranzitu, "-", osobaUTranzitu.getIme() + " " + osobaUTranzitu.getPrezime(), PrijavaController.getPristupniPodatakKorisnika().getKorisnickaRola(), LocalDateTime.now());

                Aplikacija.getListaPromjena().add(promjena);
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pogreška");
            alert.setHeaderText("Nedovoljan broj unešenih podataka!");
            alert.setContentText("Pokušajte ponovno unijeti sve potrebne podatke.");

            alert.showAndWait();

            Aplikacija.getLogger().error("Neispravan unos podataka za ulazak osobe u tranzitu!");
        }
    }

    private Integer dodajOsobuUTranzitu(OsobaUTranzitu novaOsobaUTranzitu) throws NemoguciUnosOsobe {

        Integer u = 0;

        Integer i = 0;

        for(OsobaUTranzitu osobaUTranzitu : osobeUTranzitu){
            if(osobaUTranzitu.getOIB().equals(novaOsobaUTranzitu.getOIB())){
                i++;
            }
        }

        if(i == 0){

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Ulazak osobe u tranzitu");
            alert.setHeaderText("Jeste li sigurni da želite izvršiti ulazak osobe u tranzitu?");
            alert.setContentText("Odaberite svoj odgovor.");

            ButtonType da = new ButtonType("Da");
            ButtonType ne = new ButtonType("Ne");

            alert.getButtonTypes().setAll(da, ne);

            Optional<ButtonType> rez = alert.showAndWait();

            if(rez.get() == da){

                u = 1;

                BazaPodataka.spremiNovuOsobuUTranzitu(novaOsobaUTranzitu);

                Alert alertU = new Alert(Alert.AlertType.INFORMATION);
                alertU.setTitle("Ulazak osobe u tranzitu");
                alertU.setHeaderText("Uspješno obavljen ulazak osobe u tranzitu!");
                alertU.setContentText("Osoba u tranzitu je dodana u bazu podataka.");

                alertU.showAndWait();

                Aplikacija.getLogger().info("Uspješno unešena osoba u tranzitu u bazu podataka!");
            }
        }
        else{
            throw new NemoguciUnosOsobe();
        }

        return u;
    }
}
