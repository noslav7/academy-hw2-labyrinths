package academy.maze.util;

import java.util.List;
import org.jspecify.annotations.NonNull;

/** Shared cardinal movement vectors for maze traversal algorithms. */
public final class Directions {
    private Directions() {}

    public static final List<@NonNull Direction> CARDINAL =
            List.of(new Direction(1, 0), new Direction(0, 1), new Direction(-1, 0), new Direction(0, -1));

    public record Direction(int dx, int dy) {}
}
