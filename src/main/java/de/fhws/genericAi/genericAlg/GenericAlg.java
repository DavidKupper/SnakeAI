package de.fhws.genericAi.genericAlg;

import de.fhws.filesystemManager.FileSystemManager;

import java.io.File;
import java.util.Date;
import java.util.function.Supplier;

public class GenericAlg {

	int popSize;
	int rounds;
	double mutateRate;
	double selectBestOfPercentage;
	Population pop;
	Supplier<Solution> supplier; 

	
	private GenericAlg(int popSize, int rounds, double mutateRate, double selectBestOfPercentage, Supplier<Solution> supplier) {
		this.popSize = popSize;
		this.rounds = rounds;
		this.mutateRate = mutateRate;
		this.selectBestOfPercentage = selectBestOfPercentage;
		this.supplier = supplier;
	}

	
	public Solution solve(boolean printData, int savingInterval) {
		pop = Population.generateRandomPopulation(popSize, supplier);
		for(int gen = 0; gen < rounds; gen++) {
			pop.nextGen((int)(popSize* selectBestOfPercentage), mutateRate);

			if(printData)
				System.out.println("Computed Generation " + (gen+1) + " of " + rounds +
					" Generations. Best Fitness: " + pop.getBest().getFitness() +
					"; average fitness: " + String.format("%.2f", pop.getAverageFitness()) +
					"; median fitness " + pop.getMedianFitness() +
					"; " + selectBestOfPercentage + " quintile: " + pop.getBestOfQuintile());

			if(( gen+1) % savingInterval == 0)
				pop.safeToFile("saved", "files/intervalSaves", true);
			String log = new StringBuilder()
					.append("generation: ")
					.append(gen).append(" dat: ")
					.append(new Date(System.currentTimeMillis()))
					.toString();
			FileSystemManager.writeObjectToFile(log, "files/log.txt",  true);
		}
		
		Solution best = pop.getBest();
		return best;
		
	}
	
	public static Supplier<Solution> getSupplierOfPopulation(Population pop) {
		return new Supplier<Solution>(){
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


	public static class Builder{
		
		private GenericAlg g;
		
		public Builder(Supplier<Solution> solutionSupplier) {
			g = new GenericAlg(100, 100, 0.10 , 0.25 , solutionSupplier);
		}
		
		public Builder withPopulationSize(int populationSize) {
			g.popSize = populationSize;
			return this;
		}
		
		public Builder withGenerationsAmount(int generationsAmount) {
			g.rounds = generationsAmount;
			return this;
		}
		
		public Builder withMutationRate(double mutationRate) {
			if(mutationRate < 0 || mutationRate > 1)
				throw new IllegalArgumentException("Mutate Rate must be between 0 and 1");
			
			g.mutateRate = mutationRate;
			return this;
		}
		
		public Builder withSelectBestOfPercent(double selectBestOfPercent) {
			if(selectBestOfPercent < 0 || selectBestOfPercent > 1)
				throw new IllegalArgumentException("Best-of rate must be between 0 and 1");
			
			g.selectBestOfPercentage = selectBestOfPercent;
			return this;
		}
		
		public GenericAlg build() {
			return g;
		}
		
		
		
	}
	
	
}
