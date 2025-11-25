package academy.maze;

import academy.maze.impl.DepthFirstGenerator;
import academy.maze.impl.KruskalGenerator;
import academy.maze.impl.PrimGenerator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Default implementation that registers built-in maze generators.
 */
public class DefaultGeneratorFactory implements GeneratorFactory {

    private final Map<String, Supplier<Generator>> registry = new HashMap<>();

    public DefaultGeneratorFactory() {
        register("dfs", DepthFirstGenerator::new);
        register("prim", PrimGenerator::new);
        register("kruskal", KruskalGenerator::new);
    }

    /**
     * Registers an additional alias for generator creation.
     *
     * @param alias alias for the algorithm
     * @param supplier generator supplier
     * @return this factory instance for chaining
     */
    public DefaultGeneratorFactory register(String alias, Supplier<Generator> supplier) {
        registry.put(normalize(alias), Objects.requireNonNull(supplier, "supplier"));
        return this;
    }

    @Override
    public Generator create(String algorithm) {
        Supplier<Generator> supplier = registry.get(normalize(algorithm));
        if (supplier == null) {
            throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
        }
        return supplier.get();
    }

    private String normalize(String algorithm) {
        return Objects.requireNonNull(algorithm, "algorithm").toLowerCase(Locale.ROOT);
    }
}


