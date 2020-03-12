package galgeleg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;


import javax.jws.WebService;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@WebService(endpointInterface = "galgeleg.GalgeInterface")
public class Galgelogik extends UnicastRemoteObject implements GalgeInterface {
  /** AHT afprøvning er muligeOrd synlig på pakkeniveau */
  ArrayList<String> muligeOrd = new ArrayList<String>();
  private String ordet;
  private ArrayList<String> brugteBogstaver = new ArrayList<String>();
  private String synligtOrd;
  private int antalForkerteBogstaver;
  private boolean sidsteBogstavVarKorrekt;
  private boolean spilletErVundet;
  private boolean spilletErTabt;

  public Galgelogik() throws java.rmi.RemoteException {
    /*try {
      hentOrdFraDr();
    } catch (Exception e) {
      e.printStackTrace();
    }*/
    muligeOrd.add("bil");
    muligeOrd.add("computer");
    muligeOrd.add("programmering");
    muligeOrd.add("motorvej");
    muligeOrd.add("busrute");
    muligeOrd.add("gangsti");
    muligeOrd.add("skovsnegl");
    muligeOrd.add("solsort");
    muligeOrd.add("nitten");
    nulstil();
  }


  public ArrayList<String> getBrugteBogstaver() {
    return brugteBogstaver;
  }

  public String getSynligtOrd() {
    return synligtOrd;
  }

  public String getOrdet() {
    return ordet;
  }

  public int getAntalForkerteBogstaver() {
    return antalForkerteBogstaver;
  }

  public boolean erSidsteBogstavKorrekt() {
    return sidsteBogstavVarKorrekt;
  }

  public boolean erSpilletVundet() {
    return spilletErVundet;
  }

  public boolean erSpilletTabt() {
    return spilletErTabt;
  }

  public boolean erSpilletSlut() {
    return spilletErTabt || spilletErVundet;
  }


  public void nulstil() {
    brugteBogstaver.clear();
    antalForkerteBogstaver = 0;
    spilletErVundet = false;
    spilletErTabt = false;
    if (muligeOrd.isEmpty()) throw new IllegalStateException("Listen over ord er tom!");
    ordet = muligeOrd.get(new Random().nextInt(muligeOrd.size()));
    opdaterSynligtOrd();
  }


  private void opdaterSynligtOrd() {
    synligtOrd = "";
    spilletErVundet = true;
    for (int n = 0; n < ordet.length(); n++) {
      String bogstav = ordet.substring(n, n + 1);
      if (brugteBogstaver.contains(bogstav)) {
        synligtOrd = synligtOrd + bogstav;
      } else {
        synligtOrd = synligtOrd + "*";
        spilletErVundet = false;
      }
    }
  }

  public void gætBogstav(String bogstav) {
    if (bogstav.length() != 1) return;
    System.out.println("Der gættes på bogstavet: " + bogstav);
    if (brugteBogstaver.contains(bogstav)) return;
    if (spilletErVundet || spilletErTabt) return;

    brugteBogstaver.add(bogstav);

    if (ordet.contains(bogstav)) {
      sidsteBogstavVarKorrekt = true;
      System.out.println("Bogstavet var korrekt: " + bogstav);
    } else {
      // Vi gættede på et bogstav der ikke var i ordet.
      sidsteBogstavVarKorrekt = false;
      System.out.println("Bogstavet var IKKE korrekt: " + bogstav);
      antalForkerteBogstaver = antalForkerteBogstaver + 1;
      if (antalForkerteBogstaver > 6) {
        spilletErTabt = true;
      }
    }
    opdaterSynligtOrd();
  }

  public void logStatus() {
    System.out.println("---------- ");
    System.out.println("- ordet (skult) = " + ordet);
    System.out.println("- synligtOrd = " + synligtOrd);
    System.out.println("- forkerteBogstaver = " + antalForkerteBogstaver);
    System.out.println("- brugteBogstaver = " + brugteBogstaver);
    if (spilletErTabt) System.out.println("- SPILLET ER TABT");
    if (spilletErVundet) System.out.println("- SPILLET ER VUNDET");
    System.out.println("---------- ");
  }


  public static String hentUrl(String url) throws IOException {
    System.out.println("Henter data fra " + url);
    BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
    StringBuilder sb = new StringBuilder();
    String linje = br.readLine();
    while (linje != null) {
      sb.append(linje + "\n");
      linje = br.readLine();
    }
    return sb.toString();
  }


