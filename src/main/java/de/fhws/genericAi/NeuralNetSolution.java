package de.fhws.genericAi;

import java.util.List;
import java.util.function.ToDoubleFunction;

import de.fhws.genericAi.genericAlg.Solution;
import de.fhws.genericAi.neuralNetwork.Layer;
import de.fhws.genericAi.neuralNetwork.NeuralNet;

public class NeuralNetSolution implements Solution {

	
	final double mutationFactor = 1/2d;
	
	private NeuralNet neuralNet;
	
	private ToDoubleFunction<NeuralNet> calcFitness;

	double fitness;
	
	public NeuralNetSolution(NeuralNet neuralNet,ToDoubleFunction<NeuralNet> calcFitness) {
		this.neuralNet = neuralNet;
		this.calcFitness = calcFitness;
	}

	
	@Override
	public void calculateFitness(){
		fitness = calcFitness.applyAsDouble(neuralNet);
	}
	
	@Override
	public NeuralNetSolution copy() {
		return new NeuralNetSolution(neuralNet.copy(), calcFitness);
	};

	@Override
	public NeuralNetSolution getMutatedChild() {
		NeuralNetSolution child = copy();
		List<Layer> layers = child.neuralNet.getLayers();
		for (Layer layer : layers) {
			double[][] data = layer.getWeights().getData();
			for (int x = 0; x < data.length; x++) {
				for (int y = 0; y < data[x].length; y++) {
					if(Math.random() < 0.5)
						data[x][y] += (Math.random()/mutationFactor);
					else
						data[x][y] -= (Math.random()/mutationFactor);
				}
			}
		for (int i = 0; i < layer.getBias().getData().length; i++) {
			if(Math.random() < 0.5)
				layer.getBias().getData()[i] += (Math.random()/mutationFactor);
			else
				layer.getBias().getData()[i] -= (Math.random()/mutationFactor);
		}
		}
		return child;
	}

	public NeuralNet getNeuralNetwork() {
		return neuralNet;
	}


	@Override
	public double getFitness() {
		return fitness;
	}


}
