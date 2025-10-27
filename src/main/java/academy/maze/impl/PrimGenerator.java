package academy.maze.impl;

import academy.maze.Generator;
import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PrimGenerator implements Generator {
    @Override
    public Maze generate(int width, int height) {
        if (width <= 0 || height <= 0) throw new IllegalArgumentException("Invalid size");
        CellType[][] cells = new CellType[height][width];
        for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) cells[y][x] = CellType.WALL;

        boolean[][] inMaze = new boolean[height][width];
        Random rnd = new Random();
        int sx = 0, sy = 0;
        inMaze[sy][sx] = true;
        cells[sy][sx] = CellType.PATH;
        List<int[]> frontier = new ArrayList<>();
        addFrontier(sx, sy, frontier, width, height);
        while (!frontier.isEmpty()) {
            int idx = rnd.nextInt(frontier.size());
            int[] f = frontier.remove(idx);
            int fx = f[0], fy = f[1];
            if (inMaze[fy][fx]) continue;
            // connect to one random neighbor already in maze
            List<int[]> inNeighbors = new ArrayList<>();
            if (fx > 0 && inMaze[fy][fx - 1]) inNeighbors.add(new int[] {fx - 1, fy});
            if (fx < width - 1 && inMaze[fy][fx + 1]) inNeighbors.add(new int[] {fx + 1, fy});
            if (fy > 0 && inMaze[fy - 1][fx]) inNeighbors.add(new int[] {fx, fy - 1});
            if (fy < height - 1 && inMaze[fy + 1][fx]) inNeighbors.add(new int[] {fx, fy + 1});
            if (inNeighbors.isEmpty()) continue;
            // open this cell
            cells[fy][fx] = CellType.PATH;
            inMaze[fy][fx] = true;
            addFrontier(fx, fy, frontier, width, height);
        }
        return new Maze(cells);
    }

    private void addFrontier(int x, int y, List<int[]> frontier, int w, int h) {
        if (x > 0) frontier.add(new int[] {x - 1, y});
        if (x < w - 1) frontier.add(new int[] {x + 1, y});
        if (y > 0) frontier.add(new int[] {x, y - 1});
        if (y < h - 1) frontier.add(new int[] {x, y + 1});
    }
}


