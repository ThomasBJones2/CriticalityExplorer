package com.criticalityworkbench.inputobjects;

import com.criticalityworkbench.randcomphandler.*;
import java.util.Random;
import java.util.ArrayList;

public class InputMatricesHalfZero extends InputMatrices {

	public InputMatricesHalfZero(){}

	public InputMatricesHalfZero(int n){
		this.size = n;
		this.A = new Matrix(n);
		this.B = new Matrix(n);
	}


	public InputMatricesHalfZero(Matrix A, Matrix B){
		if(A.size == B.size){
			this.size = A.size;
			this.A = new Matrix();
		  this.B = new Matrix();
			this.A.copy(A);
			this.B.copy(B);
		} else {
			throw new IllegalArgumentException();
		}
	}

	public InputMatricesHalfZero(InputMatricesHalfZero inMatrices){copy(inMatrices);}



	public static InputMatrices emptyObject(){return new InputMatricesHalfZero();}

	public static InputMatrices newInputOfSize(int n){return new InputMatricesHalfZero(n);}	

	static public void initialize(){}

	public ArrayList<DefinedLocation> getCurrentLocations(){
		return new ArrayList<DefinedLocation>();
	}

  @Override
	public void randomize(Random rand){
		this.A.randomize(rand);
		this.B.randomizeHalfZero(rand);
	}

}
