package com.example.aplikacija;

import com.example.aplikacija.BazaPodataka.BazaPodataka;
import com.example.aplikacija.Entiteti.Sluzbenik;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PretragaSluzbenikaController {

    @FXML
    private TextField imeSluzbenikaTextField;

    @FXML
    private TextField prezimeSluzbenikaTextField;

    @FXML
    private TextField OIBSluzbenikaTextField;

    @FXML
    private DatePicker datumRodjenjaDatePicker;

    @FXML
    private TableView<Sluzbenik> sluzbeniciTableView;

    @FXML
    private TableColumn<Sluzbenik, String> imeSluzbenikaTableColumn;

    @FXML
    private TableColumn<Sluzbenik, String> prezimeSluzbenikaTableColumn;

    @FXML
    private TableColumn<Sluzbenik, String> OIBSluzbenikaTableColumn;

    @FXML
    private TableColumn<Sluzbenik, String> datumRodjenjaTableColumn;


    private List<Sluzbenik> sluzbenici;

    private List<Sluzbenik> sluzbeniciGranice;


    public void initialize(){

        sluzbenici = BazaPodataka.dohvatiSluzbenike();
        sluzbeniciGranice = BazaPodataka.dohvatiGranice_Sluzbenik(sluzbenici, PrijavaController.getTrenutnaGranica());

        imeSluzbenikaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIme()));

        prezimeSluzbenikaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrezime()));

        OIBSluzbenikaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOIB()));

        datumRodjenjaTableColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Sluzbenik, String>,
                        ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(
                            TableColumn.CellDataFeatures<Sluzbenik, String> sluzbenik) {
                        SimpleStringProperty property = new
                                SimpleStringProperty();
                        DateTimeFormatter formatter =
                                DateTimeFormatter.ofPattern("dd.MM.yyyy.");
                        property.setValue(
                                sluzbenik.getValue().getDatumRodjenja().format(formatter));
                        return property;
                    }
                });

        sluzbeniciTableView.setItems(FXCollections.observableList(sluzbeniciGranice));
    }

    public void pretraga(){

        List<Sluzbenik> filtriraniSluzbenici = new ArrayList<>();

        String ime = imeSluzbenikaTextField.getText();

        if(ime.isBlank() == false){
            filtriraniSluzbenici = sluzbeniciGranice.stream().filter(s -> s.getIme().contains(ime)).toList();
        }

        String prezime = prezimeSluzbenikaTextField.getText();

        if(prezime.isBlank() == false){
            filtriraniSluzbenici = sluzbeniciGranice.stream().filter(s -> s.getPrezime().contains(prezime)).toList();
        }

        String OIB = OIBSluzbenikaTextField.getText();

        if(OIB.isBlank() == false){
            filtriraniSluzbenici = sluzbeniciGranice.stream().filter(s -> s.getOIB().contains(OIB)).toList();
        }

        LocalDate datumRodjenja = datumRodjenjaDatePicker.getValue();

        if(datumRodjenja != null){
            filtriraniSluzbenici = sluzbeniciGranice.stream().filter(s -> s.getDatumRodjenja().equals(datumRodjenja)).toList();
        }


        if(filtriraniSluzbenici.size() != 0){
            sluzbeniciTableView.setItems(FXCollections.observableList(filtriraniSluzbenici));
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pretraga službenika");
            alert.setHeaderText("Neuspješna pretraga službenika!");
            alert.setContentText("Unesite podatak potreban za pretragu službenika.");

            alert.showAndWait();

            Aplikacija.getLogger().error("Neuspješna pretraga službenika!");
        }
    }

}
