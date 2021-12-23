package de.fhws.genericAi;

import java.io.Serializable;
import java.util.function.ToDoubleFunction;

import de.fhws.genericAi.neuralNetwork.NeuralNet;

@FunctionalInterface
public interface FitnessFunction extends ToDoubleFunction<NeuralNet>, Serializable {
}
