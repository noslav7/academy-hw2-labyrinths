package academy.maze;

/**
 * Factory for creating {@link Solver} instances by algorithm identifier.
 */
public interface SolverFactory {

    /**
     * Creates a new solver for the provided algorithm identifier.
     *
     * @param algorithm algorithm identifier (case-insensitive)
     * @return solver instance
     * @throws IllegalArgumentException if the algorithm is unknown
     */
    Solver create(String algorithm);
}

