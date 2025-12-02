package academy.maze.impl;

class DijkstraSolverTest extends AbstractWeightedSolverTest<DijkstraSolver> {

    @Override
    protected DijkstraSolver solverUnderTest() {
        return new DijkstraSolver();
    }
}
