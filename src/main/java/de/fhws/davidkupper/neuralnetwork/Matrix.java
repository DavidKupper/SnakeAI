package de.fhws.davidkupper.neuralnetwork;

public class Matrix {
    private double[][] data;

    public Matrix(double[][] data) {
        if(data.length <= 0 || data[0].length <= 0)
            throw new IllegalArgumentException();
        this.data = data;
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
     * creates a new Matrix filled with random double numbers
     * @param numRows the number of rows of the matrix
     * @param numCols the number of columns of the matrix
     * @param range the range in which the random numbers should be (abs from 0)
     * @param negative if {@code true} the numbers will also be negative (but always > -range)
     * @return
     */
    public static Matrix createRandomMatrix(int numRows, int numCols, double range, boolean negative) {
        double[][] a = new double[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                double value = Math.random() * range;
                if(negative && (int) (Math.random() * 2) < 1)
                    value *= -1;
                a[i][j] = value;
            }
        }
        return new Matrix(a);
    }
}
