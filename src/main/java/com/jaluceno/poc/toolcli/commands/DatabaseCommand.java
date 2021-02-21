package com.jaluceno.poc.toolcli.commands;

import com.google.gson.JsonParser;
import com.jaluceno.poc.toolcli.database.PostgresConnection;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;

@Command(name = "database", mixinStandardHelpOptions = true, version = "0.1",
        description = "Database operations: copy")
public class DatabaseCommand extends AbstractCommand {

    @Override
    public Integer call() throws Exception {

        //String typeDb = getTypeDatabase(configFilePath);

        PostgresConnection.connect("jdbc:postgresql://localhost:5432/db1", "postgres", "password")
            .exportAllTables("sch1");


        PostgresConnection.connect("jdbc:postgresql://localhost:5432/db2", "postgres", "password")
                .importTables("sch1", Collections.singletonList("table1"));

        return 0;
    }

    public static void main(String... args){
        int exitCode = new CommandLine(new DatabaseCommand()).execute(args);
        System.exit(exitCode);
    }

    private String getTypeDatabase(String configFilePath) throws FileNotFoundException {
        return JsonParser.parseReader(new FileReader(configFilePath)).getAsJsonObject()
                .get("database").getAsJsonObject().get("source").getAsString();
    }

}
