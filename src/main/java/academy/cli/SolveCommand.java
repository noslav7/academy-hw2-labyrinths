package academy.cli;

import academy.maze.DefaultSolverFactory;
import academy.maze.Solver;
import academy.maze.SolverFactory;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import academy.maze.dto.TerrainType;
import academy.maze.util.MazeValidator;
import academy.maze.util.Mazes;
import academy.util.MazeIO;
import academy.util.MazeRenderer;
import academy.util.PointParser;
import java.io.File;
import java.util.Objects;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "solve", description = "Solve a maze with specified algorithm and points.")
public class SolveCommand implements Runnable {

    private final SolverFactory solverFactory;

    public SolveCommand() {
        this(new DefaultSolverFactory());
    }

    SolveCommand(SolverFactory solverFactory) {
        this.solverFactory = Objects.requireNonNull(solverFactory, "solverFactory");
    }

    @Option(
            names = {"-a", "--algorithm"},
            required = true)
    private String algorithm;

    @Option(
            names = {"-f", "--file"},
            required = true)
    private File mazeFile;

    @Option(
            names = {"-s", "--start"},
            required = true)
    private String startStr;

    @Option(
            names = {"-e", "--end"},
            required = true)
    private String endStr;

    @Option(names = {"-o", "--output"})
    private File output;

    @Option(names = "--unicode", description = "Render solution using Unicode pseudographics")
    private boolean unicode;

    @Override
    public void run() {
        Point start = PointParser.parseOrExit(startStr);
        Point end = PointParser.parseOrExit(endStr);

        Maze maze = MazeIO.read(mazeFile);
        MazeValidator.requireNavigablePoints(maze, start, end);

        Solver solver = solverFactory.create(algorithm);

        Path path = solver.solve(maze, start, end);
        MazeRenderer.GlyphStyle style = unicode ? MazeRenderer.GlyphStyle.UNICODE : MazeRenderer.GlyphStyle.ASCII;
        Maze viewMaze = unicode ? maze : Mazes.withUniformTerrain(maze, TerrainType.NORMAL);
        String text = MazeRenderer.renderWithPath(viewMaze, path, start, end, style);

        if (output != null) {
            MazeIO.write(output, text);
        } else {
            System.out.print(text);
        }
    }
}
