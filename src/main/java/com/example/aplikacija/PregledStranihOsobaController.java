package com.example.aplikacija;

import com.example.aplikacija.BazaPodataka.BazaPodataka;
import com.example.aplikacija.Entiteti.Osoba;
import com.example.aplikacija.Entiteti.OsobaUTranzitu;
import com.example.aplikacija.Entiteti.StraneOsobe;
import com.example.aplikacija.Entiteti.Turist;
import com.example.aplikacija.Iznimke.NePostojiNajmladjaOsoba;
import com.example.aplikacija.Iznimke.NePostojiNajstarijaOsoba;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class PregledStranihOsobaController {

    @FXML
    private  ChoiceBox<String> pregledChoiceBox;

    @FXML
    private TableView<Osoba> straneOsobeTableView;

    @FXML
    private TableColumn<Osoba, String> imeStraneOsobeTableColumn;

    @FXML
    private TableColumn<Osoba, String> prezimeStraneOsobeTableColumn;

    @FXML
    private TableColumn<Osoba, String> OIBStraneOsobeTableColumn;

    @FXML
    private TableColumn<Osoba, String> datumRodjenjaTableColumn;

    @FXML
    private TableColumn<Osoba, String> drzavaPodrijetlaTableColumn;

    @FXML
    private TableColumn<Osoba, String> mjestoPodrijetlaTableColumn;

    private static List<Turist> turisti;
    private static List<OsobaUTranzitu> osobeUTranzitu;
    private static List<Osoba> listaStranihOsoba;


    public void initialize(){
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

        pregledChoiceBox.getItems().add("Od najmlađih");
        pregledChoiceBox.getItems().add("Od najstarijih");
        pregledChoiceBox.getItems().add("Samo najmlađa i najstarija osoba");

        imeStraneOsobeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIme()));

        prezimeStraneOsobeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrezime()));

        OIBStraneOsobeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOIB()));

        datumRodjenjaTableColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Osoba, String>,
                        ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(
                            TableColumn.CellDataFeatures<Osoba, String> osoba) {
                        SimpleStringProperty property = new
                                SimpleStringProperty();
                        DateTimeFormatter formatter =
                                DateTimeFormatter.ofPattern("dd.MM.yyyy.");
                        property.setValue(
                                osoba.getValue().getDatumRodjenja().format(formatter));
                        return property;
                    }
                });

        drzavaPodrijetlaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPodrijetlo().drzava()));

        mjestoPodrijetlaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPodrijetlo().mjesto()));

        straneOsobeTableView.setItems(FXCollections.observableList(listaStranihOsoba));
    }

    public void pregled(){

        if(pregledChoiceBox.getValue() != null){

            if(pregledChoiceBox.getValue().equals("Od najmlađih")){
                pregledOsobaOdNajmladjih(listaStranihOsoba);
            }
            else if(pregledChoiceBox.getValue().equals("Od najstarijih")){
                pregledOsobaOdNajstarijih(listaStranihOsoba);
            }
            else {
                pregledNajmladjeINajstarijeOsobe(listaStranihOsoba);
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pregled stranih osoba");
            alert.setHeaderText("Neuspješan pregled stranih osoba!");
            alert.setContentText("Odaberite jednu od opcija sortiranja stranih osoba.");

            alert.showAndWait();

            Aplikacija.getLogger().error("Neuspješan pregled stranih osoba!");
        }
    }

    public void pregledOsobaOdNajmladjih(List<Osoba> osobe){

        Function<Osoba, Integer> poGodinama = (Osoba o) -> o.odrediGodine(o.getDatumRodjenja());

        Comparator<Osoba> usporedba = Comparator.comparing(poGodinama);

        List<Osoba> sortiraneOsobe = osobe.stream().sorted(usporedba).toList();

        straneOsobeTableView.setItems(FXCollections.observableList(sortiraneOsobe));
    }

    public void pregledOsobaOdNajstarijih(List<Osoba> osobe){

        Function<Osoba, Integer> poGodinama = (Osoba o) -> o.odrediGodine(o.getDatumRodjenja());

        Comparator<Osoba> usporedba = Comparator.comparing(poGodinama).reversed();

        List<Osoba> sortiraneOsobe = osobe.stream().sorted(usporedba).toList();

        straneOsobeTableView.setItems(FXCollections.observableList(sortiraneOsobe));
    }

    public void pregledNajmladjeINajstarijeOsobe(List<Osoba> osobe){

        try{
            pretragaNajmladjeINajstarijeOsobe(osobe);
        }
        catch(NePostojiNajmladjaOsoba ex){
            String message = "Neuspješno sortiranje osoba!";

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Najmlađa osoba");
            alert.setHeaderText(message);
            alert.setContentText("Ne postoji najmlađa osoba.");

            alert.showAndWait();

            Aplikacija.getLogger().error(message, ex);
        }
        catch(NePostojiNajstarijaOsoba ex){
            String message = "Neuspješno sortiranje osoba!";

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Najstarija osoba");
            alert.setHeaderText(message);
            alert.setContentText("Ne postoji najstarija osoba.");

            alert.showAndWait();

            Aplikacija.getLogger().error(message, ex);
        }
    }

    public void pretragaNajmladjeINajstarijeOsobe(List<Osoba> osobe) throws NePostojiNajmladjaOsoba, NePostojiNajstarijaOsoba {

        List<Osoba> sortiraneOsobe = new ArrayList<>();

        Integer minGodine = 100;
        Integer minIndex = 0;

        for(int i = 0; i < osobe.size(); i++){

            Integer godine = osobe.get(i).odrediGodine(osobe.get(i).getDatumRodjenja());

            if(godine < minGodine){
                minGodine = godine;
                minIndex = i;
            }
            else if(godine == minGodine){
                String message = "Postoji više najmlađih osoba!";

                throw new NePostojiNajmladjaOsoba(message);
            }

        }

        Osoba najmladjaOsoba = osobe.get(minIndex);

        sortiraneOsobe.add(najmladjaOsoba);

        Integer maxGodine = 0;
        Integer maxIndex = 0;

        for(int i = 0; i < osobe.size(); i++){

            Integer godine = osobe.get(i).odrediGodine(osobe.get(i).getDatumRodjenja());

            if(godine > maxGodine){
                maxGodine = godine;
                maxIndex = i;
            }
            else if(godine == maxGodine){
                String message = "Postoji više najstarijih osoba!";

                throw new NePostojiNajstarijaOsoba(message);
            }

        }

        Osoba najstarijaOsoba = osobe.get(maxIndex);

        sortiraneOsobe.add(najstarijaOsoba);

        straneOsobeTableView.setItems(FXCollections.observableList(sortiraneOsobe));
    }

}
