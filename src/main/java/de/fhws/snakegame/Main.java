package de.fhws.snakegame;

import de.fhws.genericAi.NeuralNetSolution;
import de.fhws.genericAi.genericAlg.GenericAlg;
import de.fhws.genericAi.neuralNetwork.NeuralNet;

public class Main {

    public static void main(String[] args) {


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
                .build();


        //NeuralNet best = NeuralNet.loadFromFile("BestNeuralNetwork");
        NeuralNet best = ((NeuralNetSolution) g.solve()).getNeuralNetwork();
        best.safeAsFile("files/bestAi.ser", false);
        double score = new SnakeAi(best).startPlayingWithDisplay();
        System.out.println("fitness : " + score);
        // TODO GenericAi Builder with optional NeuralNet makeChild() lambda
        // TODO implement option to make algorithm multithreaded
    }


}
