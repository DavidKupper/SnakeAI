package de.fhws.davidkupper.genericAlg;


import java.util.function.Supplier;

public class GenericAlgorithm {
    private Supplier<Solution> supplier;
    private int populationSize;
    private int roundsAmount;
    private int selectBestOf;
    private double mutateRate;

    public GenericAlgorithm(Supplier<Solution> supplier, int populationSize, int roundsAmount, int selectBestOf, double mutateRate) {
        this.supplier = supplier;
        this.populationSize = populationSize;
        this.roundsAmount = roundsAmount;
        this.selectBestOf = selectBestOf;
        this.mutateRate = mutateRate;
    }

    public Solution solve() {
        if(selectBestOf >= populationSize)
            throw new IllegalArgumentException("selectBestOf must be smaller than populationSize");
        Population population = Population.generateRandomPopulation(populationSize, supplier);
        for(int i = 0; i < roundsAmount; i++)
            population.nextGen(selectBestOf, mutateRate);
        return population.getBestSolution();
    }

    public static class Builder {
        private GenericAlgorithm g;
        /**
         * creates a GenericAlgorithm with the given supplier and a populationSize of 100, roundAmount of 10, selectBestOf
         * @param supplier
         */
        public Builder(Supplier<Solution> supplier) {
            g = new GenericAlgorithm(supplier, 100, 10, 25, 0.25);
        }
        public Builder setPopulationSize(int popSize) {
            g.populationSize = popSize;
            return this;
        }
        public Builder setRoundsAmount(int roundsAmount) {
            g.roundsAmount = roundsAmount;
            return this;
        }
        public Builder setSelectBestOf(int selectBestOf) {
            if(selectBestOf > g.populationSize)
                throw new IllegalArgumentException("selectBestOf must be smaller or equal to populationSize");
            g.selectBestOf = selectBestOf;
            return this;
        }
        public Builder setMutateRate(double mutateRate) {
            if(mutateRate < 0 || mutateRate > 1)
                throw new IllegalArgumentException("mutateRate must be a double between 0 and 1 (inclusive)");
            g.mutateRate = mutateRate;
            return this;
        }
        public GenericAlgorithm build() {
            return g;
        }
    }

}
