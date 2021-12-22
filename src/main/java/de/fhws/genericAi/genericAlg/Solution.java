package de.fhws.genericAi.genericAlg;

public interface Solution extends Comparable<Solution> {

	Solution copy();

	double getFitness();
	
	void calculateFitness();

	Solution getMutatedChild();

	default int compareTo(Solution o) {
		if (getFitness() - o.getFitness() > 0)
			return -1;
		else if (getFitness() - o.getFitness() < 0)
			return 1;
		else
			return 0;
	}

}