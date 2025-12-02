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
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Option;

@Command(name = "solve", description = "Solve a maze with specified algorithm and points.")
public class SolveCommand implements Callable<Integer> {

    private final SolverFactory solverFactory;

    public SolveCommand() {
        this(new DefaultSolverFactory());
    }

    private SolveCommand(SolverFactory solverFactory) {
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
    public Integer call() {
        try {
            Point start = PointParser.parse(startStr);
            Point end = PointParser.parse(endStr);

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
            return ExitCode.OK;
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return ExitCode.USAGE;
        } catch (Exception e) {
            System.err.println("Failed to solve maze: " + e.getMessage());
            return ExitCode.SOFTWARE;
        }
    }
}
