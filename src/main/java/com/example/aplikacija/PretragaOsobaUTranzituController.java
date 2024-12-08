package com.example.aplikacija;

import com.example.aplikacija.BazaPodataka.BazaPodataka;
import com.example.aplikacija.Entiteti.OsobaUTranzitu;
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

public class PretragaOsobaUTranzituController {

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

    @FXML
    private TableView<OsobaUTranzitu> osobeUTranzituTableView;

    @FXML
    private TableColumn<OsobaUTranzitu, String> imeOsobeUTranzituTableColumn;

    @FXML
    private TableColumn<OsobaUTranzitu, String> prezimeOsobeUTranzituTableColumn;

    @FXML
    private TableColumn<OsobaUTranzitu, String> OIBOsobeUTranzituTableColumn;

    @FXML
    private TableColumn<OsobaUTranzitu, String> drzavaPodrijetlaTableColumn;

    @FXML
    private TableColumn<OsobaUTranzitu, String> mjestoPodrijetlaTableColumn;

    @FXML
    private TableColumn<OsobaUTranzitu, String> datumRodjenjaTableColumn;

    @FXML
    private TableColumn<OsobaUTranzitu, String> odredisnaDrzavaTableColumn;

    private List<OsobaUTranzitu> osobeUTranzitu;

    public void initialize(){
        osobeUTranzitu = BazaPodataka.dohvatiOsobeUTranzitu();

        imeOsobeUTranzituTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIme()));

        prezimeOsobeUTranzituTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrezime()));

        OIBOsobeUTranzituTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOIB()));

        drzavaPodrijetlaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPodrijetlo().drzava()));

        mjestoPodrijetlaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPodrijetlo().mjesto()));

        datumRodjenjaTableColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<OsobaUTranzitu, String>,
                        ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(
                            TableColumn.CellDataFeatures<OsobaUTranzitu, String> osobaUTranzitu) {
                        SimpleStringProperty property = new
                                SimpleStringProperty();
                        DateTimeFormatter formatter =
                                DateTimeFormatter.ofPattern("dd.MM.yyyy.");
                        property.setValue(
                                osobaUTranzitu.getValue().getDatumRodjenja().format(formatter));
                        return property;
                    }
                });

        odredisnaDrzavaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOdredisnaDrzava()));

        osobeUTranzituTableView.setItems(FXCollections.observableList(osobeUTranzitu));
    }

    public void pretraga(){
        List<OsobaUTranzitu> filtriraneOsobeUTranzitu = new ArrayList<>();

        String ime = imeOsobeUTranzituTextField.getText();

        if(ime.isBlank() == false){
            filtriraneOsobeUTranzitu = osobeUTranzitu.stream().filter(o -> o.getIme().contains(ime)).toList();
        }

        String prezime = prezimeOsobeUTranzituTextField.getText();

        if(prezime.isBlank() == false){
            filtriraneOsobeUTranzitu = osobeUTranzitu.stream().filter(o -> o.getPrezime().contains(prezime)).toList();
        }

        String OIB = OIBOsobeUTranzituTextField.getText();

        if(OIB.isBlank() == false){
            filtriraneOsobeUTranzitu = osobeUTranzitu.stream().filter(o -> o.getOIB().contains(OIB)).toList();
        }

        String drzavaPodrijetla = drzavaPodrijetlaTextField.getText();

        if(drzavaPodrijetla.isBlank() == false){
            filtriraneOsobeUTranzitu = osobeUTranzitu.stream().filter(o -> o.getPodrijetlo().drzava().contains(drzavaPodrijetla)).toList();
        }

        String mjestoPodrijetla = mjestoPodrijetlaTextField.getText();

        if(mjestoPodrijetla.isBlank() == false){
            filtriraneOsobeUTranzitu = osobeUTranzitu.stream().filter(o -> o.getPodrijetlo().mjesto().contains(mjestoPodrijetla)).toList();
        }

        LocalDate datumRodjenja = datumRodjenjaDatePicker.getValue();

        if(datumRodjenja != null){
            filtriraneOsobeUTranzitu = osobeUTranzitu.stream().filter(o -> o.getDatumRodjenja().equals(datumRodjenja)).toList();
        }

        String odredisnaDrzava = odredisnaDrzavaTextField.getText();

        if(odredisnaDrzava.isBlank() == false){
            filtriraneOsobeUTranzitu = osobeUTranzitu.stream().filter(o -> o.getOdredisnaDrzava().contains(odredisnaDrzava)).toList();
        }


        if(filtriraneOsobeUTranzitu.size() != 0){
            osobeUTranzituTableView.setItems(FXCollections.observableList(filtriraneOsobeUTranzitu));
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pretraga osoba u tranzitu");
            alert.setHeaderText("Neuspješna pretraga osoba u tranzitu!");
            alert.setContentText("Unesite podatak potreban za pretragu osoba u tranzitu.");

            alert.showAndWait();

            Aplikacija.getLogger().error("Neuspješna pretraga osoba u tranzitu!");
        }
    }
}
