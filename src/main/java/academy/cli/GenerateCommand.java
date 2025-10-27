package academy.cli;

import academy.maze.Generator;
import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.impl.DepthFirstGenerator;
import academy.maze.impl.PrimGenerator;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "generate", description = "Generate a maze with specified algorithm and dimensions.")
public class GenerateCommand implements Runnable {

    @Option(names = {"-a", "--algorithm"}, required = true)
    private String algorithm;

    @Option(names = {"-w", "--width"}, required = true)
    private int width;

    // Note: short name -h is intended to be height for this subcommand
    @Option(names = {"-h", "--height"}, required = true)
    private int height;

    @Option(names = {"-o", "--output"})
    private File output;

    @Override
    public void run() {
        Generator generator = switch (algorithm.toLowerCase()) {
            case "dfs" -> new DepthFirstGenerator();
            case "prim" -> new PrimGenerator();
            default -> throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
        };

        Maze maze = generator.generate(width, height);
        String text = renderWithBorder(maze);

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

    private String renderWithBorder(Maze maze) {
        int w = maze.cells()[0].length;
        int h = maze.cells().length;
        StringBuilder sb = new StringBuilder();
        // top border
        sb.append("#".repeat(w + 2)).append('\n');
        for (int y = 0; y < h; y++) {
            sb.append('#');
            for (int x = 0; x < w; x++) {
                sb.append(maze.cells()[y][x] == CellType.WALL ? '#' : ' ');
            }
            sb.append('#').append('\n');
        }
        // bottom border
        sb.append("#".repeat(w + 2)).append('\n');
        return sb.toString();
    }
}


