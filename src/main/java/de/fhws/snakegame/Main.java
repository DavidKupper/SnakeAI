package de.fhws.snakegame;

import de.fhws.genericAi.FitnessFunction;
import de.fhws.genericAi.GenericAi;
import de.fhws.genericAi.neuralNetwork.NeuralNet;

public class Main {

    public static void main(String[] args) {

/*
        GenericAlg g = new GenericAlg.Builder(() -> new NeuralNetSolution(
                new NeuralNet.Builder()
                        .addLayers(24, 16, 4)
                        .build()
                        .randomize(10, true, 1, false),
						nn -> new SnakeAi(nn).startPlaying(150), 0.5, 0.5))
                .withPopulationSize(1000)
                .withGenerationsAmount(100)
                .withSelectBestOfPercent(0.05)
                .withMutationRate(0.1)
                .build();*/
        NeuralNet.Builder nnBuilder = new NeuralNet.Builder().addLayers(24, 16, 4);
        FitnessFunction fitFunc = nn -> new SnakeAi(nn).startPlaying(150);
        GenericAi ai = new GenericAi.Builder(fitFunc, nnBuilder)
                .withPopulationSize(2000)
                .withGenerationAmount(300)
                .withSelectBestOfPercent(0.05)
                .withOuterMutationRate(0.1)
                .withDataMutationRate(1)
                .withDataMutationFactor(0.25)
                .build();


        //NeuralNet best = NeuralNet.loadFromFile("BestNeuralNetwork");
        NeuralNet best = ai.doEvolution();
        best.safeAsFile("files/bestAi.ser", false);
        double score = new SnakeAi(best).startPlayingWithDisplay();
        System.out.println("fitness : " + score);
        // TODO GenericAi Builder with optional NeuralNet makeChild() lambda
        // TODO optimize Matrix multiplication
        // TODO implement option to make algorithm multithreaded
    }


}
