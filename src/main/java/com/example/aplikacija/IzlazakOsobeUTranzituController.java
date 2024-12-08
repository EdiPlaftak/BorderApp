package com.example.aplikacija;

import com.example.aplikacija.BazaPodataka.BazaPodataka;
import com.example.aplikacija.Entiteti.OsobaUTranzitu;
import com.example.aplikacija.Entiteti.Promjena;
import com.example.aplikacija.Iznimke.NemogucaPromjenaOsobe;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class IzlazakOsobeUTranzituController {

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


    private static List<OsobaUTranzitu> osobeUTranzitu;


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

        Aplikacija.osvjeziPodatke("out", osobeUTranzituTableView);
    }

    public void izlazak(){
        OsobaUTranzitu osobaUTranzitu = osobeUTranzituTableView.getSelectionModel().getSelectedItem();

        Integer u = 0;

        try{
            u = ukloniOsobuUTranzitu(osobaUTranzitu);
        }
        catch(NemogucaPromjenaOsobe ex){
            String message = "Neuspješno obavljen izlazak osobe u tranzitu!";

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Izlazak osobe u tranzitu");
            alert.setHeaderText(message);
            alert.setContentText("Označite osobu u tranzitu koja izlazi iz zemlje.");

            alert.showAndWait();

            Aplikacija.getLogger().error(message, ex);
        }

        if(u == 1){
            Promjena promjena = new Promjena(osobaUTranzitu, osobaUTranzitu.getIme() + " " + osobaUTranzitu.getPrezime(), "-", PrijavaController.getPristupniPodatakKorisnika().getKorisnickaRola(), LocalDateTime.now());

            Aplikacija.getListaPromjena().add(promjena);
        }
    }

    public Integer ukloniOsobuUTranzitu(OsobaUTranzitu osobaUTranzitu) throws NemogucaPromjenaOsobe {

        Integer u = 0;

        if(osobaUTranzitu == null){
            throw new NemogucaPromjenaOsobe();
        }
        else{

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Izlazak osobe u tranzitu");
            alert.setHeaderText("Jeste li sigurni da želite izvršiti izlazak osobe u tranzitu?");
            alert.setContentText("Odaberite svoj odgovor.");

            ButtonType da = new ButtonType("Da");
            ButtonType ne = new ButtonType("Ne");

            alert.getButtonTypes().setAll(da, ne);

            Optional<ButtonType> rez = alert.showAndWait();

            if(rez.get() == da){

                u = 1;

                BazaPodataka.ukloniOsobuUTranzitu(osobaUTranzitu);

                String message = "Uspješno obavljen izlazak osobe u tranzitu!";

                Alert alertU = new Alert(Alert.AlertType.INFORMATION);
                alertU.setTitle("Izlazak osobe u tranzitu");
                alertU.setHeaderText(message);
                alertU.setContentText("Osoba u tranzitu je uspješno uklonjena iz baze podataka!");

                alertU.showAndWait();

                Aplikacija.getLogger().info(message);
            }
        }

        return u;
    }

}
