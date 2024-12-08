package com.example.aplikacija;

import com.example.aplikacija.BazaPodataka.BazaPodataka;
import com.example.aplikacija.Entiteti.Promjena;
import com.example.aplikacija.Entiteti.Sluzbenik;
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

public class UredjivanjeSluzbenikaController {

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

        Aplikacija.osvjeziPodatke("sl", sluzbeniciTableView);
    }

    public void uredjivanje(){

        Sluzbenik sluzbenik = sluzbeniciTableView.getSelectionModel().getSelectedItem();

        Integer u = 0;

        try{
            provjera(sluzbenik);
        }
        catch(NemogucaPromjenaOsobe ex){
            String message = "Neuspješno uređivanje službenika!";

            u = 1;

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Uređivanje službenika");
            alert.setHeaderText(message);
            alert.setContentText("Odaberite službenika za uređivanje njegovih podataka.");
            alert.showAndWait();

            Aplikacija.getLogger().error(message);
        }

        if(u == 0){

            String ime = imeSluzbenikaTextField.getText();

            String prezime = prezimeSluzbenikaTextField.getText();

            String OIB = OIBSluzbenikaTextField.getText();

            LocalDate datumRodjenja = datumRodjenjaDatePicker.getValue();

            if(ime.isBlank() == false || prezime.isBlank() == false || OIB.isBlank() == false  || datumRodjenja != null){

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Uređivanje službenika");
                alert.setHeaderText("Jeste li sigurni da želite provesti uređivanje službenika?");
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

                        staraVrijednost = sluzbenik.getIme();
                    }
                    else if(prezime.isBlank() == false){
                        novaVrijednost = prezime;

                        staraVrijednost = sluzbenik.getPrezime();
                    }
                    else if(OIB.isBlank() == false){
                        novaVrijednost = OIB;

                        staraVrijednost = sluzbenik.getOIB();
                    }
                    else if(datumRodjenja != null){
                        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
                        novaVrijednost = datumRodjenja.format(format);

                        staraVrijednost = sluzbenik.getDatumRodjenja().format(format);
                    }

                    Integer sluzbenikId = sluzbenik.getId();

                    BazaPodataka.urediSluzbenika(sluzbenikId, ime, prezime, OIB, datumRodjenja);

                    Promjena promjena = new Promjena(sluzbenik, staraVrijednost, novaVrijednost, PrijavaController.getPristupniPodatakKorisnika().getKorisnickaRola(), LocalDateTime.now());

                    Aplikacija.getListaPromjena().add(promjena);

                    String message = "Uspješno obavljeno uređivanje službenika!";

                    Alert alertU = new Alert(Alert.AlertType.INFORMATION);
                    alertU.setTitle("Uređivanje službenika");
                    alertU.setHeaderText(message);
                    alertU.setContentText("Podaci službenika su uređeni u bazi podataka.");

                    alertU.showAndWait();

                    Aplikacija.getLogger().info(message);
                }
            }

            else {

                String message = "Neuspješno obavljeno uređivanje službenika!";

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Uređivanje službenika");
                alert.setHeaderText(message);
                alert.setContentText("Unesite podatak za uređivanje službenika.");
                alert.showAndWait();

                Aplikacija.getLogger().error(message);
            }
        }
    }

    public void provjera(Sluzbenik sluzbenik) throws NemogucaPromjenaOsobe {

        if(sluzbenik == null){
            throw new NemogucaPromjenaOsobe();
        }

    }

}
