package org.example;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import static org.example.Knoten.getExistingTables;

/**
 * Die Klasse Component ist ein Teil der Java-Bibliothek und stellt eine grafische
 * Benutzeroberfläche (GUI) für Swing-Anwendungen bereit.
 * Durch das Erweitern der Component-Klasse kann man benutzerdefinierte GUI-Komponenten erstellen,
 * indem man die Methoden und Eigenschaften der Component-Klasse erbt und weitere Funktionalitäten hinzufügt.
 */
public class NetzplanFrame extends Component {
    /**
     * Instanzvariablen zur Darstellung der grafischen Oberfläche
     * Ein Hauptknoten "knotenVector"
     * ein Image icon
     */
    private JFrame frame;
    private JTextField apNummerFeld;
    private JLabel apNummerLabel;
    private JTextArea apBeschreibungFeld;
    private JLabel apBeschreibungLabel;
    private JScrollPane apBeschreibungScrollPane;
    private JTextField apDauerFeld;
    private JLabel apDauerLabel;
    private JTextField apVorFeld;
    private JLabel apVorLabel;
    private Vector<Knoten> knotenVector = new Vector<Knoten>();
    private JButton knotenErstellenButton;
    private JTable knotenTable;
    private DefaultTableModel model;
    private JButton alleKnotenLoschenButton;
    private JButton knotenLoschenButton;
    private ImageIcon icon;
    // Konstruktor der Klasse NetzplanFrame
    public NetzplanFrame() {
        // Erstellt aus dem Datenpfad das Programm-Icon
        icon = new ImageIcon(("src/main/resources/MicrosoftTeams-image.png"));

        frame = new JFrame("NetzplanSQL23"); // Erstellt das Frame mit dem Titel NetzplanSQL23
        frame.getContentPane().setBackground(Color.BLACK); // Hintergrundfarbe auf Schwarz setzen
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // JFrame Schließen
        frame.setSize(1600, 800); // JFrame-Größe auf 1600x800 setzen
        frame.setLocationRelativeTo(null); // Lässt das Frame in der Mitte deines Displays erscheinen
        frame.setIconImage(icon.getImage()); // Fügt das Icon dem Frame hinzu

        // Erstellen hier die JMenuBar in das dann "Datei" und "SQL" eingefügt wird
        JMenuBar menueBar = new JMenuBar();
        JMenu dateiMenue = new JMenu("Datei");
        menueBar.add(dateiMenue);

        // Erstellung des Unterpunktes "speichern unter"
        // Ruft beim Mausklick die Methode "speichernUnter()" auf, die die Tabelle als .dat Datei auf dem Gerät speichert
        JMenuItem speichernUnter = new JMenuItem("Speichern unter");
        speichernUnter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    speichernUnter();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        dateiMenue.add(speichernUnter);

        /**
         *  Erstellung des Unterpunktes "Öffnen"
         *  Ruft zwei Methoden auf:
         *  dateiOeffnen() öffnet die auf dem Gerät gespeicherte .dat Datei
         *  updateTableModel() aktualisiert die Tabelle im Programm
         */
        JMenuItem oeffnen = new JMenuItem("Öffnen");
        oeffnen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dateiOeffnen();
                updateTableModel();
            }
        });
        dateiMenue.add(oeffnen);

        // Erstellung des Unterpunktes "Beenden" die die anwendung beendet
        JMenuItem beenden = new JMenuItem("Beenden");
        beenden.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        dateiMenue.add(beenden);

        // Erstellt ein neues JMenu "SQL"
        JMenu sqlMenu = new JMenu("SQL");
        menueBar.add(sqlMenu);

        // Erstellung des Unterpunktes "SQL Server Start"
        // Startet aus der H2JDBCUtilities Klasse die Methode "startServer()" die lediglich zeigt, ob eine Verbindung zum Server hergestellt werden kann
        JMenuItem startServerMenuItem = new JMenuItem("SQL Server Start");
        startServerMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                H2JDBCUtilities.startServer();
            }
        });
        sqlMenu.add(startServerMenuItem);

        /**
         * Erstellt den Unterpunkt "SQL Update"
         * Dies startet eine reihe JOptionPane
         * JOptionPane.showOptionDialog frägt, ob man eine neue Tabelle anlegen will oder eine bereits bestehende überschreiben möchte
         * Wenn neue Tabelle, öffnet sich ein .showInputDialog in dem man den Namen der Tabelle eingeben soll
         * Wenn bestehende, aktiviert sich die Methode aus der Knoten Klasse "getExistingTables()" in der unsere Tabellen gespeichert werden und sind
         * Durch ein erneuten .showInputDialog kannst du eine bestehende Tabelle auswählen
         * Wenn keine vorhanden startet ein .showMessageDialog der aussagt das keine Tabellen vorhanden wären
         */
        JMenuItem updateTableMenuItem = new JMenuItem("SQL Update");
        updateTableMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Zeige einen Dialog an, in dem der Benutzer auswählen kann,
                // ob er eine neue Tabelle erstellen oder eine vorhandene Tabelle verwenden möchte
                String[] options = {"Neue Tabelle", "Vorhandene Tabelle"};
                int choice = JOptionPane.showOptionDialog(frame, "Möchten Sie eine neue Tabelle erstellen oder eine vorhandene Tabelle verwenden?", "Neue Tabelle anlegen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (choice == JOptionPane.YES_OPTION) {
                    // Der Benutzer hat "Neue Tabelle" ausgewählt
                    String tableName = JOptionPane.showInputDialog(frame, "Geben Sie den Namen der neuen Tabelle ein:", "Neue Tabelle", JOptionPane.PLAIN_MESSAGE);
                    if (tableName != null && !tableName.isEmpty()) {
                        // Eine gültige Tabellenbezeichnung wurde eingegeben
                        writeToDatabases(tableName, knotenVector);
                    }
                } else if (choice == JOptionPane.NO_OPTION) {
                    // Der Benutzer hat "Vorhandene Tabelle" ausgewählt
                    String[] existingTables = getExistingTables();
                    if (existingTables.length > 0) {
                        // Zeige einen Dialog an, in dem der Benutzer eine vorhandene Tabelle auswählen kann
                        String tableName = (String) JOptionPane.showInputDialog(frame, "Wählen Sie eine vorhandene Tabelle aus:", "Vorhandene Tabelle", JOptionPane.PLAIN_MESSAGE, null, existingTables, existingTables[0]);
                        if (tableName != null && !tableName.isEmpty()) {
                            // Eine gültige Tabellenbezeichnung wurde ausgewählt
                            writeToDatabases(tableName, knotenVector);
                        }
                    } else {
                        // Es gibt keine vorhandenen Tabellen
                        JOptionPane.showMessageDialog(frame, "Es sind keine vorhandenen Tabellen verfügbar.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        sqlMenu.add(updateTableMenuItem);
        frame.setJMenuBar(menueBar);

        /**
         * initialisieren ein neues GridBagLayout und setzen es als Layout-Manager des JFrame frame.
         * Ein GridBagConstraints-Objekt erstellt, welches später verwendet wird, um die Positionierung und Größe der Komponenten im GridBagLayout zu steuern.
         */
        GridBagLayout layout = new GridBagLayout();
        frame.setLayout(layout);
        GridBagConstraints c = new GridBagConstraints();

        // Label Nummer
        apNummerLabel = new JLabel("Nummer:");
        apNummerLabel.setForeground(Color.WHITE);
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(10, 10, 0, 0);
        frame.add(apNummerLabel, c);

        // Textfield Nummer
        apNummerFeld = new JTextField(20);
        // hier ändern wir die Textart und setzten den Text auf dick mit der größe 14
        apNummerFeld.setFont(new Font("Nuance", Font.BOLD, 14));
        apNummerFeld.setBackground(Color.BLACK);
        // Erstellen ein FocusListener der die Textfeldfarbe auf Cyan setzt sobald wir drauf klicken(es fokussieren)
        // Und die Schriftfarbe auf Schwarz setzen
        // Verliert das Textfeld den Fokus so setzten wir die Textfeldfarbe wieder auf Schwarz und die Schriftfarbe wieder auf Weiß
        apNummerFeld.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                apNummerFeld.setBackground(Color.CYAN);
                apNummerFeld.setForeground(Color.BLACK);
            }

            @Override
            public void focusLost(FocusEvent e) {
                apNummerFeld.setBackground(Color.BLACK);
                apNummerFeld.setForeground(Color.WHITE);
            }
        });
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 0, 10);
        frame.add(apNummerFeld, c);

        // Label Beschreibung
        apBeschreibungLabel = new JLabel("Beschreibung:");
        apBeschreibungLabel.setForeground(Color.WHITE);
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(10, 10, 0, 0);
        frame.add(apBeschreibungLabel, c);

        // Textarea Beschreibung
        apBeschreibungFeld = new JTextArea(5, 20);
        // hier ändern wir die Textart und setzten den Text auf dick mit der größe 14
        apBeschreibungFeld.setFont(new Font("Nuance", Font.BOLD, 14));
        apBeschreibungFeld.setBackground(Color.BLACK);
        // Erstellen ein FocusListener der die Textfeldfarbe auf Cyan setzt sobald wir drauf klicken(es fokussieren)
        // Und die Schriftfarbe auf Schwarz setzen
        // Verliert das Textfeld den Fokus so setzten wir die Textfeldfarbe wieder auf Schwarz und die Schriftfarbe wieder auf Weiß
        apBeschreibungFeld.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                apBeschreibungFeld.setBackground(Color.CYAN);
                apBeschreibungFeld.setForeground(Color.BLACK);
            }

            @Override
            public void focusLost(FocusEvent e) {
                apBeschreibungFeld.setBackground(Color.BLACK);
                apBeschreibungFeld.setForeground(Color.WHITE);
            }
        });
        apBeschreibungFeld.setLineWrap(true);
        apBeschreibungFeld.setWrapStyleWord(true);
        apBeschreibungScrollPane = new JScrollPane(apBeschreibungFeld);
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(10, 10, 0, 10);
        frame.add(apBeschreibungScrollPane, c);

        // Label Dauer
        apDauerLabel = new JLabel("Dauer:");
        apDauerLabel.setForeground(Color.WHITE);
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(10, 10, 0, 0);
        frame.add(apDauerLabel, c);

        // TextField Dauer
        apDauerFeld = new JTextField(20);
        // hier ändern wir die Textart und setzten den Text auf dick mit der größe 14
        apDauerFeld.setFont(new Font("Nuance", Font.BOLD, 14));
        apDauerFeld.setBackground(Color.BLACK);
        // Erstellen ein FocusListener der die Textfeldfarbe auf Cyan setzt sobald wir drauf klicken(es fokussieren)
        // Und die Schriftfarbe auf Schwarz setzen
        // Verliert das Textfeld den Fokus so setzten wir die Textfeldfarbe wieder auf Schwarz und die Schriftfarbe wieder auf Weiß
        apDauerFeld.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                apDauerFeld.setBackground(Color.CYAN);
                apDauerFeld.setForeground(Color.BLACK);
            }

            @Override
            public void focusLost(FocusEvent e) {
                apDauerFeld.setBackground(Color.BLACK);
                apDauerFeld.setForeground(Color.WHITE);
            }
        });
        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(10, 10, 0, 10);
        frame.add(apDauerFeld, c);

        // Label Vorgänger
        apVorLabel = new JLabel("Vorgänger:");
        apVorLabel.setForeground(Color.WHITE);
        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(10, 10, 0, 0);
        frame.add(apVorLabel, c);

        // TextField Vorgänger
        apVorFeld = new JTextField(20);
        // hier ändern wir die Textart und setzten den Text auf dick mit der größe 14
        apVorFeld.setFont(new Font("Nuance", Font.BOLD, 14));
        apVorFeld.setBackground(Color.BLACK);
        // Erstellen ein FocusListener der die Textfeldfarbe auf Cyan setzt sobald wir drauf klicken(es fokussieren)
        // Und die Schriftfarbe auf Schwarz setzen
        // Verliert das Textfeld den Fokus so setzten wir die Textfeldfarbe wieder auf Schwarz und die Schriftfarbe wieder auf Weiß
        apVorFeld.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                apVorFeld.setBackground(Color.CYAN);
                apVorFeld.setForeground(Color.BLACK);
            }

            @Override
            public void focusLost(FocusEvent e) {
                apVorFeld.setBackground(Color.BLACK);
                apVorFeld.setForeground(Color.WHITE);
            }
        });
        c.gridx = 1;
        c.gridy = 3;
        c.insets = new Insets(10, 10, 0, 10);
        frame.add(apVorFeld, c);

        /**
         * Der Code erstellt einen JButton namens "knotenErstellenButton". Dieser Button hat eine schwarze Hintergrundfarbe und eine blaue Vordergrundfarbe.
         * Der Button wird anschließend an das JFrame-Objekt "frame" angefügt und mit Hilfe des GridBagLayouts auf das Layout positioniert.
         *
         * Es werden außerdem ActionEvents für den Button erstellt,
         * die ausgelöst werden, wenn der Button gedrückt wird.
         * Wenn der Button gedrückt wird, werden verschiedene Eingabefelder ausgelesen,
         * um einen neuen Knoten zu erstellen und zu einem Knotenvektor hinzuzufügen.
         * Anschließend wird eine neue Zeile zur Tabelle des JTable-Objekts hinzugefügt,
         * um den neuen Knoten anzuzeigen, und das Layout wird aktualisiert.
         * Schließlich werden auch die Werte des kritischen Pfads und des Gesamtdauerns aktualisiert,
         * bevor das JFrame-Objekt neu gezeichnet wird.
         */
        knotenErstellenButton = new JButton("Knoten erstellen");
        knotenErstellenButton.setBackground(Color.BLACK);
        knotenErstellenButton.setForeground(Color.BLUE);
        c.gridx = 1;
        c.gridy = 4;
        c.gridheight = 6;
        c.insets = new Insets(10, 10, 10, 10);
        // Fügt einen ActionListener hinzu, der ausgeführt wird, wenn der Knoten-Erstellen-Button geklickt wird.
        knotenErstellenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int nummer = Integer.parseInt(apNummerFeld.getText());
                double dauer = Double.parseDouble(apDauerFeld.getText());
                String beschreibung = apBeschreibungFeld.getText();
                Vector<Knoten> vorgaenger = new Vector<Knoten>();
                Vector<Knoten> nachfolger = new Vector<Knoten>();
                Vector<Knoten> kritischerPfad = new Vector<Knoten>();
                String[] vorgaengerNummern = apVorFeld.getText().split(",");

                /**
                 * Erstellt eine Reihe von Variablen und iteriert über die vorgängerNummern,
                 * um den Vector der Vorgänger-Knoten zu erstellen.
                 * Wenn kein Vorgänger gefunden wurde und die Nummer nicht "0" ist, wird eine JOptionPane-Meldung ausgegeben.
                 */
                for (String vorgNummer : vorgaengerNummern) {
                    boolean vorgaengerGefunden = false;
                    for (Knoten k : knotenVector) {
                        if (k.getNummer() == Integer.parseInt(vorgNummer)) {
                            vorgaenger.add(k);
                            vorgaengerGefunden = true;
                            k.setKritisch(true);
                            break;
                        }
                    }
                    if (!vorgaengerGefunden && !vorgNummer.equals("0")) {
                        JOptionPane.showMessageDialog(null, "Wenn es keinen Vorgänger gibt, trage bitte eine 0 ein");
                    }
                }
                // Erstellt einen neuen Knoten mit den angegebenen Parametern und fügt ihn dem Knoten-Vektor hinzu und
                // Überprüfen gleich ob der Knoten kritisch ist.
                Knoten knoten = new Knoten(nummer, beschreibung, dauer, vorgaenger);
                knoten.setNummer(Integer.parseInt(apNummerFeld.getText()));
                knoten.setDauer(Double.parseDouble(apDauerFeld.getText()));
                knoten.setBeschreibung(apBeschreibungFeld.getText());
                knoten.setVorgaenger(vorgaenger);
                if (knoten.isKritisch()) {
                    kritischerPfad.add(knoten);
                }
                knotenVector.add(knoten);
                // Geben die Werte erstmal bis zum Nachfolger an die Tabelle weiter
                model.addRow(new Object[]{knoten.getNummer(), knoten.getBeschreibung(), knoten.getDauer(), vorgaenger, nachfolger});
                model.fireTableDataChanged();
                // Rufen die updateTableModel() Methode auf, die unsere Tabelle aktualisiert
                updateTableModel();
                // Rufen die berechneWerte() Methode auf, um die Werte aus unserem Hauptknoten knotenVector auszurechnen
                berechneWerte(knotenVector);
                // Rufen die refresh() Methode auf, die unsere Textfelder wieder leert
                refresh();
            }
        });
        frame.add(knotenErstellenButton, c);

        // Erstellen unsere Tabelle
        model = new DefaultTableModel();
        knotenTable = new JTable(model);
        model.addColumn("ApNr.");
        model.addColumn("Beschreibung");
        model.addColumn("Dauer");
        model.addColumn("Vorgänger");
        model.addColumn("Nachfolger");
        model.addColumn("FAZ");
        model.addColumn("FEZ");
        model.addColumn("SAZ");
        model.addColumn("SEZ");
        model.addColumn("Freier Puffer");
        model.addColumn("Gesamt Puffer");
        model.addColumn("Krit.");
        knotenTable.setBackground(Color.BLACK);
        knotenTable.setForeground(Color.WHITE);
        knotenTable.setPreferredScrollableViewportSize(new Dimension(1200, 400));
        knotenTable.setFillsViewportHeight(true);

        // Machen unsere Tabelle scrollbar
        JScrollPane tableScrollPane = new JScrollPane(knotenTable);
        c.gridx = 2;
        c.gridy = 0;
        c.gridheight = 10;
        c.insets = new Insets(10, 10, 10, 10);
        frame.add(tableScrollPane, c);

        /**
         * Erstellen den alleKnotenLoschenButton der alle Werte in unserem Hauptknoten knotenVector löscht und
         * die Tabelle aktualisiert
         */
        alleKnotenLoschenButton = new JButton("Alle Knoten löschen");
        alleKnotenLoschenButton.setForeground(Color.RED);
        alleKnotenLoschenButton.setBackground(Color.BLACK);
        alleKnotenLoschenButton.setFocusPainted(false);
        c.gridx = 1;
        c.gridy = 11;
        c.gridheight = 1;
        c.insets = new Insets(0, 10, 10, 10);
        alleKnotenLoschenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                knotenVector.clear();
                model.setRowCount(0);
            }
        });
        frame.add(alleKnotenLoschenButton, c);

        /**
         * Erstellen den knotenLoschenButton der verlangt, dass man eine Zeile in der Tabelle auswählt
         * und bei dem Klick auf den Button löscht
         */
        knotenLoschenButton = new JButton("Knoten löschen");
        knotenLoschenButton.setForeground(Color.RED);
        knotenLoschenButton.setBackground(Color.BLACK);
        knotenLoschenButton.setFocusPainted(false);
        c.gridx = 2;
        c.gridy = 11;
        c.gridheight = 1;
        c.insets = new Insets(0, 10, 10, 10);
        knotenLoschenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = knotenTable.getSelectedRow();
                // Überprüfe, ob eine Zeile ausgewählt wurde
                if (selectedRow != -1) {
                    // Lösche den Knoten aus dem Vector
                    knotenVector.remove(selectedRow);
                    // Lösche die Zeile aus der Tabelle
                    model.removeRow(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(null, "Bitte wähle einen Knoten aus der Tabelle aus, den du löschen möchtest und bestätige nochmal den Button Löschen");
                }
            }
        });
        frame.add(knotenLoschenButton, c);
        // JFrame anzeigen
        frame.setVisible(true);
    }

    // Erstellen der Methode refresh() die einfach alle Textfelder leert
    public void refresh() {
        apNummerFeld.setText("");
        apBeschreibungFeld.setText("");
        apDauerFeld.setText("");
        apVorFeld.setText("");
    }

    /**
     * Erstellen die Methode updateTableModel,
     * die aktualisiert das TableModel, das die Daten für die JTable enthält.
     * Die Methode durchläuft jeden Knoten im knotenVector und fügt dann eine
     * neue Zeile mit Informationen zu diesem Knoten zum Model hinzu.
     * Dabei werden die Nummern der Vorgänger und Nachfolger des Knotens
     * in separaten Strings gespeichert, die später zusammen mit anderen Daten
     * zum Model hinzugefügt werden. Nachdem alle Zeilen zum Model hinzugefügt wurden,
     * wird das TableModel aktualisiert.
     */
    public void updateTableModel() {
        // Setze die Anzahl der Zeilen im Model auf 0
        model.setRowCount(0);
        // Für jeden Knoten in knotenVector:
        for (Knoten knoten : knotenVector) {
            // Initialisiere vorgaenger und nachfolger als leere Strings
            String vorgaenger = "";
            String nachfolger = "";
            // Füge die Nummer jedes Nachfolgers von Knoten zu nachfolger hinzu
            for (Knoten p : knoten.getNachfolger()) {
                nachfolger += p.getNummer() + " | ";
            }
            // Füge die Nummer jedes Vorgängers von Knoten zu vorgaenger hinzu
            for (Knoten k : knoten.getVorgaenger()) {
                vorgaenger += k.getNummer() + " | ";
            }
            // Füge eine neue Zeile mit den Daten des Knotens zum Model hinzu
            model.addRow(new Object[]{knoten.getNummer(), knoten.getBeschreibung(), knoten.getDauer(), vorgaenger, nachfolger, knoten.getFAZ(), knoten.getFEZ(), knoten.getSAZ(), knoten.getSEZ(), knoten.getFreiePuffer(), knoten.getGesamtPuffer(), knoten.isKritisch()});
            // Aktualisiere das TableModel
            model.fireTableDataChanged();
        }
    }

    // Erstellen die berechneWerte() Methode
    public void berechneWerte(Vector<Knoten> knotenVector) {
        // Berechnen Sie die frühesten Anfangszeiten (FAZ)
        for (Knoten knoten : knotenVector) {
            knoten.getFAZ();
        }
        // Berechnen Sie die frühesten Endzeiten (FEZ)
        for (Knoten knoten : knotenVector) {
            knoten.getFEZ();
        }
        // Berechnen Sie die spätesten Endzeiten (SEZ)
        for (Knoten knoten : knotenVector) {
            knoten.getSEZ();
        }
        // Berechnen Sie die spätesten Anfangszeiten (SAZ)
        for (Knoten knoten : knotenVector) {
            knoten.getSAZ();
        }
        // Berechnen Sie die freien Puffer
        for (Knoten knoten : knotenVector) {
            knoten.getFreiePuffer();
        }
        // Berechnen Sie die gesammte Puffer
        for (Knoten knoten : knotenVector) {
            knoten.getGesamtPuffer();
        }
    }

    /**
     * Öffnet einen Dateiauswahldialog, um den Netzplan unter einem bestimmten Namen abzuspeichern.
     * Die Datei wird als binäre Datei im .dat Format gespeichert.
     * Dabei wird ein Zeitstempel an den Dateinamen angehängt, um eine eindeutige Benennung zu gewährleisten.
     * Im Fehlerfall wird eine RuntimeException geworfen.
     *
     * @throws FileNotFoundException falls die Datei nicht gefunden werden kann
     *
     * Hier noch etwas genauer erklärt:
     *
     * JFileChooser: Ein Dialog zur Auswahl einer Datei oder eines Verzeichnisses.
     * fileChooser.setDialogTitle("Speichern unter"): Legt den Dialogtitel fest.
     * fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY): Legt fest, dass nur Dateien ausgewählt werden können.
     * userSelection == JFileChooser.APPROVE_OPTION: Überprüft, ob der Nutzer den Dialog bestätigt hat.
     * File fileToSave = fileChooser.getSelectedFile(): Holt den ausgewählten Dateipfad.
     * SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss"): Ein Formatter-Objekt, um einen Zeitstempel im Format "Jahr_Monat_Tag__Stunde_Minute_Sekunde" zu erstellen.
     * FileOutputStream fos = new FileOutputStream(filePath): Öffnet eine Verbindung zu der ausgewählten Datei zum Schreiben.
     * ObjectOutputStream oos = new ObjectOutputStream(fos): Schreibt Objekte in eine binäre Datei.
     * List<Knoten> vs = new Vector<Knoten>(knotenVector): Erstellt eine Kopie der knotenVector Liste, um sie in die Datei zu schreiben.
     * JOptionPane.showMessageDialog(null, "Netzplan erfolgreich gespeichert"): Zeigt eine Erfolgsmeldung an, wenn der Netzplan erfolgreich gespeichert wurde.
     */
    public void speichernUnter() throws FileNotFoundException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Speichern unter");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // Dialog zur Bestätigung des Speicherorts anzeigen
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            // Speicherort auswählen und Dateinamen erzeugen
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss");
            String timestamp = sdf.format(new Date());
            filePath = filePath + "__" + timestamp + ".dat";
            // Netzplan-Daten in eine binäre Datei schreiben
            try (FileOutputStream fos = new FileOutputStream(filePath);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                List<Knoten> vs = new Vector<Knoten>(knotenVector);
                oos.writeObject(vs);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            JOptionPane.showMessageDialog(null, "Netzplan erfolgreich gespeichert");
        }
    }

    /**
     * Die Methode dateiOeffnen() öffnet eine zuvor gespeicherte Datei
     * und liest ein Objekt vom Typ Vector<Knoten> aus der Datei.
     * Der gelesene Vector wird anschließend der Liste knotenVector hinzugefügt.
     */
    public void dateiOeffnen() {
        // Öffne den Dateiauswahldialog
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            // Hole die ausgewählte Datei
            File file = chooser.getSelectedFile();
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                // Lese das Objekt vom Typ Vector<Knoten> aus der Datei
                Vector<Knoten> v = (Vector<Knoten>) ois.readObject();
                // Füge den gelesenen Vector der Liste knotenVector hinzu
                knotenVector.addAll(v);
            } catch (IOException | ClassNotFoundException e) {
                // Gib eine Fehlermeldung aus, wenn ein Fehler auftritt
                e.printStackTrace();
            }
        }
    }

    /**
     * Die Methode writeToDatabases schreibt die Daten in eine H2-Datenbank,
     * die auf dem lokalen Rechner installiert ist.
     * Die Methode erhält den Namen der Tabelle und einen Vector von Knoten-Objekten als Eingabe.
     *
     * Zunächst wird eine Verbindung zur Datenbank hergestellt.
     * Dann wird überprüft, ob die Tabelle bereits existiert.
     * Wenn dies der Fall ist, wird die Tabelle gelöscht.
     * Anschließend wird eine neue Tabelle mit dem gegebenen Namen erstellt.
     * Die Tabelle besteht aus Spalten, die den Eigenschaften der Knoten-Objekte entsprechen.
     *
     * Dann werden die Daten aus dem Vector von Knoten-Objekten extrahiert und in die Datenbanktabelle eingefügt.
     * Hierbei werden die Vorgänger und Nachfolger jedes Knotens in CSV-Strings konvertiert und in der entsprechenden Spalte gespeichert.
     * Die Methode schließt die Verbindung zur Datenbank und zeigt eine Erfolgsmeldung an, wenn alles erfolgreich abgeschlossen wurde.
     *
     * Wenn während des Vorgangs ein Fehler auftritt, wird eine Fehlermeldung mit der Ursache des Fehlers angezeigt.
     * @param tableName
     * @param knotenVector
     */
    public static void writeToDatabases(String tableName, Vector<Knoten> knotenVector) {
        // JDBC URL, Benutzername und Passwort für den Datenbankzugriff
        String jdbcUrl = "jdbc:h2:~/test";
        String username = "sa";
        String password = "Lemonensaft90";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement()) {
            // Metadata-Objekt abrufen und Tabelle mit dem angegebenen Namen suchen
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet tables = meta.getTables(null, null, tableName, null);
            if (tables.next()) {
                // Tabelle löschen, falls sie bereits existiert
                statement.execute("DROP TABLE " + tableName);
            }

            // Tabelle erstellen, falls sie noch nicht existiert
            statement.execute("CREATE TABLE " + tableName + " " +
                    "(nummer INT PRIMARY KEY, beschreibung VARCHAR(255), dauer DOUBLE, vorgaenger VARCHAR(255), nachfolger VARCHAR(255), faz DOUBLE, fez DOUBLE, saz DOUBLE, sez DOUBLE, freiePuffer DOUBLE, gesamtPuffer DOUBLE, kritisch BOOLEAN)");
            // Daten in die Tabelle schreiben
            for (Knoten knoten : knotenVector) {
                // Attribute des Knotens extrahieren
                int nummer = knoten.getNummer();
                String beschreibung = knoten.getBeschreibung();
                double dauer = knoten.getDauer();
                double faz = knoten.getFAZ();
                double fez = knoten.getFEZ();
                double saz = knoten.getSAZ();
                double sez = knoten.getSEZ();
                double freiePuffer = knoten.getFreiePuffer();
                double gesamtPuffer = knoten.getGesamtPuffer();
                boolean kritisch = knoten.isKritisch();
                Vector<Knoten> vorgaenger = knoten.getVorgaenger();
                Vector<Knoten> nachfolger = knoten.getNachfolger();

                // Konvertiere Vorgänger und Nachfolger in CSV-Strings
                String vorgaengerCSV = "";
                String nachfolgerCSV = "";
                for (Knoten k : vorgaenger) {
                    vorgaengerCSV += k.getNummer() + ",";
                }
                for (Knoten k : nachfolger) {
                    nachfolgerCSV += k.getNummer() + ",";
                }
                // Entferne das letzte Komma
                if (vorgaengerCSV.length() > 0) {
                    vorgaengerCSV = vorgaengerCSV.substring(0, vorgaengerCSV.length() - 1);
                }
                if (nachfolgerCSV.length() > 0) {
                    nachfolgerCSV = nachfolgerCSV.substring(0, nachfolgerCSV.length() - 1);
                }

                // Füge Daten in die Tabelle ein
                String sql = "INSERT INTO " + tableName + " (nummer, beschreibung, dauer, vorgaenger, nachfolger, FAZ, FEZ, SAZ, SEZ, freiePuffer, gesamtPuffer, kritisch) " +
                        "VALUES (" + nummer + ", '" + beschreibung + "', " + dauer + ", '" + vorgaengerCSV + "', '" + nachfolgerCSV + "', '" + faz + "', '" + fez + "', '" + saz + "', '" + sez + "', '" + freiePuffer + "', '" + gesamtPuffer + "', '" + kritisch + "')";
                statement.executeUpdate(sql);
            }
            // Gibt an wen erfolgreich erstellt
            JOptionPane.showMessageDialog(null, "Tabelle erfolgreich erstellt");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
