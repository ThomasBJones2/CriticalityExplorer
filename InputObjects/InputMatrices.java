package InputObjects;

import RandComp.*;
import java.util.Random;
import java.util.ArrayList;

public class InputMatrices implements Input<InputMatrices>{

	public Matrix A, B;
	int size;

	public InputMatrices(){}

	public InputMatrices(int n){
		this.size = n;
		this.A = new Matrix(n);
		this.B = new Matrix(n);
	}


	public InputMatrices(Matrix A, Matrix B){
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


	public InputMatrices(InputMatrices inMatrices){copy(inMatrices);}

	public static InputMatrices emptyObject(){return new InputMatrices();}

	public static InputMatrices newInputOfSize(int n){return new InputMatrices(n);}	

	static public void initialize(){}

	public ArrayList<DefinedLocation> getCurrentLocations(){
		return new ArrayList<DefinedLocation>();
	}


	public void randomize(Random rand){
		this.A.randomize(rand);
		this.B.randomize(rand);
	}

	public int size(){
		return this.size;
	}

	public void copy(InputMatrices inMatrices){
		this.size = inMatrices.size;
		this.A = new Matrix(this.size);
		this.B = new Matrix(this.size);
		this.A.copy(inMatrices.A);
		this.B.copy(inMatrices.B);
	}

}
