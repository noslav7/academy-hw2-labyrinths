package academy.cli;

import academy.maze.Solver;
import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import academy.maze.dto.TerrainType;
import academy.maze.impl.AStarSolver;
import academy.maze.impl.BreadthFirstSolver;
import academy.maze.impl.DijkstraSolver;
import academy.maze.impl.GreedyBestFirstSolver;
import academy.maze.util.Mazes;
import academy.util.MazeIO;
import academy.util.MazeRenderer;
import academy.util.PointParser;
import java.io.File;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "solve", description = "Solve a maze with specified algorithm and points.")
public class SolveCommand implements Runnable {

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
        int width = maze.cells().length == 0 ? 0 : maze.cells()[0].length;
        int height = maze.cells().length;

        if (!inBounds(start, width, height)) {
            System.err.println("Start point is out of bounds: " + start + ", expected within [0," + (width - 1)
                    + "]x[0," + (height - 1) + "]");
            System.exit(2);
        }
        if (!inBounds(end, width, height)) {
            System.err.println("End point is out of bounds: " + end + ", expected within [0," + (width - 1) + "]x[0,"
                    + (height - 1) + "]");
            System.exit(2);
        }
        if (maze.cells()[start.y()][start.x()] == CellType.WALL) {
            System.err.println(
                    "Start point is on a wall: " + start + ". Choose coordinates where the maze has a space ' '.");
            System.exit(2);
        }
        if (maze.cells()[end.y()][end.x()] == CellType.WALL) {
            System.err.println(
                    "End point is on a wall: " + end + ". Choose coordinates where the maze has a space ' '.");
            System.exit(2);
        }

        Solver solver =
                switch (algorithm.toLowerCase()) {
                    case "astar", "a-star", "a*" -> new AStarSolver();
                    case "dijkstra" -> new DijkstraSolver();
                    case "bfs" -> new BreadthFirstSolver();
                    case "greedy", "gbfs" -> new GreedyBestFirstSolver();
                    default -> throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
                };

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

    private boolean inBounds(Point p, int w, int h) {
        return p.x() >= 0 && p.x() < w && p.y() >= 0 && p.y() < h;
    }
}
