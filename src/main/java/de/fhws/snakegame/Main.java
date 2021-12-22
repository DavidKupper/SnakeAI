package de.fhws.snakegame;

import java.util.function.ToDoubleFunction;

import de.fhws.genericAi.NeuralNetSolution;
import de.fhws.genericAi.genericAlg.GenericAlg;
import de.fhws.genericAi.neuralNetwork.ActivationFunction;
import de.fhws.genericAi.neuralNetwork.NeuralNet;

public class Main {

	public static void main(String[] args) {
		
		//Test Comment
		
		ToDoubleFunction<NeuralNet> fitness = (nn) -> new SnakeAi(nn).startPlaying(150);
		
		
		ActivationFunction sigmoid = d -> (1 + Math.tanh(d /2 )) /2;
		
		GenericAlg g = new GenericAlg.Builder(() -> { 
					return new NeuralNetSolution(
							new NeuralNet.Builder(sigmoid)
							.addLayers(24,16,4)
							.build()
							.randomize(10, true, 1, false),
					fitness);	
				})
                .setPopulationSize(1000)
                .setRoundsAmount(10)
				.setSelectBestOfProz(0.05)
				.setMutateRate(0.1)
				.build();
		
		
		//NeuralNet best = NeuralNet.loadFromFile("BestNeuralNetwork");
		NeuralNet best = ((NeuralNetSolution)g.solve()).getNeuralNetwork();
		best.safeAsFile("files/bestAi.ser", false);
		double score = new SnakeAi(best).startPlayingWithDisplay();
        System.out.println("fitness : " + score);
		// TODO sigmoid standard
		// TODO GenericAi Builder with optional NeuralNet makeChild()
		// TODO refactor getMutatedChild() to getChild()
	}
	
	
}
