package com.example.aplikacija;

import com.example.aplikacija.BazaPodataka.BazaPodataka;
import com.example.aplikacija.Entiteti.Promjena;
import com.example.aplikacija.Entiteti.Turist;
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

public class IzlazakTuristaController {

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

    private static List<Turist> turisti;

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

        Aplikacija.osvjeziPodatke("t", turistiTableView);
    }

    public void izlazak(){
        Turist turist = turistiTableView.getSelectionModel().getSelectedItem();

        Integer u = 0;

        try{
            u = ukloniTurista(turist);
        }
        catch(NemogucaPromjenaOsobe ex){
            String message = "Neuspješno obavljen izlazak turista!";

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Izlazak turista");
            alert.setHeaderText(message);
            alert.setContentText("Označite turista koji izlazi iz zemlje.");

            alert.showAndWait();

            Aplikacija.getLogger().error(message, ex);
        }

        if(u == 1){

            Promjena promjena = new Promjena(turist, turist.getIme() + " " + turist.getPrezime(), "-", PrijavaController.getPristupniPodatakKorisnika().getKorisnickaRola(), LocalDateTime.now());

            Aplikacija.getListaPromjena().add(promjena);
        }
    }

    public Integer ukloniTurista(Turist turist) throws NemogucaPromjenaOsobe {

        Integer u = 0;

        if(turist == null){
            throw new NemogucaPromjenaOsobe();
        }
        else{

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Izlazak turista");
            alert.setHeaderText("Jeste li sigurni da želite izvršiti izlazak turista?");
            alert.setContentText("Odaberite svoj odgovor.");

            ButtonType da = new ButtonType("Da");
            ButtonType ne = new ButtonType("Ne");

            alert.getButtonTypes().setAll(da, ne);

            Optional<ButtonType> rez = alert.showAndWait();

            if(rez.get() == da){

                u = 1;

                BazaPodataka.ukloniTurista(turist);

                String message = "Uspješno obavljen izlazak turista!";

                Alert alertU = new Alert(Alert.AlertType.INFORMATION);
                alertU.setTitle("Izlazak turista");
                alertU.setHeaderText(message);
                alertU.setContentText("Turist je uspješno uklonjen iz baze podataka!");

                alertU.showAndWait();

                Aplikacija.getLogger().info(message);
            }
        }

        return u;
    }
}
