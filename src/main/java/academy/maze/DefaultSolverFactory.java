package academy.maze;

import academy.maze.impl.AStarSolver;
import academy.maze.impl.BreadthFirstSolver;
import academy.maze.impl.DijkstraSolver;
import academy.maze.impl.GreedyBestFirstSolver;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Default implementation that registers built-in maze solvers.
 */
public class DefaultSolverFactory implements SolverFactory {

    private final Map<String, Supplier<Solver>> registry = new HashMap<>();

    public DefaultSolverFactory() {
        register("astar", AStarSolver::new);
        register("a-star", AStarSolver::new);
        register("a*", AStarSolver::new);
        register("dijkstra", DijkstraSolver::new);
        register("bfs", BreadthFirstSolver::new);
        register("greedy", GreedyBestFirstSolver::new);
        register("gbfs", GreedyBestFirstSolver::new);
    }

    /**
     * Registers an additional alias for solver creation.
     *
     * @param alias alias for the algorithm
     * @param supplier solver supplier
     * @return this factory instance for chaining
     */
    public DefaultSolverFactory register(String alias, Supplier<Solver> supplier) {
        registry.put(normalize(alias), Objects.requireNonNull(supplier, "supplier"));
        return this;
    }

    @Override
    public Solver create(String algorithm) {
        Supplier<Solver> supplier = registry.get(normalize(algorithm));
        if (supplier == null) {
            throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
        }
        return supplier.get();
    }

    private String normalize(String algorithm) {
        return Objects.requireNonNull(algorithm, "algorithm").toLowerCase(Locale.ROOT);
    }
}

