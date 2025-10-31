package academy.maze.impl;

import academy.maze.Generator;
import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PrimGenerator implements Generator {
    private final Random random;

    public PrimGenerator() {
        this(new Random());
    }

    // package-private for tests
    PrimGenerator(Random random) {
        this.random = random;
    }
    @Override
    public Maze generate(int width, int height) {
        if (width <= 0 || height <= 0) throw new IllegalArgumentException("Invalid size");
        CellType[][] cells = new CellType[height][width];
        for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) cells[y][x] = CellType.WALL;

        // For very small mazes, just open everything inside
        if (width < 3 || height < 3) {
            for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) cells[y][x] = CellType.PATH;
            return new Maze(cells);
        }

        boolean[][] inMaze = new boolean[height][width];
        int sx = 1, sy = 1; // start on odd cell
        inMaze[sy][sx] = true;
        cells[sy][sx] = CellType.PATH;
        List<int[]> frontier = new ArrayList<>();
        addFrontierOdd(sx, sy, frontier, width, height);

        while (!frontier.isEmpty()) {
            int idx = random.nextInt(frontier.size());
            int[] f = frontier.remove(idx);
            int fx = f[0], fy = f[1];
            if (inMaze[fy][fx]) continue;

            // neighbors at distance 2 that are already in the maze (odd-grid)
            List<int[]> inNeighbors = new ArrayList<>();
            if (fx > 1 && inMaze[fy][fx - 2]) inNeighbors.add(new int[] {fx - 2, fy});
            if (fx < width - 2 && inMaze[fy][fx + 2]) inNeighbors.add(new int[] {fx + 2, fy});
            if (fy > 1 && inMaze[fy - 2][fx]) inNeighbors.add(new int[] {fx, fy - 2});
            if (fy < height - 2 && inMaze[fy + 2][fx]) inNeighbors.add(new int[] {fx, fy + 2});
            if (inNeighbors.isEmpty()) continue;

            int[] n = inNeighbors.get(random.nextInt(inNeighbors.size()));
            int nx = n[0], ny = n[1];

            // open the wall between (fx, fy) and (nx, ny)
            int bx = (fx + nx) / 2;
            int by = (fy + ny) / 2;
            cells[by][bx] = CellType.PATH;

            // open the frontier cell and mark it in the maze
            cells[fy][fx] = CellType.PATH;
            inMaze[fy][fx] = true;
            addFrontierOdd(fx, fy, frontier, width, height);
        }
        return new Maze(cells);
    }

    private void addFrontierOdd(int x, int y, List<int[]> frontier, int w, int h) {
        if (x > 1) frontier.add(new int[] {x - 2, y});
        if (x < w - 2) frontier.add(new int[] {x + 2, y});
        if (y > 1) frontier.add(new int[] {x, y - 2});
        if (y < h - 2) frontier.add(new int[] {x, y + 2});
    }
}


