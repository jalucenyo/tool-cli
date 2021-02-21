package com.jaluceno.poc.toolcli.commands;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "init", mixinStandardHelpOptions = true, version = "0.1",
        description = "Initialize environment project")
public class InitCommand extends AbstractCommand {

    @Override
    public Integer call() throws Exception {
        return null;
    }

    public static void main(String... args){
        int exitCode = new CommandLine(new InitCommand()).execute(args);
        System.exit(exitCode);
    }
}
