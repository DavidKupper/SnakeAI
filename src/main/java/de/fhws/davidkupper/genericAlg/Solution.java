package de.fhws.davidkupper.genericAlg;

public interface Solution extends Comparable<Solution>{

    Solution copy();
    double getFitness();
    void mutate();

    default int compareTo(Solution o) {
        double fitness = getFitness();
        double fitnessOther = o.getFitness();
        if(fitness == fitnessOther)
            return 0;
        else {
            return (fitness - fitnessOther > 0 ? -1 : 1);
        }
    }
}
