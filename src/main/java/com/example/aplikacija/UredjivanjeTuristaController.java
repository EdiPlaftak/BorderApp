package com.example.aplikacija;

import com.example.aplikacija.BazaPodataka.BazaPodataka;
import com.example.aplikacija.Entiteti.Promjena;
import com.example.aplikacija.Entiteti.Turist;
import com.example.aplikacija.Iznimke.NemogucaPromjenaOsobe;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class UredjivanjeTuristaController {

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

    public void uredjivanje(){

        Turist turist = turistiTableView.getSelectionModel().getSelectedItem();

        Integer u = 0;

        try{
            provjera(turist);
        }
        catch(NemogucaPromjenaOsobe ex){
            String message = "Neuspješno uređivanje turista!";

            u = 1;

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Uređivanje turista");
            alert.setHeaderText(message);
            alert.setContentText("Odaberite turista za uređivanje njegovih podataka.");
            alert.showAndWait();

            Aplikacija.getLogger().error(message);
        }

        if(u == 0){
            String ime = imeTuristaTextField.getText();

            String prezime = prezimeTuristaTextField.getText();

            String OIB = OIBTuristaTextField.getText();

            String drzavaPodrijetla = drzavaPodrijetlaTextField.getText();

            String mjestoPodrijetla = mjestoPodrijetlaTextField.getText();

            LocalDate datumRodjenja = datumRodjenjaDatePicker.getValue();

            String lokacijaPosjeta = lokacijaPosjetaTextField.getText();

            if(ime.isBlank() == false || prezime.isBlank() == false || OIB.isBlank() == false || drzavaPodrijetla.isBlank() == false || mjestoPodrijetla.isBlank() == false || datumRodjenja != null || lokacijaPosjeta.isBlank() == false){

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Uređivanje turista");
                alert.setHeaderText("Jeste li sigurni da želite provesti uređivanje turista?");
                alert.setContentText("Odaberite svoj odgovor.");

                ButtonType da = new ButtonType("Da");
                ButtonType ne = new ButtonType("Ne");

                alert.getButtonTypes().setAll(da, ne);

                Optional<ButtonType> rez = alert.showAndWait();

                if(rez.get() == da){

                    String staraVrijednost = "";
                    String novaVrijednost = "";

                    if(ime.isBlank() == false){
                        novaVrijednost = ime;

                        staraVrijednost = turist.getIme();
                    }
                    else if(prezime.isBlank() == false){
                        novaVrijednost = prezime;

                        staraVrijednost = turist.getPrezime();
                    }
                    else if(OIB.isBlank() == false){
                        novaVrijednost = OIB;

                        staraVrijednost = turist.getOIB();
                    }
                    else if(drzavaPodrijetla.isBlank() == false){
                        novaVrijednost = drzavaPodrijetla;

                        staraVrijednost = turist.getPodrijetlo().drzava();
                    }
                    else if(mjestoPodrijetla.isBlank() == false){
                        novaVrijednost = mjestoPodrijetla;

                        staraVrijednost = turist.getPodrijetlo().mjesto();
                    }
                    else if(datumRodjenja != null){
                        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
                        novaVrijednost = datumRodjenja.format(format);

                        staraVrijednost = turist.getDatumRodjenja().format(format);
                    }
                    else if(lokacijaPosjeta.isBlank() == false){
                        novaVrijednost = lokacijaPosjeta;

                        staraVrijednost = turist.getLokacijaPosjeta();
                    }

                    Integer turistId = turist.getId();

                    BazaPodataka.urediTurista(turistId, ime, prezime, OIB, drzavaPodrijetla, mjestoPodrijetla, datumRodjenja, lokacijaPosjeta);

                    Promjena promjena = new Promjena(turist, staraVrijednost, novaVrijednost, PrijavaController.getPristupniPodatakKorisnika().getKorisnickaRola(), LocalDateTime.now());

                    Aplikacija.getListaPromjena().add(promjena);

                    String message = "Uspješno obavljeno uređivanje turista!";

                    Alert alertU = new Alert(Alert.AlertType.INFORMATION);
                    alertU.setTitle("Uređivanje turista");
                    alertU.setHeaderText(message);
                    alertU.setContentText("Podaci turista su uređeni u bazi podataka.");

                    alertU.showAndWait();

                    Aplikacija.getLogger().info(message);
                }

            }
            else {

                String message = "Neuspješno obavljeno uređivanje turista!";

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Uređivanje turista");
                alert.setHeaderText(message);
                alert.setContentText("Unesite podatak za uređivanje turista.");
                alert.showAndWait();

                Aplikacija.getLogger().error(message);
            }
        }
    }

    public void provjera(Turist turist) throws NemogucaPromjenaOsobe {

        if(turist == null){
            throw new NemogucaPromjenaOsobe();
        }

    }
}
