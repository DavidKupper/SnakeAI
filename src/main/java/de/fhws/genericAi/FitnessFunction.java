package de.fhws.genericAi;

import de.fhws.genericAi.neuralNetwork.NeuralNet;

import java.util.function.ToDoubleFunction;

@FunctionalInterface
public interface FitnessFunction extends ToDoubleFunction<NeuralNet> {
}
