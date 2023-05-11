package org.example;

import javax.swing.*;
import java.sql.SQLException;

public class H2JDBCUtilities {

    // Start H2 database server
    public static void startServer() {
        // Erstellt ein Server-Objekt vom Typ org.h2.tools.Server
        org.h2.tools.Server server;
        try {
            // Erstellt einen H2-Datenbankserver und startet ihn
            server = org.h2.tools.Server.createTcpServer().start();
            JOptionPane.showMessageDialog(null, "H2 Server is started");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "H2 Server fail to start");
            return;
        }
    }
}
