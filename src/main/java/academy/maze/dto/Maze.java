package academy.maze.dto;

/**
 * Лабиринт.
 *
 * @param cells   Массив ячеек лабиринта (WALL или PATH).
 * @param terrain Массив поверхностей для проходимых клеток. Для стен значение игнорируется.
 */
public record Maze(CellType[][] cells, TerrainType[][] terrain) {
    public Maze(CellType[][] cells) {
        this(cells, defaultTerrain(cells));
    }

    public int costAt(int x, int y) {
        if (terrain == null || terrain.length == 0) return 1;
        return terrain[y][x].cost();
    }

    private static TerrainType[][] defaultTerrain(CellType[][] cells) {
        int h = cells.length;
        int w = h == 0 ? 0 : cells[0].length;
        TerrainType[][] t = new TerrainType[h][w];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                t[y][x] = TerrainType.NORMAL;
            }
        }
        return t;
    }
}
