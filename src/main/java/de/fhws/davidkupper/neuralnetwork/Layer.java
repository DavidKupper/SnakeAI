package de.fhws.davidkupper.neuralnetwork;


import java.util.function.DoubleUnaryOperator;

public class Layer {
    private Matrix weights;
    private LinearVector bias;
    private DoubleUnaryOperator fActivation;

    public Layer(int n, int linkedN,
                 double weightRange, boolean weightsNegative, double biasRange, boolean biasNegative, DoubleUnaryOperator activationFunction) {

        if(n < 1 || linkedN < 1)
            throw new IllegalArgumentException("n and linkedN must be greater than 0");
        weights = Matrix.createRandomMatrix(n, linkedN, weightRange, weightsNegative);
        bias = LinearVector.createRandomLinearVector(n, biasRange, biasNegative);
        this.fActivation = activationFunction;
    }

    /**
     * gets the number of the nodes in this layer
     * @return number of nodes in this layer
     */
    public int getNumNodes() {
        return bias.size();
    }

    /**
     * calculates the activation of this layer, based on the given activation of the linked layer
     * @param linkedActivation layer on which the activation is based
     * @return a LinearVector with the activation of this layer as a vector
     * @throws IllegalArgumentException if the number of columns of the weights does not fit to the size of linkedActivation
     */
    public LinearVector calcActivation(LinearVector linkedActivation) {
        if(weights.getNumCols() != linkedActivation.size())
            throw new IllegalArgumentException("size of linkedActivation does not fit with weights columns");
        return weights.multiply(linkedActivation).sub(bias).apply(fActivation);
    }



}
