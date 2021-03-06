package de.fhws.genericAi;

import java.util.function.Supplier;

import de.fhws.genericAi.genericAlg.GenericAlg;
import de.fhws.genericAi.genericAlg.Population;
import de.fhws.genericAi.genericAlg.Solution;
import de.fhws.genericAi.genericAlg.Visualizer;
import de.fhws.genericAi.neuralNetwork.NeuralNet;
import de.fhws.genericAi.neuralNetwork.VisualizeNeuralNet.NeuralNetVisualizer;

public class GenericAi {
	private GenericAlg alg;
	private boolean printData;
	private int savingInterval;

	private GenericAi(GenericAlg alg, boolean printData, int savingInterval) {
		this.alg = alg;
		this.printData = printData;
		this.savingInterval = savingInterval;
	};

	public NeuralNet evolve() {
		Solution solution = alg.solve(printData, savingInterval);
		if (solution instanceof NeuralNetSolution)
			return ((NeuralNetSolution) solution).getNeuralNetwork();
		else
			return null;
	}

	
	public boolean safePopulationAsFile() {
		return safePopulationAsFile(false);
	}
	
	public boolean safePopulationAsFile(boolean override) {
		String dir = "files/populations";
		return alg.getPopulation().safeToFile("savedPopulation", dir, override);
	}
	
	public boolean safePopulationAsFile(String fname, String dir, boolean override) {
		return alg.getPopulation().safeToFile(fname, dir, override);
	}

	public static class Builder {
		FitnessFunction fitnessFunction;
		NeuralNet.Builder nnBuilder;
		double weightRange = 10;
		boolean weightsNegative = true;
		double biasRange = 1;
		boolean biasNegative = false;
		int popSize = 100;
		int genAmount = 100;
		double selectBestOfPercent = 0.25;
		double outerMutationRate = 0.1;
		double dataMutationRate = 0.5;
		double dataMutationFactor = 0.5;
		boolean crossover = false;
		Population givenPop;
		boolean priorRun = false;
		int savingInterval = -1;
		String rootDirPath = null;
		boolean printData = false;
		boolean visualize = false;
		int visualizerWidth = 1900;
		int visualizerHeight = 1000;
		int threadsAmount = Runtime.getRuntime().availableProcessors();

		public Builder(FitnessFunction fitnessFunction, NeuralNet.Builder nnBuilder) {
			this.fitnessFunction = fitnessFunction;
			this.nnBuilder = nnBuilder;
		}

		public Builder withWeightRange(double weightRange) {
			this.weightRange = weightRange;
			return this;
		}

		public Builder withWeightsNegative(boolean weightsNegative) {
			this.weightsNegative = weightsNegative;
			return this;
		}

		public Builder withBiasRange(double biasRange) {
			this.biasRange = biasRange;
			return this;
		}

		public Builder withBiasNegative(boolean biasNegative) {
			this.biasNegative = biasNegative;
			return this;
		}

		public Builder withPopulationSize(int popSize) {
			this.popSize = popSize;
			return this;
		}

		public Builder withGenerationAmount(int genAmount) {
			this.genAmount = genAmount;
			return this;
		}

		public Builder withSelectBestOfPercent(double selectBestOfPercent) {
			this.selectBestOfPercent = selectBestOfPercent;
			return this;
		}

		public Builder withOuterMutationRate(double outerMutationRate) {
			this.outerMutationRate = outerMutationRate;
			return this;
		}

		public Builder withDataMutationRate(double dataMutationRate) {
			this.dataMutationRate = dataMutationRate;
			return this;
		}

		public Builder withDataMutationFactor(double dataMutationFactor) {
			this.dataMutationFactor = dataMutationFactor;
			return this;
		}

		public Builder withCrossover(boolean crossover) {
			this.crossover = crossover;
			return this;
		}

		public Builder withPrintData(boolean printData) {
			this.printData = printData;
			return this;
		}

		public Builder withBasePopulation(Population givenPop) {
			this.givenPop = givenPop;
			return this;
		}

		public Builder withPriorRun(String logDir) {
			withBasePopulation(Population.loadPopulationFromFile(logDir + "/savedPop.ser"));
			withSaveMetaDataTo(logDir);
			return this;
		}

		public Builder withSavingInterval(int savingInterval) {
		    this.savingInterval = savingInterval;
		    return this;
        }

		public Builder withSaveMetaDataTo(String rootDirPath) {
			this.rootDirPath = rootDirPath;
			return this;
		}

        public Builder withAmountOfParallelThreads(int threads) {
			this.threadsAmount = threads;
			return this;
		}
        
        public Builder withVisualize(boolean visualize) {
        	this.visualize = visualize;
        	return this;
        }
        
        public Builder withResizedVisualizer(int width, int height) {
        	this.visualizerWidth = width;
        	this.visualizerHeight = height;
        	return this;
        }
        
        

		public GenericAi build() {
			Supplier<Solution> givenSupplier;
			if (givenPop == null) {
				givenSupplier = () -> new NeuralNetSolution(
						nnBuilder.build().randomize(weightRange, weightsNegative, biasRange, biasNegative),
						fitnessFunction, outerMutationRate, dataMutationRate, dataMutationFactor, crossover);
			} else {
				this.popSize = givenPop.getSize();
				givenSupplier = GenericAlg.getSupplierOfPopulation(givenPop);
			}
			
			Visualizer visualizer = null;
			if(visualize) {
				visualizer = new NeuralNetVisualizer(visualizerWidth, visualizerHeight);
			}
			
			return new GenericAi(
					new GenericAlg.Builder(givenSupplier)
					.withPopulationSize(popSize)
					.withGenerationsAmount(genAmount)
					.withSelectBestOfPercent(selectBestOfPercent)
					.withSaveMetaDataTo(rootDirPath)
					.withAmountOfParallelThreads(threadsAmount)
					.withVisualize(visualize)
					.withCustomVisualizer(visualizer)
					.build(),
					printData, savingInterval);
		}
	}
}
