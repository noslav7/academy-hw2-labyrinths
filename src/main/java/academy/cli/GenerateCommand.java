package academy.cli;

import academy.maze.Generator;
import academy.maze.dto.Maze;
import academy.maze.impl.DepthFirstGenerator;
import academy.maze.impl.KruskalGenerator;
import academy.maze.impl.PrimGenerator;
import academy.util.MazeRenderer;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "generate", description = "Generate a maze with specified algorithm and dimensions.")
public class GenerateCommand implements Runnable {

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
    public void run() {
        Generator generator =
                switch (algorithm.toLowerCase()) {
                    case "dfs" -> new DepthFirstGenerator();
                    case "prim" -> new PrimGenerator();
                    case "kruskal" -> new KruskalGenerator();
                    default -> throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
                };

        Maze maze = generator.generate(width, height);
        MazeRenderer.GlyphStyle style = unicode ? MazeRenderer.GlyphStyle.UNICODE : MazeRenderer.GlyphStyle.ASCII;
        String text = MazeRenderer.renderMaze(maze, style, true);

        if (output != null) {
            try {
                if (output.getParentFile() != null) {
                    output.getParentFile().mkdirs();
                }
                Files.writeString(output.toPath(), text, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.print(text);
        }
    }
}
