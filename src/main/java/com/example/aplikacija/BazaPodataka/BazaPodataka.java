package com.example.aplikacija.BazaPodataka;

import com.example.aplikacija.Aplikacija;
import com.example.aplikacija.Entiteti.*;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BazaPodataka {

    private static final String datoteka = "dat/bazaPodataka.properties";


    private static Connection spajanjeNaBazu() throws IOException, SQLException {

        Properties svojstva = new Properties();

        svojstva.load(new FileReader(datoteka));

        String urlBazePodataka = svojstva.getProperty("urlBazePodataka");
        String korisnickoIme = svojstva.getProperty("korisnickoIme");
        String lozinka = svojstva.getProperty("lozinka");

        Connection veza = DriverManager.getConnection(urlBazePodataka, korisnickoIme, lozinka);

        return veza;
    }

    public static List<Turist> dohvatiTuriste() {

        List<Turist> turisti = new ArrayList<>();

        try (Connection veza = spajanjeNaBazu()) {

            Statement upit = veza.createStatement();

            ResultSet resultSet = upit.executeQuery("SELECT * FROM TURIST");

            while (resultSet.next()) {

                Integer id = resultSet.getInt("id");
                String ime = resultSet.getString("ime");
                String prezime = resultSet.getString("prezime");
                String oib = resultSet.getString("oib");
                String drzavaPodrijetla = resultSet.getString("drzava_podrijetla");
                String mjestoPodrijetla = resultSet.getString("mjesto_podrijetla");
                LocalDate datumRodjenja = resultSet.getTimestamp("datum_rodjenja").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String lokacijaPosjeta = resultSet.getString("lokacija_posjeta");

                Podrijetlo podrijetlo = new Podrijetlo(drzavaPodrijetla, mjestoPodrijetla);

                Turist turist = new Turist(id, ime, prezime, oib, podrijetlo, datumRodjenja, lokacijaPosjeta);

                turisti.add(turist);
            }
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka!";

            Aplikacija.getLogger().error(poruka, ex);
        }

        return turisti;
    }

    public static void spremiNovogTurista(Turist turist) {

        try (Connection veza = spajanjeNaBazu()) {

            PreparedStatement preparedStatement = veza.prepareStatement("INSERT INTO TURIST(ime, prezime, oib, drzava_podrijetla, mjesto_podrijetla, datum_rodjenja, lokacija_posjeta) VALUES(?, ?, ?, ?, ?, ?, ?)");

            preparedStatement.setString(1, turist.getIme());
            preparedStatement.setString(2, turist.getPrezime());
            preparedStatement.setString(3, turist.getOIB());
            preparedStatement.setString(4, turist.getPodrijetlo().drzava());
            preparedStatement.setString(5, turist.getPodrijetlo().mjesto());
            preparedStatement.setDate(6, Date.valueOf(turist.getDatumRodjenja()));
            preparedStatement.setString(7, turist.getLokacijaPosjeta());

            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka!";

            Aplikacija.getLogger().error(poruka, ex);
        }
    }

    public static void ukloniTurista(Turist turist) {

        try (Connection veza = spajanjeNaBazu()) {

            PreparedStatement preparedStatement = veza.prepareStatement("DELETE FROM TURIST WHERE ID = ?");

            preparedStatement.setInt(1, turist.getId());

            preparedStatement.executeUpdate();
        } catch (IOException | SQLException ex) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka!";

            Aplikacija.getLogger().error(poruka, ex);
        }
    }

    public static void urediTurista(Integer turistId, String ime, String prezime, String oib, String drzava, String mjesto, LocalDate datumRodjenja, String lokacija) {

        try (Connection veza = spajanjeNaBazu()) {

            veza.setAutoCommit(false);

            if (ime.isBlank() == false) {

                PreparedStatement preparedStatement = veza.prepareStatement("UPDATE TURIST SET IME = ? WHERE ID = ?");

                preparedStatement.setString(1, ime);
                preparedStatement.setInt(2, turistId);

                preparedStatement.executeUpdate();

            }
            if (prezime.isBlank() == false) {

                PreparedStatement preparedStatement = veza.prepareStatement("UPDATE TURIST SET PREZIME = ? WHERE ID = ?");

                preparedStatement.setString(1, prezime);
                preparedStatement.setInt(2, turistId);

                preparedStatement.executeUpdate();

            }
            if (oib.isBlank() == false) {

                PreparedStatement preparedStatement = veza.prepareStatement("UPDATE TURIST SET OIB = ? WHERE ID = ?");

                preparedStatement.setString(1, oib);
                preparedStatement.setInt(2, turistId);

                preparedStatement.executeUpdate();

            }
            if (drzava.isBlank() == false) {

                PreparedStatement preparedStatement = veza.prepareStatement("UPDATE TURIST SET DRZAVA_PODRIJETLA = ? WHERE ID = ?");

                preparedStatement.setString(1, drzava);
                preparedStatement.setInt(2, turistId);

                preparedStatement.executeUpdate();

            }
            if (mjesto.isBlank() == false) {

                PreparedStatement preparedStatement = veza.prepareStatement("UPDATE TURIST SET MJESTO_PODRIJETLA = ? WHERE ID = ?");

                preparedStatement.setString(1, mjesto);
                preparedStatement.setInt(2, turistId);

                preparedStatement.executeUpdate();
            }
            if(datumRodjenja != null){

                PreparedStatement preparedStatement = veza.prepareStatement("UPDATE TURIST SET DATUM_RODJENJA = ? WHERE ID = ?");

                preparedStatement.setDate(1, Date.valueOf(datumRodjenja));
                preparedStatement.setInt(2, turistId);

                preparedStatement.executeUpdate();

            }
            if (lokacija.isBlank() == false) {

                PreparedStatement preparedStatement = veza.prepareStatement("UPDATE TURIST SET LOKACIJA_POSJETA = ? WHERE ID = ?");

                preparedStatement.setString(1, lokacija);
                preparedStatement.setInt(2, turistId);

                preparedStatement.executeUpdate();
            }

            veza.commit();
            veza.setAutoCommit(true);
        }
        catch (IOException | SQLException ex){

            String poruka = "Došlo je do pogreške u radu s bazom podataka!";

            Aplikacija.getLogger().error(poruka, ex);
        }
    }


    public static List<OsobaUTranzitu> dohvatiOsobeUTranzitu(){

        List<OsobaUTranzitu> osobeUTranzitu = new ArrayList<>();

        try(Connection veza = spajanjeNaBazu()){

            Statement upit = veza.createStatement();

            ResultSet resultSet = upit.executeQuery("SELECT * FROM OSOBA_U_TRANZITU");

            while(resultSet.next()){

                Integer id = resultSet.getInt("id");
                String ime = resultSet.getString("ime");
                String prezime = resultSet.getString("prezime");
                String oib = resultSet.getString("oib");
                String drzavaPodrijetla = resultSet.getString("drzava_podrijetla");
                String mjestoPodrijetla = resultSet.getString("mjesto_podrijetla");
                LocalDate datumRodjenja = resultSet.getTimestamp("datum_rodjenja").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String odredisnaDrzava = resultSet.getString("odredisna_drzava");

                Podrijetlo podrijetlo = new Podrijetlo(drzavaPodrijetla, mjestoPodrijetla);

                OsobaUTranzitu osobaUTranzitu = new OsobaUTranzitu(id, ime, prezime, oib, podrijetlo, datumRodjenja, odredisnaDrzava);

                osobeUTranzitu.add(osobaUTranzitu);
            }
        }
        catch(SQLException | IOException ex){
            String poruka = "Došlo je do pogreške u radu s bazom podataka!";

            Aplikacija.getLogger().error(poruka, ex);
        }

        return osobeUTranzitu;
    }

    public static void spremiNovuOsobuUTranzitu(OsobaUTranzitu osobaUTranzitu){

        try(Connection veza = spajanjeNaBazu()){

            PreparedStatement preparedStatement = veza.prepareStatement("INSERT INTO OSOBA_U_TRANZITU(ime, prezime, oib, drzava_podrijetla, mjesto_podrijetla, datum_rodjenja, odredisna_drzava) VALUES(?, ?, ?, ?, ?, ?, ?)");

            preparedStatement.setString(1, osobaUTranzitu.getIme());
            preparedStatement.setString(2, osobaUTranzitu.getPrezime());
            preparedStatement.setString(3, osobaUTranzitu.getOIB());
            preparedStatement.setString(4, osobaUTranzitu.getPodrijetlo().drzava());
            preparedStatement.setString(5, osobaUTranzitu.getPodrijetlo().mjesto());
            preparedStatement.setDate(6, Date.valueOf(osobaUTranzitu.getDatumRodjenja()));
            preparedStatement.setString(7, osobaUTranzitu.getOdredisnaDrzava());

            preparedStatement.executeUpdate();
        }
        catch(SQLException | IOException ex){
            String poruka = "Došlo je do pogreške u radu s bazom podataka!";

            Aplikacija.getLogger().error(poruka, ex);
        }
    }

    public static void ukloniOsobuUTranzitu(OsobaUTranzitu osobaUTranzitu){

        try(Connection veza = spajanjeNaBazu()){

            PreparedStatement preparedStatement = veza.prepareStatement("DELETE FROM OSOBA_U_TRANZITU WHERE ID = ?");

            preparedStatement.setInt(1, osobaUTranzitu.getId());

            preparedStatement.executeUpdate();
        }
        catch(IOException | SQLException ex){
            String poruka = "Došlo je do pogreške u radu s bazom podataka!";

            Aplikacija.getLogger().error(poruka, ex);
        }
    }

    public static void urediOsobuUTranzitu(Integer osobaUTranzituId, String ime, String prezime, String oib, String drzava, String mjesto, LocalDate datumRodjenja, String odredisnaDrzava) {

        try (Connection veza = spajanjeNaBazu()) {

            veza.setAutoCommit(false);

            if (ime.isBlank() == false) {

                PreparedStatement preparedStatement = veza.prepareStatement("UPDATE OSOBA_U_TRANZITU SET IME = ? WHERE ID = ?");

                preparedStatement.setString(1, ime);
                preparedStatement.setInt(2, osobaUTranzituId);

                preparedStatement.executeUpdate();

            }
            if (prezime.isBlank() == false) {

                PreparedStatement preparedStatement = veza.prepareStatement("UPDATE OSOBA_U_TRANZITU SET PREZIME = ? WHERE ID = ?");

                preparedStatement.setString(1, prezime);
                preparedStatement.setInt(2, osobaUTranzituId);

                preparedStatement.executeUpdate();

            }
            if (oib.isBlank() == false) {

                PreparedStatement preparedStatement = veza.prepareStatement("UPDATE OSOBA_U_TRANZITU SET OIB = ? WHERE ID = ?");

                preparedStatement.setString(1, oib);
                preparedStatement.setInt(2, osobaUTranzituId);

                preparedStatement.executeUpdate();

            }
            if (drzava.isBlank() == false) {

                PreparedStatement preparedStatement = veza.prepareStatement("UPDATE OSOBA_U_TRANZITU SET DRZAVA_PODRIJETLA = ? WHERE ID = ?");

                preparedStatement.setString(1, drzava);
                preparedStatement.setInt(2, osobaUTranzituId);

                preparedStatement.executeUpdate();
            }
            if (mjesto.isBlank() == false) {

                PreparedStatement preparedStatement = veza.prepareStatement("UPDATE OSOBA_U_TRANZITU SET MJESTO_PODRIJETLA = ? WHERE ID = ?");

                preparedStatement.setString(1, mjesto);
                preparedStatement.setInt(2, osobaUTranzituId);

                preparedStatement.executeUpdate();
            }
            if(datumRodjenja != null){

                PreparedStatement preparedStatement = veza.prepareStatement("UPDATE OSOBA_U_TRANZITU SET DATUM_RODJENJA = ? WHERE ID = ?");

                preparedStatement.setDate(1, Date.valueOf(datumRodjenja));
                preparedStatement.setInt(2, osobaUTranzituId);

                preparedStatement.executeUpdate();
            }
            if (odredisnaDrzava.isBlank() == false) {

                PreparedStatement preparedStatement = veza.prepareStatement("UPDATE OSOBA_U_TRANZITU SET ODREDISNA_DRZAVA = ? WHERE ID = ?");

                preparedStatement.setString(1, odredisnaDrzava);
                preparedStatement.setInt(2, osobaUTranzituId);

                preparedStatement.executeUpdate();
            }

            veza.commit();
            veza.setAutoCommit(true);
        }
        catch (IOException | SQLException ex){

            String poruka = "Došlo je do pogreške u radu s bazom podataka!";

            Aplikacija.getLogger().error(poruka, ex);
        }
    }

    public static List<Sluzbenik> dohvatiSluzbenike(){

        List<Sluzbenik> sluzbenici = new ArrayList<>();

        try(Connection veza = spajanjeNaBazu()){

            Statement upit = veza.createStatement();

            ResultSet resultSet = upit.executeQuery("SELECT * FROM SLUZBENIK");

            while(resultSet.next()){

                Integer id = resultSet.getInt("id");
                String ime = resultSet.getString("ime");
                String prezime = resultSet.getString("prezime");
                String oib = resultSet.getString("oib");
                LocalDate datumRodjenja = resultSet.getTimestamp("datum_rodjenja").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                Sluzbenik sluzbenik = new Sluzbenik.SluzbenikBuilder(id, oib).unesiIme(ime).unesiPrezime(prezime).unesiDatumRodjenja(datumRodjenja).build();

                sluzbenici.add(sluzbenik);
            }
        }
        catch(SQLException | IOException ex){
            String poruka = "Došlo je do pogreške u radu s bazom podataka!";

            Aplikacija.getLogger().error(poruka, ex);
        }

        return sluzbenici;
    }

    public static void spremiNovogSluzbenika(Sluzbenik sluzbenik){

        try(Connection veza = spajanjeNaBazu()){

            PreparedStatement preparedStatement = veza.prepareStatement("INSERT INTO SLUZBENIK(ime, prezime, oib, datum_rodjenja) VALUES(?, ?, ?, ?)");

            preparedStatement.setString(1, sluzbenik.getIme());
            preparedStatement.setString(2, sluzbenik.getPrezime());
            preparedStatement.setString(3, sluzbenik.getOIB());
            preparedStatement.setDate(4, Date.valueOf(sluzbenik.getDatumRodjenja()));

            preparedStatement.executeUpdate();
        }
        catch(SQLException | IOException ex){
            String poruka = "Došlo je do pogreške u radu s bazom podataka!";

            Aplikacija.getLogger().error(poruka, ex);
        }
    }

    public static void ukloniSluzbenika(Sluzbenik sluzbenik){

        try(Connection veza = spajanjeNaBazu()){

            PreparedStatement preparedStatement = veza.prepareStatement("DELETE FROM SLUZBENIK WHERE ID = ?");

            preparedStatement.setInt(1, sluzbenik.getId());

            preparedStatement.executeUpdate();
        }
        catch(IOException | SQLException ex){
            String poruka = "Došlo je do pogreške u radu s bazom podataka!";

            Aplikacija.getLogger().error(poruka, ex);
        }
    }

    public static void urediSluzbenika(Integer sluzbenikId, String ime, String prezime, String oib, LocalDate datumRodjenja) {

        try (Connection veza = spajanjeNaBazu()) {

            veza.setAutoCommit(false);

            if (ime.isBlank() == false) {

                PreparedStatement preparedStatement = veza.prepareStatement("UPDATE SLUZBENIK SET IME = ? WHERE ID = ?");

                preparedStatement.setString(1, ime);
                preparedStatement.setInt(2, sluzbenikId);

                preparedStatement.executeUpdate();

            }
            if (prezime.isBlank() == false) {

                PreparedStatement preparedStatement = veza.prepareStatement("UPDATE SLUZBENIK SET PREZIME = ? WHERE ID = ?");

                preparedStatement.setString(1, prezime);
                preparedStatement.setInt(2, sluzbenikId);

                preparedStatement.executeUpdate();

            }
            if (oib.isBlank() == false) {

                PreparedStatement preparedStatement = veza.prepareStatement("UPDATE SLUZBENIK SET OIB = ? WHERE ID = ?");

                preparedStatement.setString(1, oib);
                preparedStatement.setInt(2, sluzbenikId);

                preparedStatement.executeUpdate();

            }
            if(datumRodjenja != null){

                PreparedStatement preparedStatement = veza.prepareStatement("UPDATE SLUZBENIK SET DATUM_RODJENJA = ? WHERE ID = ?");

                preparedStatement.setDate(1, Date.valueOf(datumRodjenja));
                preparedStatement.setInt(2, sluzbenikId);

                preparedStatement.executeUpdate();
            }

            veza.commit();
            veza.setAutoCommit(true);
        }
        catch (IOException | SQLException ex){

            String poruka = "Došlo je do pogreške u radu s bazom podataka!";

            Aplikacija.getLogger().error(poruka, ex);
        }
    }

    public static List<Granica> dohvatiGranice(List<Osoba> listaStranihOsoba){

        List<Granica> granice = new ArrayList<>();

        try(Connection veza = spajanjeNaBazu()){

            Statement upit = veza.createStatement();

            ResultSet resultSet = upit.executeQuery("SELECT * FROM GRANICA");

            while(resultSet.next()){

                Integer id = resultSet.getInt("id");
                String naziv = resultSet.getString("naziv");

                Granica granica = new Granica(id, naziv, null, listaStranihOsoba);

                granice.add(granica);
            }
        }
        catch(SQLException | IOException ex){
            String poruka = "Došlo je do pogreške u radu s bazom podataka!";

            Aplikacija.getLogger().error(poruka, ex);
        }

        return granice;
    }

    public static List<Sluzbenik> dohvatiGranice_Sluzbenik(List<Sluzbenik> sluzbenici, Granica granica){

        List<Sluzbenik> sluzbeniciGranice = new ArrayList<>();

        Integer idGranice = granica.getId();

        try(Connection veza = spajanjeNaBazu()){

            Statement upit = veza.createStatement();

            ResultSet resultSet = upit.executeQuery("SELECT * FROM GRANICA_SLUZBENIK WHERE GRANICA_ID =" + idGranice);

            while(resultSet.next()){

                Integer sluzbenikId = resultSet.getInt("sluzbenik_id");

                for(Sluzbenik sluzbenik : sluzbenici){
                    if(sluzbenik.getId() == sluzbenikId){

                        sluzbeniciGranice.add(sluzbenik);
                    }
                }
            }
        }
        catch(SQLException | IOException ex){
            String poruka = "Došlo je do pogreške u radu s bazom podataka!";

            Aplikacija.getLogger().error(poruka, ex);
        }

        return sluzbeniciGranice;
    }

    public static void spremiNoviGranicaSluzbenik(Granica granica, Sluzbenik sluzbenik){

        try(Connection veza = spajanjeNaBazu()){

            PreparedStatement preparedStatement = veza.prepareStatement("INSERT INTO GRANICA_SLUZBENIK(granica_id, sluzbenik_id) VALUES(?, ?)");

            preparedStatement.setInt(1, granica.getId());
            preparedStatement.setInt(2, sluzbenik.getId());

            preparedStatement.executeUpdate();
        }
        catch(SQLException | IOException ex){
            String poruka = "Došlo je do pogreške u radu s bazom podataka!";

            Aplikacija.getLogger().error(poruka, ex);
        }
    }

    public static void ukloniGranicaSluzbenik(Granica granica, Sluzbenik sluzbenik){

        try(Connection veza = spajanjeNaBazu()){

            PreparedStatement preparedStatement = veza.prepareStatement("DELETE FROM GRANICA_SLUZBENIK WHERE GRANICA_ID = ? AND SLUZBENIK_ID = ?");

            preparedStatement.setInt(1, granica.getId());
            preparedStatement.setInt(2, sluzbenik.getId());

            preparedStatement.executeUpdate();
        }
        catch(IOException | SQLException ex){
            String poruka = "Došlo je do pogreške u radu s bazom podataka!";

            Aplikacija.getLogger().error(poruka, ex);
        }
    }

    public static void spremiNoviPristupniPodatak(PristupniPodatak<String, String> pristupniPodatak){

        try(Connection veza = spajanjeNaBazu()){

            PreparedStatement preparedStatement = veza.prepareStatement("INSERT INTO PRISTUPNI_PODATAK(id_sluzbenika, korisnicko_ime, lozinka, korisnicka_rola) VALUES(?, ?, ?, ?)");

            preparedStatement.setInt(1, pristupniPodatak.getIdSluzbenika());
            preparedStatement.setString(2, pristupniPodatak.getKorisnickoIme());
            preparedStatement.setString(3, pristupniPodatak.getLozinka());
            preparedStatement.setString(4, pristupniPodatak.getKorisnickaRola());

            preparedStatement.executeUpdate();
        }
        catch(SQLException | IOException ex){
            String poruka = "Došlo je do pogreške u radu s bazom podataka!";

            Aplikacija.getLogger().error(poruka, ex);
        }
    }

    public static void ukloniPristupniPodatak(Integer idSluzbenika){

        try(Connection veza = spajanjeNaBazu()){

            PreparedStatement preparedStatement = veza.prepareStatement("DELETE FROM PRISTUPNI_PODATAK WHERE ID_SLUZBENIKA = ?");

            preparedStatement.setInt(1, idSluzbenika);

            preparedStatement.executeUpdate();
        }
        catch(IOException | SQLException ex){
            String poruka = "Došlo je do pogreške u radu s bazom podataka!";

            Aplikacija.getLogger().error(poruka, ex);
        }
    }

}
