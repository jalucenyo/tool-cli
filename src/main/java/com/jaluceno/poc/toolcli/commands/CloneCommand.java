package com.jaluceno.poc.toolcli.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

@Command(name = "clone", mixinStandardHelpOptions = true, version = "0.1",
        description = "clone git projects")
public class CloneCommand extends AbstractCommand {

    @Option(names = { "-u", "--user" }, description = "User by git server", required = true, interactive = true)
    private String gitUser;

    @Option(names = { "-p", "--password"}, description = "Password by git server", required = true, interactive = true)
    private char[] password;

    public Integer call() throws Exception {

        System.out.printf("Config File: %s\n", configFilePath);

        getRepositories(configFilePath)
                .forEach(repository -> {
                    System.out.println("Clone: " + repository.toString());
                    gitClone(
                            getUrlRepository(repository),
                            getTargetFolder(repository),
                            gitUser, String.copyValueOf(password));
                });

        return 0;
    }

    private String getUrlRepository(JsonElement repository){
        return repository.getAsJsonObject().get("url").getAsString();
    }

    private String getTargetFolder(JsonElement repository){
       return "services" + File.separator + repository.getAsJsonObject().get("folder").getAsString();
    }

    private JsonArray getRepositories(String configFilePath) throws FileNotFoundException {
        return JsonParser.parseReader(new FileReader(configFilePath)).getAsJsonObject()
                .get("clone").getAsJsonObject().get("repositories").getAsJsonArray();
    }

    private void gitClone(String urlRepository, String targetDirectory, String user, String password){
        Git.cloneRepository()
                .setURI(urlRepository)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(user, password))
                .setDirectory(new File(targetDirectory))
                .setProgressMonitor(new TextProgressMonitor())
                .call();
    }

    public static void main(String... args){
        int exitCode = new CommandLine(new CloneCommand()).execute(args);
        System.exit(exitCode);
    }

}
