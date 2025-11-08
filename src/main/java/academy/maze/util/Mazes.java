package academy.maze.util;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.TerrainType;

public final class Mazes {
    private Mazes() {}

    public static Maze openAll(int width, int height) {
        CellType[][] cells = new CellType[height][width];
        for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) cells[y][x] = CellType.PATH;
        return new Maze(cells);
    }

    public static Maze withUniformTerrain(Maze maze, TerrainType terrainType) {
        CellType[][] cells = maze.cells();
        int height = cells.length;
        int width = height == 0 ? 0 : cells[0].length;
        TerrainType[][] terrain = new TerrainType[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                terrain[y][x] = terrainType;
            }
        }
        return new Maze(cells, terrain);
    }
}
