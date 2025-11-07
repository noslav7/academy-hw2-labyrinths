package academy.maze.impl;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import java.util.ArrayList;
import java.util.List;

public class PrimGenerator extends AbstractRandomizedGenerator {
    public PrimGenerator() {}

    // package-private for tests
    PrimGenerator(java.util.Random random) {
        super(random);
    }

    @Override
    public Maze generate(int width, int height) {
        validateDimensions(width, height);
        Maze small = smallMazeOrNull(width, height);
        if (small != null) {
            return small;
        }
        CellType[][] cells = newWallGrid(width, height);

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
            int bx = fx + ((nx - fx) / 2);
            int by = fy + ((ny - fy) / 2);
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
