package com.example.aplikacija.Datoteke;

import com.example.aplikacija.Aplikacija;
import com.example.aplikacija.Entiteti.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Datoteke {

    private static final Integer BROJ_ZAPISA_PRISTUPNOG_PODATKA = 4;

    public static void dohvatiPristupnePodatke(Set<PristupniPodatak<String, String>> pristupniPodaci){

        try(BufferedReader bufReader = new BufferedReader(new FileReader("dat/pristupniPodaci.txt"))){

            List<String> datoteka = bufReader.lines().toList();

            for(int i = 0; i < (datoteka.size() / BROJ_ZAPISA_PRISTUPNOG_PODATKA); i++){
                String idSluzbenikaS = datoteka.get(i * BROJ_ZAPISA_PRISTUPNOG_PODATKA);
                String korisnickoIme = datoteka.get(i * BROJ_ZAPISA_PRISTUPNOG_PODATKA + 1);
                String lozinka = datoteka.get(i * BROJ_ZAPISA_PRISTUPNOG_PODATKA + 2);
                String korisnickaRola = datoteka.get(i * BROJ_ZAPISA_PRISTUPNOG_PODATKA + 3);

                Integer idSluzbenika = Integer.parseInt(idSluzbenikaS);

                pristupniPodaci.add(new PristupniPodatak<>(idSluzbenika, korisnickoIme, lozinka, korisnickaRola));
            }
        }
        catch(IOException ex){
            String poruka = "Pogreška prilikom čitanja sadržaja datoteke pristupniPodaci.txt!";

            Aplikacija.getLogger().error(poruka, ex);
        }
    }

    public static void zapisiPristupnePodatke(Sluzbenik sluzbenik, String korisnickoIme, String hashiranaLozinka, String korisnickaRola) throws IOException {

        BufferedWriter bW = new BufferedWriter(new FileWriter("dat/pristupniPodaci.txt", true));

        bW.newLine();

        bW.write(sluzbenik.getId().toString());

        bW.newLine();

        bW.write(korisnickoIme);

        bW.newLine();

        bW.write(hashiranaLozinka);

        bW.newLine();

        bW.write(korisnickaRola);

        bW.close();
    }

    public static void zapisiPromjene(List<Promjena> promjene){

        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dat/promjene.dat"))){

            out.writeObject(promjene);
        }
        catch (IOException ex) {
            String poruka = "Pogreška prilikom serijalizacije promjene u promjene.dat!";

            Aplikacija.getLogger().error(poruka, ex);
        }
    }

    public static List<Promjena> dohvatiPromjene(){

        List<Promjena> promjene = new ArrayList<>();

        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream("dat/promjene.dat"))){

            promjene = (List<Promjena>) in.readObject();
        }
        catch (IOException | ClassNotFoundException ex) {
            String poruka = "Pogreška prilikom deserijalizacije promjena iz promjene.dat!";

            Aplikacija.getLogger().error(poruka, ex);
        }

        return promjene;
    }

}
