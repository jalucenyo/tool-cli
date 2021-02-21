package com.jaluceno.poc.toolcli.commands;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "create", mixinStandardHelpOptions = true, version = "0.1",
        description = "Create by template projects")
public class CreateCommand extends AbstractCommand {

    @Override
    public Integer call() throws Exception {
        return null;
    }

    public static void main(String... args){
        int exitCode = new CommandLine(new CreateCommand()).execute(args);
        System.exit(exitCode);
    }
}
