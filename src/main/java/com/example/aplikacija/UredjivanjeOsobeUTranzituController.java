package com.example.aplikacija;

import com.example.aplikacija.BazaPodataka.BazaPodataka;
import com.example.aplikacija.Entiteti.OsobaUTranzitu;
import com.example.aplikacija.Entiteti.Promjena;
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

public class UredjivanjeOsobeUTranzituController {

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

    public void uredjivanje(){

        OsobaUTranzitu osobaUTranzitu = osobeUTranzituTableView.getSelectionModel().getSelectedItem();

        Integer u = 0;

        try{
            provjera(osobaUTranzitu);
        }
        catch(NemogucaPromjenaOsobe ex){
            String message = "Neuspješno uređivanje osobe u tranzitu!";

            u = 1;

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Uređivanje osobe u tranzitu");
            alert.setHeaderText(message);
            alert.setContentText("Odaberite osobu u tranzitu za uređivanje njegovih podataka.");
            alert.showAndWait();

            Aplikacija.getLogger().error(message);
        }

        if(u == 0){

            String ime = imeOsobeUTranzituTextField.getText();

            String prezime = prezimeOsobeUTranzituTextField.getText();

            String OIB = OIBOsobeUTranzituTextField.getText();

            String drzavaPodrijetla = drzavaPodrijetlaTextField.getText();

            String mjestoPodrijetla = mjestoPodrijetlaTextField.getText();

            LocalDate datumRodjenja = datumRodjenjaDatePicker.getValue();

            String odredisnaDrzava = odredisnaDrzavaTextField.getText();

            if(ime.isBlank() == false || prezime.isBlank() == false || OIB.isBlank() == false || drzavaPodrijetla.isBlank() == false || mjestoPodrijetla.isBlank() == false || datumRodjenja != null || odredisnaDrzava.isBlank() == false){

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Uređivanje osobe u tranzitu");
                alert.setHeaderText("Jeste li sigurni da želite provesti uređivanje osobu u tranzitu?");
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

                        staraVrijednost = osobaUTranzitu.getIme();
                    }
                    else if(prezime.isBlank() == false){
                        novaVrijednost = prezime;

                        staraVrijednost = osobaUTranzitu.getPrezime();
                    }
                    else if(OIB.isBlank() == false){
                        novaVrijednost = OIB;

                        staraVrijednost = osobaUTranzitu.getOIB();
                    }
                    else if(drzavaPodrijetla.isBlank() == false){
                        novaVrijednost = drzavaPodrijetla;

                        staraVrijednost = osobaUTranzitu.getPodrijetlo().drzava();
                    }
                    else if(mjestoPodrijetla.isBlank() == false){
                        novaVrijednost = mjestoPodrijetla;

                        staraVrijednost = osobaUTranzitu.getPodrijetlo().mjesto();
                    }
                    else if(datumRodjenja != null){
                        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
                        novaVrijednost = datumRodjenja.format(format);

                        staraVrijednost = osobaUTranzitu.getDatumRodjenja().format(format);
                    }
                    else if(odredisnaDrzava.isBlank() == false){
                        novaVrijednost = odredisnaDrzava;

                        staraVrijednost = osobaUTranzitu.getOdredisnaDrzava();
                    }

                    Integer osobaUTranzituId = osobaUTranzitu.getId();

                    BazaPodataka.urediOsobuUTranzitu(osobaUTranzituId, ime, prezime, OIB, drzavaPodrijetla, mjestoPodrijetla, datumRodjenja, odredisnaDrzava);

                    Promjena promjena = new Promjena(osobaUTranzitu, staraVrijednost, novaVrijednost, PrijavaController.getPristupniPodatakKorisnika().getKorisnickaRola(), LocalDateTime.now());

                    Aplikacija.getListaPromjena().add(promjena);

                    String message = "Uspješno obavljeno uređivanje turista!";

                    Alert alertU = new Alert(Alert.AlertType.INFORMATION);
                    alertU.setTitle("Uređivanje osobe u tranzitu");
                    alertU.setHeaderText(message);
                    alertU.setContentText("Podaci osobe u tranzitu su uređeni u bazu podataka.");

                    alertU.showAndWait();

                    Aplikacija.getLogger().info(message);
                }

            }
            else {

                String message = "Neuspješno obavljeno uređivanje osobe u tranzitu!";

                Alert alertN = new Alert(Alert.AlertType.ERROR);
                alertN.setTitle("Uređivanje osobe u tranzitu");
                alertN.setHeaderText(message);
                alertN.setContentText("Unesite podatak za uređivanje osobe u tranzitu.");
                alertN.showAndWait();

                Aplikacija.getLogger().error(message);
            }
        }
    }

    public void provjera(OsobaUTranzitu osobaUTranzitu) throws NemogucaPromjenaOsobe {

        if(osobaUTranzitu == null){
            throw new NemogucaPromjenaOsobe();
        }

    }

}
