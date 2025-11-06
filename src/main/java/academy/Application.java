package academy;

import academy.cli.GenerateCommand;
import academy.cli.SolveCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
        name = "maze-app",
        version = "1.0",
        description = "Maze generator and solver CLI application.",
        mixinStandardHelpOptions = true,
        subcommands = {GenerateCommand.class, SolveCommand.class})
public class Application implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private static final ObjectReader YAML_READER =
            new ObjectMapper(new YAMLFactory()).findAndRegisterModules().reader();

    // Hidden legacy/demo options preserved but not shown in help
    @Option(
            names = {"-s", "--font-size"},
            description = "Font size",
            hidden = true)
    int fontSize;

    @Parameters(paramLabel = "<word>", description = "Words to be processed.", hidden = true)
    private String[] words;

    @Option(
            names = {"-c", "--config"},
            description = "Path to YAML config file",
            hidden = true)
    private File configPath;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Application()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        // Optionally load legacy/demo config (hidden options)
        try {
            var config = loadConfig();
            LOGGER.atInfo().addKeyValue("config", config).log("Config content");
        } catch (RuntimeException ex) {
            LOGGER.warn("Config load skipped/failed: {}", ex.toString());
        }
        // Root command just shows help when no subcommand provided
        new CommandLine(this).usage(System.out);
    }

    private AppConfig loadConfig() {
        if (configPath == null)
            return new AppConfig(fontSize, words == null ? Collections.emptyList() : Arrays.asList(words));
        try {
            return YAML_READER.readValue(configPath, AppConfig.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
