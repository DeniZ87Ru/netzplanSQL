package org.example;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
/**
 * Die Klasse Knoten repräsentiert einen Knoten im Netzplan.
 * Ein Knoten enthält Informationen wie seine Nummer, Dauer, Beschreibung, Vorgänger, Nachfolger usw.
 * Erstellen Klasse Knoten, die das Serializable-Interface implementiert.
 * Das bedeutet, dass Objekte dieser Klasse in ein Byte-Array umgewandelt werden können,
 * um sie z.B. in einer Datei zu speichern oder über das Netzwerk zu übertragen.
 * Dadurch wird es möglich, Knoten-Objekte in verschiedenen Kontexten zu verwenden und zu übertragen,
 * ohne dass dabei Daten verloren gehen oder verfälscht werden.
 */
public class Knoten implements Serializable {
    // Die Nummer des Knotens
    int nummer;
    // Die Dauer des Knotens
    double dauer;
    // Der früheste Anfangszeitpunkt (FAZ) des Knotens
    double FAZ;
    // Der früheste Endzeitpunkt (FEZ) des Knotens
    double FEZ;
    // Der späteste Anfangszeitpunkt (SAZ) des Knotens
    double SAZ;
    // Der späteste Endzeitpunkt (SEZ) des Knotens
    double SEZ;
    // Der freie Puffer des Knotens
    double FreiePuffer;
    // Der gesamte Puffer des Knotens
    double GesamtPuffer;
    boolean kritisch;
    // Die Beschreibung des Knotens
    String beschreibung;
    // Die Vorgänger des Knotens
    Vector<Knoten> vorgaenger;
    // Die Nachfolger des Knotens
    Vector<Knoten> nachfolger;

