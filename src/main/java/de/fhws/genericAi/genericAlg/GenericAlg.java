package de.fhws.genericAi.genericAlg;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import de.fhws.filesystemManager.FileManager;

public class GenericAlg {

    int popSize;
    int rounds;
    double mutateRate;
    double selectBestOfPercentage;
    Population pop;
    Supplier<Solution> supplier;
    ExecutorService executor;


    private GenericAlg(int popSize, int rounds, double mutateRate, double selectBestOfPercentage, Supplier<Solution> supplier, ExecutorService executor) {
        this.popSize = popSize;
        this.rounds = rounds;
        this.mutateRate = mutateRate;
        this.selectBestOfPercentage = selectBestOfPercentage;
        this.supplier = supplier;
        this.executor = executor;
    }


    public Solution solve(boolean printData, int savingInterval) {
    	boolean firstSafe = true;
        pop = Population.generateRandomPopulation(popSize, supplier);
        for (int gen = 0; gen < rounds; gen++) {
            pop.nextGen((int) (popSize * selectBestOfPercentage), mutateRate, executor);

            if (printData) {
                printData(gen);
            }

            if (savingInterval != -1 && (gen + 1) % savingInterval == 0) {
                pop.safeToFile("saved", "files/intervalSaves", true);
                String log = new StringBuilder()
                        .append("saved generation: ")
                        .append(gen).append(" dat: ")
                        .append(new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss").format(Calendar.getInstance().getTime()))
                        .append("\n")
                        .toString();
                FileManager.writeStringToFile(log, "log.txt", "files/intervalSaves", !firstSafe);
                firstSafe = false;
            }
        }

        Solution best = pop.getBest();
        if(executor != null)
            executor.shutdown();

        return best;
    }


	/**
	 * @param gen
	 */
	private void printData(int gen) {
		String printDataString = (gen + 1) + " of " + rounds + " gens" +
		        "\t avg: " + String.format("%.2f", pop.getAverageFitness()) +
		        "\t best: " + String.format("%.2f", pop.getBest().getFitness()) +
		        "\t " + selectBestOfPercentage + " quintile: " + String.format("%.2f", pop.getBestOfQuintile()) +
		        "\t median: " + String.format("%.2f", pop.getMedianFitness()) +
		        "\t worst: " + String.format("%.2f", pop.getWorstFitness());

		System.out.println(printDataString);

		String metaData = gen +
		        ";" +
		        String.format("%.2f", pop.getAverageFitness()) +
		        ";" +
		        String.format("%.2f", pop.getBest().getFitness()) +
		        ";" +
		        String.format("%.2f", pop.getBestOfQuintile()) +
		        ";" +
		        String.format("%.2f", pop.getMedianFitness()) +
		        ";" +
		        String.format("%.2f", pop.getWorstFitness()) +
		        ";" +
		        "\n";
		FileManager.writeStringToFile(metaData, "metaData.txt", "files", true);
	}

    public static Supplier<Solution> getSupplierOfPopulation(Population pop) {
        return new Supplier<>() {
            int counter = 0;

            @Override
            public Solution get() {
                return pop.getSolutions().get(counter++);
            }
        };
    }

    public Population getPopulation() {
        return pop;
    }


    public static class Builder {

        private int popSize = 100;
        private int genAmount = 100;
        private double mutationRate = 0.1;
        private double selectBestOfPercent = 0.25;
        private Supplier<Solution> solutionSupplier;
        private int threadAmount = 0;

        public Builder(Supplier<Solution> solutionSupplier) {
            this.solutionSupplier = solutionSupplier;
        }

        public Builder withPopulationSize(int populationSize) {
            this.popSize = populationSize;
            return this;
        }

        public Builder withGenerationsAmount(int generationsAmount) {
            this.genAmount = generationsAmount;
            return this;
        }

        public Builder withMutationRate(double mutationRate) {
            if (mutationRate < 0 || mutationRate > 1)
                throw new IllegalArgumentException("Mutate Rate must be between 0 and 1");

            this.mutationRate = mutationRate;
            return this;
        }

        public Builder withSelectBestOfPercent(double selectBestOfPercent) {
            if (selectBestOfPercent < 0 || selectBestOfPercent > 1)
                throw new IllegalArgumentException("Best-of rate must be between 0 and 1");

            this.selectBestOfPercent = selectBestOfPercent;
            return this;
        }

        public Builder withAmountOfParallelThreads(int threads) {
            if(threads <= 0)
                this.threadAmount = 0;
            else
                this.threadAmount = threads;
            return this;
        }

        public GenericAlg build() {
            ExecutorService executor;
            if(threadAmount > 0)
                executor = Executors.newFixedThreadPool(threadAmount);
            else
                executor = null;

            return new GenericAlg(popSize, genAmount, mutationRate, selectBestOfPercent, solutionSupplier, executor);
        }


    }


}
