package de.fhws.genericAi.genericAlg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

class Population {

	private List<Solution> solutions;
	int size;
	
	private Population(List<Solution> solutions, int size) {
		this.solutions = solutions;
		this.size = size;
	}
	
	public static Population generateRandomPopulation(int size, Supplier<Solution> supplier) {
		List<Solution> list = new ArrayList<>();
		for(int i = 0; i < size; i++) {
			list.add(supplier.get());
		}
		return new Population(list, size);
	}
	
	public void nextGen(int selectBestOf, double mutateRate) {
		solutions.forEach(s -> s.calculateFitness());
		Collections.sort(solutions);
		solutions = new ArrayList<>(solutions.subList(0, selectBestOf));
		
		int index = 0;
		while(solutions.size() < size) {
			if(Math.random() > mutateRate) {
				solutions.add(solutions.get(index).getChild());
			}
			else {
				solutions.add(solutions.get(index).copy());
			}
			index = index + 1 % selectBestOf;
		}
	}
	
	public double getAverageFitness() {
		double sum = 0;
		for(Solution s : solutions) {
			sum += s.getFitness();
		}
		return sum/solutions.size();
		
	}
	
	public Solution getBestSolution() {
		return Collections.min(solutions);
	}
	
	
}
