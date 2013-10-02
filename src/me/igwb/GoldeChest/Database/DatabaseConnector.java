package me.igwb.GoldeChest.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import me.igwb.GoldenChest.Plugin;

public class DatabaseConnector {

    private Plugin parentPlugin;

    public DatabaseConnector(final Plugin parent)  {

        try {
            parentPlugin = parent;

            Class.forName("org.sqlite.JDBC");
            initialize();

        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        }
    }

    private void initialize() {

        String dbLocation;
        Connection conn = null;
        ResultSet rs = null;

        try {

            dbLocation = parentPlugin.getDbPath();

            conn = DriverManager.getConnection("jdbc:sqlite:" + dbLocation);
            Statement stat = conn.createStatement();


            stat.executeUpdate("PRAGMA foreign_keys = ON;");
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS Players (Id INTEGER PRIMARY KEY, Name TEXT NOT NULL UNIQUE, X INTEGER, Y INTEGER, Z INTEGER, OverflowAmount FLOAT);");

        } catch (SQLException e) {
            //TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
