package de.fhws.snakegame;

import de.fhws.genericAi.NeuralNetSolution;
import de.fhws.genericAi.genericAlg.GenericAlg;
import de.fhws.genericAi.neuralNetwork.ActivationFunction;
import de.fhws.genericAi.neuralNetwork.NeuralNet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.function.DoubleUnaryOperator;
import java.util.function.ToDoubleFunction;

public class Main {
    public static void main(String[] args) {
        DoubleUnaryOperator reLu = d -> d < 0 ? 0 : d;
        ActivationFunction sigmoid = d -> (1 + Math.tanh(d / 2)) / 2;

        ToDoubleFunction<NeuralNet> fitnessFunc = nn -> new SnakeAi(nn).startPlaying(150);

        GenericAlg genericSnakeGames = new GenericAlg.Builder(() -> {
            return new NeuralNetSolution(
                    new NeuralNet.Builder(sigmoid)
                            .addLayers(24, 16, 4)
                            .build()
                            .randomize(10, true, 1, false),
                            fitnessFunc
                    );
                })
                .setPopulationSize(1000)
                .setRoundsAmount(200)
                .setSelectBestOf(0.05)
                .setMutateRate(0.1)
                .build();
        NeuralNet player = ((NeuralNetSolution) genericSnakeGames.solve()).getNeuralNetwork();
        SnakeAi ai = new SnakeAi(player);
        double fitness = ai.startPlayingWithDisplay();
        System.out.println(fitness);


        /*
        // Test NN
        NeuralNet nn = new NeuralNet.NeuralNetBuilder(10, true, 10, false, sigmoid)
                .addLayers(10, 4, 2)
                .build();
        LinearVector in = LinearVector.createRandomLinearVector(10, 1, false);
        System.out.println(in);
        nn.calcAllLayer(in).forEach(v -> System.out.println(v));

         */
    }
}
