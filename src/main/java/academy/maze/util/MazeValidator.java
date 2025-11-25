package academy.maze.util;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Point;

/** Utility methods for validating maze coordinates before solving. */
public final class MazeValidator {

    private MazeValidator() {}

    /**
     * Ensures that the given start and end points are within the maze bounds and not walls. Exits with status code 2 if
     * validation fails.
     */
    public static void requireNavigablePoints(Maze maze, Point start, Point end) {
        int height = maze.cells().length;
        int width = height == 0 ? 0 : maze.cells()[0].length;

        requireNavigablePoint(maze, start, width, height, "Start");
        requireNavigablePoint(maze, end, width, height, "End");
    }

    private static void requireNavigablePoint(Maze maze, Point point, int width, int height, String pointName) {
        if (!inBounds(point, width, height)) {
            System.err.println(pointName + " point is out of bounds: " + point + ", expected within [0," + (width - 1)
                    + "]x[0," + (height - 1) + "]");
            System.exit(2);
        }
        if (maze.cells()[point.y()][point.x()] == CellType.WALL) {
            System.err.println(pointName + " point is on a wall: " + point
                    + ". Choose coordinates where the maze has a space ' '.");
            System.exit(2);
        }
    }

    private static boolean inBounds(Point p, int w, int h) {
        return p.x() >= 0 && p.x() < w && p.y() >= 0 && p.y() < h;
    }
}
