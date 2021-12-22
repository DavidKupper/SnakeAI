package de.fhws.snakegame;

import de.fhws.genericAi.NeuralNetSolution;
import de.fhws.genericAi.genericAlg.GenericAlg;
import de.fhws.genericAi.neuralNetwork.NeuralNet;

import java.util.function.DoubleUnaryOperator;
import java.util.function.ToDoubleFunction;

public class Main {
    public static void main(String[] args) {
        DoubleUnaryOperator reLu = d -> d < 0 ? 0 : d;
        DoubleUnaryOperator sigmoid = d -> (1 + Math.tanh(d / 2)) / 2;
        ToDoubleFunction<NeuralNet> fitnessFunc = nn -> new SnakeAi(nn).startPlaying(50);

        GenericAlg genericSnakeGames = new GenericAlg.Builder(() -> {
            return new NeuralNetSolution(
                    new NeuralNet.Builder(10, true, 1, false, sigmoid)
                            .addLayers(24, 16, 4)
                            .build(),
                            fitnessFunc
                    );
                })
                .setPopulationSize(1000)
                .setSelectBestOf(100)
                .setRoundsAmount(100)
                .setMutateRate(0.5)
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
