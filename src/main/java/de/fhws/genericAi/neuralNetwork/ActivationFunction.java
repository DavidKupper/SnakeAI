package de.fhws.genericAi.neuralNetwork;

import java.io.Serializable;
import java.util.function.DoubleUnaryOperator;

@FunctionalInterface
public interface ActivationFunction extends DoubleUnaryOperator, Serializable{

}
