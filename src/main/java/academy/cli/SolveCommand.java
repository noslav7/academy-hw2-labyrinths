package academy.cli;

import academy.maze.Solver;
import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import academy.maze.impl.AStarSolver;
import academy.maze.impl.DijkstraSolver;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "solve", description = "Solve a maze with specified algorithm and points.")
public class SolveCommand implements Runnable {

    @Option(names = {"-a", "--algorithm"}, required = true)
    private String algorithm;

    @Option(names = {"-f", "--file"}, required = true)
    private File mazeFile;

    @Option(names = {"-s", "--start"}, required = true)
    private String startStr;

    @Option(names = {"-e", "--end"}, required = true)
    private String endStr;

    @Option(names = {"-o", "--output"})
    private File output;

    @Override
    public void run() {
        Point start = parsePointOrExit(startStr);
        Point end = parsePointOrExit(endStr);

        Maze maze = readMaze(mazeFile);
        int width = maze.cells().length == 0 ? 0 : maze.cells()[0].length;
        int height = maze.cells().length;

        if (!inBounds(start, width, height)) {
            System.out.println(
                    "Start point is out of bounds: " + start + ", expected within [0," + (width - 1) + "]x[0," + (height - 1) + "]");
            System.exit(0);
        }
        if (!inBounds(end, width, height)) {
            System.out.println(
                    "End point is out of bounds: " + end + ", expected within [0," + (width - 1) + "]x[0," + (height - 1) + "]");
            System.exit(0);
        }
        if (maze.cells()[start.y()][start.x()] == CellType.WALL) {
            System.out.println("Start point is on a wall: " + start + ". Choose coordinates where the maze has a space ' '.");
            System.exit(0);
        }
        if (maze.cells()[end.y()][end.x()] == CellType.WALL) {
            System.out.println("End point is on a wall: " + end + ". Choose coordinates where the maze has a space ' '.");
            System.exit(0);
        }

        Solver solver = switch (algorithm.toLowerCase()) {
            case "astar", "a-star", "a*" -> new AStarSolver();
            case "dijkstra" -> new DijkstraSolver();
            default -> throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
        };

        Path path = solver.solve(maze, start, end);
        String text = overlayPath(maze, path, start, end);

        if (output != null) {
            try {
                if (output.getParentFile() != null) output.getParentFile().mkdirs();
                Files.writeString(output.toPath(), text, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.print(text);
        }
    }

    private Point parsePointOrExit(String s) {
        if (s == null || !s.contains(",")) {
            System.out.println("Invalid point format: " + s + ", expected format: x,y");
            System.exit(0);
        }
        String[] parts = s.split(",", -1);
        if (parts.length != 2) {
            System.out.println("Invalid point format: " + s + ", expected format: x,y");
            System.exit(0);
        }
        try {
            int x = Integer.parseInt(parts[0].trim());
            int y = Integer.parseInt(parts[1].trim());
            return new Point(x, y);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid point format: " + s + ", expected format: x,y");
            System.exit(0);
            return new Point(0, 0); // unreachable
        }
    }

    private boolean inBounds(Point p, int w, int h) {
        return p.x() >= 0 && p.x() < w && p.y() >= 0 && p.y() < h;
    }

    private Maze readMaze(File file) {
        try {
            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            if (lines.isEmpty()) return new Maze(new CellType[0][0]);
            int width = lines.get(0).length();
            int height = lines.size();
            // If the file contains a border, we will read as-is; solvers should work on inner area.
            CellType[][] cells = new CellType[height][width];
            for (int y = 0; y < height; y++) {
                String line = lines.get(y);
                for (int x = 0; x < width; x++) {
                    char c = line.charAt(x);
                    cells[y][x] = (c == '#') ? CellType.WALL : CellType.PATH;
                }
            }
            return new Maze(cells);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String overlayPath(Maze maze, Path path, Point start, Point end) {
        int width = maze.cells()[0].length;
        int height = maze.cells().length;
        char[][] out = new char[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                out[y][x] = maze.cells()[y][x] == CellType.WALL ? '#' : ' ';
            }
        }
        for (Point p : path.points()) {
            if (p.x() >= 0 && p.x() < width && p.y() >= 0 && p.y() < height) {
                out[p.y()][p.x()] = '.';
            }
        }
        // Mark start and end
        if (start.x() >= 0 && start.x() < width && start.y() >= 0 && start.y() < height) out[start.y()][start.x()] = 'O';
        if (end.x() >= 0 && end.x() < width && end.y() >= 0 && end.y() < height) out[end.y()][end.x()] = 'X';

        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            sb.append(out[y]);
            sb.append('\n');
        }
        return sb.toString();
    }
}


