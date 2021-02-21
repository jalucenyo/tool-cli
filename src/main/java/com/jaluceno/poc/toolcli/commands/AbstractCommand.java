package com.jaluceno.poc.toolcli.commands;

import picocli.CommandLine.Option;

import java.io.File;
import java.util.concurrent.Callable;

public abstract class AbstractCommand implements Callable<Integer> {

    @Option(names = { "-c", "--config"}, description = "File configuration")
    protected String configFilePath = "config" + File.separator + "config.json";


}
