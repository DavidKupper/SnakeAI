package de.fhws.genericAi.genericAlg;

public interface Solution extends Comparable<Solution> {

	Solution copy();

	double getFitness();
	
	void calculateFitness();

	Solution getChild();

	default int compareTo(Solution o) {
		if (getFitness() - o.getFitness() == 0)
			return 0;
		else
			return getFitness() - o.getFitness() < 0 ? 1 : -1;
	}

}