package de.fhws.genericAi.neuralNetwork;


import java.io.Serializable;
import java.util.function.DoubleUnaryOperator;

public class Layer implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3844443062431620792L;
	
	
	private Matrix weights;
    private LinearVector bias;
    private ActivationFunction fActivation;

    public Layer(int n, int linkedN, ActivationFunction activationFunction) {
        if(n < 1 || linkedN < 1)
            throw new IllegalArgumentException("n and linkedN must be greater than 0");
        weights = new Matrix(n, linkedN);
        bias = new LinearVector(n);
        this.fActivation = activationFunction;
    }

    /**
     * Copy Constructor
     * @return a copy of the given Layer
     */
    private Layer(Layer copy) {
    	this.weights = new Matrix(copy.weights.getData());
    	this.bias = new LinearVector(copy.bias.getData());
    	this.fActivation = copy.fActivation;
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

    public void randomize(double weightRange, boolean weightsNegative, double biasRange, boolean biasNegative) {
        weights.randomize(weightRange, weightsNegative);
        bias.randomize(biasRange, biasNegative);
    }
    
    /**
     * gets the Matrix of weights of this layer
     * @return Matrix of weights in this layer
     */
    public Matrix getWeights() { return weights;}
    
    public LinearVector getBias() { return this.bias;}

    public Layer copy() {
    	return new Layer(this);
    	
    }

}
