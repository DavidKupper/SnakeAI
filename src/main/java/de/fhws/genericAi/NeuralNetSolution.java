package de.fhws.genericAi;

import java.util.List;
import java.util.function.ToDoubleFunction;

import de.fhws.genericAi.genericAlg.Solution;
import de.fhws.genericAi.neuralNetwork.Layer;
import de.fhws.genericAi.neuralNetwork.NeuralNet;

public class NeuralNetSolution implements Solution {

	
	private NeuralNet neuralNet;
	private ToDoubleFunction<NeuralNet> fitnessFunction;
	private double dataMutationRate;
	private double dataMutationFactor;
	private double fitness;

	
	public NeuralNetSolution(NeuralNet neuralNet, ToDoubleFunction<NeuralNet> fitnessFunction, double dataMutationRate, double dataMutationFactor) {
		this.neuralNet = neuralNet;
		this.fitnessFunction = fitnessFunction;
		this.dataMutationRate = dataMutationRate;
		this.dataMutationFactor = dataMutationFactor;
	}

	
	@Override
	public void calculateFitness(){
		fitness = fitnessFunction.applyAsDouble(neuralNet);
	}
	
	@Override
	public NeuralNetSolution copy() {
		return new NeuralNetSolution(neuralNet.copy(), fitnessFunction, dataMutationRate, dataMutationFactor);
	};

	@Override
	public NeuralNetSolution getChild() {
		return getMutatedChild();
	}

	public NeuralNetSolution getMutatedChild() {
		NeuralNetSolution child = copy();
		child.mutate();
		return child;
	}

	public void mutate() {
		List<Layer> layers = this.neuralNet.getLayers();
		for (Layer layer : layers) {
			double[][] data = layer.getWeights().getData();
			for (int x = 0; x < data.length; x++) {
				for (int y = 0; y < data[x].length; y++) {
					if(Math.random() < dataMutationRate) {
						if(Math.random() < 0.5)
							data[x][y] += (Math.random()/ dataMutationFactor);
						else
							data[x][y] -= (Math.random()/ dataMutationFactor);
					}
				}
			}
			for (int i = 0; i < layer.getBias().getData().length; i++) {
				if(Math.random() < dataMutationRate) {
					if(Math.random() < 0.5)
						layer.getBias().getData()[i] += (Math.random() * dataMutationFactor);
					else
						layer.getBias().getData()[i] -= (Math.random() * dataMutationFactor);
				}
			}
		}
	}

	public NeuralNetSolution getCrossoverChild(NeuralNetSolution parent1, NeuralNetSolution parent2) {
		NeuralNetSolution child = parent1.copy();
		List<Layer> layers = child.neuralNet.getLayers();
		for(int i = 0; i < layers.size(); i++) {
			double[][] data = layers.get(i).getWeights().getData();
			for (int x = 0; x < data.length; x++) {
				for (int y = 0; y < data[x].length; y++) {
					if(Math.random() < 0.5)
						data[x][y] = parent2.neuralNet.getLayers().get(i).getWeights().get(x, y);
				}
			}
			for(int j = 0; j < layers.get(i).getBias().size(); j++) {
				if(Math.random() < 0.5)
					layers.get(i).getBias().getData()[j] = parent2.neuralNet.getLayers().get(i).getBias().get(j);
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
