package com.example.aplikacija;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;

import java.io.IOException;

public class IzbornikController {

    @FXML
    private Menu sluzbeniciMenu;

    public void initialize(){
        if(PrijavaController.getPristupniPodatakKorisnika().getKorisnickaRola().equals("K")){
            sluzbeniciMenu.setVisible(false);
        }
    }

    public void showPretragaTuristaScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("pretragaTurista.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        Aplikacija.getMainStage().setTitle("Pretraga turista");
        Aplikacija.getMainStage().setScene(scene);
        Aplikacija.getMainStage().show();
    }

    public void showUlazakTuristaScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("ulazakTurista.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        Aplikacija.getMainStage().setTitle("Ulazak turista");
        Aplikacija.getMainStage().setScene(scene);
        Aplikacija.getMainStage().show();
    }

    public void showIzlazakTuristaScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("izlazakTurista.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        Aplikacija.getMainStage().setTitle("Izlazak turista");
        Aplikacija.getMainStage().setScene(scene);
        Aplikacija.getMainStage().show();
    }

    public void showUredjivanjeTuristaScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("uredjivanjeTurista.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        Aplikacija.getMainStage().setTitle("Uređivanje turista");
        Aplikacija.getMainStage().setScene(scene);
        Aplikacija.getMainStage().show();
    }

    public void showPretragaOsobaUTranzituScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("pretragaOsobaUTranzitu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        Aplikacija.getMainStage().setTitle("Pretraga osoba u tranzitu");
        Aplikacija.getMainStage().setScene(scene);
        Aplikacija.getMainStage().show();
    }

    public void showUlazakOsobeUTranzituScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("ulazakOsobeUTranzitu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        Aplikacija.getMainStage().setTitle("Ulazak osobe u tranzitu");
        Aplikacija.getMainStage().setScene(scene);
        Aplikacija.getMainStage().show();
    }

    public void showIzlazakOsobeUTranzituScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("izlazakOsobeUTranzitu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        Aplikacija.getMainStage().setTitle("Izlazak osobe u tranzitu");
        Aplikacija.getMainStage().setScene(scene);
        Aplikacija.getMainStage().show();
    }

    public void showUredjivanjeOsobeUTranzituScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("uredjivanjeOsobeUTranzitu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        Aplikacija.getMainStage().setTitle("Uređivanje osobe u tranzitu");
        Aplikacija.getMainStage().setScene(scene);
        Aplikacija.getMainStage().show();
    }

    public void showPregledStranihOsobaScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("pregledStranihOsoba.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        Aplikacija.getMainStage().setTitle("Pregled stranih osoba");
        Aplikacija.getMainStage().setScene(scene);
        Aplikacija.getMainStage().show();
    }

    public void showPretragaSluzbenikaScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("pretragaSluzbenika.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        Aplikacija.getMainStage().setTitle("Pretraga službenika");
        Aplikacija.getMainStage().setScene(scene);
        Aplikacija.getMainStage().show();
    }

    public void showUnosSluzbenikaScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("unosSluzbenika.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        Aplikacija.getMainStage().setTitle("Unos službenika");
        Aplikacija.getMainStage().setScene(scene);
        Aplikacija.getMainStage().show();
    }

    public void showUklanjanjeSluzbenikaScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("uklanjanjeSluzbenika.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        Aplikacija.getMainStage().setTitle("Uklanjanje službenika");
        Aplikacija.getMainStage().setScene(scene);
        Aplikacija.getMainStage().show();
    }

    public void showUredjivanjeSluzbenikaScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("uredjivanjeSluzbenika.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        Aplikacija.getMainStage().setTitle("Uređivanje službenika");
        Aplikacija.getMainStage().setScene(scene);
        Aplikacija.getMainStage().show();
    }

    public void showPregledPromjenaScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("pregledPromjena.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        Aplikacija.getMainStage().setTitle("Pregled promjena");
        Aplikacija.getMainStage().setScene(scene);
        Aplikacija.getMainStage().show();
    }
}
