package academy.maze.impl;

class AStarSolverTest extends AbstractWeightedSolverTest<AStarSolver> {

    @Override
    protected AStarSolver solverUnderTest() {
        return new AStarSolver();
    }
}
