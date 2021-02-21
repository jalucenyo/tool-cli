package com.jaluceno.poc.toolcli.commands;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jaluceno.poc.toolcli.utils.StreamGobbler;
import org.apache.commons.io.FileUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.nio.charset.StandardCharsets.*;

@Command(name = "start", mixinStandardHelpOptions = true, version = "0.1",
        description = "start container by developer environment")
public class StartCommand implements Callable<Integer> {

    private final String DOCKER_COMPOSE_OUTPUT = "docker-compose.yml";
    private final String TEMPLATES_CONTAINERS_FOLDER = "containers";

    @Option(names = { "-c", "--config"}, description = "File configuration")
    private String configFilePath = "config" + File.separator + "config.json";

    @Option(names = "-s")
    boolean minimal;

    @Override
    public Integer call() throws Exception {

        generateDockerComposeFile(getContainers());

        String currentFolder = System.getProperty("user.dir");

        ProcessBuilder builder = new ProcessBuilder();
        builder.command("docker-compose", "up", "-d", "--force-recreate");
        builder.directory(new File(currentFolder));

        Process process = builder.start();
        Executors.newSingleThreadExecutor()
                .submit(new StreamGobbler(process.getInputStream(), process.getErrorStream(),
                        System.out::println));

        return process.waitFor();

    }

    private void generateDockerComposeFile(List<String> containers) throws IOException {

        FileUtils.deleteQuietly(new File(DOCKER_COMPOSE_OUTPUT));

        FileUtils.writeLines(new File(DOCKER_COMPOSE_OUTPUT),
            FileUtils.readLines(new File(TEMPLATES_CONTAINERS_FOLDER,
                    "docker-compose-base.yml"),UTF_8), true);

        for (String container: containers) {
            List<String> lines = FileUtils.readLines(new File(TEMPLATES_CONTAINERS_FOLDER, container), UTF_8);
            FileUtils.writeLines(new File(DOCKER_COMPOSE_OUTPUT),
                    lines.subList(lines.indexOf("services:") + 1, lines.size()), true);
        }

    }

    private List<String> getContainers() throws FileNotFoundException {
        List<String> minimals = getContainersMinimalConfig(configFilePath);

        return Arrays.asList((new File("containers")).list())
            .stream()
            .filter(container -> minimals.stream()
                .anyMatch(miniaml -> container.equalsIgnoreCase("docker-compose-" + miniaml + ".yml")))
            .collect(Collectors.toList());
    }

    private List<String> getContainersMinimalConfig(String configFilePath) throws FileNotFoundException {
        return StreamSupport.stream(
            JsonParser.parseReader(new FileReader(configFilePath))
                    .getAsJsonObject()
                    .get("start").getAsJsonObject()
                    .get("minimal").getAsJsonArray().spliterator(), false)
                .map(JsonElement::getAsString)
                .collect(Collectors.toList());
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new StartCommand()).execute(args);
        System.exit(exitCode);
    }

}
