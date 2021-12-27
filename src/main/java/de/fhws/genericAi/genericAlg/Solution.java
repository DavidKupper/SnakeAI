package de.fhws.genericAi.genericAlg;

import java.io.Serializable;

public interface Solution extends Comparable<Solution>, Serializable{

	Solution copy();

	double getFitness();
	
	void calculateFitness();

	Solution getChild(Population population);

	default int compareTo(Solution o) {
		if (getFitness() - o.getFitness() == 0)
			return 0;
		else
			return getFitness() - o.getFitness() < 0 ? 1 : -1;
	}

}