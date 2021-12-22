package de.fhws.genericAi.genericAlg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

class Population {
    private List<Solution> solutions;

    private Population(int size) {
        solutions = new ArrayList<>(size);
    }

    public static Population generateRandomPopulation(int sizeOfPopulation, Supplier<Solution> supplier) {
        Population p = new Population(sizeOfPopulation);
        for(int i = 0; i < sizeOfPopulation; i++) {
            p.solutions.add(supplier.get());
        }
        return p;
    }

    public void nextGen(int selectBestOf, double mutateRate) {
        solutions.forEach(Solution::calcFitness);
        Collections.sort(solutions);
        int size = solutions.size();
        solutions = new ArrayList<>(solutions.subList(0, selectBestOf));
        int index = selectBestOf;
        while (solutions.size() < size)
            solutions.add(solutions.get(index++ % selectBestOf).copy());

        solutions.forEach(solution -> {if(Math.random() < mutateRate) solution.mutate();});
    }

    public Solution getBestSolution() {
        return Collections.min(solutions);
    }

    public double getAverageFitness() {
        double sum = 0;
        for(Solution s : solutions)
            sum += s.getFitness();
        return sum / solutions.size();
    }


}
