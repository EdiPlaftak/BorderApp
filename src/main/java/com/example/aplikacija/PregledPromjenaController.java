package com.example.aplikacija;

import com.example.aplikacija.Datoteke.Datoteke;
import com.example.aplikacija.Entiteti.Promjena;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class PregledPromjenaController {

    @FXML
    private TableView<Promjena> promjeneTableView;

    @FXML
    private TableColumn<Promjena, String> osobaTableColumn;

    @FXML
    private TableColumn<Promjena, String> staraVrijednostTableColumn;

    @FXML
    private TableColumn<Promjena, String> novaVrijednostTableColumn;

    @FXML
    private TableColumn<Promjena, String> korisnickaRolaTableColumn;

    @FXML
    private TableColumn<Promjena, String> datumIVrijemeTableColumn;


    private List<Promjena> promjene;


    public void initialize(){

        Datoteke.zapisiPromjene(Aplikacija.getListaPromjena());

        promjene = Datoteke.dohvatiPromjene();

        osobaTableColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Promjena, String>,
                        ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(
                            TableColumn.CellDataFeatures<Promjena, String> promjena) {
                        SimpleStringProperty property = new
                                SimpleStringProperty();
                        property.setValue(
                                promjena.getValue().getOsoba().getIme() + " " + promjena.getValue().getOsoba().getPrezime());
                        return property;
                    }
                });

        staraVrijednostTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStaraVrijednost()));

        novaVrijednostTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNovaVrijednost()));

        korisnickaRolaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKorisnickaRola()));

        datumIVrijemeTableColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Promjena, String>,
                        ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(
                            TableColumn.CellDataFeatures<Promjena, String> promjena) {
                        SimpleStringProperty property = new
                                SimpleStringProperty();
                        DateTimeFormatter formatter =
                                DateTimeFormatter.ofPattern("dd.MM.yyyy.HH:mm");
                        property.setValue(
                                promjena.getValue().getDatumIVrijeme().format(formatter));
                        return property;
                    }
                });

        promjeneTableView.setItems(FXCollections.observableList(promjene));
    }

}
