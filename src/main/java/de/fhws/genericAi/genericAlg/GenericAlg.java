package de.fhws.genericAi.genericAlg;

import java.util.function.Supplier;

public class GenericAlg {

	int popSize;
	int rounds;
	double mutateRate;
	double selectBestOfPercentage;
	Supplier<Solution> supplier; 

	
	private GenericAlg(int popSize, int rounds, double mutateRate, double selectBestOfPercentage, Supplier<Solution> supplier) {
		this.popSize = popSize;
		this.rounds = rounds;
		this.mutateRate = mutateRate;
		this.selectBestOfPercentage = selectBestOfPercentage;
		this.supplier = supplier;
	}

	
	public Solution solve() {
		Population pop = Population.generateRandomPopulation(popSize, supplier);
		for(int i = 0; i < rounds; i++) {
						
			pop.nextGen((int)(popSize* selectBestOfPercentage), mutateRate);
			
			System.out.println("Computed Generation " + i + " of " + rounds + " Generations. Best Fitness: " + pop.getBestSolution().getFitness() + " average fitness: " + pop.getAverageFitness());
		}
		
		Solution best = pop.getBestSolution();
		return best;
		
	}
	
	public static class Builder{
		
		private GenericAlg g;
		
		public Builder(Supplier<Solution> supplier) {
			g = new GenericAlg(100, 100, 0.10 , 0.25 , supplier);
		}
		
		public Builder setPopulationSize(int populationSize) {
			g.popSize = populationSize;
			return this;
		}
		
		public Builder setRoundsAmount(int rounds) {
			g.rounds = rounds;
			return this;
		}
		
		public Builder setMutateRate(double mutateRate) {
			if(mutateRate < 0 || mutateRate > 1) 
				throw new IllegalArgumentException("Mutate Rate must be between 0 and 1");
			
			g.mutateRate = mutateRate;
			return this;
		}
		
		public Builder setSelectBestOfProz(double selectBestOf) {
			if(selectBestOf < 0 || selectBestOf > 1) 
				throw new IllegalArgumentException("Best-of rate must be between 0 and 1");
			
			g.selectBestOfPercentage = selectBestOf;
			return this;
		}
		
		public GenericAlg build() {
			return g;
		}
		
		
		
	}
	
	
}
