package de.fhws.genericAi.neuralNetwork;

import java.io.Serializable;
import java.util.Arrays;

public class Matrix implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3912936837859085090L;
	
	private double[][] data;

	public Matrix(int rows, int cols) {
        if(rows == 0 || cols == 0)
            throw new IllegalArgumentException();
        this.data = new double[rows][cols];
        for(int i = 0; i < this.data.length; i++) {
            this.data[i] = new double[data[i].length];
            Arrays.fill(this.data[i], 0);
        }
    }

    public Matrix(double[][] data) {
        if(data.length == 0 || data[0].length == 0)
            throw new IllegalArgumentException();
        
        this.data = new double[data.length][];
        for(int i = 0; i < this.data.length; i++) {
        	this.data[i] = new double[data[i].length];
        	for(int j = 0; j < this.data[i].length; j++) {
        		this.data[i][j] = data[i][j];
        	}
        }        
    }

    /**
     * multiplies this matrix with a linear vector
     * @param v linear vector which is multiplied with the matrix
     * @return the result of the multiplication
     */
    public LinearVector multiply(LinearVector v) {
        if(v.size() != this.getNumCols())
            throw new IllegalArgumentException("vector must have the same amount of rows as the matrix columns");
        double[] retData = new double[data.length];
        for(int i = 0; i < data.length; i++) {
            double sum = 0;
            for(int j = 0; j < data[i].length; j++) {
                sum += data[i][j] * v.get(j);
            }
            retData[i] = sum;
        }
        return new LinearVector(retData);
    }

    /**
     * gets the double value in the specified row and column
     * @param row row of the value
     * @param col column of the value
     * @return the specified double value
     */
    public double get(int row, int col) {
        return data[row][col];
    }

    /**
     * gets the number of rows
     * @return number of rows
     */
    public int getNumRows() {
        return data.length;
    }

    /**
     * gets the number of columns
     * @return number of columns
     */
    public int getNumCols() {
        return data.length == 0 ? 0 : data[0].length;
    }

    /**
     * randomizes this matrix
     * @param range the range in which the random numbers should be (abs from 0)
     * @param negative if {@code true} the numbers will also be negative (but always > -range)
     */
    public void randomize( double range, boolean negative) {
        for (int i = 0; i < getNumRows(); i++) {
            for (int j = 0; j < getNumCols(); j++) {
                double value = Math.random() * range;
                if(negative && (int) (Math.random() * 2) < 1)
                    value *= -1;
                data[i][j] = value;
            }
        }
    }
    
    
    /**
     * get the data of the Matrix
     * @return double[][] data
     */
    public double[][] getData(){return this.data;}
}	
