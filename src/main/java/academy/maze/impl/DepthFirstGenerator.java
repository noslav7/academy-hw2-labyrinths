package academy.maze.impl;

import academy.maze.Generator;
import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DepthFirstGenerator implements Generator {
    private final Random random;

    public DepthFirstGenerator() {
        this(new Random());
    }

    // package-private for tests
    DepthFirstGenerator(Random random) {
        this.random = random;
    }
    @Override
    public Maze generate(int width, int height) {
        if (width <= 0 || height <= 0) throw new IllegalArgumentException("Invalid size");
        CellType[][] cells = new CellType[height][width];
        for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) cells[y][x] = CellType.WALL;

        // Small grids cannot form corridors with separating walls; open them fully
        if (width < 3 || height < 3) {
            for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) cells[y][x] = CellType.PATH;
            return new Maze(cells);
        }

        boolean[][] visitedRooms = new boolean[height][width];
        // Start from the first odd cell inside the border
        carveRooms(1, 1, cells, visitedRooms, random, width, height);
        return new Maze(cells);
    }

    // Recursive backtracker over a grid of "rooms" located at odd coordinates.
    private void carveRooms(
            int x,
            int y,
            CellType[][] cells,
            boolean[][] visitedRooms,
            Random rnd,
            int width,
            int height) {
        visitedRooms[y][x] = true;
        cells[y][x] = CellType.PATH;

        List<int[]> dirs = new ArrayList<>();
        dirs.add(new int[] {2, 0});
        dirs.add(new int[] {-2, 0});
        dirs.add(new int[] {0, 2});
        dirs.add(new int[] {0, -2});
        Collections.shuffle(dirs, rnd);

        for (int[] d : dirs) {
            int nx = x + d[0];
            int ny = y + d[1];
            if (nx > 0 && nx < width - 1 && ny > 0 && ny < height - 1 && !visitedRooms[ny][nx]) {
                // Open the wall between (x,y) and (nx,ny)
                int bx = x + d[0] / 2;
                int by = y + d[1] / 2;
                cells[by][bx] = CellType.PATH;
                carveRooms(nx, ny, cells, visitedRooms, rnd, width, height);
            }
        }
    }
}


