package me.igwb.GoldenChest.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Location;

import java.sql.PreparedStatement;

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

        Connection conn = null;
        ResultSet rs = null;

        try {

            conn = getConnection();
            Statement stat = conn.createStatement();


            stat.executeUpdate("PRAGMA foreign_keys = ON;");
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS Players (Id INTEGER PRIMARY KEY, Name TEXT NOT NULL UNIQUE, Chests BLOB, OverflowAmount FLOAT);");
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS Chests (Id INTEGER PRIMARY KEY, Owner TEXT NOT NULL, World STRING, X INTEGER, Y INTEGER, Z INTEGER);");

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

    private Connection getConnection() {

        try {

            String dbLocation;

            dbLocation = parentPlugin.getDbPath();

            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbLocation);

            return conn;
        } catch (SQLException e) {
            parentPlugin.logSevere(e.getMessage());
            return null;
        }
    }


    public DBAddResult addPlayer(String name) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            con = getConnection();

            st = con.createStatement();

            rs = st.executeQuery("SELECT * FROM Players WHERE Name= \'" + name + "\';");

            //Check if the player is already in the database
            if (rs.next()) {

                return DBAddResult.exists;
            } else {

                st.execute("INSERT INTO Players(Name) Values(" + "\'" + name + "\'" + ");");
                return DBAddResult.success;
            }

        } catch (SQLException e) {
            parentPlugin.logSevere(e.getMessage());
            return DBAddResult.error;
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                parentPlugin.logSevere(e.getMessage());
            }
        }
    }

    public float getOveflowAmount(String player) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            con = getConnection();

            st = con.createStatement();

            rs = st.executeQuery("SELECT OverflowAmount FROM Players WHERE Name= \'" + player + "\';");

            //Check if the player is in the database
            if (rs.next()) {

                return rs.getFloat(0);
            } else {

                return 0;
            }

        } catch (SQLException e) {
            parentPlugin.logSevere(e.getMessage());
            return 0;
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                parentPlugin.logSevere(e.getMessage());
            }
        }
    }

    public DBAddResult addChest(String owner, Location chestLocation) {

        Connection con = null;
        Statement st = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT * FROM Chests WHERE Owner= \'" + owner + "\';");
            parentPlugin.logMessage("selected");

            //Check if the chest is already in the database
            while (rs.next()) {

                if (rs.getString(3).equals(chestLocation.getWorld().getName()) && rs.getInt(4) == chestLocation.getBlockX() && rs.getInt(5) == chestLocation.getBlockY() && rs.getInt(6) == chestLocation.getBlockZ()) {
                    parentPlugin.logMessage("match");
                    return DBAddResult.exists;
                }
                parentPlugin.logMessage("checked");
            }

            parentPlugin.logMessage("start insert");

            pst = con.prepareStatement("INSERT INTO Chests(Owner, World, X, Y, Z) Values (?,?,?,?,?);");
            pst.setString(1, owner);
            pst.setString(2, chestLocation.getWorld().getName());
            pst.setInt(3, chestLocation.getBlockX());
            pst.setInt(4, chestLocation.getBlockY());
            pst.setInt(5, chestLocation.getBlockZ());

            pst.execute();

            parentPlugin.logMessage("inserted");

            return DBAddResult.success;

        } catch (SQLException e) {
            parentPlugin.logSevere(e.getMessage());
            return DBAddResult.error;
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                parentPlugin.logSevere(e.getMessage());
            }
        }

    }

    public void removeChest(String owner, Location chestLocation) {

    }

}
