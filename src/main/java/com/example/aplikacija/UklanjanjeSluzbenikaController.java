package com.example.aplikacija;

import com.example.aplikacija.BazaPodataka.BazaPodataka;
import com.example.aplikacija.Entiteti.Promjena;
import com.example.aplikacija.Entiteti.Sluzbenik;
import com.example.aplikacija.Iznimke.NemogucaPromjenaOsobe;
import com.example.aplikacija.Iznimke.NemoguceUklanjanjeTrenutnogKorisnika;
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

public class UklanjanjeSluzbenikaController {

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

    public void uklanjanje(){
        Sluzbenik sluzbenik = sluzbeniciTableView.getSelectionModel().getSelectedItem();

        Integer u = 0;

        try{
            u = izbrisiSluzbenika(sluzbenik);
        }
        catch(NemogucaPromjenaOsobe ex){
            String message = "Neuspješno obavljeno uklanjanje službenika!";

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Uklanjenja službenika");
            alert.setHeaderText(message);
            alert.setContentText("Označite službenika za uklanjanje iz baze podataka.");

            alert.showAndWait();

            Aplikacija.getLogger().error(message, ex);
        }
        catch(NemoguceUklanjanjeTrenutnogKorisnika ex){
            String message = "Neuspješno obavljeno uklanjanje službenika!";

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Uklanjanje službenika");
            alert.setHeaderText(message);
            alert.setContentText("Ne možete ukloniti službenika koji trenutno koristi aplikaciju!");

            alert.showAndWait();

            Aplikacija.getLogger().error(message, ex);
        }

        if(u == 1){
            Promjena promjena = new Promjena(sluzbenik, sluzbenik.getIme() + " " + sluzbenik.getPrezime(), "-", PrijavaController.getPristupniPodatakKorisnika().getKorisnickaRola(), LocalDateTime.now());

            Aplikacija.getListaPromjena().add(promjena);
        }
    }

    public Integer izbrisiSluzbenika(Sluzbenik sluzbenik) throws NemogucaPromjenaOsobe, NemoguceUklanjanjeTrenutnogKorisnika {

        Integer u = 0;

        if(sluzbenik == null){
            throw new NemogucaPromjenaOsobe();
        }
        else if(sluzbenik.getId() == PrijavaController.getPristupniPodatakKorisnika().getIdSluzbenika()){
            throw new NemoguceUklanjanjeTrenutnogKorisnika();
        }
        else{

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Uklanjanje službenika");
            alert.setHeaderText("Jeste li sigurni da želite izvršiti uklanjanje službenika?");
            alert.setContentText("Odaberite svoj odgovor.");

            ButtonType da = new ButtonType("Da");
            ButtonType ne = new ButtonType("Ne");

            alert.getButtonTypes().setAll(da, ne);

            Optional<ButtonType> rez = alert.showAndWait();

            if(rez.get() == da){

                u = 1;

                BazaPodataka.ukloniPristupniPodatak(sluzbenik.getId());
                BazaPodataka.ukloniGranicaSluzbenik(PrijavaController.getTrenutnaGranica(), sluzbenik);
                BazaPodataka.ukloniSluzbenika(sluzbenik);

                String message = "Uspješno obavljeno uklanjanje službenika!";

                Alert alertU = new Alert(Alert.AlertType.INFORMATION);
                alertU.setTitle("Uklanjanje službenika");
                alertU.setHeaderText(message);
                alertU.setContentText("Službenik je uspješno uklonjen iz baze podataka!");

                alertU.showAndWait();

                Aplikacija.getLogger().info(message);
            }
        }

        return u;
    }
}
