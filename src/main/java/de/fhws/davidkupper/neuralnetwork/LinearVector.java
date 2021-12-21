package de.fhws.davidkupper.neuralnetwork;

import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;

public class LinearVector {
    private double[] data;

    public LinearVector(double[] data) {
        if(data.length <= 0)
            throw new IllegalArgumentException();

        this.data = data;
    }

    /**
     * gets data at index
     * @param index index of desired data
     * @return data at index
     */
    public double get(int index) {
        return data[index];
    }

    /**
     * gets the size of the vector
     * @return size of the vector
     */
    public int size() {
        return data.length;
    }

    /**
     * adds the given vector to this vector
     * @param v the vector which is added on this vector
     * @return the result of the addition
     */
    public LinearVector add(LinearVector v) {
        double[] retData = new double[v.data.length];
        for(int i = 0; i < retData.length; i++) {
            retData[i] = this.data[i] + v.data[i];
        }
        return new LinearVector(retData);
    }

    /**
     * subtracts the given vector to this vector
     * @param v the vector which is subtracted from this vector
     * @return the result of the subtraction
     */
    public LinearVector sub(LinearVector v) {
        double[] retData = new double[v.data.length];
        for(int i = 0; i < retData.length; i++) {
            retData[i] = this.data[i] - v.data[i];
        }
        return new LinearVector(retData);
    }

    /**
     * applies the DoubleUnaryOperator (Function with Double accepted and Double returned) to this vector, so on every
     * value
     * @param function function which is applied to every value of the vector
     * @return this vector, after the function was applied
     */
    public LinearVector apply(DoubleUnaryOperator function) {
        this.data = Arrays.stream(data).map(function).toArray();
        return this;
    }

    /**
     * finds the index of the biggest number in this vector
     * @return the index of the biggest number in this vector
     */
    public int getIndexOfBiggest() {
        int index = 0;
        for (int i = 1; i < data.length; i++) {
            if(data[i] > data[index])
                index = i;
        }
        return index;
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < data.length; i++) {
            s += "| " + i + ": " + String.format("%.2f", data[i]) + " |\n";
        }
        return s;
    }

    /**
     * creates a new LinearVector filled with random double numbers
     * @param n the number of rows of the vector
     * @param range the range in which the random numbers should be (abs from 0)
     * @param negative if {@code true} the numbers will also be negative (but always > -range)
     * @return
     */
    public static LinearVector createRandomLinearVector(int n, double range, boolean negative) {
        double[] a = new double[n];
        for (int i = 0; i < n; i++) {
            double value = Math.random() * range;
            if(negative && (int) (Math.random() * 2) < 1)
                value *= -1;
            a[i] = value;
        }
        return new LinearVector(a);
    }

}
