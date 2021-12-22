package de.fhws.davidkupper.snakegame;

import de.fhws.davidkupper.neuralnetwork.NeuralNet;

import java.util.function.DoubleUnaryOperator;

public class Main {
    public static void main(String[] args) {
        DoubleUnaryOperator reLu = d -> d < 0 ? 0 : d;
        DoubleUnaryOperator sigmoid = d -> (1 + Math.tanh(d / 2)) / 2;


        NeuralNet player = new NeuralNet.Builder(10, true, 10, false, sigmoid)
                // for snake top down view
                //        .addLayers(FIELD_WIDTH*FIELD_HEIGHT, 200, 100, 50, 25, 4)
                // for directional snake view
                .addLayers(3*8, 200, 100, 50, 25, 4)
                .build();

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
