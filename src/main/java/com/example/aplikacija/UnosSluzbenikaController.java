package com.example.aplikacija;

import com.example.aplikacija.BazaPodataka.BazaPodataka;
import com.example.aplikacija.Datoteke.Datoteke;
import com.example.aplikacija.Entiteti.PristupniPodatak;
import com.example.aplikacija.Entiteti.Promjena;
import com.example.aplikacija.Entiteti.Sluzbenik;
import com.example.aplikacija.Iznimke.NemoguciUnosOsobe;
import com.example.aplikacija.Iznimke.NemoguciUnosSluzbenika;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class UnosSluzbenikaController {

    @FXML
    private TextField imeSluzbenikaTextField;

    @FXML
    private TextField prezimeSluzbenikaTextField;

    @FXML
    private TextField OIBSluzbenikaTextField;

    @FXML
    private DatePicker datumRodjenjaDatePicker;

    @FXML
    private TextField korisnickoImeTextField;

    @FXML
    private TextField lozinkaTextField;

    @FXML
    private RadioButton adminRadioButton;

    @FXML
    private RadioButton korisnikRadioButton;

    private ToggleGroup group;


    private static Set<PristupniPodatak<String, String>> pristupniPodaci;


    private static Sluzbenik trenutniSluzbenik;


    private  List<Sluzbenik> sluzbenici;


    public void initialize(){

        pristupniPodaci = new HashSet<>();
        Datoteke.dohvatiPristupnePodatke(pristupniPodaci);

        sluzbenici = BazaPodataka.dohvatiSluzbenike();

        group = new ToggleGroup();

        adminRadioButton.setToggleGroup(group);
        korisnikRadioButton.setToggleGroup(group);
    }

    public void unos(){

        String ime = imeSluzbenikaTextField.getText();

        String prezime = prezimeSluzbenikaTextField.getText();

        String OIB = OIBSluzbenikaTextField.getText();

        LocalDate datumRodjenja = datumRodjenjaDatePicker.getValue();

        String korisnickoIme = korisnickoImeTextField.getText();

        String lozinka = lozinkaTextField.getText();

        Integer u = 0;

        try{
            u = provjera(korisnickoIme, lozinka);
        }
        catch(NemoguciUnosSluzbenika ex){
            String message = "Nemogući unos službenika s već postojećim pristupnim podacima u bazi podataka!";

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Unos službenika");
            alert.setHeaderText("Neuspješno obavljen unos službenika!");
            alert.setContentText(message);

            alert.showAndWait();

            Aplikacija.getLogger().error(message, ex);
        }

        if (u == 0) {
            if(ime.isBlank() == false && prezime.isBlank() == false && OIB.isBlank() == false && datumRodjenja != null && korisnickoIme.isBlank() == false && lozinka.isBlank() == false && (adminRadioButton.isSelected() || korisnikRadioButton.isSelected())){

                Sluzbenik sluzbenik = new Sluzbenik.SluzbenikBuilder(null, OIB).unesiIme(ime).unesiPrezime(prezime).unesiDatumRodjenja(datumRodjenja).build();

                Integer p = 0;

                try{
                    p = dodajSluzbenika(sluzbenik);
                }
                catch(NemoguciUnosOsobe ex){
                    String message = "Nemogući unos službenika s već postojećim OIB-om u bazi podataka!";

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Unos službenika");
                    alert.setHeaderText("Neuspješno obavljen unos službenika!");
                    alert.setContentText(message);

                    alert.showAndWait();

                    Aplikacija.getLogger().error(message, ex);
                }

                if(p == 1){

                    Promjena promjena = new Promjena(sluzbenik, "-", sluzbenik.getIme() + " " + sluzbenik.getPrezime(), PrijavaController.getPristupniPodatakKorisnika().getKorisnickaRola(), LocalDateTime.now());

                    Aplikacija.getListaPromjena().add(promjena);

                    try{

                        PristupniPodatak<String, String> pristupniPodatak = null;

                        if(adminRadioButton.isSelected()){
                            pristupniPodatak = new PristupniPodatak<>(trenutniSluzbenik.getId(), korisnickoIme, lozinka, "A");
                        }
                        else {
                            pristupniPodatak = new PristupniPodatak<>(trenutniSluzbenik.getId(), korisnickoIme, lozinka, "K");
                        }

                        BazaPodataka.spremiNoviPristupniPodatak(pristupniPodatak);

                        String hashiranaLozinka = Aplikacija.hashLozinke(lozinka);

                        if(adminRadioButton.isSelected()){
                            Datoteke.zapisiPristupnePodatke(trenutniSluzbenik, korisnickoIme, hashiranaLozinka, "A");
                        }
                        else {
                            Datoteke.zapisiPristupnePodatke(trenutniSluzbenik, korisnickoIme, hashiranaLozinka, "K");
                        }
                    }
                    catch(IOException ex){
                        Aplikacija.getLogger().error("Neuspješno zapisivanje pristupnih podataka!", ex);
                    }
                }
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Pogreška");
                alert.setHeaderText("Nedovoljan broj unešenih podataka!");
                alert.setContentText("Pokušajte ponovno unijeti sve potrebne podatke.");

                alert.showAndWait();

                Aplikacija.getLogger().error("Neispravan unos podataka za dodavanje službenika!");
            }
        }
    }

    public Integer provjera(String korisnickoIme, String lozinka) throws NemoguciUnosSluzbenika{

        Integer broj = 0;

        String hashiranaLozinka = Aplikacija.hashLozinke(lozinka);

        for(PristupniPodatak<String, String> pristupniPodatak : pristupniPodaci){
            if(pristupniPodatak.getKorisnickoIme().equals(korisnickoIme) || pristupniPodatak.getLozinka().equals(hashiranaLozinka)){
                broj = 1;

                throw new NemoguciUnosSluzbenika();
            }
        }

        return broj;
    }

    public Integer dodajSluzbenika(Sluzbenik noviSluzbenik) throws NemoguciUnosOsobe {

        Integer i = 0;

        Integer u = 0;

        List<Sluzbenik> sluzbeniciP;

        for(Sluzbenik sluzbenik : sluzbenici){
            if(sluzbenik.getOIB().equals(noviSluzbenik.getOIB())){
                i++;
            }
        }

        if(i == 0){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Unos službenika");
            alert.setHeaderText("Jeste li sigurni da želite izvršiti unos službenika?");
            alert.setContentText("Odaberite svoj odgovor.");

            ButtonType da = new ButtonType("Da");
            ButtonType ne = new ButtonType("Ne");

            alert.getButtonTypes().setAll(da, ne);

            Optional<ButtonType> rez = alert.showAndWait();

            if(rez.get() == da){

                u = 1;

                BazaPodataka.spremiNovogSluzbenika(noviSluzbenik);

                sluzbeniciP = BazaPodataka.dohvatiSluzbenike();

                for(Sluzbenik sluzbenik : sluzbeniciP){
                    if(noviSluzbenik.getOIB().equals(sluzbenik.getOIB())){
                        trenutniSluzbenik = sluzbenik;
                    }
                }

                BazaPodataka.spremiNoviGranicaSluzbenik(PrijavaController.getTrenutnaGranica(), trenutniSluzbenik);

                Alert alertU = new Alert(Alert.AlertType.INFORMATION);
                alertU.setTitle("Unos službenika");
                alertU.setHeaderText("Uspješno obavljen unos službenika!");
                alertU.setContentText("Službenika je dodan u bazu podataka.");

                alertU.showAndWait();

                Aplikacija.getLogger().info("Uspješno unešen službenik u bazu podataka!");
            }
        }
        else{
            throw new NemoguciUnosOsobe();
        }

        return u;
    }

}
