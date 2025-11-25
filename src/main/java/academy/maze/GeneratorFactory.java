package academy.maze;

/**
 * Factory for creating {@link Generator} instances by algorithm identifier.
 */
public interface GeneratorFactory {

    /**
     * Creates a new generator for the provided algorithm identifier.
     *
     * @param algorithm algorithm identifier (case-insensitive)
     * @return generator instance
     * @throws IllegalArgumentException if the algorithm is unknown
     */
    Generator create(String algorithm);
}