  /**
   * Hent ord fra DRs forside (https://dr.dk)
   */
  /*public void hentOrdFraDr() throws Exception {

    Client client = ClientBuilder.newClient();
    Response res = client.target("https://www.dr.dk/mu-online/api/1.3/page/tv/front")
            .request(MediaType.APPLICATION_JSON).get();
    String svar = res.readEntity(String.class);
    //System.out.println(svar);

      //Parse svar som et JSON-objekt
      JSONObject json = new JSONObject(svar);
      for(int i = 0;i < 3; i++) {
        //System.out.println("json=" + json);

        JSONArray data = json.getJSONArray("Live");
        JSONArray data2 = data.getJSONObject(0).getJSONArray("Next");
       // System.out.println(data2.getJSONObject(i));

        JSONObject data10 = data2.getJSONObject(i);

        String array[] = new String[5];


        array[i] = data10.getString("Description").split("\\s+");
        for(int j=0; j < array.length; j++){
          array[i] = array[i].replaceAll("[^a-zæøå]", " ");
          System.out.println(array[i]);
        }


      }






   /* data = data.substring(data.indexOf("<body")). // fjern headere
            replaceAll("<.+?>", " ").toLowerCase(). // fjern tags
            replaceAll("&#198;", "æ"). // erstat HTML-tegn
            replaceAll("&#230;", "æ"). // erstat HTML-tegn
            replaceAll("&#216;", "ø"). // erstat HTML-tegn
            replaceAll("&#248;", "ø"). // erstat HTML-tegn
            replaceAll("&oslash;", "ø"). // erstat HTML-tegn
            replaceAll("&#229;", "å"). // erstat HTML-tegn
            replaceAll("[^a-zæøå]", " "). // fjern tegn der ikke er bogstaver
            replaceAll(" [a-zæøå] "," "). // fjern 1-bogstavsord
            replaceAll(" [a-zæøå][a-zæøå] "," "); // fjern 2-bogstavsord

    System.out.println("data = " + data);
    System.out.println("data = " + Arrays.asList(data.split("\\s+")));
    muligeOrd.clear();
    muligeOrd.addAll(new HashSet<String>(Arrays.asList(data.split(" "))));
*/


    /*System.out.println("muligeOrd = " + muligeOrd);
    nulstil();
  }*/


  /**
   * Hent ord og sværhedsgrad fra et online regneark. Du kan redigere i regnearket, på adressen
   * https://docs.google.com/spreadsheets/d/1RnwU9KATJB94Rhr7nurvjxfg09wAHMZPYB3uySBPO6M/edit?usp=sharing
   * @param sværhedsgrader en streng med de tilladte sværhedsgrader - f.eks "3" for at medtage kun svære ord, eller "12" for alle nemme og halvsvære ord
   * @throws Exception
   */

  public void hentOrdFraRegneark(String sværhedsgrader) throws Exception {
    String id = "1RnwU9KATJB94Rhr7nurvjxfg09wAHMZPYB3uySBPO6M";

    System.out.println("Henter data som kommasepareret CSV fra regnearket https://docs.google.com/spreadsheets/d/"+id+"/edit?usp=sharing");

    String data = hentUrl("https://docs.google.com/spreadsheets/d/" + id + "/export?format=csv&id=" + id);
    int linjeNr = 0;

    muligeOrd.clear();
    for (String linje : data.split("\n")) {
      if (linjeNr<20) System.out.println("Læst linje = " + linje); // udskriv de første 20 linjer
      if (linjeNr++ < 1 ) continue; // Spring første linje med kolonnenavnene over
      String[] felter = linje.split(",", -1);// -1 er for at beholde tomme indgange, f.eks. bliver ",,," splittet i et array med 4 tomme strenge
      String sværhedsgrad = felter[0].trim();
      String ordet = felter[1].trim().toLowerCase();
      if (sværhedsgrad.isEmpty() || ordet.isEmpty()) continue; // spring over linjer med tomme ord
      if (!sværhedsgrader.contains(sværhedsgrad)) continue; // filtrér på sværhedsgrader
      System.out.println("Tilføjer "+ordet+", der har sværhedsgrad "+sværhedsgrad);
      muligeOrd.add(ordet);
    }

    System.out.println("muligeOrd = " + muligeOrd);
    nulstil();
  }

  public String klientOutput(){
    StringBuilder sb = new StringBuilder("- synligtOrd = " + synligtOrd + "\n" +
            "- forkerteBogstaver = " + antalForkerteBogstaver + "\n" +
            "- brugteBogstaver = " + brugteBogstaver + "\n");
    if(spilletErTabt) sb.append("Spillet er tabt");
    if(spilletErVundet) sb.append("Spillet er vundet");
    return sb.toString();
  }
}
