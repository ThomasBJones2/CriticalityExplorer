package InputObjects;

import RandComp.*;
import java.util.Random;
import java.util.ArrayList;
import java.math.BigInteger;
import java.lang.Math;

public class Matrix implements Input<Matrix>{
	final int BIG_INT_SIZE = 10;

	BigInteger[][] values;
  int size;

	public Matrix(){}

	public Matrix(int n){
		size = n;
		values = new BigInteger[n][n];
	}

	public void randomize(Random rand){
		for(int i = 0; i < size; i ++)
			for(int j = 0; j < size; j ++)
				values[i][j] = new BigInteger(size, rand); 
	}

	public double frobeniusNorm(Matrix otherMatrix){
		double out = 0;
		if(otherMatrix.size == size) {
			for(int i = 0; i < this.size; i ++){
				for(int j = 0; j < this.size; j ++){
						double val = this.values[i][j].doubleValue() -
							otherMatrix.values[i][j].doubleValue();
						out += val*val;
				}
			}
			return Math.sqrt(out);
		}
		System.out.println("Matrix sizes do not match for Frobenius Norm " 
				+ otherMatrix.size 
				+ " " + this.size);
		return Double.MAX_VALUE;
	}

	public double infinityNorm(Matrix otherMatrix){
		double out = 0;
		if(otherMatrix.size == size) {
			for(int i = 0; i < this.size; i ++){
				double maxVal = Double.MIN_VALUE;
				for(int j = 0; j < this.size; j ++){
						double val = this.values[i][j].doubleValue() -
							otherMatrix.values[i][j].doubleValue();
						if(maxVal < val)
							maxVal = val;
				}
				out += maxVal;
			}
			return out;
		}
		System.out.println("Matrix sizes do not match for Frobenius Norm " 
				+ otherMatrix.size 
				+ " " + this.size);
		return Double.MAX_VALUE;
	}

	public Matrix(Matrix inMatrix){copy(inMatrix);}

	public static Matrix emptyObject(){return new Matrix();}

	public static Matrix newInputOfSize(int n){return new Matrix(n);}	

	static public void initialize(){}

	public ArrayList<DefinedLocation> getCurrentLocations(){
		return new ArrayList<DefinedLocation>();
	}

	public int size(){
		return this.size;
	}


	public void copy(Matrix inMatrix){
		size = inMatrix.size;
		values = new BigInteger[size][size];
		for(int i = 0; i < size; i ++)
			for(int j = 0; j < size; j ++)
				values[i][j] = inMatrix.values[i][j];
	}

}
