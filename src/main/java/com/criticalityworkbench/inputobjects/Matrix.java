package com.criticalityworkbench.inputobjects;

import com.criticalityworkbench.randcomphandler.*;
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
				values[i][j] = new BigInteger(BIG_INT_SIZE, rand); 
	}

	public void randomizeHalfZero(Random rand){
		for(int i = 0; i < size; i ++)
			for(int j = 0; j < size; j ++)
				if(j < size/2)
				    values[i][j] = new BigInteger(BIG_INT_SIZE, rand); 
		    else
					  values[i][j] = BigInteger.ZERO;
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

	public Matrix add(Matrix addMatrix){
		Matrix out = new Matrix(this.size);
		if(addMatrix.size == this.size){
			for(int i = 0; i < size; i ++){
				for(int j = 0; j < size; j ++){
					out.values[i][j] = this.values[i][j].add(addMatrix.values[i][j]);
				}
			}
			return out;
		} else {
			throw new IllegalArgumentException();
		}
	}

	public Matrix subtract(Matrix subMatrix){
		Matrix out = new Matrix(this.size);
		if(subMatrix.size == this.size){
			for(int i = 0; i < size; i ++){
				for(int j = 0; j < size; j ++){
					out.values[i][j] = this.values[i][j].subtract(subMatrix.values[i][j]);
				}
			}
			return out;
		} else {
			throw new IllegalArgumentException();
		}
	}

	public String toString(){
		String out = "";
		for(int i = 0; i < this.size; i ++){
			for(int j = 0; j < this.size; j ++){
				out += this.values[i][j] + " ";
			}
			out += "\n\n";
		}
		return out;
	}

	public void print(){
		System.out.println(this.toString());	
	}

	public static int i_translate(int i, int quarterNumber, int size){
		if(quarterNumber < 2){
			return i;
		}
		return i + size;
	}

	public static int j_translate(int j, int quarterNumber, int size){
		if(quarterNumber % 2 == 0){
			return j;
		}
		return j + size;
	}

	public static Matrix merge(Matrix A, Matrix B, Matrix C, Matrix D){
		Matrix out = new Matrix(A.size*2);
		if(A.size == B.size && C.size == D.size && B.size == C.size){
			for(int i = 0; i < A.size; i ++){
				for(int j = 0; j < A.size; j ++){
					out.values[i_translate(i, 0, A.size)][j_translate(j, 0, A.size)] = A.values[i][j];
					out.values[i_translate(i, 1, B.size)][j_translate(j, 1, B.size)] = B.values[i][j];
					out.values[i_translate(i, 2, C.size)][j_translate(j, 2, C.size)] = C.values[i][j];
					out.values[i_translate(i, 3, D.size)][j_translate(j, 3, D.size)] = D.values[i][j];
				}
			}
		} else {
			throw new IllegalArgumentException();
		}
		return out;
	}

	public static Matrix halfSplit(Matrix inMatrix, int quarterNumber){
		Matrix out = new Matrix(inMatrix.size/2);
		for(int i = 0; i < out.size; i ++){
			for(int j = 0; j < out.size; j ++){
				out.values[i][j] = 
					inMatrix.values[i_translate(i, quarterNumber, out.size)]
					[j_translate(j, quarterNumber, out.size)];
			}
		}
		return out;	
	}

	public void copy(Matrix inMatrix){
		size = inMatrix.size;
		values = new BigInteger[size][size];
		for(int i = 0; i < size; i ++)
			for(int j = 0; j < size; j ++)
				values[i][j] = inMatrix.values[i][j];
	}

}
