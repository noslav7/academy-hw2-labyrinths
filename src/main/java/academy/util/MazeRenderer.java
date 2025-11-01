package academy.util;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;

public final class MazeRenderer {
    private MazeRenderer() {}

    public static String renderWithPath(Maze maze, Path path, Point start, Point end) {
        int width = maze.cells()[0].length;
        int height = maze.cells().length;
        char[][] out = new char[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (maze.cells()[y][x] == CellType.WALL) {
                    out[y][x] = '#';
                } else {
                    // отрисовываем символ поверхности
                    out[y][x] = maze.terrain()[y][x].symbol();
                }
            }
        }
        for (Point p : path.points()) {
            if (p.x() >= 0 && p.x() < width && p.y() >= 0 && p.y() < height) {
                out[p.y()][p.x()] = '+';
            }
        }
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


