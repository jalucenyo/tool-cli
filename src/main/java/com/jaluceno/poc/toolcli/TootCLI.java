package com.jaluceno.poc.toolcli;

import com.jaluceno.poc.toolcli.commands.*;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(mixinStandardHelpOptions = true, version = "0.1",
        subcommands = {
                InitCommand.class,
                CreateCommand.class,
                CloneCommand.class,
                StartCommand.class,
                DatabaseCommand.class
        })
public class TootCLI {

    public static void main(String... args) {
        int exitCode = new CommandLine(new TootCLI()).execute(args);
        System.exit(exitCode);
    }

}
