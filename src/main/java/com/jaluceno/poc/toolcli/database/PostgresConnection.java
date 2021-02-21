package com.jaluceno.poc.toolcli.database;

import org.apache.commons.io.FileUtils;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresConnection {

    private Connection connection;

    public static PostgresConnection connect(String uri, String user, String password) throws SQLException {

        try{
            Class.forName("org.postgresql.Driver");
        }catch (ClassNotFoundException ex){
            System.out.println("Not load postgresql driver.");
        }

        PostgresConnection connection = new PostgresConnection();
        connection.connection = DriverManager.getConnection(uri, user, password);
        return connection;
    }

    public PostgresConnection exportAllTables(String schema) throws SQLException, IOException {
        return exportTables(schema, getTablesBySchema(schema));
    }

    public PostgresConnection importTables(String schema, List<String> tables){

        return this;
    }

    public PostgresConnection exportTables(String schema, List<String> tables) throws SQLException, IOException {
        for (String table: tables) {
            CopyManager copyManager = new CopyManager((BaseConnection) connection);
            copyManager.copyOut("COPY(SELECT * FROM " + schema + "." + table + ") TO STDOUT WITH (FORMAT CSV)",
                    FileUtils.openOutputStream(new File("data/temp", schema + "_" +table + ".csv")));
        }
        return this;
    }

    public List<String> getTablesBySchema(String schema) throws SQLException {

        List<String> tables = new ArrayList<>();

        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tablesRs = metaData.getTables(schema, null, null,
                new String[] { "TABLE" });

        while (tablesRs.next()){
            tables.add(tablesRs.getString("TABLE_NAME"));
        }
        return tables;

    }

    private PostgresConnection() { }
}
