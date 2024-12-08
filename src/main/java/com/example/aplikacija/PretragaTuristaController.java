package com.example.aplikacija;

import com.example.aplikacija.BazaPodataka.BazaPodataka;
import com.example.aplikacija.Entiteti.Turist;
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

public class PretragaTuristaController {

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

    @FXML
    private TableView<Turist> turistiTableView;

    @FXML
    private TableColumn<Turist, String> imeTuristaTableColumn;

    @FXML
    private TableColumn<Turist, String> prezimeTuristaTableColumn;

    @FXML
    private TableColumn<Turist, String> OIBTuristaTableColumn;

    @FXML
    private TableColumn<Turist, String> drzavaPodrijetlaTableColumn;

    @FXML
    private TableColumn<Turist, String> mjestoPodrijetlaTableColumn;

    @FXML
    private TableColumn<Turist, String> datumRodjenjaTableColumn;

    @FXML
    private TableColumn<Turist, String> lokacijaPosjetaTableColumn;

    private List<Turist> turisti;

    public void initialize(){
        turisti = BazaPodataka.dohvatiTuriste();

        imeTuristaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIme()));

        prezimeTuristaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrezime()));

        OIBTuristaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOIB()));

        drzavaPodrijetlaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPodrijetlo().drzava()));

        mjestoPodrijetlaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPodrijetlo().mjesto()));

        datumRodjenjaTableColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Turist, String>,
                        ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(
                            TableColumn.CellDataFeatures<Turist, String> turist) {
                        SimpleStringProperty property = new
                                SimpleStringProperty();
                        DateTimeFormatter formatter =
                                DateTimeFormatter.ofPattern("dd.MM.yyyy.");
                        property.setValue(
                                turist.getValue().getDatumRodjenja().format(formatter));
                        return property;
                    }
                });

        lokacijaPosjetaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLokacijaPosjeta()));

        turistiTableView.setItems(FXCollections.observableList(turisti));
    }

    public void pretraga(){

        List<Turist> filtriraniTuristi = new ArrayList<>();

        String ime = imeTuristaTextField.getText();

        if(ime.isBlank() == false){
            filtriraniTuristi = turisti.stream().filter(t -> t.getIme().contains(ime)).toList();
        }

        String prezime = prezimeTuristaTextField.getText();

        if(prezime.isBlank() == false){
            filtriraniTuristi = turisti.stream().filter(t -> t.getPrezime().contains(prezime)).toList();
        }

        String OIB = OIBTuristaTextField.getText();

        if(OIB.isBlank() == false){
            filtriraniTuristi = turisti.stream().filter(t -> t.getOIB().contains(OIB)).toList();
        }

        String drzavaPodrijetla = drzavaPodrijetlaTextField.getText();

        if(drzavaPodrijetla.isBlank() == false){
            filtriraniTuristi = turisti.stream().filter(t -> t.getPodrijetlo().drzava().contains(drzavaPodrijetla)).toList();
        }

        String mjestoPodrijetla = mjestoPodrijetlaTextField.getText();

        if(mjestoPodrijetla.isBlank() == false){
            filtriraniTuristi = turisti.stream().filter(t -> t.getPodrijetlo().mjesto().contains(mjestoPodrijetla)).toList();
        }

        LocalDate datumRodjenja = datumRodjenjaDatePicker.getValue();

        if(datumRodjenja != null){
            filtriraniTuristi = turisti.stream().filter(t -> t.getDatumRodjenja().equals(datumRodjenja)).toList();
        }

        String lokacijaPosjeta = lokacijaPosjetaTextField.getText();

        if(lokacijaPosjeta.isBlank() == false){
            filtriraniTuristi = turisti.stream().filter(t -> t.getLokacijaPosjeta().contains(lokacijaPosjeta)).toList();
        }


        if(filtriraniTuristi.size() != 0){
            turistiTableView.setItems(FXCollections.observableList(filtriraniTuristi));
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pretraga turista");
            alert.setHeaderText("Neuspješna pretraga turista!");
            alert.setContentText("Unesite podatak potreban za pretragu turista.");

            alert.showAndWait();

            Aplikacija.getLogger().error("Neuspješna pretraga turista!");
        }

    }

}
