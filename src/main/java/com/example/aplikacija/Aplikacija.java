package com.example.aplikacija;

import com.example.aplikacija.Datoteke.Datoteke;
import com.example.aplikacija.Entiteti.Promjena;
import com.example.aplikacija.Niti.OsvjezavanjePodatakaNit;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Aplikacija extends Application {


    private static List<Promjena> listaPromjena;

    private static Stage mainStage;

    private static final Logger logger = LoggerFactory.getLogger(Aplikacija.class);


    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        listaPromjena = Datoteke.dohvatiPromjene();
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("prijava.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        stage.setTitle("Prijava");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getMainStage(){return mainStage;}

    public static Logger getLogger(){return logger;}

    public static List<Promjena> getListaPromjena() {return listaPromjena;}


    public static String hashLozinke(String lozinka){

        String hashiranaLozinka = null;

        try{
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(lozinka.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < bytes.length; i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            hashiranaLozinka = sb.toString();
        }
        catch(NoSuchAlgorithmException ex){
            Aplikacija.getLogger().error("Pogreška prilikom hashiranja lozinke!", ex);
        }

        return hashiranaLozinka;
    }

    public static void osvjeziPodatke(String naziv, TableView tableView){
        Timeline osvjezavanjePodataka = new Timeline(
                new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Platform.runLater(new OsvjezavanjePodatakaNit(naziv, tableView));
                    }
                }));
        osvjezavanjePodataka.setCycleCount(Timeline.INDEFINITE);
        osvjezavanjePodataka.play();
    }
}