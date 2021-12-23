package de.fhws.genericAi.genericAlg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

class Population {

	private List<Solution> solutions;
	int size;
	private double averageFitness;
	private double medianFitness;
	private double bestOfQuintile;
	private Solution best;

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
	
	public void nextGen(int selectBestOf, double mutationRate) {
		solutions.forEach(Solution::calculateFitness);
		Collections.sort(solutions);
		// retrieve data before repopulating; important: solutions must be sorted
		calcAvgFit();
		calcMedianFit();
		calcBestOfQuintile(selectBestOf);
		calcBest();
		// kill all other Solutions
		solutions = new ArrayList<>(solutions.subList(0, selectBestOf));
		
		int index = 0;
		while(solutions.size() < size) {
			if(Math.random() > mutationRate) {
				solutions.add(solutions.get(index).getChild());
			}
			else {
				solutions.add(solutions.get(index).copy());
			}
			index = index + 1 % selectBestOf;
		}
	}

	private void calcAvgFit() {
		double sum = 0;
		for(Solution s : solutions) {
			sum += s.getFitness();
		}
		averageFitness = sum / solutions.size();
	}

	private void calcMedianFit() {
		medianFitness = solutions.get(solutions.size() / 2).getFitness();
	}

	private void calcBestOfQuintile(int seleceBestOf) {
		bestOfQuintile = solutions.get(seleceBestOf-1).getFitness();
	}

	private void calcBest() {
		best = solutions.get(0);
	}
	
	public double getAverageFitness() {
		return averageFitness;
	}

	public double getMedianFitness() {
		return medianFitness;
	}

	public double getBestOfQuintile() {
		return bestOfQuintile;
	}
	
	public Solution getBest() {
		return best;
	}
	
	
}
