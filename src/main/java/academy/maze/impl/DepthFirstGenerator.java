package academy.maze.impl;

import academy.maze.Generator;
import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DepthFirstGenerator implements Generator {
    @Override
    public Maze generate(int width, int height) {
        if (width <= 0 || height <= 0) throw new IllegalArgumentException("Invalid size");
        // Represent inner grid without outer border. We create cells as PATH and surround with walls on rendering.
        CellType[][] cells = new CellType[height][width];
        for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) cells[y][x] = CellType.WALL;

        boolean[][] visited = new boolean[height][width];
        carve(0, 0, cells, visited, new Random());
        return new Maze(cells);
    }

    private void carve(int x, int y, CellType[][] cells, boolean[][] visited, Random rnd) {
        int h = cells.length, w = cells[0].length;
        visited[y][x] = true;
        cells[y][x] = CellType.PATH;
        List<int[]> dirs = new ArrayList<>();
        dirs.add(new int[] {1, 0});
        dirs.add(new int[] {-1, 0});
        dirs.add(new int[] {0, 1});
        dirs.add(new int[] {0, -1});
        Collections.shuffle(dirs, rnd);
        for (int[] d : dirs) {
            int nx = x + d[0];
            int ny = y + d[1];
            if (nx >= 0 && nx < w && ny >= 0 && ny < h && !visited[ny][nx]) {
                carve(nx, ny, cells, visited, rnd);
            }
        }
    }
}


