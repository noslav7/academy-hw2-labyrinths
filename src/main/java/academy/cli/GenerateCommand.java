package academy.cli;

import academy.maze.DefaultGeneratorFactory;
import academy.maze.Generator;
import academy.maze.GeneratorFactory;
import academy.maze.dto.Maze;
import academy.maze.dto.TerrainType;
import academy.maze.util.Mazes;
import academy.util.MazeIO;
import academy.util.MazeRenderer;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Option;

@Command(name = "generate", description = "Generate a maze with specified algorithm and dimensions.")
public class GenerateCommand implements Callable<Integer> {

    private final GeneratorFactory generatorFactory;

    public GenerateCommand() {
        this(new DefaultGeneratorFactory());
    }

    private GenerateCommand(GeneratorFactory generatorFactory) {
        this.generatorFactory = Objects.requireNonNull(generatorFactory, "generatorFactory");
    }

    @Option(
            names = {"-a", "--algorithm"},
            required = true)
    private String algorithm;

    @Option(
            names = {"-w", "--width"},
            required = true)
    private int width;

    // Note: short name -h is intended to be height for this subcommand
    @Option(
            names = {"-h", "--height"},
            required = true)
    private int height;

    @Option(names = {"-o", "--output"})
    private File output;

    @Option(names = "--unicode", description = "Render maze using Unicode pseudographics")
    private boolean unicode;

    @Override
    public Integer call() {
        try {
            Generator generator = generatorFactory.create(algorithm);

            Maze maze = generator.generate(width, height);
            MazeRenderer.GlyphStyle style = unicode ? MazeRenderer.GlyphStyle.UNICODE : MazeRenderer.GlyphStyle.ASCII;
            Maze displayMaze = unicode ? maze : Mazes.withUniformTerrain(maze, TerrainType.NORMAL);
            String text = MazeRenderer.renderMaze(displayMaze, style, true);

            if (output != null) {
                MazeIO.write(output, text);
            } else {
                System.out.print(text);
            }
            return ExitCode.OK;
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return ExitCode.USAGE;
        } catch (Exception e) {
            System.err.println("Failed to generate maze: " + e.getMessage());
            return ExitCode.SOFTWARE;
        }
    }
}
