package de.fhws.genericAi.neuralNetwork;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NeuralNet implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5984131490435879432L;

	int numInput;
	private List<Layer> layers;

	private NeuralNet() {
		layers = new ArrayList<>();
	}

	private NeuralNet(int numInput, List<Layer> layers) {
		this.numInput = numInput;
		this.layers = layers;
	}

	/**
	 * calculates the output based on the given input vector
	 * 
	 * @param input vector with the input values; must be the size specified at
	 *              built
	 * @return the calculated output vector
	 * @throws IllegalArgumentException if the size of {@code input} is not the
	 *                                  specified one
	 */
	public LinearVector calcOutput(LinearVector input) {
		if (input.size() != numInput)
			throw new IllegalArgumentException("input num not equal to given input size");
		for (Layer l : layers) {
			input = l.calcActivation(input);
		}
		return input;
	}

	/**
	 * calculates the output based on the given input vector
	 * 
	 * @param input vector with the input values; must be the size specified at
	 *              built
	 * @return all calculated vectors (hidden layers and output layer)
	 * @throws IllegalArgumentException if the size of {@code input} is not the
	 *                                  specified one
	 */
	public List<LinearVector> calcAllLayer(LinearVector input) {
		if (input.size() != numInput)
			throw new IllegalArgumentException("input num not equal to given input size");
		List<LinearVector> list = new ArrayList<>(layers.size());
		for (Layer l : layers) {
			input = l.calcActivation(input);
			list.add(input);
		}
		return list;
	}

	/**
	 * save NeuralNetwork to a File
	 * 
	 * @param fname    is the name of File
	 * @param override == true results in the File beeing overwritten and override
	 *                 == false results writing the result at the end of the File
	 */
	public void safeAsFile(String fname, boolean override) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fname, override))) {
			oos.writeObject(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
     * loads a NeuralNet Object from a File
     * @param fname is the name of File
     * @return the loaded NeuralNet
     */
    public static NeuralNet loadFromFile(String fname) {
    	try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fname))) {
    		return (NeuralNet) ois.readObject();
    		
    	}catch(IOException | ClassNotFoundException e) {
    		e.printStackTrace();
    		return null;
    	}
    }

    public NeuralNet randomize(double weightRange, boolean weightsNegative, double biasRange, boolean biasNegative) {
    	for(Layer l : layers)
    		l.randomize(weightRange, weightsNegative, biasRange, biasNegative);
    	return this;
	}

	/**
	 * copy the current NeuralNet
	 * 
	 * @return copy of the current NeuralNet
	 */
	public NeuralNet copy() {
		List<Layer> copiedLayers = new ArrayList<>();
		for (int i = 0; i < layers.size(); i++) {
			copiedLayers.add(layers.get(i).copy());
		}
		return new NeuralNet(this.numInput, copiedLayers);
	}

	public List<Layer> getLayers() {
		return layers;
	}

	public static class Builder {
		private ActivationFunction activationFunction;
		private NeuralNet nn;
		private boolean inputLayerSet = false;

		/**
		 * Constructor to create a Builder which is capable to build a NeuralNet
		 * @throws IllegalArgumentException if depth is less or equal 1 or if inputNodes
		 *                                  is less than 1
		 */
		public Builder() {
			nn = new NeuralNet();
			activationFunction = d -> (1 + Math.tanh(d /2 )) /2;
		}


		/**
		 * set activation function of the neural network. Must be called before adding any layers
		 * @param aFunc ActivationFunction (Function that accepts Double
		 *                           and returns Double) to describe the activation
		 *                           function which is applied on every layer on
		 *                           calculation
		 */
		public Builder setActivationFunction(ActivationFunction aFunc) {
			if(inputLayerSet)
				throw new IllegalStateException("activation function must be declared before adding any layers");
			activationFunction = aFunc;
			return this;
		}

		/**
		 * adds a layer to the neural network
		 * 
		 * @param numNodes the number of nodes of the added layer
		 * @return this
		 * @throws IllegalArgumentException if numNodes are 0 or smaller
		 */
		public Builder addLayer(int numNodes) {
			if (numNodes <= 0)
				throw new IllegalArgumentException("numNodes must be at least 1");

			int linkedN;
			if (nn.layers.isEmpty()) {
				if (!inputLayerSet) {
					inputLayerSet = true;
					nn.numInput = numNodes;
					return this;
				} else
					linkedN = nn.numInput;
			} else
				linkedN = nn.layers.get(nn.layers.size() - 1).getNumNodes();

			nn.layers.add(new Layer(numNodes, linkedN, activationFunction));
			return this;
		}

		/**
		 * adds the specified amount of layers to the neural network
		 * 
		 * @param amount   the amount of layers added
		 * @param numNodes the number of nodes of the added layers
		 * @return this
		 */
		public Builder addLayers(int amount, int numNodes) {
			for (int i = 0; i < amount; i++) {
				addLayer(numNodes);
			}
			return this;
		}

		/**
		 * adds layers of the specified sizes to the neural network
		 * 
		 * @param numNodes array of the number of nodes which are added
		 * @return this
		 */
		public Builder addLayers(int... numNodes) {
			for (int n : numNodes) {
				addLayer(n);
			}
			return this;
		}

		/**
		 * builds the NeuralNet
		 * @return the built NeuralNet
		 * @throws IllegalStateException
		 */
		public NeuralNet build() {
			if(!inputLayerSet)
				throw new IllegalStateException("can not build NeuralNet because no layer has been added");
			return nn;
		}

	}

}
