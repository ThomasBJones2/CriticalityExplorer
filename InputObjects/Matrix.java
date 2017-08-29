package InputObjects;

import RandComp.*;
import java.util.Random;
import java.util.ArrayList;
import java.math.BigInteger


public class Matrix implements Input<Matrix>{
	final int BIG_INT_SIZE = 100;

	BigInteger[][] values;
  int size;

	public Matrix(){}

	public Matrix(int n){
		size = n;
		values = new BigInteger[n][n];
	}

	public Matrix(Matrix inMatrix){copy(inMatrix);}

	public static Matrix emptyObject(){return new Matrix();}

	public static Matrix newInputOfSize(int n){return new Matrix(n);}	

	static public void initialize(){}

	public ArrayList<DefinedLocation> getCurrentLocations(){
		return new ArrayList<DefinedLocation>();
	}


	public void randomize(Random rand){
		for(int i = 0; i < size; i ++)
			for(int j = 0; j < size; j ++)
				values[i][j] = new BigInteger(size, rand)

	}

	public int size(){
		return this.size;
	}


	public void copy(Matrix inMatrix){
		size = inMatrix.size;
		values = new BigInteger[size][size];
		for(int i = 0; i < size; i ++)
			for(int j = 0; j < size; j ++)
				values[i][j] = inMatrix[i][j];
	}

}
