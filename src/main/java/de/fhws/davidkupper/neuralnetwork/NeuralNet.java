package de.fhws.davidkupper.neuralnetwork;


import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

public class NeuralNet {
    int numInput;
    private List<Layer> layers;

    private NeuralNet() {
        layers = new ArrayList<>();
    }

    /**
     * calculates the output based on the given input vector
     * @param input vector with the input values; must be the size specified at built
     * @return the calculated output vector
     * @throws IllegalArgumentException if the size of {@code input} is not the specified one
     */
    public LinearVector calcOutput(LinearVector input) {
        if(input.size() != numInput)
            throw new IllegalArgumentException("input num not equal to given input size");
        for(Layer l : layers) {
            input = l.calcActivation(input);
        }
        return input;
    }

    /**
     * calculates the output based on the given input vector
     * @param input vector with the input values; must be the size specified at built
     * @return all calculated vectors (hidden layers and output layer)
     * @throws IllegalArgumentException if the size of {@code input} is not the specified one
     */
    public List<LinearVector> calcAllLayer(LinearVector input) {
        if(input.size() != numInput)
            throw new IllegalArgumentException("input num not equal to given input size");
        List<LinearVector> list = new ArrayList<>(layers.size());
        for(Layer l : layers) {
            input = l.calcActivation(input);
            list.add(input);
        }
        return list;
    }


    public static class Builder {
        private double weightRange;
        private boolean weightsNegative;
        private double biasRange;
        private boolean biasNegative;
        private DoubleUnaryOperator activationFunction;
        private NeuralNet nn;
        private boolean numInputSet = false;

        /**
         * Constructor to create a Builder which is capable to build a NeuralNet
         * @param weightRange the possible range of the weights
         * @param weightsNegative if {@code true} the weights will be by chance also negative
         * @param biasRange the possible range of the bias
         * @param biasNegative if {@code true} the bias will be by chance also negative
         * @param activationFunction DoubleUnaryOperator (Function that accepts Double and returns Double) to describe
         *                           the activation function which is applied on every layer on calculation
         * @throws IllegalArgumentException if depth is less or equal 1 or if inputNodes is less than 1
         */
        public Builder(double weightRange, boolean weightsNegative, double biasRange, boolean biasNegative, DoubleUnaryOperator activationFunction) {
            nn = new NeuralNet();
            this.weightRange = weightRange;
            this.weightsNegative = weightsNegative;
            this.biasRange = biasRange;
            this.biasNegative = biasNegative;
            this.activationFunction = activationFunction;
        }

        /**
         * adds a layer to the neural network
         * @param numNodes the number of nodes of the added layer
         * @return this
         * @throws IllegalArgumentException if numNodes are 0 or smaller
         */
        public Builder addLayer(int numNodes) {
            if(numNodes <= 0)
                throw new IllegalArgumentException("numNodes must be at least 1");

            int linkedN;
            if(nn.layers.isEmpty()) {
                if(!numInputSet) {
                    numInputSet = true;
                    nn.numInput = numNodes;
                    return this;
                }
                else
                    linkedN = nn.numInput;
            }
            else
                linkedN = nn.layers.get(nn.layers.size()-1).getNumNodes();

            nn.layers.add(new Layer(numNodes, linkedN, weightRange, weightsNegative, biasRange, biasNegative, activationFunction));
            return this;
        }

        /**
         * adds the specified amount of layers to the neural network
         * @param amount the amount of layers added
         * @param numNodes the number of nodes of the added layers
         * @return this
         */
        public Builder addLayers(int amount, int numNodes) {
            for(int i = 0; i < amount; i++) {
                addLayer(numNodes);
            }
            return this;
        }

        /**
         * adds layers of the specified sizes to the neural network
         * @param numNodes array of the number of nodes which are added
         * @return this
         */
        public Builder addLayers(int... numNodes) {
            for(int n : numNodes) {
                addLayer(n);
            }
            return this;
        }

        /**
         * builds the NeuralNet
         * @return the built NeuralNet
         */
        public NeuralNet build() {
            return nn;
        }

    }

}
