package de.fhws.genericAi.genericAlg;


import java.util.function.Supplier;

public class GenericAlg {
    private Supplier<Solution> supplier;
    private int populationSize;
    private int roundsAmount;
    private double selectBestOf;
    private double mutateRate;

    public GenericAlg(Supplier<Solution> supplier, int populationSize, int roundsAmount, double selectBestOf, double mutateRate) {
        this.supplier = supplier;
        this.populationSize = populationSize;
        this.roundsAmount = roundsAmount;
        this.selectBestOf = selectBestOf;
        this.mutateRate = mutateRate;
    }

    public Solution solve() {
        int absoluteBestOf = (int) (selectBestOf * populationSize);
        Population pop = Population.generateRandomPopulation(populationSize, supplier);
        for(int i = 0; i < roundsAmount; i++) {
            pop.nextGen(absoluteBestOf, mutateRate);
            System.out.println("Computed generation " + i + " of " + roundsAmount +
                    "; best fit: " + pop.getBestSolution().getFitness() + "; avg fit: " + pop.getAverageFitness());
        }
        return pop.getBestSolution();
    }

    public static class Builder {
        private GenericAlg g;
        /**
         * creates a GenericAlgorithm with the given supplier and a populationSize of 100, roundAmount of 10, selectBestOf
         * @param supplier
         */
        public Builder(Supplier<Solution> supplier) {
            g = new GenericAlg(supplier, 100, 10, 25, 0.25);
        }
        public Builder setPopulationSize(int popSize) {
            g.populationSize = popSize;
            return this;
        }
        public Builder setRoundsAmount(int roundsAmount) {
            g.roundsAmount = roundsAmount;
            return this;
        }
        public Builder setSelectBestOf(double selectBestOf) {
            if(selectBestOf < 0 ||selectBestOf > 1)
                throw new IllegalArgumentException("selectBestOf must be between 0 and 1 (inclusive)");
            g.selectBestOf = selectBestOf;
            return this;
        }
        public Builder setMutateRate(double mutateRate) {
            if(mutateRate < 0 || mutateRate > 1)
                throw new IllegalArgumentException("mutateRate must be a double between 0 and 1 (inclusive)");
            g.mutateRate = mutateRate;
            return this;
        }
        public GenericAlg build() {
            return g;
        }
    }

}