    /**
     * Der Konstruktor der Klasse Knoten.
     * @param nummer die Nummer des Knotens
     * @param beschreibung die Beschreibung des Knotens
     * @param dauer die Dauer des Knotens
     * @param vorgaenger die Vorgänger des Knotens
     */
    public Knoten(int nummer, String beschreibung, double dauer, Vector<Knoten> vorgaenger) {
        // Initialisiert die Knotenattribute mit den angegebenen Werten
        this.nummer = nummer;
        this.dauer = dauer;
        this.beschreibung = beschreibung;
        this.vorgaenger = vorgaenger;
        this.nachfolger = new Vector<Knoten>();
    }
    /**
     * Setzt die Nummer des Knotens.
     * @param nummer die Nummer des Knotens
     */
    public void setNummer(int nummer) {
        this.nummer = nummer;
    }
    /**
     * Setzt die Dauer des Knotens.
     * @param dauer die Dauer des Knotens
     */
    public void setDauer(double dauer) {
        this.dauer = dauer;
    }
    /**
     * Setzt die Beschreibung des Knotens.
     * @param beschreibung die Beschreibung des Knotens
     */
    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }
    /**
     * Setzt die Vorgänger des Knotens.
     * @param vorgaenger die Vorgänger des Knotens
     */
    public void setVorgaenger(Vector<Knoten> vorgaenger) {
        this.vorgaenger = vorgaenger;
        for (Knoten vorgaengerKnoten : vorgaenger) {
            vorgaengerKnoten.nachfolger.add(this);
        }
    }
    /**

     Setzt die Nachfolger dieses Knotens.
     @param nachfolger Eine Liste von Knoten, die die Nachfolger dieses Knotens sind.
     */
    public void setNachfolger(Vector<Knoten> nachfolger) {
        this.nachfolger = nachfolger;
    }
    /**

     Setzt den frühesten Anfangszeitpunkt (FAZ) dieses Knotens.
     @param FAZ Der früheste Anfangszeitpunkt dieses Knotens.
     */
    public void setFAZ(double FAZ) {
        this.FAZ = FAZ;
    }
    /**

     Setzt den frühesten Endzeitpunkt (FEZ) dieses Knotens.
     @param FEZ Der früheste Endzeitpunkt dieses Knotens.
     */
    public void setFEZ(double FEZ) {
        this.FEZ = FEZ;
    }
    /**

     Setzt den spätesten Anfangszeitpunkt (SAZ) dieses Knotens.
     @param SAZ Der späteste Anfangszeitpunkt dieses Knotens.
     */
    public void setSAZ(double SAZ) {
        this.SAZ = SAZ;
    }
    /**

     Setzt den spätesten Endzeitpunkt (SEZ) dieses Knotens.
     @param SEZ Der späteste Endzeitpunkt dieses Knotens.
     */
    public void setSEZ(double SEZ) {
        this.SEZ = SEZ;
    }
    /**

     Setzt die Anzahl der freien Puffer dieses Knotens.
     @param FreiePuffer Die Anzahl der freien Puffer dieses Knotens.
     */
    public void setFreiePuffer(double FreiePuffer) {
        this.FreiePuffer = FreiePuffer;
    }
    /**

     Setzt die Anzahl der gesamten Puffer dieses Knotens.
     @param GesamtPuffer Die Anzahl der gesamten Puffer dieses Knotens.
     */
    public void setGesamtPuffer(double GesamtPuffer) {
        this.GesamtPuffer = GesamtPuffer;
    }
    /**

     Setzt, ob dieser Knoten kritisch ist oder nicht.
     @param kritisch true, falls dieser Knoten kritisch ist, false sonst.
     */
    public void setKritisch(boolean kritisch) {
        this.kritisch = kritisch;
    }
    /**

     Gibt die Nummer dieses Knotens zurück.
     @return Die Nummer dieses Knotens.
     */
    public int getNummer() {
        return nummer;
    }
    /**

     Gibt die Dauer dieses Knotens zurück.
     @return Die Dauer dieses Knotens.
     */
    public double getDauer() {
        return dauer;
    }
    /**

     Gibt die Beschreibung dieses Knotens zurück.
     @return Die Beschreibung dieses Knotens.
     */
    public String getBeschreibung() {
        return beschreibung;
    }
    /**

     Gibt eine Liste von Vorgängern dieses Knotens zurück.
     @return Eine Liste von Vorgängern dieses Knotens.
     */
    public Vector<Knoten> getVorgaenger() {
        return this.vorgaenger;
    }
    /**

     Gibt eine Liste von Nachfolgern dieses Knotens zurück.
     @return Eine Liste von Nachfolgern dieses Knotens.
     */
    public Vector<Knoten> getNachfolger() {
        return this.nachfolger;
    }
    /**

     Gibt den frühesten Anfangszeitpunkt (FAZ) dieses Knotens zurück.
     @return Der früheste Anfangszeitpunkt dieses Knotens.
     */
    public double getFAZ() {
        if (this.vorgaenger.size() == 0) {
            return 0;
        }
        return this.getHighestFezVorgaenger();
    }
    /**
     * Berechnet den Frühesten Endzeitpunkt (FEZ) des aktuellen Knotens.
     * @return Der FEZ des aktuellen Knotens.
     */
    public double getFEZ() {
        return this.getFAZ() + this.getDauer();
    }
    /**
     * Berechnet den Spätesten Anfangszeitpunkt (SAZ) des aktuellen Knotens.
     * @return Der SAZ des aktuellen Knotens.
     */
    public double getSAZ() {
        return this.getSEZ() - this.getDauer();
    }
    /**
     * Berechnet den Spätesten Endzeitpunkt (SEZ) des aktuellen Knotens.
     * Wenn der Knoten keine Nachfolger hat, ist der SEZ gleich dem FEZ.
     * @return Der SEZ des aktuellen Knotens.
     */
    public double getSEZ() {
        if (this.nachfolger.size() == 0) {
            return this.getFEZ();
        }
        return getMinSazNachfolger();
    }
    /**
     * Berechnet den freien Puffer des aktuellen Knotens.
     * Wenn der Knoten keine Nachfolger hat, ist der freie Puffer gleich 0.
     * @return Der freie Puffer des aktuellen Knotens.
     */
    public double getFreiePuffer() {
        if (this.nachfolger.size() == 0) {
            return 0;
        }
        return getMinFazNachfolger() - this.getFEZ();
    }
    /**
     * Berechnet den gesamten Puffer des aktuellen Knotens.
     * @return Der gesamte Puffer des aktuellen Knotens.
     */
    public double getGesamtPuffer() {
        return this.getSAZ() - this.getFAZ();
    }
    /**
     * Überprüft, ob der aktuelle Knoten kritisch ist.
     * Ein Knoten ist kritisch, wenn sein freier Puffer gleich 0 ist.
     * @return true, wenn der Knoten kritisch ist, ansonsten false.
     */
    public boolean isKritisch() {
        if (this.getFreiePuffer() == 0) {
            this.kritisch = true;
            return true;
        }
        return false;
    }
    /**
     * Hilfsmethode, die den höchsten FEZ aller Vorgängerknoten des aktuellen Knotens berechnet.
     * @return Der höchste FEZ aller Vorgängerknoten des aktuellen Knotens.
     */
    private double getHighestFezVorgaenger() {
        double highestFez = 0;
        for (Knoten v : this.vorgaenger) {
            if (v.getFEZ() > highestFez) {
                highestFez = v.getFEZ();
            }
        }
        return highestFez;
    }
    /**
     * Hilfsmethode, die den kleinsten FAZ aller Nachfolgerknoten des aktuellen Knotens berechnet.
     * @return Der kleinsten FAZ aller Nachfolgerknoten des aktuellen Knotens.
     */
    private double getMinFazNachfolger() {
        double lowestFaz = Double.MAX_VALUE;
        for (Knoten nachfolger : this.nachfolger) {
            if (nachfolger.getFAZ() < lowestFaz) {
                lowestFaz = nachfolger.getFAZ();
            }
        }
        return lowestFaz;
    }
    /**
    * Hilfsmethode, die den kleinsten SAZ aller Nachfolgerknoten des aktuellen Knotens berechnet.
    * @return Der kleinsten SAZ aller Nachfolger Knoten des aktuellen Knotens.
    */
    private double getMinSazNachfolger() {
        double lowestSAZ = Double.MAX_VALUE;
        // Iteriere durch alle Nachfolgerknoten des aktuellen Knotens.
        for (Knoten nachfolger : this.nachfolger) {
            // Wenn der SAZ des aktuellen Nachfolgerknotens kleiner als der bisher kleinste SAZ ist,
            // aktualisiere den Wert von lowestSAZ auf den SAZ des aktuellen Nachfolgerknotens.
            if (nachfolger.getSAZ() < lowestSAZ) {
                lowestSAZ = nachfolger.getSAZ();
            }
        }
        // Gib den kleinsten SAZ aller Nachfolgerknoten des aktuellen Knotens zurück.
        return lowestSAZ;
    }
    /**
     * Gibt eine Liste aller Tabellennamen der Datenbank zurück, die im aktuellen Verzeichnis mit dem Namen "test" gespeichert ist.
     * @return Eine String-Liste mit den Tabellennamen
     */
    static String[] getExistingTables() {
        // Verbindungsdaten für die H2-Datenbank festlegen
        String jdbcUrl = "jdbc:h2:~/test";
        String username = "sa";
        String password = "Lemonensaft90";
        // Liste für Tabellennamen erstellen
        List<String> tables = new ArrayList<>();
        // Versucht, eine Verbindung zur Datenbank herzustellen und die Tabellennamen abzurufen
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement()) {
        // SQL-Abfrage zum Abrufen der Tabellennamen definieren
            ResultSet resultSet = statement.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC'");
            // Schleife durch alle Ergebniszeilen
            while (resultSet.next()) {
                // Tabellennamen zu Liste hinzufügen
                tables.add(resultSet.getString("TABLE_NAME"));
            }
        // Fehlerbehandlung für SQL-Exceptions
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Liste als Array zurückgeben
        return tables.toArray(new String[0]);
    }
}
