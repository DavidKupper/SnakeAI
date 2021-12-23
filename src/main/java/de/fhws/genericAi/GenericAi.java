package de.fhws.genericAi;

import de.fhws.genericAi.genericAlg.GenericAlg;
import de.fhws.genericAi.genericAlg.Solution;
import de.fhws.genericAi.neuralNetwork.NeuralNet;
import de.fhws.snakegame.SnakeAi;

public class GenericAi {
    private GenericAlg alg;
    
    private GenericAi(GenericAlg alg) {
        this.alg = alg;
    };
    
    public NeuralNet evolve() {
        Solution solution = alg.solve();
        if(solution instanceof NeuralNetSolution)
            return (NeuralNet) ((NeuralNetSolution) solution).getNeuralNetwork();
        else
            return null;
    }
    
    
    public static class Builder {
        FitnessFunction fitnessFunction;
        NeuralNet.Builder nnBuilder;
        double weightRange = 10;
        boolean weightsNegative = true;
        double biasRange = 1;
        boolean biasNegative = false;
        int popSize = 100;
        int genAmount = 100;
        double selectBestOfPercent = 0.25;
        double outerMutationRate = 0.1;
        double dataMutationRate = 0.5;
        double dataMutationFactor = 0.5;
        
        public Builder(FitnessFunction fitnessFunction, NeuralNet.Builder nnBuilder) {
            this.fitnessFunction = fitnessFunction;
            this.nnBuilder = nnBuilder;
        }

        public Builder withWeightRange(double weightRange) {
            this.weightRange = weightRange;
            return this;
        }

        public Builder withWeightsNegative(boolean weightsNegative) {
            this.weightsNegative = weightsNegative;
            return this;
        }

        public Builder withBiasRange(double biasRange) {
            this.biasRange = biasRange;
            return this;
        }

        public Builder withBiasNegative(boolean biasNegative) {
            this.biasNegative = biasNegative;
            return this;
        }

        public Builder withPopulationSize(int popSize) {
            this.popSize = popSize;
            return this;
        }

        public Builder withGenerationAmount(int genAmount) {
            this.genAmount = genAmount;
            return this;
        }

        public Builder withSelectBestOfPercent(double selectBestOfPercent) {
            this.selectBestOfPercent = selectBestOfPercent;
            return this;
        }

        public Builder withOuterMutationRate(double outerMutationRate) {
            this.outerMutationRate = outerMutationRate;
            return this;
        }

        public Builder withDataMutationRate(double dataMutationRate) {
            this.dataMutationRate = dataMutationRate;
            return this;
        }

        public Builder withDataMutationFactor(double dataMutationFactor) {
            this.dataMutationFactor = dataMutationFactor;
            return this;
        }

        public GenericAi build() {
            return new GenericAi(new GenericAlg.Builder(() ->
                    new NeuralNetSolution(nnBuilder.build().randomize(weightRange, weightsNegative, biasRange, biasNegative),
                    fitnessFunction, dataMutationRate, dataMutationFactor))
                    .withPopulationSize(popSize)
                    .withGenerationsAmount(genAmount)
                    .withSelectBestOfPercent(selectBestOfPercent)
                    .withMutationRate(outerMutationRate)
                    .build());
        }
    }
}
